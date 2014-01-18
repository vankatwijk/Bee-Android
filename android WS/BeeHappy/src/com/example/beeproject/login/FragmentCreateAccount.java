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

public class FragmentCreateAccount extends Fragment {
	
	public OnClickCreateButton mOnClickCreateButton;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        super.onCreateView(inflater, container, savedInstanceState);
	
        final View view = inflater.inflate(R.layout.fragmentcreateaccount, container, false);
        
        Button createAccount = (Button)view.findViewById(R.id.createAccount);
        
        
        createAccount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText username = (EditText)view.findViewById(R.id.newusernameEditText);
		        EditText password = (EditText)view.findViewById(R.id.newpasswordEditText);
		        
		        final String usernameText = username.getText().toString();
		        final String passwordText = password.getText().toString();
				mOnClickCreateButton.createAccount(usernameText, passwordText);
				
			}
		});
        
        Button cancelCreateAccount = (Button)view.findViewById(R.id.cancelCreateAccountButton);
        
        cancelCreateAccount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mOnClickCreateButton.cancelCreateAccount();
			}
        });
        return view;
        
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnClickCreateButton = (OnClickCreateButton) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}
	
	public interface OnClickCreateButton{
		public void createAccount(String username, String password);
		public void cancelCreateAccount();
	}
}
