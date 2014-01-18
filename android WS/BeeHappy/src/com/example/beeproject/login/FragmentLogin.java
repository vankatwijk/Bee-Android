package com.example.beeproject.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.beeproject.R;

public class FragmentLogin extends Fragment{

	public OnClickLoginButton mOnClickLoginButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        super.onCreateView(inflater, container, savedInstanceState);
	
        final View view = inflater.inflate(R.layout.fragmentlogin, container, false);
        
        
        
        Button login = (Button)view.findViewById(R.id.loginButton);
        
        login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText username = (EditText)view.findViewById(R.id.usernameEditText);
		        EditText password = (EditText)view.findViewById(R.id.passwordEditText);
		        
		        final String usernameText = username.getText().toString();
		        final String passwordText = password.getText().toString();
				mOnClickLoginButton.loginToApp(usernameText, passwordText);
				
			}
		});
        
        Button createAccountFragment = (Button)view.findViewById(R.id.createAccountFragment);
        
        createAccountFragment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mOnClickLoginButton.createAccountFragment();
				
			}
		});
        
        
        return view;
        
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnClickLoginButton = (OnClickLoginButton) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}
	
	public interface OnClickLoginButton{
		public void loginToApp(String username, String password);
	 	public void createAccountFragment();
	}
	
}
