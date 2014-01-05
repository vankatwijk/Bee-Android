package com.example.beeproject.diseases;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.beeproject.R;
import com.example.testbeeclient.Matrix;
import com.google.gson.Gson;


public class FragmentDiseases extends Fragment {
	public final String LOG_TAG ="DebugServlet";

	
	public FragmentDiseases() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragmentdiseases, container, false);
		
		
		return rootView;
	}
	
	
	class ServletTask extends AsyncTask<String, Void, String> {

	    protected String doInBackground(String... urls) {
        		Log.d(LOG_TAG, "ServletTask.doInBackground()");
	            String result = "";
	           

	            
	            try {
	                HttpClient httpclient = new DefaultHttpClient();
	                HttpPost httppost = new HttpPost("http://10.0.2.2:8089/TestServer/TestServlet");
	                //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	                HttpResponse response = httpclient.execute(httppost);
	                HttpEntity entity = response.getEntity();
	                InputStream is = entity.getContent();
	        		Log.d(LOG_TAG, "input stream created");

	                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	                StringBuilder sb = new StringBuilder();
	        		Log.d(LOG_TAG, "1");
	                String line = null;
	                while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	                }

	        		Log.d(LOG_TAG, "2");
	                is.close();
	                result = sb.toString();
	        		Log.d(LOG_TAG, "3, result=["+result+"]");
	                
	            } catch (Exception e) {
	            	result = "Error parsing data " + e.toString();
	                Log.d(LOG_TAG, "Error parsing data " + e.toString());
	            }
	            return result;
	    }

	    protected void onPostExecute(String result) {
    		Log.d(LOG_TAG, "ServletTask.onPostExecute(), result=["+result+"]");
	    	 TextView textView1 = (TextView) findViewById(R.id.textView1);
             textView1.setText(result);
             Gson gson = new Gson();
             Matrix mobj = gson.fromJson(result, Matrix.class);   

             Log.d(LOG_TAG, "Matrix: " + mobj + " values:" + mobj.values[0][0]+ " "+mobj.values[0][1]);
	    }
	}
	
}