package com.genomu.starttravel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genomu.starttravel.travel_data.Travel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private static final String TAG = TravelAdapter.class.getSimpleName();
    private Activity activity;
    private List<Order> orderList;

    public OrderAdapter(Activity activity, List<Order> orderList) {
        this.activity = activity;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.row_travel_item, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        final OrderAdapter.OrderViewHolder vh = holder;
        final String UID = UserAuth.getInstance().getUserUID();
        final Order order = orderList.get(position);
        final Travel travel = order.getTravel();
//        volleyRequest(position);
        holder.title.setText(travel.getTitle());
        holder.price.setText(travel.getPrice() + "元");
        holder.lower.setText("最少" + travel.getLower_bound() + "人成行");
        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(activity,UserOrderActivity.class);
                    intent.putExtra("order",order);
                    activity.startActivity(intent);
                }
        });

    }

    private void volleyRequest(int position) {
        final Travel travel = orderList.get(position).getTravel();
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
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        View box;
        ImageView image;
        TextView title;
        TextView price;
        TextView lower;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_row_travel);
            box = itemView.findViewById(R.id.box_row_travel);
            title = itemView.findViewById(R.id.title_row_travel);
            price = itemView.findViewById(R.id.price_row_travel);
            lower = itemView.findViewById(R.id.lower_row_travel);
        }
    }
}
