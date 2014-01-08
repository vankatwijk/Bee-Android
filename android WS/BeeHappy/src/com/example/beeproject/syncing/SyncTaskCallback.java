package com.example.beeproject.syncing;

/**
 * In the activity, that runs the SyncTask, this callback must be implemented
 * to define what will happen after the asynchronous SyncTask is finished
 * @author rezolya
 *
 */
public interface SyncTaskCallback { 
	/**
	 * This method will be called after the SyncTask is finished.
	 * @param result
	 */
	public void onSyncTaskFinished(String result);
}
