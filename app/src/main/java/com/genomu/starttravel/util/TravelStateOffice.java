package com.genomu.starttravel.util;

import com.genomu.starttravel.travel_data.Travel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TravelStateOffice {
    public static final int CAN_BE_MODIFIED = 2;
    public static final int NOT_YET_START = 1;
    public static final int ON_THE_ROAD = 0;
    public static final int ALREADY_END = -1;

    public static final int FULL = 1;
    public static final int ENSURE = 0;
    public static final int LACK = -1;

    private static Date now = Calendar.getInstance().getTime();
    private Date start_date;
    private Date end_date;
    private int grouping;

    private static SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");

    private int state;
    public TravelStateOffice(Travel travel) throws ParseException {
        start_date = dateFormat.parse(travel.getStart_date());
        end_date = dateFormat.parse(travel.getEnd_date());
        if(start_date.after(now)){
            state = NOT_YET_START;
            if(modifyDeadLine().after(now)){
                state = CAN_BE_MODIFIED;
            }
        }else if(end_date.before(now)){
            state = ALREADY_END;
        }else{
            state = ON_THE_ROAD;
        }
        if(travel.getLower_bound()<=travel.getPurchased()){
            grouping = ENSURE;
            if(travel.getUpper_bound()==travel.getPurchased()){
                grouping = FULL;
            }
        }else {
            grouping = LACK;
        }
    }
    private Date modifyDeadLine(){
        return new Date(start_date.getTime()-7 * 24 * 60 * 60 * 1000L);
    }

    public static Date getNow(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_WEEK,offset);
        return calendar.getTime();
    }

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public int getState() {
        return state;
    }

    public int getGrouping() {
        return grouping;
    }

    public boolean isCancelled(){
        return state<NOT_YET_START&&grouping==LACK;
    }

    public boolean isRoundedOff(){
        return state==ALREADY_END&&grouping==ENSURE;
    }

    public long dayCount(){
        long day = start_date.getTime() - now.getTime();
        return day/(1000 * 60 * 60 * 24);
    }
}
