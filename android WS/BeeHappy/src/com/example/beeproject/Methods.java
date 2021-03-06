package com.example.beeproject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.GlobalVar;
import com.example.beeproject.global.classes.UserObject;
import com.example.beeproject.yards.FragmentCheckForm;
import com.example.beeproject.yards.FragmentHive;
import com.example.beeproject.yards.FragmentYard;
import com.j256.ormlite.dao.RuntimeExceptionDao;

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
	
	public void changepass(View v){
		DatabaseManager dbManager = new DatabaseManager();
		final DatabaseHelper db = dbManager.getHelper(getApplicationContext());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Type in your new password");

		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	/*
				RuntimeExceptionDao<UserObject, Integer> userDao = db.getUserRunDao();
				userDao.updateRaw("password", input.getText().toString());*/
		    	
	    	   RuntimeExceptionDao<UserObject, Integer> userClassDao = db.getUserRunDao();
	    	   
	    	   UserObject user = userClassDao.queryForId(GlobalVar.getInstance().getUserID());
	    	   if(user != null){
	    		String newPass = null;
				try {
					newPass = encodePassword(input.getText().toString());
		    	    user.setPassword(newPass);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Something went wrong!",
							   Toast.LENGTH_LONG).show();
				}

	    	    int nrRowsUpdated = userClassDao.update(user);
	    	    if(nrRowsUpdated == 1){
					Toast.makeText(getApplicationContext(), "Your password has been changed!",
							   Toast.LENGTH_LONG).show();
	    	    }
	    	    else
	    	    {
	    	     //sth wrong, either not updated or updated too much(should not be possible....)
					Toast.makeText(getApplicationContext(), "Something went wrong,Your password has not been changed!",
							   Toast.LENGTH_LONG).show();
	    	    }
	    	   }
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.show();		
	}
	
	//Encrypts the password
	public String encodePassword(String password) throws Exception{
		
		//Byte representation of the password
		byte[ ] passwordInput = Base64.decode(password, 0);
		
		//TO DO Hide the secret key
		//Secret key
		byte[ ] sKey = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6};
		
		for(int i = 0; i < passwordInput.length; i++){
            //Copy the bytes from the string into the sKey array,
            //Leaving the rest of the array (the padding) intact
            sKey[i] = passwordInput[i];
         }
		
		SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
        //Get the cipher
        Cipher cipher = Cipher.getInstance("AES");

        //Initialize the cipher
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        //Encrypt the string into bytes
        byte[ ] encryptedBytes = cipher.doFinal(password.getBytes());

        //Convert the encrypted bytes back into a string
        String encrypted = Base64.encodeToString(encryptedBytes, 0);

        return encrypted;
	}
}

