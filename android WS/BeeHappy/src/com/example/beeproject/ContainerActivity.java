package com.example.beeproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.example.beeproject.calendar.FragmentCalender;
import com.example.beeproject.diseases.FragmentDiseases;
import com.example.beeproject.profile.FragmentProfileInfo;
import com.example.beeproject.statistics.FragmentStatistics;
import com.example.beeproject.weather.WeatherActivity;
import com.example.beeproject.yards.FragmentYardList;

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
				FragmentYardList fragment = new FragmentYardList();
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
				
			}else if(MainActivity.status == "2"){

				Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
				startActivity(intent);					
		        
			}else if(MainActivity.status == "3"){

				FragmentCalender fragment = new FragmentCalender();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
					
		        
			}else if(MainActivity.status == "4"){

				FragmentProfileInfo fragment = new FragmentProfileInfo();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
					
		        
			}else if(MainActivity.status == "5"){

				FragmentStatistics fragment = new FragmentStatistics();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
					
		        
			}else if(MainActivity.status == "6"){

				FragmentDiseases fragment = new FragmentDiseases();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
					 
			}
		}
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
}
