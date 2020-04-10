package com.genomu.starttravel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genomu.starttravel.util.AddOrderCommand;
import com.genomu.starttravel.util.AddRawListCommand;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.travel_data.Travel;

import java.util.List;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {

    private static final String TAG = TravelAdapter.class.getSimpleName();
    private Activity activity;
    private List<Travel> travelList;

    public TravelAdapter(Activity activity, List<Travel> travelList) {
        this.activity = activity;
        this.travelList = travelList;
    }


    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.row_travel_item, parent, false);
        return new TravelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        final TravelViewHolder vh = holder;
        final String UID = UserAuth.getInstance().getUserUID();
        final Travel travel = travelList.get(position);
//        volleyRequest(position);
        holder.title.setText(travel.getTitle());
        holder.price.setText(travel.getPrice() + "元");
        holder.lower.setText("最少" + travel.getLower_bound() + "人成行");
        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,TravelDetailActivity.class);
                intent.putExtra("travel",travel);
                activity.startActivity(intent);
//                new AlertDialog.Builder(activity)
//                        .setTitle(travel.getTitle())
//                        .setMessage("出發日期" + travel.getStart_date() + "結束日期" + travel.getEnd_date())
//                        .setPositiveButton("預訂", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (UID != "b07505019") {
//                                    DatabaseInvoker invoker = new DatabaseInvoker();
//                                    invoker.addCommand(new AddOrderCommand(new HanWen(), UID, new Order(travel, 1, 0, 0)));
//                                    invoker.assignCommand();
//
//                                }
//                            }
//                        })
//                        .setNegativeButton("取消", null)
//                        .show();
            }
        });

    }

    private void volleyRequest(int position) {
        final Travel travel = travelList.get(position);
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "https://pixabay.com/api/";
        String key = "?key="+"15945961-2835fdd302951c8f463bbf738";
        String[] array = travel.getTitle().split(" ");
        Log.d(TAG, "search"+array[0]);
        String q = "&q=" + array[0];  //key word
        String image_type = "&image_type=photo";
        String endpoint = url+key+q+image_type+"&lang=zh&per_page=3";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: "+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return travelList.size();
    }

    public class TravelViewHolder extends RecyclerView.ViewHolder {
        View box;
        ImageView image;
        TextView title;
        TextView price;
        TextView lower;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_row_travel);
            box = itemView.findViewById(R.id.box_row_travel);
            title = itemView.findViewById(R.id.title_row_travel);
            price = itemView.findViewById(R.id.price_row_travel);
            lower = itemView.findViewById(R.id.lower_row_travel);
        }
    }
}
