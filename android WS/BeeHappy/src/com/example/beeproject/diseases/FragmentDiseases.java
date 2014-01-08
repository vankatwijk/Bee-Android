package com.example.beeproject.diseases;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beeproject.R;
import com.example.beeproject.syncing.SyncTask;
import com.example.beeproject.syncing.SyncTaskCallback;



public class FragmentDiseases extends Fragment {
	public final String LOG_TAG ="DebugServlet";
	TextView diseasesTextView;
	
	public FragmentDiseases() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragmentdiseases, container, false);
		diseasesTextView = (TextView) rootView.findViewById(R.id.DiseasesTextView);
		
    	
		return rootView;
	}
	
	public void syncroniseToServer(MenuItem v){

		SyncTaskCallback syncTaskCallback = new SyncTaskCallback() {
			@Override
			public void onSyncTaskFinished(String result) {
				Toast.makeText(getActivity().getApplicationContext(), result,  Toast.LENGTH_LONG).show();
			}
		};
		
    	new SyncTask(getActivity(), syncTaskCallback).execute("");
    }
	
}