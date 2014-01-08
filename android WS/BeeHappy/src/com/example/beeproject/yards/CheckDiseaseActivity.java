package com.example.beeproject.yards;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.beeproject.R;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.DiseaseNotesObject;
import com.example.beeproject.global.classes.OutbrakeObject;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;

public class CheckDiseaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkdisease);
		
		DatabaseManager dbManager = new DatabaseManager();
		DatabaseHelper db = dbManager.getHelper(this);
		
		final RuntimeExceptionDao<OutbrakeObject, Integer> outbrakeDao = db.getOutbrakeRunDao();
		final RuntimeExceptionDao<DiseaseNotesObject, Integer> diseaseNotesDao = db.getDiseaseNotesRunDao();

		
		Intent intent = getIntent();
		final int hiveID = intent.getIntExtra("hiveID", 0);
		final String diseaseName = intent.getStringExtra("diseaseName");
		
		
		TextView diseaseNameTV = (TextView)findViewById(R.id.diseaseNameTV);
		diseaseNameTV.setText(diseaseName);
		
		final EditText checkDiseaseDescription = (EditText)findViewById(R.id.checkDiseaseDescription);
		final EditText checkDiseaseDate = (EditText)findViewById(R.id.noteDate);
		final EditText diseaseEndDate = (EditText)findViewById(R.id.diseaseEndDate);
		
		final List<OutbrakeObject> outbrake;
		List<DiseaseNotesObject> diseaseNotes;	
		
		try {
			outbrake = outbrakeDao.query(outbrakeDao.queryBuilder()
					 .where()
					 .eq("hiveID_id", hiveID)
					 .and()
					 .isNull("endDate")
					 .prepare());
			
			diseaseNotes = diseaseNotesDao.query(diseaseNotesDao.queryBuilder()
					 .where()
					 .eq("outbrakeID_id", outbrake.get(0).getID())
					 .prepare());
			
			if(diseaseNotes.size() > 0){
				checkDiseaseDescription.setText(diseaseNotes.get(diseaseNotes.size()-1).getDescription());
				checkDiseaseDate.setText(diseaseNotes.get(diseaseNotes.size()-1).getDate());
			}
			
			Button saveButton = (Button)findViewById(R.id.saveCheckDisease);
			
			saveButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String description = checkDiseaseDescription.getText().toString();
					String date = checkDiseaseDate.getText().toString();
					
					DiseaseNotesObject diseaseNote = new DiseaseNotesObject(outbrake.get(0), date, description);
					diseaseNotesDao.create(diseaseNote);
					
					if(!diseaseEndDate.getText().toString().equals("")){
						
						try {
							UpdateBuilder<OutbrakeObject, Integer> updateBuilder = outbrakeDao.updateBuilder();
							updateBuilder.updateColumnValue("endDate", diseaseEndDate.getText().toString());
							updateBuilder.where().eq("id", outbrake.get(0).getID());
							updateBuilder.update();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					finish();
					
				}
			});
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
