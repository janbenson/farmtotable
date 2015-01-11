package com.jbsoft.farmtotable;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;

@TargetApi(11)
public class StockPreferenceFragment extends PreferenceFragment {
 
  
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    int res=getActivity()
              .getResources()
              .getIdentifier(getArguments().getString("resource"),
                              "xml",
                              getActivity().getPackageName());
    
    addPreferencesFromResource(res);
  }
}