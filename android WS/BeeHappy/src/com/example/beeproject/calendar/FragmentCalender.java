package com.example.beeproject.calendar;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class FragmentCalender extends CaldroidFragment{
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new CaldroidGridAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
	}
}