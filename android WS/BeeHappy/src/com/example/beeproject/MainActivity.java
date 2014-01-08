package com.example.beeproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.beeproject.calendar.FragmentCalender;
import com.example.beeproject.diseases.FragmentDiseases;
import com.example.beeproject.global.classes.GlobalVar;
import com.example.beeproject.login.FragmentLogin;
import com.example.beeproject.login.LoginActivity;
import com.example.beeproject.profile.FragmentProfileInfo;
import com.example.beeproject.statistics.FragmentStatistics;
import com.example.beeproject.statistics.Statistics;
import com.example.beeproject.weather.WeatherActivity;
import com.example.beeproject.yards.FragmentYardList;


public class MainActivity extends FragmentActivity implements
		ItemListFragment.Callbacks {

	private boolean mTwoPane;
	public static String status = "0";  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		if (findViewById(R.id.item_detail_container) != null) {
			mTwoPane = true;

			((ItemListFragment) getSupportFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	
	@Override
	public void onItemSelected(String id) {
		status = id;
		if (mTwoPane) {
			if(id == "1"){			
				FragmentYardList fragment = new FragmentYardList();
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
				
			}else if(id == "2"){

				Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
				startActivity(intent);
		        
			}else if(id == "3"){

				FragmentCalender fragment = new FragmentCalender();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
					
		        
			}else if(id == "4"){

				FragmentProfileInfo fragment = new FragmentProfileInfo();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
					
		        
			}else if(id == "5"){

				/*FragmentStatistics fragment = new FragmentStatistics();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
				*/
				Intent intent = new Intent(getApplicationContext(), Statistics.class);
				startActivity(intent);	
		        
			}else if(id == "6"){

				FragmentDiseases fragment = new FragmentDiseases();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
					 
			}

		} else {
			Intent detailIntent = new Intent(this, ContainerActivity.class);
			startActivity(detailIntent);
		}
	}
}
