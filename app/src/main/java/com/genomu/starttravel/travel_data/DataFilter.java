package com.genomu.starttravel.travel_data;

import java.util.ArrayList;
import java.util.List;

public abstract class DataFilter {
    FilterStrategy filterStrategy;
    private List<TravelData> rawList;
    protected FilterStrategy.TravelDataType type;

    public DataFilter(FilterStrategy filterStrategy,List list){
        this.filterStrategy = filterStrategy;
        rawList = list;
    }

    public abstract ArrayList getFilteredList();

    public void choiceStrategy(FilterStrategy filterStrategy) {
        this.filterStrategy = filterStrategy;
    }

    public ArrayList getRawDataList(){
        return (ArrayList) rawList;
    }
}
