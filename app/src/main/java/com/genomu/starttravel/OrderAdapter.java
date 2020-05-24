package com.genomu.starttravel;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.genomu.starttravel.activity.UserOrderActivity;
import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.OnOneOffClickListener;
import com.genomu.starttravel.util.TravelStateOffice;

import java.text.ParseException;
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
        View view = activity.getLayoutInflater().inflate(R.layout.row_order_item, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        final OrderAdapter.OrderViewHolder vh = holder;
        final String UID = UserAuth.getInstance().getUserUID();
        final Order order = orderList.get(position);
        final Travel travel = order.getTravel();
        long seed = TravelAdapter.getSeed(travel);
        //waiting GUI here
        holder.image.setImageResource(R.drawable.alert);
        TravelAdapter.parseCountryName(travel.getTravel_code(),activity,holder.image,seed);
        if(travel.getTitle().length()>20){
            holder.title.setText(travel.getTitle().substring(0,20)+"...");
        }else {
            holder.title.setText(travel.getTitle());
        }
        holder.status.setVisibility(View.GONE);
        holder.day.setVisibility(View.GONE);
        try {
            TravelStateOffice office = new TravelStateOffice(travel);
            if(office.getState()==TravelStateOffice.CAN_BE_MODIFIED){
                holder.status.setImageResource(R.drawable.od_sta_await);
                holder.day.setText(Long.toString(office.dayCount()));
                holder.day.setVisibility(View.VISIBLE);
            }else if(office.getState()==TravelStateOffice.ON_THE_ROAD){
                holder.status.setImageResource(R.drawable.od_sta_ing);
            }else if(office.isRoundedOff()){
                holder.status.setImageResource(R.drawable.od_sta_home);
            }else if(office.isCancelled()){
                holder.status.setImageResource(R.drawable.od_sta_close);
            }
            holder.status.setVisibility(View.VISIBLE);
        }catch(ParseException e){
            e.printStackTrace();
        }
        holder.price.setText(order.getTotal()+ "元");
        holder.box.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(activity, UserOrderActivity.class);
                intent.putExtra("order",order);
                activity.startActivityForResult(intent,UserOrderActivity.FUNC_USO);
            }
        });

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        CardView box;
        ImageView image;
        TextView title;
        TextView price;
        TextView day;
        ImageView status;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_row_travel);
            status = itemView.findViewById(R.id.order_status_pic);
            box = itemView.findViewById(R.id.box_travel);
            title = itemView.findViewById(R.id.title_row_travel);
            price = itemView.findViewById(R.id.price_row_travel);
            day = itemView.findViewById(R.id.order_day_count);
//            lower = itemView.findViewById(R.id.lower_row_travel);
        }
    }
}
