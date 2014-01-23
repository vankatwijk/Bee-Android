package com.example.beeproject.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beeproject.R;
import com.example.beeproject.global.classes.GlobalVar;


public class FragmentProfileInfo extends Fragment {


	private static final int MODE_PRIVATE = 0;

	public FragmentProfileInfo() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragmentprofileinfo,
				container, false);
		
		SharedPreferences pref = this.getActivity().getSharedPreferences(GlobalVar.USERPREFS,MODE_PRIVATE);
		String username = pref.getString(GlobalVar.PREFS_login_username, null);
		
		TextView usernameView;
		usernameView = (TextView) rootView.findViewById(R.id.UsernameTextView);
		usernameView.setText("USERNAME : "+username);

		return rootView;
	}
	
}