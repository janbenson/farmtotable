package com.jbsoft.farmtotable;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapLocations {

  JSONObject raw=null;
  JSONArray results;

  MapLocations(JSONObject raw) {
    this.raw=raw;
    results=raw.optJSONArray("results");
  }

  int getResultsCount() {
    return(results.length());
  }

  String getChapterFile(int position) {
    JSONObject MapLocations=results.optJSONObject(position);

    return(MapLocations.optString("file"));
  }

  String getTitle() {
    return(raw.optString("icon"));
  }
  String getSnippet() {
	    return(raw.optString("snippet"));
	  }
  String get() {
	    return(raw.optString("title"));
	  }
}
