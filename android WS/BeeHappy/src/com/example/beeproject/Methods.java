package com.example.beeproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.beeproject.yards.FragmentAddYard;
import com.example.beeproject.yards.FragmentEditYard;
import com.example.beeproject.yards.FragmentHive;
import com.example.beeproject.yards.FragmentYard;
import com.example.beeproject.yards.FragmentYardList;

public class Methods extends FragmentActivity{
	
	public static int SelectedYard = -1;
	
	public void yardSelected(int arg1){
		
		SelectedYard = arg1;
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
	
	public void addYard(int arg2){
		SelectedYard = arg2;
		
		FragmentAddYard fragment = new FragmentAddYard();
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();
	}
	
	public void finishaddYard(int arg3){
		SelectedYard = arg3;
		
		FragmentYardList fragment = new FragmentYardList();
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();
		
	}
	
	public void editSelectedYard(String selectedYard){
		
		Bundle bundle = new Bundle();
		bundle.putString("selectedYard", selectedYard);
		
		FragmentEditYard fragment = new FragmentEditYard();
		
		fragment.setArguments(bundle);
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();
		
	}

}

