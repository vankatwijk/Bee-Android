package com.example.beeproject.yards;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import com.example.beeproject.global.classes.GlobalVar;
import com.example.beeproject.global.classes.HiveObject;
import com.example.beeproject.global.classes.StockObject;
import com.example.beeproject.global.classes.UserObject;
import com.example.beeproject.global.classes.YardObject;
import com.example.beeproject.syncing.DeletedObject;
import com.example.beeproject.syncing.SyncHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;


public class FragmentYard extends Fragment {

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
				createAddYardDialog();
				
				
			}
		});
		
		
        registerForContextMenu(yardListView);
        
        Button checkStock = (Button)getView().findViewById(R.id.checkStockButton);
        
        checkStock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createStockDialog();
				
				
			}
		});
        
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
			createEditYardDialog(selectedYard);
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
        	            		int yardServerSideId = yardIDList.get(0).getServerSideID();
        	            		
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
								
								int nrDeletedRows = deleteBuilder.delete();
								if(nrDeletedRows == 1){
									/* A YardObject has been deleted from the local database.
									 * Therefore, the infromation about the deleted object must be saved in DeletedObjects table
									 * in order to syncronise the deleting to the server 
									 * next time the syncronisation (i.e SyncHelper.syncronizeToServer()) is run 
									 * */
									SyncHelper syncHelper = new SyncHelper(getActivity());
									DeletedObject deletedObjectInfo = new DeletedObject(YardObject.class.getName(), yardServerSideId);
									syncHelper.storeDeletedObjectForSyncronisation(deletedObjectInfo);
								}
								
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
	
	private void createAddYardDialog(){
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Yard:");
        
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

		    final TextView txName = new TextView(getActivity());
		    txName.setText("Yard Name:");
		    layout.addView(txName);
	
	        final EditText yardName = new EditText(getActivity()); 
	        layout.addView(yardName);
	        
	        final TextView txLocation = new TextView(getActivity());
		    txLocation.setText("Yard Location:");
		    layout.addView(txLocation);
	
	        final EditText yardLocation = new EditText(getActivity()); 
	        layout.addView(yardLocation);
	        
	    builder.setView(layout);

	    
	    
	    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
       	 
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            
            	RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
        		RuntimeExceptionDao<UserObject, Integer> userDao = db.getUserRunDao();
            	
            	String yardNameValue = yardName.getText().toString();
            	String yardLocationValue = yardLocation.getText().toString();
            	
            	List<UserObject> user;
				List<YardObject> checkName;
            	
				if(yardNameValue.equals("")){
					Toast.makeText(getActivity().getApplicationContext(), "Please write a name for the yard!",
							   Toast.LENGTH_LONG).show();
				}else if(yardLocationValue.equals("")){
					Toast.makeText(getActivity().getApplicationContext(), "Please write a location for the yard!",
							   Toast.LENGTH_LONG).show();
				}else {
				
					try {
						user = userDao.query(
								userDao.queryBuilder().where()
							         .eq("id", userID)
							         .prepare());
						
						Boolean check = false;
						
						UserObject userIDObject = user.get(0);
						
						checkName = yardDao.query(yardDao.queryBuilder().where()
								.eq("yardName", yardNameValue)
								.and()
								.eq("userID_id", userID)
								.prepare());
						
						if(checkName.size() == 0)
							check = true;
						
						if(check == true){
							YardObject yard = new YardObject(yardNameValue, yardLocationValue, userIDObject, false);
							
							yardDao.create(yard);
							
							Toast.makeText(getActivity().getApplicationContext(), "Yard Created!",
									   Toast.LENGTH_LONG).show();
							
							updateData();
						}
						else{
							Toast.makeText(getActivity().getApplicationContext(), "Yard Name already exists! Please choose another!",
									   Toast.LENGTH_LONG).show();
						}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	            	return;
				}
            }
            
	    });
	    
	    builder.show();
	    
	}
	
	private void createEditYardDialog(final String selectedYard){
		
		final RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
		
		List<YardObject> searchyard;
		
		try {
			searchyard = yardDao.query(
				      yardDao.queryBuilder().where()
				      	 .eq("yardName", selectedYard)
				      	 .and()
				         .eq("userID_id", userID)
				         .prepare());
			
			YardObject yard = searchyard.get(0);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Edit Yard:");
			
	        LinearLayout layout = new LinearLayout(getActivity());
	        layout.setOrientation(LinearLayout.VERTICAL);

			    final TextView txName = new TextView(getActivity());
			    txName.setText("Yard Name:");
			    layout.addView(txName);
		
		        final EditText yardNameET = new EditText(getActivity());
		        yardNameET.setText(yard.getYardName());
		        layout.addView(yardNameET);
		        
		        final TextView txLocation = new TextView(getActivity());
			    txLocation.setText("Yard Location:");
			    layout.addView(txLocation);
		
		        final EditText yardLocationET = new EditText(getActivity());
		        yardLocationET.setText(yard.getLocation());
		        layout.addView(yardLocationET);
		        
		    builder.setView(layout);
		    
		    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        	 
	            @Override
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	
	            	
					try {
						UpdateBuilder<YardObject, Integer> updateBuilder = yardDao.updateBuilder();
						updateBuilder.updateColumnValue("yardName", yardNameET.getText().toString());
						updateBuilder.updateColumnValue("location", yardLocationET.getText().toString());
						updateBuilder.updateColumnValue("synced", false); //object was changed, need to be synced to the server
						updateBuilder.where().eq("yardName", selectedYard);
						updateBuilder.update();
						
						updateData();
						
						Toast.makeText(getActivity().getApplicationContext(), "Changes have been saved!",
								   Toast.LENGTH_LONG).show();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	            	
	            	return;
	            }
		    });
		    builder.show();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void createStockDialog(){
		final RuntimeExceptionDao<StockObject, Integer> stockDao = db.getStockRunDao();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Current Stock:");
        builder.setMessage("This is the current number of frames available. Click to update:");
        
        final Boolean check = checkIfStockExists(); 
        
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        if(check == true){
    		List<StockObject> stockList;
    		try {
				stockList = stockDao.query(
					      stockDao.queryBuilder().where()
					         .eq("id", userID)
					         .prepare());
				
				input.setText(stockList.get(0).getNumberOfFrames());
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        builder.setView(input);
        
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        	 
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                              
                
                if(check == false){
                	StockObject stock = new StockObject(userID, value);
                	stockDao.create(stock);
                } else if(check == true){           	
                	try {
                		UpdateBuilder<StockObject, Integer> updateBuilder = stockDao.updateBuilder();
                    	updateBuilder.updateColumnValue("numberOfFrames", value);
						updateBuilder.where().eq("id", userID);
						updateBuilder.update();
						
						Toast.makeText(getActivity().getApplicationContext(), "Changes have been saved!",
								   Toast.LENGTH_LONG).show();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
                }
                
                return;
            }
            
            
        });
        builder.show();
        
	}
	
	private boolean checkIfStockExists(){
		boolean check = false;
		
		RuntimeExceptionDao<StockObject, Integer> stockDao = db.getStockRunDao();
		
		List<StockObject> stockList;
		try {
			stockList = stockDao.query(
				      stockDao.queryBuilder().where()
				         .eq("id", userID)
				         .prepare());
			if(stockList.size() > 0){
				check = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return check;
	}
}