package com.example.beeproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.beeproject.calendar.CalendarActivity;
import com.example.beeproject.global.classes.GlobalVar;
import com.example.beeproject.login.LoginActivity;
import com.example.beeproject.profile.FragmentProfileInfo;
import com.example.beeproject.statistics.Statistics;
import com.example.beeproject.syncing.SyncTask;
import com.example.beeproject.syncing.SyncTaskCallback;
import com.example.beeproject.weather.WeatherActivity;
import com.example.beeproject.yards.FragmentYard;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link MainActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ItemDetailFragment}.
 */
public class ContainerActivity extends Methods{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);


		if (savedInstanceState == null) {

			
			if(MainActivity.status == "1"){			
				FragmentYard fragment = new FragmentYard();
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
				
			}else if(MainActivity.status == "2"){

				Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
				startActivity(intent);					
		        
			}else if(MainActivity.status == "3"){
								
				Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
				startActivity(intent);	
					
		        
			}else if(MainActivity.status == "4"){

				FragmentProfileInfo fragment = new FragmentProfileInfo();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
					
		        
			}else if(MainActivity.status == "5"){

				/*FragmentStatistics fragment = new FragmentStatistics();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
				*/
				Intent intent = new Intent(getApplicationContext(), Statistics.class);
				startActivity(intent);	
		        
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this,
					new Intent(this, MainActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void LogoutOfActivity(MenuItem v){
		//set username and password to null in shared prefs
		getSharedPreferences(GlobalVar.USERPREFS,MODE_PRIVATE)
        .edit()
        .putString(GlobalVar.PREFS_login_username, null)
        .putString(GlobalVar.PREFS_login_encodePassword, null)
        .commit();
		//display a logout message
		Toast.makeText(getApplicationContext(), "You have been logged out !",
				   Toast.LENGTH_LONG).show();
		
		//switch to the login activity
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
	}
	
	public void syncroniseToServer(MenuItem v){

		SyncTaskCallback syncTaskCallback = new SyncTaskCallback() {
			@Override
			public void onSyncTaskFinished(String result) {
				Toast.makeText(getApplicationContext(), result,  Toast.LENGTH_LONG).show();
			}
		};
		
    	new SyncTask(this, syncTaskCallback).execute("");
    }
	
}
