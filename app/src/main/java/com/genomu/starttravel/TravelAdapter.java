package com.genomu.starttravel;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.genomu.starttravel.util.AddOrderCommand;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.travel_data.Travel;

import java.util.List;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {

    private static final String TAG = TravelAdapter.class.getSimpleName();
    private Activity activity;
    private List<Travel> travelList;
    private boolean clickable;

    public TravelAdapter(Activity activity,List<Travel> travelList,boolean clickable) {
        this.activity = activity;
        this.travelList = travelList;
        this.clickable = clickable;
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.row_travel_item,parent,false);
        return new TravelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        final String UID = UserAuth.getInstance().getUserUID();
        final Travel travel = travelList.get(position);
        holder.title.setText(travel.getTitle());
        holder.price.setText(travel.getPrice()+"元");
        holder.lower.setText("最少"+travel.getLower_bound()+"人成行");
        if(clickable){
            holder.box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity)
                            .setTitle(travel.getTitle())
                            .setMessage("出發日期"+travel.getStart_date()+"結束日期"+travel.getEnd_date())
                            .setPositiveButton("預訂", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(UID!="b07505019"){
                                        DatabaseInvoker invoker = new DatabaseInvoker();
                                        invoker.addCommand(new AddOrderCommand(new HanWen(),UID,new Order(travel)));
                                        invoker.assignCommand();
                                    }
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return travelList.size();
    }

    public class TravelViewHolder extends RecyclerView.ViewHolder {
        View box;
        TextView title;
        TextView price;
        TextView lower;
        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            box = itemView.findViewById(R.id.box_row_travel);
            title = itemView.findViewById(R.id.title_row_travel);
            price = itemView.findViewById(R.id.price_row_travel);
            lower = itemView.findViewById(R.id.lower_row_travel);
        }
    }
}
