package com.genomu.starttravel.travel_data;

import java.util.ArrayList;
import java.util.List;

public class TravelCodeFilter extends DataFilter {
    private List<TravelCode> rawList;
    private List<TravelCode> resultList;
    public TravelCodeFilter(FilterStrategy filterStrategy, List list) {
        super(filterStrategy, list);
        this.rawList = list;
        this.type = FilterStrategy.TravelDataType.TravelCode;
    }

    @Override
    public ArrayList<TravelCode> getFilteredList() {
        resultList = filterStrategy.execute(rawList,type);
        return (ArrayList<TravelCode>)resultList;
    }

    @Override
    public ArrayList<TravelCode> getRawDataList() {
    	return (ArrayList<TravelCode>)rawList;
    }
}
