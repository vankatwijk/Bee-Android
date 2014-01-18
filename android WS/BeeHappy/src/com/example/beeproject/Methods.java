package com.example.beeproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.beeproject.yards.FragmentCheckForm;
import com.example.beeproject.yards.FragmentHive;
import com.example.beeproject.yards.FragmentYard;

public class Methods extends FragmentActivity{
	
	public static int SelectedYard = -1;
	
	public void yardSelected(int yardID){
		
		Bundle bundle = new Bundle();
		bundle.putInt("yardID", yardID);
		
		FragmentHive fragment = new FragmentHive();	
		
		fragment.setArguments(bundle);
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();	
		
	}
	
	public void finishaddYard(int arg3){
		SelectedYard = arg3;
		
		FragmentYard fragment = new FragmentYard();
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();
		
	}

	public void finishaddHive(int yardID) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("yardID", yardID);
		
		FragmentHive fragment = new FragmentHive();	
		
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
		
		FragmentHive fragment = new FragmentHive();	
		
		fragment.setArguments(bundle);
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.item_detail_container, fragment).commit();
		
	}

}

