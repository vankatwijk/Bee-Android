package com.example.beeproject.diseases;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beeproject.R;
import com.example.beeproject.commandexecution.commands.BeeCommand;
import com.example.beeproject.commandexecution.commands.PingCommand;
import com.example.beeproject.commandexecution.commands.SelectCommand;
import com.example.beeproject.commandexecution.results.BeeCommandResult;
import com.example.beeproject.global.classes.YardObject;
import com.example.beeproject.gsonconvertion.GsonProvider;
import com.example.beeproject.syncing.BeeServerHttpClient;
import com.example.beeproject.syncing.SyncHelper;
import com.google.gson.Gson;



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
		
		/*
    	Log.d(LOG_TAG, "Connecting to servlet");
    	BeeCommand command = new PingCommand();
    	new ServletTask().execute(command);
    	
    	command = new SelectCommand(YardObject.class.getName());
    	*/
    	new ServletTask().execute("");
    	
		return rootView;
	}
	
	
	class ServletTask extends AsyncTask<String, Void, String> {

	    protected String doInBackground(String... arg) {
        		Log.d(LOG_TAG, "ServletTask.doInBackground()");
        		SyncHelper syncHelper = new SyncHelper(getActivity());
        		
	            String result = syncHelper.syncronizeToServer();
	            
        		
	            return result;
	    }

	    protected void onPostExecute(String result) {
	    	Log.d(LOG_TAG, "ServletTask.onPostExecute(), result=["+result+"]");
	    	
	    	if(result!=null){
	    		diseasesTextView.setText(diseasesTextView.getText() + "\n" + result.toString());
	    	}
	    	else{
	    		diseasesTextView.setText(diseasesTextView.getText() + "\n" + "--");
	    	}
	    }
	}
	
}