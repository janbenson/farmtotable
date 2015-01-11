package com.jbsoft.farmtotable;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jbsoft.farmtotable.FarmToTableActivity;


class PopupAdapter implements InfoWindowAdapter {
	  LayoutInflater inflater=null;

	  public PopupAdapter(LayoutInflater inflater,MarkerOptions markeropts) {
	    this.inflater=inflater;
	  }

	  public PopupAdapter(FarmToTableActivity farmtoableActivity, MarkerOptions markeropts) {
		// TODO Auto-generated constructor stub
	}

	@Override
	  public View getInfoWindow(Marker marker) {
	    return(null);
	  }

	  @Override
	  public View getInfoContents(Marker marker) {
	    View popup=inflater.inflate(R.layout.popup, null);
	    ImageView iconvw =(ImageView)popup.findViewById(R.id.icon);
	    TextView titletv=(TextView)popup.findViewById(R.id.title);
	    TextView snippettv=(TextView)popup.findViewById(R.id.snippet);

	    titletv.setText(marker.getTitle());
	    snippettv.setText(marker.getSnippet());
	  //  iconvw.setImageDrawable(marker.getImage());
	    return(popup);
	  }
	}