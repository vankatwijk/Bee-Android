package com.example.beeproject.statistics;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.example.beeproject.R;
import com.example.beeproject.global.classes.DatabaseHelper;
import com.example.beeproject.global.classes.DatabaseManager;
import com.example.beeproject.global.classes.GlobalVar;
import com.example.beeproject.global.classes.HiveObject;
import com.example.beeproject.global.classes.YardObject;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class Statistics extends FragmentActivity {


	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	DatabaseHelper db;
	public List<Object[]> yardsAndHives; // o[0] - String, o[1] - HiveObject

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		//preload list of yardNames and hives belonging to them
		//no point to load them every time DummySectionFragment.onCreateView() runs
		db = new DatabaseManager().getHelper(this);
		yardsAndHives = new ArrayList<Object[]>();
		try{
			RuntimeExceptionDao<YardObject, Integer> yardDao = db.getYardRunDao();
			RuntimeExceptionDao<HiveObject, Integer> hiveDao = db.getHiveRunDao();
			
			QueryBuilder<YardObject, Integer> yardQuery = yardDao.queryBuilder();
			Where<YardObject, Integer> yardWhere = yardQuery.where().eq("userID_id", GlobalVar.getInstance().getUserID());
			yardQuery.setWhere(yardWhere);
			List<YardObject> yards = yardDao.query(yardQuery.orderBy("yardName", true).prepare());
			
			QueryBuilder<HiveObject, Integer> hiveQuery = hiveDao.queryBuilder();
			for(YardObject yard : yards){
				Where<HiveObject, Integer> where = hiveQuery.where().eq("yardID_id", yard.getId());
				hiveQuery.setWhere(where);
				List<HiveObject> hives= hiveDao.query(hiveQuery.orderBy("hiveName", true).prepare());
				for(HiveObject hive : hives){
					yardsAndHives.add(new Object[] { yard.getYardName(), hive } );
				}
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}


	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new StatisticsSectionFragment();
			Bundle args = new Bundle();
			args.putInt(StatisticsSectionFragment.ARG_SECTION_NUMBER, position + 1);
			args.putSerializable(StatisticsSectionFragment.ARG_YARDS_AND_HIVES, (Serializable) yardsAndHives);
			
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	
}
