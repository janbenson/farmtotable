package com.jbsoft.farmtotable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ContentsAdapter extends FragmentStatePagerAdapter {
  private MapLocations contents=null;

  public ContentsAdapter(SherlockFragmentActivity ctxt,
                         MapLocations contents) {
    super(ctxt.getSupportFragmentManager());

    this.contents=contents;
  }

  @Override
  public Fragment getItem(int position) {
    String path=contents.getChapterFile(position);

    return(SimpleContentFragment.newInstance("file:///android_asset/book/" + path));
  }

  @Override
  public int getCount() {
    return(contents.getResultsCount());
  }
}