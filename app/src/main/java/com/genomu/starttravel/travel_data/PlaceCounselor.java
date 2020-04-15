package com.genomu.starttravel.travel_data;

import android.util.Log;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlaceCounselor {
    private final static String TAG = PlaceCounselor.class.getSimpleName();
    private List<PlaceSuggestion> placeList;
    public interface OnFindSuggestionsListener{
        void onResults(List<PlaceSuggestion> results);
    }

    public PlaceCounselor(String[] places){
        placeList = new ArrayList<>();
        for(String place:places){
            placeList.add(new PlaceSuggestion(place));
        }
    }

    public void findSuggestions(String query,final int limit,final long simulatedDelay,final OnFindSuggestionsListener listener){
        new Filter(){
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return super.convertResultToString(resultValue);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<PlaceSuggestion> suggestions = new ArrayList<>();
                if(!(constraint == null||constraint.length()==0)){
                    for(PlaceSuggestion placeSuggestion:placeList){
                        Log.d(TAG, "performFiltering: "+placeSuggestion.getBody());
                        if(placeSuggestion.getBody().contains(constraint.toString())){
                            if(suggestions.size()<limit){
                                suggestions.add(placeSuggestion);
                            }
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(listener!=null){
                    listener.onResults((List<PlaceSuggestion>)results.values);
                }
            }
        }.filter(query);
    }
}
