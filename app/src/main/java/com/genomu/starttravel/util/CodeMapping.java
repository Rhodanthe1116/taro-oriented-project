package com.genomu.starttravel.util;

import com.google.firebase.firestore.Query;

public class CodeMapping {
    public static Query parseCodes(String place,HanWen hanWen){
        switch (place){
            case "東北亞":
                return hanWen.seekFromTravels("travel_code",401,41,342,343);
            case "中國北部":
                return hanWen.seekFromTravels("travel_code",426,427,432,435,428);
            case "中國南部":
                return hanWen.seekFromTravels("travel_code",430,431,433,436,439,440,441,442);
            case "東南亞":
                return hanWen.seekFromTravels("travel_code",405,406,417,416,411,410,391,392);
            case "南亞":
                return hanWen.seekFromTravels("travel_code",394,399,99);
            case "西亞":
                return hanWen.seekFromTravels("travel_code",368,412);
            case "北美":
                return hanWen.seekFromTravels("travel_code",409,43);
            case "西歐":
                return hanWen.seekFromTravels("travel_code",414,413);
            case "中歐":
                return hanWen.seekFromTravels("travel_code",101,393,394);
            case "北歐":
                return hanWen.seekFromTravels("travel_code",40,396,395);
            case "東歐":
                return hanWen.seekFromTravels("travel_code",404,44);
            case "南歐":
                return hanWen.seekFromTravels("travel_code",397,398);
            case "大洋":
                return hanWen.seekFromTravels("travel_code",408,407);
            case "非洲":
                return hanWen.seekFromTravels("travel_code",100,415);
            default:
                return hanWen.seekFromTravels("travel_code",999);
        }
    }
}
