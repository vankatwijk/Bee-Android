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
import com.example.beeproject.global.classes.GlobalVar;
import com.example.beeproject.global.classes.UserObject;
import com.example.beeproject.global.classes.YardObject;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class FragmentAddYard extends Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
    	
		final View view = inflater.inflate(R.layout.fragmentaddyard,container, false);
		
		Button addYard = (Button)view.findViewById(R.id.addYardButton);
		
		addYard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText yardNameText = (EditText)view.findViewById(R.id.yardName);
				EditText yardLocationText = (EditText)view.findViewById(R.id.yardLocation);
				
				String yardName = yardNameText.getText().toString();
				String yardLocation = yardLocationText.getText().toString();
				
				
				
				DatabaseManager dbManager = new DatabaseManager();
				DatabaseHelper db = dbManager.getHelper(getActivity().getApplicationContext());
				
				RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
				RuntimeExceptionDao<UserObject, Integer> userDao = db.getUserRunDao();
				
				int userIDValue = GlobalVar.getInstance().getUserID();
				
				List<UserObject> user;
				List<YardObject> checkName;
				
				try {
					user = userDao.query(
							userDao.queryBuilder().where()
						         .eq("id", userIDValue)
						         .prepare());
					
					UserObject userID = user.get(0);
					
					checkName = yardDao.query(yardDao.queryBuilder().where()
										.eq("yardName", yardName)
										.and()
										.eq("userID_id", userIDValue)
										.prepare());
					
					Boolean check = false;
					
					if(checkName.size() == 0)
						check = true;
					
					if(check == true){
						YardObject yard = new YardObject(yardName, yardLocation, userID, false);
						
						yardDao.create(yard);
						
						Toast.makeText(getActivity().getApplicationContext(), "Yard Created!",
								   Toast.LENGTH_LONG).show();
						
						((Methods) getActivity()).finishaddYard(1);
					}
					else{
						Toast.makeText(getActivity().getApplicationContext(), "Yard Name already exists! Please choose another!",
								   Toast.LENGTH_LONG).show();
					}
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				
				
				
				
			}
		});
		
		
		
		
		
    	return view;
    }
	
}
