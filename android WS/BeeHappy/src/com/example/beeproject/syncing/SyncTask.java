package com.example.beeproject.syncing;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class SyncTask  extends AsyncTask<String, Void, String> {
	private SyncTaskCallback callback;
	private Activity context;
	
	
	public SyncTask(Activity context, SyncTaskCallback callback){
		this.context = context;
		this.callback = callback;
	}

    protected String doInBackground(String... arg) {
		SyncHelper syncHelper = new SyncHelper(context);
        String result = syncHelper.syncronizeToServer();
        return result;
    }
    

    protected void onPostExecute(String result) {
    	callback.onSyncTaskFinished(result);
    }

}
