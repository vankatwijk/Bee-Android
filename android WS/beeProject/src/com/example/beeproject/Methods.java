package com.example.beeproject;

import android.support.v4.app.FragmentActivity;

public class Methods extends FragmentActivity{
	
	public static int SelectedYard = -1;
	
	public void yardSelected(int arg2){
		
		SelectedYard = arg2;
		FragmentYard fragment = new FragmentYard();	
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();	
		
	}
	
	public void hiveSelected(int arg2){
		
		SelectedYard = arg2;
		FragmentHive fragment = new FragmentHive();
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();
	}

}

