package com.example.beeproject.login;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.widget.Toast;

import com.example.beeproject.DatabaseHandler;
import com.example.beeproject.MainActivity;
import com.example.beeproject.R;
import com.example.beeproject.login.FragmentCreateAccount.OnClickCreateButton;
import com.example.beeproject.login.FragmentLogin.OnClickLoginButton;

public class LoginActivity extends FragmentActivity implements OnClickLoginButton, OnClickCreateButton{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		FragmentLogin fragmentLogin = new FragmentLogin();
		getSupportFragmentManager().beginTransaction().replace(R.id.frame_login_swap, fragmentLogin).commit();
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
		DatabaseHandler db = new DatabaseHandler(this);
		
		String encodedPassowrd = null;
		try {
			encodedPassowrd = encodePassword(password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Boolean login = db.checkUser(username, encodedPassowrd);
		
		if(login == true){
			Toast.makeText(getApplicationContext(), "Login Successful!",
					   Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Incorrect Username or Password!",
					   Toast.LENGTH_LONG).show();
		}
		
	}

	//Gets the username and password and creates a new account while checking if
	//the username exists already
	@Override
	public void createAccount(String username, String password) {

		DatabaseHandler db = new DatabaseHandler(this);
		
		String encodedPassowrd = null;
		try {
			encodedPassowrd = encodePassword(password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Boolean checkInsertion = db.addUser(username, encodedPassowrd);
		
		if(checkInsertion == true){
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
