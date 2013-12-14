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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.beeproject.Methods;
import com.example.beeproject.R;
import com.example.beeproject.global.classes.CheckFormObject;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.GlobalVar;
import com.example.beeproject.global.classes.HiveObject;
import com.example.beeproject.global.classes.YardObject;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;


public class FragmentYardList extends Fragment {

	int userID = GlobalVar.getInstance().getUserID();	
	ArrayAdapter<String> aa;
	DatabaseManager dbManager = new DatabaseManager();
	DatabaseHelper db = dbManager.getHelper(getActivity());
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
    	
    	return inflater.inflate(R.layout.fragmentyardlist,container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
		
		ArrayList<String> yardlist = getYardData();
		
		final ListView yardListView = (ListView)getView().findViewById(R.id.yardListView);
        
		aa = new ArrayAdapter<String>(getActivity().getBaseContext(),
						android.R.layout.simple_list_item_1,
						yardlist);
		
		yardListView.setAdapter(aa);
		
		yardListView.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
				String yardName =(String) (yardListView.getItemAtPosition(arg2));
				
				List<YardObject> yardlistObj;
				try {
					yardlistObj = yardDao.query(
						      yardDao.queryBuilder().where()
						         .eq("userID_id", userID)
						         .and()
						         .eq("yardName", yardName)
						         .prepare());
					
					YardObject yard = yardlistObj.get(0);
					int yardId = yard.getId();
					
					((Methods) getActivity()).yardSelected(yardId);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		
		Button addYard = (Button) getView().findViewById(R.id.addYard);
		
		addYard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Methods) getActivity()).addYard(1);
				
			}
		});
		
		
        registerForContextMenu(yardListView);
        
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
		
		
		
		menu.setHeaderTitle("Yard");
		
		String[] menuItems = {"Edit", "Delete"};
		
		for (int i = 0; i<menuItems.length; i++) {
		      menu.add(Menu.NONE, i, i, menuItems[i]);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Object data = aa.getItem(info.position);
		
		final String selectedYard = data.toString();
		
		String check = item.toString();
		
		if(check.equals("Edit")){
			((Methods) getActivity()).editSelectedYard(selectedYard);
		}
		else if(check.equals("Delete")){
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        	alertDialog.setTitle("Delete Yard");
        	alertDialog.setMessage("Are you sure you want to delete the selected yard?");
        	
        	alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        	            public void onClick(DialogInterface dialog, int which) {
        	            	DatabaseManager dbManager = new DatabaseManager();
        	        		DatabaseHelper db = dbManager.getHelper(getActivity());
        	        		
        	            	RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
        	            	RuntimeExceptionDao<HiveObject, Integer> hiveDao = db.getHiveRunDao();
        	            	RuntimeExceptionDao<CheckFormObject, Integer> checkFormDao = db.getCheckFormRunDao();
        	            	
        	            	DeleteBuilder<YardObject, Integer> deleteBuilder = yardDao.deleteBuilder();
        	            	DeleteBuilder<HiveObject, Integer> deleteHiveBuilder = hiveDao.deleteBuilder();
        	            	DeleteBuilder<CheckFormObject, Integer> deleteCheckFormBuilder = checkFormDao.deleteBuilder();
        	            	
        	            	List<YardObject> yardIDList;
        	            	List<HiveObject> hiveList;
        	            	
        	            	try {
        	            		
        	            		yardIDList = yardDao.query(yardDao.queryBuilder().where()
										.eq("yardName", selectedYard)
										.prepare());
        	            		int yardID = yardIDList.get(0).getId();
        	            		
        	            		hiveList = hiveDao.query(hiveDao.queryBuilder().where()
										.eq("yardID_id", yardID)
										.prepare());
        	            		
        	            		for(int i = 0; i < hiveList.size(); i++){
        	            			HiveObject hive = hiveList.get(i);
        	            			int hiveID = hive.getId();
        	            			
        	            			deleteCheckFormBuilder.where().eq("hiveID_id", hiveID);
            	            		deleteCheckFormBuilder.delete();
            	            		
            	            		deleteHiveBuilder.where()
            	            		 .eq("id", hiveID)
									 .and()
									 .eq("yardID_id", yardID);
            	            		deleteHiveBuilder.delete();
        	            		}
        	            		
								deleteBuilder.where().eq("yardName", selectedYard)
													 .and()
													 .eq("userID_id", userID);
								deleteBuilder.delete();
								
								Toast.makeText(getActivity().getApplicationContext(), "Yard has been deleted!",
										   Toast.LENGTH_LONG).show();
								
								updateData();
								
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
	
	private void updateData() {
		ArrayList<String> yardlist = getYardData();
	    
		aa.clear();
		aa.addAll(yardlist);
		aa.notifyDataSetChanged();
	}
	
	private ArrayList<String> getYardData(){
		
		ArrayList<String> yardlist = new ArrayList<String>();
		
		List<YardObject> yardlistObj;
		RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
		try {
			yardlistObj = yardDao.query(
			      yardDao.queryBuilder().where()
			         .eq("userID_id", userID)
			         .prepare());
			
			for(int i = 0; i < yardlistObj.size(); i++){
				String yardName = yardlistObj.get(i).getYardName();
				yardlist.add(yardName);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return yardlist;
	}
	
}