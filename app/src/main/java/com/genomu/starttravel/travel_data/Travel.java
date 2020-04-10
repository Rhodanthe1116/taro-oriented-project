package com.genomu.starttravel.travel_data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Travel extends TravelData implements Serializable {
    private String title;
    private int travel_code;
    private String product_key;
    private int price;
    private String start_date;
    private String end_date;
    private int lower_bound;
    private int upper_bound;
    private int purchased;
    public static Travel dummy = new Travel("Dummy",100,"VDRxxx01",102,"2020-02-29","2020-03-07",5,21,0);

    public Travel(){

    }
    public Travel(String title, int travel_code, String product_key, int price, String start_date, String end_date,
			int lower_bound, int upper_bound,int purchased) {
		super();
		this.title = title;
		this.travel_code = travel_code;
		this.product_key = product_key;
		this.price = price;
		this.start_date = start_date;
		this.end_date = end_date;
		this.lower_bound = lower_bound;
		this.upper_bound = upper_bound;
		this.purchased = purchased;
    }

	public Travel(JSONObject jsonObject) {
        try {
            title = jsonObject.getString("title");
            travel_code = jsonObject.getInt("travel_code");
            product_key = jsonObject.getString("product_key");
            price = jsonObject.getInt("price");
            start_date = jsonObject.getString("start_date");
            end_date = jsonObject.getString("end_date");
            lower_bound = jsonObject.getInt("lower_bound");
            upper_bound = jsonObject.getInt("upper_bound");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTravel_code() {
        return travel_code;
    }

    public void setTravel_code(int travel_code) {
        this.travel_code = travel_code;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getLower_bound() {
        return lower_bound;
    }

    public void setLower_bound(int lower_bound) {
        this.lower_bound = lower_bound;
    }

    public int getUpper_bound() {
        return upper_bound;
    }

    public void setUpper_bound(int upper_bound) {
        this.upper_bound = upper_bound;
    }

    public int getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }
}
