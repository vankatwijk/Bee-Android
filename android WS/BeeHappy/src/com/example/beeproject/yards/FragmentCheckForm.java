package com.example.beeproject.yards;

import java.security.Timestamp;
import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beeproject.Methods;
import com.example.beeproject.R;
import com.example.beeproject.global.classes.CheckFormObject;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.DiseaseObject;
import com.example.beeproject.global.classes.HiveObject;
import com.example.beeproject.global.classes.OutbrakeObject;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class FragmentCheckForm extends Fragment {
	
	String hiveName = null;
	int yardID = 0;
	
	DatabaseManager dbManager = new DatabaseManager();
	DatabaseHelper db = dbManager.getHelper(getActivity());
	
	RuntimeExceptionDao<CheckFormObject, Integer> checkFormDao = db.getCheckFormRunDao();
	RuntimeExceptionDao<HiveObject, Integer> hiveDao = db.getHiveRunDao();
	RuntimeExceptionDao<OutbrakeObject, Integer> outbrakeDao = db.getOutbrakeRunDao();
	RuntimeExceptionDao<DiseaseObject, Integer> diseaseDao = db.getDiseaseRunDao();

	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
		
		final View view = inflater.inflate(R.layout.fragmentcheckform,container, false);
		
		Bundle args = getArguments();
        hiveName = args.getString("hiveName");
        yardID = args.getInt("yardID");
        
        TextView hiveNameTV = (TextView)view.findViewById(R.id.hiveName);
        hiveNameTV.setText(hiveName);
        
        final CheckBox hasQueen = (CheckBox)view.findViewById(R.id.queenCheckBox);
        final EditText qDateBornET = (EditText)view.findViewById(R.id.qDateBorn);
        final CheckBox qWingsClippedCB = (CheckBox)view.findViewById(R.id.qWingsClippedCB);
        final EditText qRaceET = (EditText)view.findViewById(R.id.qRace);
        final EditText nrOfFramesET = (EditText)view.findViewById(R.id.nrOfFrames);
        final EditText ocupiedFramesET = (EditText)view.findViewById(R.id.ocupiedFrames);
        final EditText nrOfLayersET = (EditText)view.findViewById(R.id.nrOfLayersET);
        final EditText eggsET = (EditText)view.findViewById(R.id.eggs);
        final EditText larveET = (EditText)view.findViewById(R.id.larve);
        final EditText pupaeET = (EditText)view.findViewById(R.id.pupae);
        final EditText nrOfMitesET = (EditText)view.findViewById(R.id.nrOfMites);
        final EditText honeyCombsET = (EditText)view.findViewById(R.id.honeyCombsET);
        final EditText commentsET = (EditText)view.findViewById(R.id.commentsET);
        final TextView diseaseTV = (TextView)view.findViewById(R.id.diseaseStatus);
        
        
  
        final List<HiveObject> hiveIDList;
        
        try {
			hiveIDList = hiveDao.query(
				      hiveDao.queryBuilder().where()
				         .eq("hiveName", hiveName)
				         .prepare());
			
			final int hiveID = hiveIDList.get(0).getId();
	        
	        List<CheckFormObject> checkFormList;
	        
	        checkFormList = checkFormDao.query(checkFormDao.queryBuilder()
	        							 .where()
	        							 .eq("hiveID_id", hiveID)
	        							 .prepare());
	        
	        
	        
	        if(checkFormList.size() != 0){
	        	CheckFormObject lastCheckForm = checkFormList.get(checkFormList.size()-1);
	        	
	        	if(lastCheckForm.isHasQueen()){
	        		hasQueen.setChecked(true);
	        		qDateBornET.setText(lastCheckForm.getqDateBorn());
	        		if(lastCheckForm.isqWingsCliped()){
	        			qWingsClippedCB.setChecked(true);
	        		}
	        		qRaceET.setText(lastCheckForm.getqRace());
	        	}
	        	
	        	nrOfFramesET.setText(Integer.toString(lastCheckForm.getNrOfFrames()));
	        	ocupiedFramesET.setText(Integer.toString(lastCheckForm.getOccupiedFrames()));
	        	nrOfLayersET.setText(Integer.toString(lastCheckForm.getNrOfLayers()));
	        	eggsET.setText(Integer.toString(lastCheckForm.getEggs()));
	        	larveET.setText(Integer.toString(lastCheckForm.getLarve()));
	        	pupaeET.setText(Integer.toString(lastCheckForm.getPupae()));
	        	nrOfMitesET.setText(Integer.toString(lastCheckForm.getNrOfMites()));
	        	honeyCombsET.setText(Integer.toString(lastCheckForm.getHoneyCombs()));
	        	commentsET.setText(lastCheckForm.getComments());
	        }
	        
	        List<OutbrakeObject> checkOutbrake;
	        
	        checkOutbrake = outbrakeDao.query(outbrakeDao.queryBuilder()
					 .where()
					 .eq("hiveID_id", hiveID)
					 .and()
					 .isNull("endDate")
					 .prepare());
	        
	        if(checkOutbrake.size() > 0){
	        		List<DiseaseObject> diseaseList;
		        	diseaseList = diseaseDao.query(diseaseDao.queryBuilder()
							 .where()
							 .eq("id", checkOutbrake.get(checkOutbrake.size()-1).getDiseaseID().getId())
							 .prepare());
		        	
		        	diseaseTV.setText(diseaseList.get(0).getDiseaseName());
	        }else if(checkOutbrake.size() == 0){
	        	diseaseTV.setText("No disease");
	        }
	        
	        Button addDisease = (Button)view.findViewById(R.id.AddDisease);
	        
	        addDisease.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(diseaseTV.getText().toString().equals("No disease")){
						Intent intent = new Intent(getActivity(), OutbrakeActivity.class);
						intent.putExtra("hiveID", hiveID);
						startActivity(intent);
					}else{
						Toast.makeText(getActivity().getApplicationContext(), "Only one disease per hive can be set at once!",
								   Toast.LENGTH_LONG).show();
					}
				}
			});
	        
	        Button checkDisease = (Button)view.findViewById(R.id.checkDiseaseButton);
	        
	        checkDisease.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!diseaseTV.getText().toString().equals("No disease")){
						Intent intent = new Intent(getActivity(), CheckDiseaseActivity.class);
						intent.putExtra("hiveID", hiveID);
						intent.putExtra("diseaseName", diseaseTV.getText().toString());
						startActivity(intent);
					}
				}
			});
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        Button saveForm = (Button)view.findViewById(R.id.saveForm);
        
        saveForm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int nrOfFrames;
				int ocupiedFrames;
				int nrOfLayers;
				int eggs;
				int larve;
				int pupae;
				int nrOfMites;
				int honeyCombs;
				
				String nrOfFramesS = nrOfFramesET.getText().toString();
				if(nrOfFramesS.equals("")){
					nrOfFrames = 0;
				}else
				{
					nrOfFrames = Integer.parseInt(nrOfFramesS);
				}
				
				String ocupiedFramesS = ocupiedFramesET.getText().toString();
				if(ocupiedFramesS.equals("")){
					ocupiedFrames = 0;
				}else
				{
					ocupiedFrames = Integer.parseInt(ocupiedFramesS);
				}
				
				String nrOfLayersS = nrOfLayersET.getText().toString();
				if(nrOfLayersS.equals("")){
					nrOfLayers = 0;
				}else
				{
					nrOfLayers = Integer.parseInt(nrOfLayersS);
				}
				
				String eggsS = eggsET.getText().toString();
				if(eggsS.equals("")){
					eggs = 0;
				}else
				{
					eggs = Integer.parseInt(eggsS);
				}
				
				String larveS = larveET.getText().toString();
				if(larveS.equals("")){
					larve = 0;
				}else
				{
					larve = Integer.parseInt(larveS);
				}
				
				String pupaeS = pupaeET.getText().toString();
				if(pupaeS.equals("")){
					pupae = 0;
				}else
				{
					pupae = Integer.parseInt(pupaeS);
				}
				
				String nrOfMitesS = nrOfMitesET.getText().toString();
				if(nrOfMitesS.equals("")){
					nrOfMites = 0;
				}else
				{
					nrOfMites = Integer.parseInt(nrOfMitesS);
				}
				
				String honeyCombsS = honeyCombsET.getText().toString();
				if(honeyCombsS.equals("")){
					honeyCombs = 0;
				}else
				{
					honeyCombs = Integer.parseInt(honeyCombsS);
				}
				
				String comments = commentsET.getText().toString();
				
				Long timestamp = System.currentTimeMillis();///1000;
				
				
				List<HiveObject> hiveIDList = null;
				
				try {
					hiveIDList = hiveDao.query(
						      hiveDao.queryBuilder().where()
						         .eq("hiveName", hiveName)
						         .prepare());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				HiveObject hiveID = hiveIDList.get(0);
				
				
				if(hasQueen.isChecked()){				
					String qDateBorn = qDateBornET.getText().toString();
					
					Boolean qWingsClipped = false;
					if(qWingsClippedCB.isChecked())
						qWingsClipped = true;
					String qRace = qRaceET.getText().toString();
					
					CheckFormObject checkform = new CheckFormObject(hiveID, timestamp, true, qDateBorn, qWingsClipped, qRace, nrOfFrames, ocupiedFrames, nrOfLayers, eggs, larve, pupae, nrOfMites, honeyCombs, comments, false);
					checkFormDao.create(checkform);
					
					Toast.makeText(getActivity().getApplicationContext(), "Checkform saved!",
							   Toast.LENGTH_LONG).show();
					
					((Methods) getActivity()).finishcheckform(yardID);
					
				}
				else{
					CheckFormObject checkform = new CheckFormObject(hiveID, timestamp, false, null, false, null, nrOfFrames, ocupiedFrames, nrOfLayers, eggs, larve, pupae, nrOfMites, honeyCombs, comments, false);
					checkFormDao.create(checkform);
					
					Toast.makeText(getActivity().getApplicationContext(), "Checkform saved!",
							   Toast.LENGTH_LONG).show();
					
					((Methods) getActivity()).finishcheckform(yardID);
				}
			}
		});
		
	return view;
	}
	
	@Override
	 public void onResume() {
	     super.onResume();
	     System.out.println("Testing");
	     
	     final TextView diseaseTV = (TextView)getView().findViewById(R.id.diseaseStatus);

	     Bundle args = getArguments();
	     hiveName = args.getString("hiveName");
	     
	     List<HiveObject> hiveIDList;
	     
	     try {
			hiveIDList = hiveDao.query(
				      hiveDao.queryBuilder().where()
				         .eq("hiveName", hiveName)
				         .prepare());
			
			final int hiveID = hiveIDList.get(0).getId();
		     
		     List<OutbrakeObject> checkOutbrake;
		        
		        checkOutbrake = outbrakeDao.query(outbrakeDao.queryBuilder()
						 .where()
						 .eq("hiveID_id", hiveID)
						 .and()
						 .isNull("endDate")
						 .prepare());
		        
		        if(checkOutbrake.size() > 0){
		        		List<DiseaseObject> diseaseList;
			        	diseaseList = diseaseDao.query(diseaseDao.queryBuilder()
								 .where()
								 .eq("id", checkOutbrake.get(checkOutbrake.size()-1).getDiseaseID().getId())
								 .prepare());
			        	
			        	diseaseTV.setText(diseaseList.get(0).getDiseaseName());
		        }else if(checkOutbrake.size() == 0){
		        	diseaseTV.setText("No disease");
		        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	 }
}
