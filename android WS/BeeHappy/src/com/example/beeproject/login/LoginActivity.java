package com.example.beeproject.login;

import java.sql.SQLException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.widget.Toast;

import com.example.beeproject.MainActivity;
import com.example.beeproject.R;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.GlobalVar;
import com.example.beeproject.global.classes.UserObject;
import com.example.beeproject.login.FragmentCreateAccount.OnClickCreateButton;
import com.example.beeproject.login.FragmentLogin.OnClickLoginButton;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class LoginActivity extends FragmentActivity implements OnClickLoginButton, OnClickCreateButton{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		SharedPreferences pref = getSharedPreferences(GlobalVar.USERPREFS,MODE_PRIVATE);   
		String username = pref.getString(GlobalVar.PREFS_login_username, null);
		String encodePassword = pref.getString(GlobalVar.PREFS_login_encodePassword, null);
		
		if (username == null || encodePassword == null) {
			FragmentLogin fragmentLogin = new FragmentLogin();
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_login_swap, fragmentLogin).commit();	
		}else{	
			loginToAppWithSharedPref(username, encodePassword);		
		}	
	}

	@Override
	public void createAccountFragment() {
		FragmentCreateAccount fragmentCreateAccount = new FragmentCreateAccount();
		getSupportFragmentManager().beginTransaction().replace(R.id.frame_login_swap, fragmentCreateAccount).commit();
	}

	//Gets the username and password and checks if they are correct with the entries
	//in the database--if true login the user
	@Override
	public void loginToApp(String username, String password) {
		String encodedPassowrd = null;
		try {
			encodedPassowrd = encodePassword(password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		DatabaseManager dbManager = new DatabaseManager();
		DatabaseHelper db = dbManager.getHelper(getApplicationContext());
		
		
		
		List<UserObject> user;
		try {
			RuntimeExceptionDao<UserObject, Integer> userDao = db.getUserRunDao();
			user = userDao.query(
			      userDao.queryBuilder().where()
			         .eq("username", username)
			         .and()
			         .eq("password", encodedPassowrd)
			         .prepare());
			
			if(user.size() == 1){
				Toast.makeText(getApplicationContext(), "Login Successful!",
						   Toast.LENGTH_LONG).show();
				
				UserObject userLoged = user.get(0);
				
				int userID = userLoged.getId();
				
				GlobalVar.getInstance().setUserID(userID);
				
				//save the username and password to a shared preference folder
				getSharedPreferences(GlobalVar.USERPREFS,MODE_PRIVATE)
		        .edit()
		        .putString(GlobalVar.PREFS_login_username, username)
		        .putString(GlobalVar.PREFS_login_encodePassword, encodedPassowrd)
		        .commit();
				//switch to main activity if username and password are correct
								
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				
				
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Incorrect Username or Password!",
						   Toast.LENGTH_LONG).show();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	//Gets the username and password and checks if they are correct with the entries
	//in the database--if true login the user
	public void loginToAppWithSharedPref(String username, String encodePassword) {
		
		String encodedPassowrd = encodePassword;
		
		DatabaseManager dbManager = new DatabaseManager();
		DatabaseHelper db = dbManager.getHelper(getApplicationContext());
		
		
		List<UserObject> user;
		try {
			RuntimeExceptionDao<UserObject, Integer> userDao = db.getUserRunDao();
			//there is a problem with checking for the password
			user = userDao.query(
			      userDao.queryBuilder().where()
			         .eq("username", username)
			         .prepare());
	
			if(user.size() == 1){
				Toast.makeText(getApplicationContext(), "Login Successful!",
						   Toast.LENGTH_LONG).show();
				
				UserObject userLoged = user.get(0);
				
				int userID = userLoged.getId();
				
				GlobalVar.getInstance().setUserID(userID);
				
				//switch to main activity if username and password are correct
								
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
					
			}
			else
			{
				/*Toast.makeText(getApplicationContext(), "Something has gone wrong ,Please login manually"+ user.size()+"--"+username+"--"+encodePassword,
						   Toast.LENGTH_LONG).show();*/
				
				FragmentLogin fragmentLogin = new FragmentLogin();
				getSupportFragmentManager().beginTransaction().replace(R.id.frame_login_swap, fragmentLogin).commit();	
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	//Gets the username and password and creates a new account while checking if
	//the username exists already
	@Override
	public void createAccount(String username, String password) {
		
		String encodedPassowrd = null;
		try {
			encodedPassowrd = encodePassword(password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DatabaseManager dbManager = new DatabaseManager();
		DatabaseHelper db = dbManager.getHelper(getApplicationContext());
		
		RuntimeExceptionDao<UserObject, Integer> userDao = db.getUserRunDao();
		
		UserObject user = new UserObject(username, encodedPassowrd);
		
		List<UserObject> checkuser;
		try {
			checkuser = userDao.query(
				      userDao.queryBuilder().where()
				         .eq("username", username)
				         .and()
				         .prepare());
			
			if(checkuser.size() == 0){
				
				userDao.create(user);
			
				Toast.makeText(getApplicationContext(), "Account Created!",
						   Toast.LENGTH_LONG).show();
				
				FragmentLogin fragmentCreateAccount = new FragmentLogin();
				getSupportFragmentManager().beginTransaction().replace(R.id.frame_login_swap, fragmentCreateAccount).commit();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Account exists already! Choose another username!",
						   Toast.LENGTH_LONG).show();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	//Cancels the account creation and brings the user back to login screen
		@Override
		public void cancelCreateAccount() {
			FragmentLogin fragmentCreateAccount = new FragmentLogin();
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_login_swap, fragmentCreateAccount).commit();		
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
