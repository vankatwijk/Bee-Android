package com.example.beeproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


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

				FragmentStatistics fragment = new FragmentStatistics();				
				getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();	
					
		        
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
