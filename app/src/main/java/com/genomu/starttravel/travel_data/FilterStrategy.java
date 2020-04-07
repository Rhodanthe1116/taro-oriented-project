package com.genomu.starttravel.travel_data;

import java.util.ArrayList;
import java.util.List;

public interface FilterStrategy {
	public static enum TravelDataType{
		TravelCode,Travel
	}
    ArrayList execute(List rawList,TravelDataType type);
}
