package com.jbsoft.farmtotable;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SimpleContentFragment extends Fragment {
  private static final String KEY_FILE="file";

  protected static Fragment newInstance(String file) {
    Fragment f=new SimpleContentFragment();

    Bundle args=new Bundle();

    args.putString(KEY_FILE, file);
    f.setArguments(args);

    return(f);
  }

  String getPage() {
    return(getArguments().getString(KEY_FILE));
  }
}