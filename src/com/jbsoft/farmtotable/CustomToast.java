package com.jbsoft.farmtotable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class CustomToast extends Dialog implements InfoWindowAdapter{

  public CustomToast(Context layoutInflater, MarkerOptions markeropts) {
  super(layoutInflater);
 }

public CustomToast(Context layoutInflater, Object markeropts) {
	  super(layoutInflater);
}

public View getInfoWindow(MarkerOptions MarkerOpts) {
   return(null);
 }

 public View getInfoContents(MarkerOptions markeropts) {
	 requestWindowFeature(Window.FEATURE_NO_TITLE);
	  Context layoutInflater = null;
	  LayoutInflater inflater = (LayoutInflater) layoutInflater.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
	  View popup = inflater.inflate(R.layout.popup, null);
	   
	  TextView titletv = (TextView) popup.findViewById(R.id.title);
	  TextView snippettv=(TextView) popup.findViewById(R.id.snippet);
	  TextView hldirection = (TextView)  popup.findViewById(R.id.directions);
	  TextView moreinfo = (TextView)  popup.findViewById(R.id.moreinfo);
	  ImageView icon = (ImageView) popup.findViewById(R.id.icon); 
	  titletv.setText(markeropts.getTitle());
	  snippettv.setText(markeropts.getSnippet());
	  hldirection.setText(R.string.directions);
	  moreinfo.setText(R.string.moreinfo);
	  setContentView(popup);
	  show();
	  setCanceledOnTouchOutside(true);
	  setCancelable(true);
	  Window window = getWindow();
	  window.setGravity(Gravity.BOTTOM);
	  new Handler().postDelayed(new Runnable() {

	   public void run() {
	    dismiss();
	   }
	  }, 2500);
	return snippettv;
	
	}

@Override
public View getInfoContents(Marker marker) {
	// TODO Auto-generated method stub
	return null;
}



@Override
public View getInfoWindow(Marker arg0) {
	// TODO Auto-generated method stub
	return null;
}
}
