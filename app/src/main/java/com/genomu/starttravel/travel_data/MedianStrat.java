package com.genomu.starttravel.travel_data;

import java.util.ArrayList;
import java.util.List;

public class MedianStrat implements FilterStrategy {

	@Override
	public ArrayList execute(List rawList,TravelDataType type) {
		switch(type) {
		case Travel:
			return (ArrayList<Travel>)executeTravel(rawList);
		case TravelCode:
			List filtered = executeTravelCode(rawList);
			return (ArrayList<TravelCode>) filtered;
		default:
			return new ArrayList();	
		}		
	}

	private List<Travel> executeTravel(List<Travel> rawList){
		int[] codes = new int[rawList.size()];
		for(int i = 0 ;i<rawList.size();i++) {
			Travel t = (Travel)rawList.get(i);
			codes[i] = t.getTravel_code();
		}
		int m = Median.find(codes, 0, codes.length-1);
		List<Travel> tList = new ArrayList<Travel>();
		for(int i = 0 ;i<rawList.size();i++) {
			Travel t = (Travel)rawList.get(i);
			if(t.getTravel_code()>=m) {
				tList.add(t);
			}
		}
		return tList;
	}
	
	private List<TravelCode> executeTravelCode(List<TravelCode> rawList) {
		int[] codes = new int[rawList.size()];
		for(int i = 0 ;i<rawList.size();i++) {
			TravelCode tc = (TravelCode)rawList.get(i);
			codes[i] = tc.getTravelCode();
		}
		int m = Median.find(codes, 0, codes.length-1);
		
		List<TravelCode> tcList = new ArrayList<TravelCode>();
		
		for(int i = 0 ;i<rawList.size();i++) {
			TravelCode tc = (TravelCode)rawList.get(i);
			if(tc.getTravelCode()>=m) {
				tcList.add(tc);
			}
		}
		return tcList;
	}
	
	//median algorithm
	private static class Median
	{
	    
	    private Median()
	    {
	    }
	 
	 
	    public static void swap(int[] a, int i1, int i2)
	    {
	        int temp = a[i1];
	        a[i1] = a[i2];
	        a[i2] = temp;
	    }
	 
	    public static int find(int[] a, int from, int to)
	    {
	        int low = from;
	        int high = to;
	        int median = (low + high) / 2;
	        do
	        {
	            if (high <= low)
	            {
	                return a[median];
	            }
	            if (high == low + 1)
	            {
	                if (a[low] > a[high])
	                {
	                    swap(a, low, high);
	                }
	                return a[median];
	            }
	            int middle = (low + high) / 2;
	            if (a[middle] > a[high])
	            {
	                swap(a, middle, high);
	            }
	            if (a[low] > a[high])
	            {
	                swap(a, low, high);
	            }
	            if (a[middle] > a[low])
	            {
	                swap(a, middle, low);
	            }
	            swap(a, middle, low + 1);
	            int ll = low + 1;
	            int hh = high;
	            do
	            {
	                do
	                {
	                    ll++;
	                }
	                while(a[low] > a[ll]);
	                do
	                {
	                    hh--;
	                }
	                while(a[hh] > a[low]);
	                if (hh < ll)
	                {
	                    break;
	                }
	                swap(a, ll, hh);
	            }
	            while(true);
	            swap(a, low, hh);
	            if (hh <= median)
	            {
	                low = ll;
	            }
	            if (hh >= median)
	            {
	                high = hh - 1;
	            }
	        }
	        while(true);
	    }
	}


}
