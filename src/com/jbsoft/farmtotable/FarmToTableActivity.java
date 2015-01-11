package com.jbsoft.farmtotable;
 
/***
Copyright (c) 2012 CommonsWare, LLC
Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
by applicable law or agreed to in writing, software distributed under the
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
OF ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.

From _The Busy Coder's Guide to Android Development_
  http://commonsware.com/Android
*/


import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.animation.Animation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;

 


 
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class FarmToTableActivity extends AbstractMapActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener{
	private static final String STATE_NAV="nav";
	private static final int[] MAP_TYPES= { 
		GoogleMap.MAP_TYPE_NORMAL,GoogleMap.MAP_TYPE_HYBRID, GoogleMap.MAP_TYPE_SATELLITE,GoogleMap.MAP_TYPE_TERRAIN };
	private static final int[] MAP_TYPE_NAMES= {
		R.string.normal,R.string.hybrid, R.string.satellite, R.string.terrain };

	private static GoogleMap map=null;
	private LocationManager locationManager;
	public Location location;
	protected static final String TAG_ERROR_DIALOG_FRAGMENT="errorDialog";
	OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
  	  public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
  		  initprefs();
  		  getPreferences(sharedPrefs);
  		  refresh_data();
  	  }
  	};
  	 final LocationListener gpsLocationListener =new LocationListener(){
	        @Override
	        public void onStatusChanged(String provider, int status, Bundle extras) {
	        }
	        @Override
	        public void onProviderEnabled(String provider) {
	        }
	        @Override
	        public void onProviderDisabled(String provider) {
	        }
	        @Override
	        public void onLocationChanged(Location location) {
	      	  
	        } 
	       };
    
	public static boolean NOFM = false;
	public static boolean NOFS = false;
	public static boolean NOVR = false;
	public static boolean NOOR = false; 
	public static boolean COPN = false; 
	public String zipcode = ""; 
	public static JSONObject usdajson = null;
	public static JSONObject gplacesjson = null;
	public static String usdaurl="http://www.jbbenson.com/jan/usdaapi.php?zipcode="; 
	public static String usdaurl_sav="http://www.jbbenson.com/jan/usdaapi.php?zipcode="; 
	public static String gplaceurl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
	public static String gplaceurl_sav = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
	public static String placeurl_save = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
	public static String queryorganic = "organic"; 
	public static String queryfarms = "organic+farm+stands"; 
	public static String queryvegan = "vegan+restaurant"; 
	public static String queryvegetarian = "vegetarian+restaurant"; 
	public static String marketname;
	public static String address;
	public static String schedule;
	public static String products;
	public static String latlong;  
	public static String lat; 
	public static String lon;
	public static String MName;
	public static MarkerOptions markerOptions = new MarkerOptions();
	public double latitude;
	public double longitude;
	public int marketcount;
	private static final long MIN_TIME_BW_UPDATES = 0;
	private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
	
	public ArrayList<String> marketsList = new ArrayList<String>();
	public String markets;
	public Toast myLabel;
	public ProgressDialog progress;
	public static Drawable drawable = null;
	public SharedPreferences sharedPrefs ;
	
	private ContentsAdapter adapter=null;
	public static boolean nozip;
	public static boolean opennow;
	public static boolean restyes;
	public static boolean farmyes;
	public static boolean veganyes;
	public static boolean vegatarian;
	public static String photoref;
	private static SharedPreferences sharedprefs=null;
	private static SharedPreferences prefs=null;
	private static SharedPreferences prefs_sav=null;
