package com.example.beeproject.yards;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beeproject.Methods;
import com.example.beeproject.R;
import com.example.beeproject.global.classes.CheckFormObject;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.HiveObject;
import com.example.beeproject.global.classes.YardObject;
import com.example.beeproject.syncing.DeletedObject;
import com.example.beeproject.syncing.SyncHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;

public class FragmentHive extends Fragment{

	ArrayAdapter<String> aa;
	DatabaseManager dbManager = new DatabaseManager();
	DatabaseHelper db = dbManager.getHelper(getActivity());
	
	int yardID = 0;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
    	
    	return inflater.inflate(R.layout.fragmentyard,container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        final ListView hiveListView = (ListView)getView().findViewById(R.id.hiveListView);
        
        Bundle args = getArguments();
        yardID = args.getInt("yardID");
        
        ArrayList<String> hivelist = getHiveData(yardID);
        
        
        
        aa = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,hivelist);
        
        hiveListView.setAdapter(aa);
        
        hiveListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String hiveName =(String) (hiveListView.getItemAtPosition(arg2));
				
				((Methods) getActivity()).checkform(hiveName, yardID);
				
			}
		});
        
        Button addYard = (Button) getView().findViewById(R.id.addHive);
		
		addYard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createAddHiveDialog(yardID);
				
			}
		});
		registerForContextMenu(hiveListView);
    }
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
		
		
		
		menu.setHeaderTitle("Hive");
		
		String[] menuItems = {"Delete"};
		
		for (int i = 0; i<menuItems.length; i++) {
		      menu.add(Menu.NONE, i, i, menuItems[i]);
		}
	}
	
	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		
		
		
		
		
		String check = item.toString();
		
		if(check.equals("Delete")){
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        	alertDialog.setTitle("Delete Hive");
        	alertDialog.setMessage("Are you sure you want to delete the selected hive?");
        	
        	alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	            public void onClick(DialogInterface dialog, int which) {
        	            	DatabaseManager dbManager = new DatabaseManager();
        	        		DatabaseHelper db = dbManager.getHelper(getActivity());
        	            	RuntimeExceptionDao<HiveObject, Integer> hiveDao = db.getHiveRunDao();
        	            	RuntimeExceptionDao<CheckFormObject, Integer> checkFormDao = db.getCheckFormRunDao();
        	            	
        	            	
        	            	List<HiveObject> hiveIDList;
        	            	
        	            	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	            		Object data = aa.getItem(info.position);
    	            		final String selectedHive = data.toString();
        	            	
        	            	DeleteBuilder<HiveObject, Integer> deleteHiveBuilder = hiveDao.deleteBuilder();
        	            	DeleteBuilder<CheckFormObject, Integer> deleteCheckFormBuilder = checkFormDao.deleteBuilder();
        	            	try {
        	            		
        	            		hiveIDList = hiveDao.query(hiveDao.queryBuilder().where()
										.eq("hiveName", selectedHive)
										.prepare());
        	            		
        	            		int hiveID = hiveIDList.get(0).getId();
        	            		int hiveServerSideId = hiveIDList.get(0).getServerSideID();
        	            		
        	            		deleteCheckFormBuilder.where().eq("hiveID_id", hiveID);
        	            		deleteCheckFormBuilder.delete();
        	            		
								deleteHiveBuilder.where().eq("hiveName", selectedHive)
													 .and()
													 .eq("yardID_id", yardID);
								int nrDeletedRows = deleteHiveBuilder.delete();
								if(nrDeletedRows == 1){
									/* A HiveObject has been deleted from the local database.
									 * Therefore, the infromation about the deleted object must be saved in DeletedObjects table
									 * in order to syncronise the deleting to the server 
									 * next time the syncronisation (i.e SyncHelper.syncronizeToServer()) is run 
									 * */
									SyncHelper syncHelper = new SyncHelper(getActivity());
									DeletedObject deletedObjectInfo = new DeletedObject(HiveObject.class.getName(), hiveServerSideId);
									syncHelper.storeDeletedObjectForSyncronisation(deletedObjectInfo);
								}
								
								Toast.makeText(getActivity().getApplicationContext(), "Hive has been deleted!",
										   Toast.LENGTH_LONG).show();
								
								updateData(yardID);
								
								dialog.cancel();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
        	            	
        	            }
        	        });
        	alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
        	            public void onClick(DialogInterface dialog, int which) {
        	                dialog.cancel();
        	            }
        	        });
        	alertDialog.show();
		}
		
		return true;
	}
	
	private void updateData(int yardID) {
		ArrayList<String> hivelist = getHiveData(yardID);
	    
		aa.clear();
		aa.addAll(hivelist);
		aa.notifyDataSetChanged();
	}
	
    
    private ArrayList<String> getHiveData(int yardID) {
    	
    	ArrayList<String> hivelist = new ArrayList<String>();
		
		List<HiveObject> hivelistObj;
		RuntimeExceptionDao<HiveObject, Integer> hiveDao = db.getHiveRunDao();
		try {
			hivelistObj = hiveDao.query(
			      hiveDao.queryBuilder().where()
			         .eq("yardID_id", yardID)
			         .prepare());
			
			
			
			for(int i = 0; i < hivelistObj.size(); i++){
				String yardName = hivelistObj.get(i).getHiveName();
				hivelist.add(yardName);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hivelist;
	}
	
    private void createAddHiveDialog(final int yardID){
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Hive:");
        
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

		    final TextView txName = new TextView(getActivity());
		    txName.setText("Hive Name:");
		    layout.addView(txName);
	
	        final EditText hiveName = new EditText(getActivity()); 
	        layout.addView(hiveName);
	        
	    builder.setView(layout);
	    
	    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	       	 
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            	
            	String hiveNameValue = hiveName.getText().toString();
            	
        		RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
				RuntimeExceptionDao<HiveObject, Integer> hiveDao = db.getHiveRunDao();

        		
        		List<YardObject> yardList = null;
            	
        		try {
					yardList = yardDao.query(yardDao.queryBuilder().where()
							.eq("id", yardID)
							.prepare());
					
					YardObject yardIDObject = yardList.get(0);
					
					if(hiveNameValue.equals("")){
						Toast.makeText(getActivity().getApplicationContext(), "Please write a name for the hive!",
								   Toast.LENGTH_LONG).show();
					}
					else{
						
						List<HiveObject> checkName;
						
						checkName = hiveDao.query(hiveDao.queryBuilder().where()
								.eq("hiveName", hiveNameValue)
								.and()
								.eq("yardID_id", yardID)
								.prepare());
						
						Boolean check = false;
						
						if(checkName.size() == 0)
							check = true;
						
						
						if(check == true){
							HiveObject hive = new HiveObject(hiveNameValue, yardIDObject, false);
							
							hiveDao.create(hive);
							
							Toast.makeText(getActivity().getApplicationContext(), "Hive added!",
									   Toast.LENGTH_LONG).show();
							
							updateData(yardID);
						}
						else
						{
							Toast.makeText(getActivity().getApplicationContext(), "Hive Name already exists! Please choose another!",
									   Toast.LENGTH_LONG).show();
						}
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
            	return;
            }
            
	    });
	    builder.show();
    }
	
}
