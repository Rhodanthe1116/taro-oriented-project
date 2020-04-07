package com.genomu.starttravel.travel_data;

import java.util.ArrayList;
import java.util.List;

public class TravelFilter extends DataFilter {
	private List<Travel> rawList;
    private List<Travel> resultList;
	public TravelFilter(FilterStrategy filterStrategy, List list) {
		super(filterStrategy, list);
		this.rawList = list;
		this.type = FilterStrategy.TravelDataType.Travel;
	}

	@Override
	public ArrayList<Travel> getFilteredList() {
		resultList = filterStrategy.execute(rawList,type);
        return (ArrayList<Travel>)resultList;
	}
	
	@Override
    public ArrayList<Travel> getRawDataList() {
    	return (ArrayList<Travel>)rawList;
    }

}
