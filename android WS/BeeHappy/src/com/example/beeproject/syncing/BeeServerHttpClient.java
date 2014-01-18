package com.example.beeproject.syncing;

import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.example.beeproject.commandexecution.commands.BeeCommand;
import com.example.beeproject.commandexecution.results.BeeCommandResult;
import com.example.beeproject.gsonconvertion.GsonProvider;
import com.google.gson.Gson;

public class BeeServerHttpClient {
	public static final String LOG_TAG ="BeeServerHttpClient";

    private static String BASE_URL = "http://10.0.2.2:8089/BeeHappyServer/CommandExecuteServlet"; //TODO: take the url from settings file
    
    @SuppressWarnings("null")
	public static BeeCommandResult executeCommand(BeeCommand command) {
        HttpURLConnection con = null ;
        InputStream is = null;
            
        try {
        	Gson gson = GsonProvider.getGson();
        	String commandJson = gson.toJson(command, BeeCommand.class);
    		//Log.d(LOG_TAG, "commandJson: " + commandJson);
        	
        	HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(BASE_URL);
            
            StringEntity se = new StringEntity(commandJson);
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            String jsonResult = EntityUtils.toString(response.getEntity());
    		//Log.d(LOG_TAG, "3, jsonResult=["+jsonResult+"]");
    		
    		BeeCommandResult commandResult = gson.fromJson(jsonResult, BeeCommandResult.class);
            return commandResult;
        }
        catch(Throwable t) {
                t.printStackTrace();
        }
        finally {
                try { is.close(); } catch(Throwable t) {}
                try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;
                            
    }
    

}
