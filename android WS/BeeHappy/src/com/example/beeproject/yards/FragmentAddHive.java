package com.example.beeproject.yards;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beeproject.Methods;
import com.example.beeproject.R;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.HiveObject;
import com.example.beeproject.global.classes.YardObject;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class FragmentAddHive extends Fragment {

	
	
	int yardID = 0;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
    	
		final View view = inflater.inflate(R.layout.fragmentaddhive,container, false);
		
		Bundle args = getArguments();
	    yardID = args.getInt("yardID");
		
		Button addHive = (Button)view.findViewById(R.id.addHiveButton);
		
		addHive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				YardObject yard = getYardID();
				
				EditText hiveNameET = (EditText)view.findViewById(R.id.hiveNameET);
				String hiveName = hiveNameET.getText().toString();
				
				if(hiveName.equals("")){
					Toast.makeText(getActivity().getApplicationContext(), "Please write a name for the hive!",
							   Toast.LENGTH_LONG).show();
				}
				else{
					DatabaseManager dbManager = new DatabaseManager();
					DatabaseHelper db = dbManager.getHelper(getActivity().getApplicationContext());
					
					RuntimeExceptionDao<HiveObject, Integer> hiveDao = db.getHiveRunDao();
					
					List<HiveObject> checkName;
					
					try {
						checkName = hiveDao.query(hiveDao.queryBuilder().where()
								.eq("hiveName", hiveName)
								.and()
								.eq("yardID_id", yardID)
								.prepare());
						
						Boolean check = false;
						
						if(checkName.size() == 0)
							check = true;
						
						
						if(check == true){
							HiveObject hive = new HiveObject(hiveName, yard, false);
							
							hiveDao.create(hive);
							
							Toast.makeText(getActivity().getApplicationContext(), "Hive added!",
									   Toast.LENGTH_LONG).show();
							((Methods) getActivity()).finishaddHive(yardID);
						}
						else
						{
							Toast.makeText(getActivity().getApplicationContext(), "Hive Name already exists! Please choose another!",
									   Toast.LENGTH_LONG).show();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
			}
		});
		
		
		
	    
				
	    
	    
	    
		
				
		
				
			

			
		
		
		
		
		return view;
	}

	private YardObject getYardID() {
		
		DatabaseManager dbManager = new DatabaseManager();
		DatabaseHelper db = dbManager.getHelper(getActivity().getApplicationContext());
		RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
		
		List<YardObject> yardList = null;
		
		try {
			yardList = yardDao.query(yardDao.queryBuilder().where()
					.eq("id", yardID)
					.prepare());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		YardObject yard = yardList.get(0);
		
		return yard;
	}
	
}