////////////////////////////////////////////////////////////////
// INSTANTIATED ACTIVITY VARIABLES
////////////////////////////////////////////////////////////////

	public static Activity activity1;
	public static Activity activity2;
	public static Activity activity3;
	
	protected static String TAG;
	@Override
	public void onStart()
	{
	    // RUN SUPER | REGISTER ACTIVITY AS INSTANTIATED IN APP CLASS

	        super.onStart();
	        FarmToTableActivity.activity1 = this;
	}

	@Override
	public void onDestroy()
	{
            android.os.Process.killProcess(android.os.Process.myPid());
	        super.onDestroy();
	       // FarmToTableActivity.activity1 = null;
	        
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	  	  progress = new ProgressDialog(this); 
		  super.onCreate(savedInstanceState);
		
		  if (readyToGo()) {
		     setContentView(R.layout.activity_main);
		    
		   SupportMapFragment mapFrag=
		         (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
		      
			
		
		    initListNav();  
		    
		    getSupportActionBar().setHomeButtonEnabled(true);
		    sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		    getPreferences(sharedPrefs);
		    
		    sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
		    
		    map=mapFrag.getMap();
		   locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		    // Getting GPS status
		    boolean isNETWORKEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		    boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		           // If GPS enabled, get latitude/longitude using GPS Services
		   
		       
		    if (isGPSEnabled) {
		            locationManager.requestLocationUpdates(
		                    LocationManager.GPS_PROVIDER,
		                    MIN_TIME_BW_UPDATES,
		                    MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsLocationListener);
		            Log.d("GPS Enabled", "GPS Enabled");
		            if (locationManager != null) {
		                location = locationManager
		                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
		                if (location != null) {
		                    latitude = location.getLatitude();
		                    longitude = location.getLongitude();
		                }
		            }
		        }
		   
		    
		    if (savedInstanceState == null) {
		  	  
		      CameraUpdate center=
		          CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
		          map.moveCamera(center);
		    }
		    
		    if (NOFM){
			      //Reverse geocoder to zipcode
			      getZipFromLocation(location, this);
			      
			      //start progress box going
			      //Call api to retrieve Farmers Market from the UDSA site 
			      usdaurl = usdaurl + zipcode; 
		    }else
		    {nozip = true;}
		  //start progress box going
		      progress.setMessage("Getting Farmers Markets and Other Organic Options in your area:)");
		      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		      progress.setIndeterminate(true);  
		      progress.show(); 
		    gplaceurl = placeurl_save;
		    placeurl_save = gplaceurl;
		    if (NOVR) 
	        {
	  	        gplaceurl = gplaceurl  + queryvegan;
	        } 
		    
		    if (NOOR) {
		    	gplaceurl = placeurl_save;
		    	gplaceurl = gplaceurl  + queryvegetarian;
		    }
		   
		    if (NOFS){
		    	gplaceurl = placeurl_save;
		  	  gplaceurl = gplaceurl + queryfarms;
 		      }
		    
			if ((NOVR) && (NOOR))  {
				gplaceurl = placeurl_save;
			  	  gplaceurl = gplaceurl  + queryvegan + "&" + queryvegetarian ;
			    }
		 
		    if ((NOVR) && (NOFS)) {
		    	gplaceurl = placeurl_save;
			  	  gplaceurl = gplaceurl  + queryvegan + "&" + queryfarms;
			    }
		     
		    if ((NOOR) && (NOFS)) {
		    	gplaceurl = placeurl_save;
			  	  gplaceurl = gplaceurl  + queryvegetarian + "&" + queryfarms;
			    }
		     
		    if ((NOVR) && (NOOR) && (NOFS)) {
		    	gplaceurl = placeurl_save;
			  	  gplaceurl = gplaceurl  + queryvegan + "&" +  queryvegetarian + "&" + queryfarms;
			    } 
	         			
 		       
		    //The Google Places API Text Search Service 
		    gplaceurl = gplaceurl + "&location=" + latitude + "," + longitude + "&radius=10&key=AIzaSyA_fzl-7ZkF4EINWhuQ0bcXp3zkdAXZc5o";
		    //Call Asynch process Api 
		 
		    new restAPICall().execute(usdaurl,gplaceurl);
		    }
				   
	
		  	  map.setInfoWindowAdapter(new CustomToast(this, null));
			 // map.setOnInfoWindowClickListener((OnInfoWindowClickListener) 
		  	  map.setMyLocationEnabled(true);
		  	  CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);
			  map.animateCamera(zoom);
}  
		  
		protected void onResume(Bundle savedInstanceState){ 
			super.onResume();
			@SuppressWarnings("unused")
			OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		    	  public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
		    		  initprefs();
		    		  getPreferences(sharedPrefs);
		    		  refresh_data();
		    	  }
		    	};

		    sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
		}
		public void initprefs(){
			  NOFM = false;
			  NOFS = false;
			  NOVR = false;
			  NOOR = false;
			  COPN = false;
  	  }

		 
		public  void  refresh_data() {
			if (NOFM){
				if (nozip){
			      //Reverse geocoder to zipcode
				  getZipFromLocation(location, this);
			      //Call api to retrieve Farmers Market from the UDSA site 
			      usdaurl = usdaurl + zipcode;
			      nozip=false;
				} 
		  }else{ usdaurl = usdaurl_sav;}
			//start progress box going
		      progress.setMessage("Getting Farmers Markets and Other Organic Options in your area:)");
		      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		      progress.setIndeterminate(true);  
		      progress.show(); 
		      gplaceurl = gplaceurl_sav;
			  placeurl_save = gplaceurl;
			  if (NOVR) 
		        {
		  	        gplaceurl = gplaceurl  + queryvegan;
		        } 
			    
			    if (NOOR) {
			    	gplaceurl = placeurl_save;
			    	gplaceurl = gplaceurl  + queryvegetarian;
			    }
			   
			    if (NOFS){
			    	gplaceurl = placeurl_save;
			  	  gplaceurl = gplaceurl + queryfarms;
	 		      }
			    
				if ((NOVR) && (NOOR))  {
					gplaceurl = placeurl_save;
				  	  gplaceurl = gplaceurl  + queryvegan + "&" + queryvegetarian ;
				    }
			 
			    if ((NOVR) && (NOFS)) {
			    	gplaceurl = placeurl_save;
				  	  gplaceurl = gplaceurl  + queryvegan + "&" + queryfarms;
				    }
			     
			    if ((NOOR) && (NOFS)) {
			    	gplaceurl = placeurl_save;
				  	  gplaceurl = gplaceurl  + queryvegetarian + "&" + queryfarms;
				    }
			     
			    if ((NOVR) && (NOOR) && (NOFS)) {
			    	gplaceurl = placeurl_save;
				  	  gplaceurl = gplaceurl  + queryvegan + "&" +  queryvegetarian + "&" + queryfarms;
				    } 
	 		      {markerOptions = new MarkerOptions();
		  //The Google Places API Text Search Service 
		  gplaceurl = gplaceurl + "&location=" + latitude + "," + longitude + "&radius=10&key=AIzaSyA_fzl-7ZkF4EINWhuQ0bcXp3zkdAXZc5o";
		  //Call Asynch process Api 
		  new restAPICall().execute(usdaurl,gplaceurl);
		
		  }
		  
		  map.setInfoWindowAdapter(new CustomToast(this, null));
		 // map.setOnInfoWindowClickListener((OnInfoWindowClickListener) 
		  map.setMyLocationEnabled(true);
		  CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);
		  map.animateCamera(zoom);
				
		}  
		
		private static void getPreferences(SharedPreferences sharedPrefs) {
			   if (sharedPrefs.getBoolean("FMART",true)){
				  	  NOFM = true;
				  	    } 
				  	if (sharedPrefs.getBoolean("OFARM" , true)) {
				  	  NOFS = true;
				  	  }
				  	if (sharedPrefs.getBoolean("VREST", true)) {
				  	  NOVR = true;
				  	  }
				  	if (sharedPrefs.getBoolean("OREST", true)) {
				  	  NOOR = true;
				  	  }
					if (sharedPrefs.getBoolean("COPEN", true)) {
					  	  COPN = true;
					  	  }
			
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
		  case android.R.id.home:
		   // pager.setCurrentItem(0, false);
		    return(true);
		
		  case R.id.about:
		    Intent i=new Intent(this, SimpleContentActivity.class);
		
		    i.putExtra(SimpleContentActivity.EXTRA_FILE,
		               "file:///android_asset/misc/about.html");/**
		     * Back button listener.
		     * Will close the application if the back button pressed twice.
		     */
		    startActivity(i);
		    
		  case R.id.help:
		    i=new Intent(this, SimpleContentActivity.class);
		    i.putExtra(SimpleContentActivity.EXTRA_FILE,
		               "file:///android_asset/misc/help.html");
		    startActivity(i);
		
		    return(true);
		  case R.id.Settings:
	         startActivity(new Intent(this,Preferences.class));
	         
	         
	    	 return(true);}
		
		return false;}
		
		private void  getZipFromLocation(final Location location, final Context context) {
		    Geocoder geocoder = new Geocoder(context, Locale.getDefault());   
		    
		    try {
		        List<Address> list = geocoder.getFromLocation(
		                location.getLatitude(), location.getLongitude(), 1);
		        if (list != null && list.size() > 0) {
		            Address address = list.get(0);
		            // sending back first address line and locality
		            zipcode = address.getPostalCode();
		        }
		    } catch (IOException e) {  
		        Log.e(TAG, "Impossible to connect to Geocoder", e);
		    } finally {
		        }
		};
		 
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		  map.setMapType(MAP_TYPES[itemPosition]); 
		  return(true);
		}
		 
		@Override
		public void onSaveInstanceState(Bundle savedInstanceState) {
		  super.onSaveInstanceState(savedInstanceState);
		
		  savedInstanceState.putInt(STATE_NAV,
		                            getSupportActionBar().getSelectedNavigationIndex());
		}
		
		@Override
		public void onRestoreInstanceState(Bundle savedInstanceState) {
		  super.onRestoreInstanceState(savedInstanceState);
		
		  getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_NAV));
		}
		
		
		public void onInfoWindowClick(MarkerOptions markerOptions) {
			new CustomToast(this, markerOptions).show(); 
			
		}
		 
		private void initListNav() {
		  ArrayList<String> items=new ArrayList<String>();
		  ArrayAdapter<String> nav=null;
		  ActionBar bar=getSupportActionBar(); 
		
		  for (int type : MAP_TYPE_NAMES) {
		    items.add(getString(type));
		  }
		
		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		    nav=
		        new ArrayAdapter<String>(
		                                 bar.getThemedContext(),
		                                 android.R.layout.simple_spinner_item,
		                                 items);
		  }
		  else {
		    nav=
		        new ArrayAdapter<String>(
		                                 this,
		                                 android.R.layout.simple_spinner_item, items);
		  }
		
		  nav.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		  bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		}
		
		
		public static JSONObject POST(){
		    InputStream inputStream = null;
		    String result = "";
		    if (NOFM){  
		    try {
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpResponse httpResponse = httpclient.execute(new HttpPost(usdaurl));
		        inputStream = httpResponse.getEntity().getContent(); 
		        if(inputStream != null) 
		        {
		            result = convertInputStreamToString(inputStream);
		            usdajson = new JSONObject(result);}
		        }
		        catch (Exception e) 
		        	{
		        	Log.d("InputStream", e.getLocalizedMessage());
		        	}
		    	}
		      if ((NOFS) || (NOVR) || (NOOR)){
		    	  try {
  		          HttpClient httpclient2 = new DefaultHttpClient();
		          HttpResponse httpResponse2 = httpclient2.execute(new HttpPost(gplaceurl));
		          inputStream = httpResponse2.getEntity().getContent();
		            {
		          	 if ((NOFS) || (NOVR) || (NOOR))
		          	 {
		              String result2 = convertInputStreamToString(inputStream);
		              gplacesjson = new JSONObject(result2);
		          	 }
		            } 
		          }
		    	 catch (Exception e) {
		          Log.d("InputStream", e.getLocalizedMessage());
		         }
		      }
		   
			return gplacesjson;
		}
		private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		    String line = "";
		    String result = "";
		    while((line = bufferedReader.readLine()) != null) 
		     result += line; 
		     inputStream.close(); 
		     return result;
		} 
		private class restAPICall  extends AsyncTask<String, String, String> {
			  @Override
		  	  protected String doInBackground  (String... urls) {  
		  		  	POST();
		  		  	return null;
			  }
 		     @Override
		     protected void onPostExecute(String result) {
 		    	map.clear();  
 		    	int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
 		    	
		  	  if( (NOFM) && ((hour < 18) || (hour<8))){
		  	  try {
		  	    	JSONArray PointsArray = usdajson.getJSONArray("results");
		  	        map.clear();
		  	        for (int i = 0; i < PointsArray.length(); i++) { 
		  	        	   MName = "";
		  	        	   JSONObject geopoints = PointsArray.getJSONObject(i);
		  	        	   Double LAT = geopoints.getDouble("lat");
		  	        	   Double LON = geopoints.getDouble("long");
		  	        	   
		  	        	   String[] separated = geopoints.getString("marketname").split(" ");
		  	        	   for (int s=1 ;s < separated.length;s++){
		  	        		 String Distance = separated[0];  
		  	        		 MName =   MName + " " + separated[s];   
		  	        	   }
		  	        	   //String MName = geopoints.getString("marketname");
		  	        	   String  MAddress= geopoints.getString("Address");
		  	        	   String  MProducts= geopoints.getString("Products");
		  	        	   String  MSchedule= geopoints.getString("Schedule");
		  	    		   LatLng GP = new LatLng(LAT,LON);
		  	               BitmapDescriptor bitmapMarker;
		  	               bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.fmart2);
		  	              // Setting latitude and longitude for the marker= true 
		  	               markerOptions.position(GP);
		  	               markerOptions.title(MName);
		  	               markerOptions.icon(bitmapMarker);
		  	               
		  	               markerOptions.snippet(MAddress + (Html.fromHtml("<p>Get Directions</p>")));
		  	               map.addMarker(markerOptions);
		  	        }
		  		    }
		  	  
		  	       catch (NumberFormatException e) {
		  			
		  			e.printStackTrace();
		  		   } 
		  	       catch (JSONException e) {
		  			// TODO Auto-generated catch block
		  			e.printStackTrace();
		  		   }
		  	  }
		  	 
		  	  if ((NOFS) || (NOVR) || (NOOR)){
		  	  try {
			    	JSONArray PlacesArray= gplacesjson.getJSONArray("results");
			        //loop thru json array for geopointsbitmapMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
			        for (int i = 0; i < 18; i++) {
			        	//for (int i = 0; i < PlacesArray.length(); i++) {progress
			        	   JSONObject geopoints = PlacesArray.getJSONObject(i);
			        	   JSONObject geolocation = geopoints.getJSONObject("geometry").getJSONObject("location");
			        	   JSONObject open_now =  geopoints.getJSONObject("opening_hours");
			        	   JSONArray types = geopoints.getJSONArray("types");
			        	   //JSONObject photos =  geopoints.getJSONObject("photos");
			        	   
			        	   Double lat = geolocation.getDouble("lat");
			        	   Double lng = geolocation.getDouble("lng");
			        	   String icon = geopoints.getString("icon");
			        	   
			        	   String[] separated = geopoints.getString("icon").split("/");
			        	   String imagename =   separated[6];
			        	   opennow = open_now.getBoolean("open_now");
			        	   if (opennow){
			        	   for (int t=0;t < types.length();t++){
			        		   if ((types.getString(t).contains("cafe") || (types.getString(t).contains("restaurant")))) {
			        			   restyes = true;
			        		   }
			        		   if (types.getString(t).contains("farm")) {
			        			   farmyes = true;
			        		   }
			        		   if (types.getString(t).contains("vegatarian")) {
			        			   vegatarian = true;
			        		   }
			        		   if (types.getString(t).contains("vegan")) {
			        			   veganyes = true;
			        		   }
			        	   }
			        	   //photoref = photos.getString("photo_reference");
			 	           String MName = geopoints.getString("name");
			        	   String  MAddress= geopoints.getString("formatted_address");
			    		   LatLng GP = new LatLng(lat,lng);
		  	              // Setting latitude and longitude for the marker
			               markerOptions.position(GP);
			               markerOptions.title(MName);
			               markerOptions.snippet(MAddress);
			               if ((imagename.contains("restaurant")) || (imagename.contains("cafe")) || restyes ||
			            	 ((MName.contains("restaurant")) || (MName.contains("cafe")))
			            	  ){		            	   
			            	   if (veganyes || MName.contains("vegan") || MName.contains("Vegan")){
			            		   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurantvegan));
			            	   }
			                   else
				               {
			                	   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurantvegetarian));   
				               }
				               }
		                			            	   
			        	 
			              if ((imagename.contains("farm")) || (MName.contains("farm")) || farmyes){   
			            	 markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.farmstand));
			        	   }
			              // Adding marker on the Google Map
			               map.addMarker(markerOptions);
			        	   }
			             }
		  	  }
			       catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   } 
			       catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				   }	  	    		  
		  	  progress.dismiss();
			       }
		
		}}
		@Override
		public void onLocationChanged(Location arg0) {
			 if (NOFM){
			      //Reverse geocoder to zipcode
			      getZipFromLocation(arg0, this);
			      
			      //start progress box going
			      progress.setMessage("Getting Farmers Markets and Other Organic Options in your area:)");
			      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			      progress.setIndeterminate(true);  
			      progress.show(); 
			      
			      //Call api to retrieve Farmers Market from the UDSA site 
			      usdaurl = usdaurl + zipcode; 
		   }
		    
			 if (NOVR) 
		        {	  
		  	        gplaceurl = gplaceurl + queryvegan + queryorganic;
		        } 
			    else
			    if (NOOR) {
			  	  gplaceurl = gplaceurl + queryvegetarian + queryorganic;
			    }
			    else
				if ((NOVR) && (NOOR)) {
				  	  gplaceurl = gplaceurl + queryvegan + queryvegetarian + queryorganic;
				    }
			    if (NOFS){
			  	  gplaceurl = gplaceurl + "+" + queryfarms;
	 		      {MarkerOptions markerOptions = new MarkerOptions();}
		   //The Google Places API Text Search Service 
		   gplaceurl = gplaceurl + "&location=" + latitude + "," + longitude + "&radius=10&key=AIzaSyA_fzl-7ZkF4EINWhuQ0bcXp3zkdAXZc5o";
		   //Call Asynch process Api 
		   
		   new restAPICall().execute(usdaurl,gplaceurl);
		   }
			
		}
		
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onConnected(Bundle connectionHint) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * Back button listener.
		 * Will close the application if the back button pressed twice.
		 */
		public void onBackPressed()
		{
		    finish();
		    
		}
		@Override
		public void onDisconnected() {
			finish();
		}
		 ////////////////////////////////////////////////////////////////
	    // CLOSE APP METHOD
	    ////////////////////////////////////////////////////////////////

	        public static void close()
	        {
	            if (FarmToTableActivity.activity3 != null) {FarmToTableActivity.activity3.finish();}
	            if (FarmToTableActivity.activity2 != null) {FarmToTableActivity.activity2.finish();}
	            if (FarmToTableActivity.activity1 != null) {FarmToTableActivity.activity1.finish();}
	        }		

}