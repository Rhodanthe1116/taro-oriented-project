package com.genomu.starttravel.util;

import android.app.Activity;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genomu.starttravel.travel_data.TravelCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class LookForCodesCommand extends DBCommand {
    private final static String TAG = LookForCodesCommand.class.getSimpleName();
    private int code;
    private TravelCode travelCode;
    private Activity activity;
    private ImageView imageView;
    private long seed;

    public LookForCodesCommand(HanWen hanWen, int code, TravelCode travelCode, Activity activity, ImageView imageView,long seed) {
        super(hanWen);
        this.code = code;
        this.travelCode = travelCode;
        this.activity = activity;
        this.imageView = imageView;
        this.seed = seed;
    }

    public LookForCodesCommand(HanWen hanWen) {
        super(hanWen);
        this.code = 396;
    }

    @Override
    void work() {
        hanWen.seekFromRaw("codes","travelCode",code)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc: task.getResult()){
                        travelCode.setCountry(doc.get("country",String.class));
                        travelCode.setTravelCode(code);
                        travelCode.setTravelCodeName(doc.get("travelCodeName",String.class));
                        RequestQueue queue = Volley.newRequestQueue(activity);
                        String url = "https://pixabay.com/api/";
                        String key = "?key="+"15945961-2835fdd302951c8f463bbf738";
                        String[] s = travelCode.getCountry().split(" ");
                        for(String str:s){
                            Log.d(TAG, "onComplete: in country "+str);
                        }
                        String q = "&q=" + s[new Random(seed).nextInt(s.length)];  //key word
                        String image_type = "&image_type=photo";
                        String endpoint = url+key+q+image_type+"&lang=zh&per_page=20";
                        Log.d(TAG, "onComplete: source >> "+endpoint);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                                new Response.Listener<String>() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG, "onResponse: "+parseURL(response));
                                        new ImageFromURLTask(imageView).execute(parseURL(response));
                                    }
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    private String parseURL(String source){
                                        String parsedUrl = "";
                                        try {
                                            JSONArray results = new JSONObject(source).getJSONArray("hits");
                                            for(int i = 0;i<results.length();i++){
                                                JSONObject reader = results.getJSONObject(i);
                                                float ratio = (float)reader.getInt("webformatWidth")
                                                        /(float) reader.getInt("webformatHeight");
                                                if(!(ratio>1.1f&&ratio<1.6f)&results.length()>1){
                                                    JSONObject remove = (JSONObject) results.remove(i);
//                                                    checkRemoveInLog(ratio, remove);
                                                }
                                            }
                                            if(results.length() <= 0)
                                                throw new IllegalArgumentException();
                                            int random_index = new Random(seed).nextInt(results.length());
                                            JSONObject result = results.getJSONObject(random_index);
                                            parsedUrl = result.getString("webformatURL");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }catch (IllegalArgumentException e){
                                            e.printStackTrace();
                                        }
                                        return parsedUrl;
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        queue.add(stringRequest);
                    }
                }else{
                    Log.w(TAG, "onComplete: ", task.getException());
                }
            }
        });
    }

    private void checkRemoveInLog(float ratio, JSONObject remove) throws JSONException {
        Log.d(TAG, "removing(w/h) : ("+
                remove.getInt("webformatWidth")+
                " / "+
                remove.getInt("webformatHeight")+
                ") ; ratio: " +ratio +" ; URL :"+
                remove.getString("webformatURL")
        );
    }
}
