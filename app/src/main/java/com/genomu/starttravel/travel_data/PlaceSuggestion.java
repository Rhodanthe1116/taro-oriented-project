package com.genomu.starttravel.travel_data;

import android.os.Parcel;
import android.widget.Filter;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class PlaceSuggestion implements SearchSuggestion {
    private String placeName;

    public PlaceSuggestion(String placeName){
        this.placeName = placeName;
    }

    public PlaceSuggestion(Parcel source) {
        this.placeName = source.readString();
    }

    @Override
    public String getBody() {
        return placeName;
    }

    public static final Creator<PlaceSuggestion> CREATOR = new Creator<PlaceSuggestion>() {
        @Override
        public PlaceSuggestion createFromParcel(Parcel source) {
            return new PlaceSuggestion(source);
        }

        @Override
        public PlaceSuggestion[] newArray(int size) {
            return new PlaceSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        new Filter(){
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return super.convertResultToString(resultValue);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeName);
    }
}
