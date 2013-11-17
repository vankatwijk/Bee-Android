package com.example.beeproject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class FragmentYard extends ListFragment{

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
    	
    	return inflater.inflate(R.layout.fragmentyard,container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        String[] hivelist = {"hive1","hive2","hive3","hive4"};

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,hivelist));
        
        registerForContextMenu(getListView());
        
    }
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
		
		menu.setHeaderTitle("Test");
		
		String[] menuItems = {"Open", "Delete"};
		
		for (int i = 0; i<menuItems.length; i++) {
		      menu.add(Menu.NONE, i, i, menuItems[i]);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		String check = item.toString();
		
		if(check.equals("Open")){
			((Methods) getActivity()).hiveSelected(1);
		}
		
		return true;
	}
	
}
