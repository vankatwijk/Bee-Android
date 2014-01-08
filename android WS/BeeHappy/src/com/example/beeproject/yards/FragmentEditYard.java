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
import com.example.beeproject.global.classes.YardObject;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;

public class FragmentEditYard extends Fragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
    	
		final View view = inflater.inflate(R.layout.fragmentedityard,container, false);
		
		 DatabaseManager dbManager = new DatabaseManager();
		 DatabaseHelper db = dbManager.getHelper(getActivity());
		
		 int userID = GlobalVar.getInstance().getUserID();
		 Bundle args = this.getArguments();
		 
		 final EditText yardNameET = (EditText)view.findViewById(R.id.editYardName);
		 final EditText yardLocET = (EditText)view.findViewById(R.id.editYardLocation);
		 
		 final String selectedYard = args.getString("selectedYard");
		 
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
			
			yardNameET.append(yard.getYardName());
			yardLocET.append(yard.getLocation());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 Button saveEdit = (Button)view.findViewById(R.id.saveEditYardButton);
		 
		 saveEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try {
					UpdateBuilder<YardObject, Integer> updateBuilder = yardDao.updateBuilder();
					updateBuilder.updateColumnValue("yardName", yardNameET.getText().toString());
					updateBuilder.updateColumnValue("location", yardLocET.getText().toString());
					updateBuilder.updateColumnValue("synced", false); //object was changed, need to be synced to the server
					updateBuilder.where().eq("yardName", selectedYard);
					updateBuilder.update();
					
					
					
					Toast.makeText(getActivity().getApplicationContext(), "Changes have been saved!",
							   Toast.LENGTH_LONG).show();
					
					((Methods) getActivity()).finishaddYard(1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		});
		 
		return view;
	}
}
