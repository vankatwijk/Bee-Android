package com.example.beeproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.beeproject.yards.FragmentAddHive;
import com.example.beeproject.yards.FragmentAddYard;
import com.example.beeproject.yards.FragmentCheckForm;
import com.example.beeproject.yards.FragmentEditYard;
import com.example.beeproject.yards.FragmentHive;
import com.example.beeproject.yards.FragmentYard;
import com.example.beeproject.yards.FragmentYardList;

public class Methods extends FragmentActivity{
	
	public static int SelectedYard = -1;
	
	public void yardSelected(int yardID){
		
		Bundle bundle = new Bundle();
		bundle.putInt("yardID", yardID);
		
		FragmentYard fragment = new FragmentYard();	
		
		fragment.setArguments(bundle);
		
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
	
	public void addHive(int yardID){
		
		Bundle bundle = new Bundle();
		bundle.putInt("yardID", yardID);
		
		FragmentAddHive fragment = new FragmentAddHive();	
		
		fragment.setArguments(bundle);
		
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

	public void finishaddHive(int yardID) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("yardID", yardID);
		
		FragmentYard fragment = new FragmentYard();	
		
		fragment.setArguments(bundle);
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();
		
	}

	public void checkform(String hiveName, int yardID) {
		Bundle bundle = new Bundle();
		bundle.putString("hiveName", hiveName);
		bundle.putInt("yardID", yardID);
		
		FragmentCheckForm fragment = new FragmentCheckForm();	
		
		fragment.setArguments(bundle);
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();
		
	}

	public void finishcheckform(int yardID) {
		Bundle bundle = new Bundle();
		bundle.putInt("yardID", yardID);
		
		FragmentYard fragment = new FragmentYard();	
		
		fragment.setArguments(bundle);
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();
		
	}

}

