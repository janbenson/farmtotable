package com.jbsoft.farmtotable;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockPreferenceActivity;



public class Preferences extends SherlockPreferenceActivity {

 

@SuppressWarnings("deprecation")
@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  //  if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB) {
      addPreferencesFromResource(R.xml.pref_display);
    //}
  }}
  
  
 
  
//public void onBuildHeaders(List<Header> target) {
	 
    /*loadHeadersFromResource(R.xml.preferences_headers, target);
    
  }
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  protected boolean isValidFragment(String fragmentName) {
	  return StockPreferenceFragment.class.getName().equals(fragmentName);
	}
  
}*/
