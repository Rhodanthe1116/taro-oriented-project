package com.genomu.starttravel;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.genomu.starttravel.activity.TravelDetailActivity;
import com.genomu.starttravel.travel_data.TravelCode;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.LookForCodesCommand;
import com.genomu.starttravel.util.OnOneOffClickListener;
import com.genomu.starttravel.util.TravelStateOffice;

import java.text.ParseException;
import java.util.List;

import static com.genomu.starttravel.activity.TravelDetailActivity.FUNC_TRA;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {

    public static long getSeed(Travel travel){
        return Long.parseLong(travel.getProduct_key().substring(4))
                +Long.parseLong(travel.getEnd_date().substring(8))*51;
    }

    private static final String TAG = TravelAdapter.class.getSimpleName();
    private Activity activity;
    private List<Travel> travelList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        final Travel travel = travelList.get(position);
        setStatusUI(holder, travel);
        long seed = getSeed(travel);
        // waiting GUI here
        holder.image.setImageResource(R.drawable.alert);
        parseCountryName(travel.getTravel_code(),activity,holder.image,seed);
        if(travel.getTitle().length()>20){
            holder.title.setText(travel.getTitle().substring(0,15)+"...");
        }else {
            holder.title.setText(travel.getTitle());
        }
        holder.price.setText(travel.getPrice() + "元");
        holder.box.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(activity, TravelDetailActivity.class);
                intent.putExtra("travel",travel);
                activity.startActivityForResult(intent,FUNC_TRA);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatusUI(@NonNull TravelViewHolder holder, Travel travel) {
        try {
            TravelStateOffice office = new TravelStateOffice(travel);

            if(office.getGrouping()==TravelStateOffice.ENSURE){
                holder.status.setText(activity.getString(R.string.row_travel_status02));
                holder.status.setBackground(activity.getDrawable(R.drawable.status02));
            }else if(office.getGrouping()==TravelStateOffice.FULL){
                holder.status.setText(activity.getString(R.string.row_travel_status03));
                holder.status.setBackground(activity.getDrawable(R.drawable.status03));
            }else {
                holder.status.setText(activity.getString(R.string.row_travel_status01));
                holder.status.setBackground(activity.getDrawable(R.drawable.status01));
            }

            if(office.getState()<TravelStateOffice.NOT_YET_START || office.getGrouping()==TravelStateOffice.FULL){
                holder.grey.setAlpha(0.5f);
            }else{
                holder.grey.setAlpha(0.0f);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void parseCountryName(int code,Activity activity,ImageView imageView,long seed){
        DatabaseInvoker invoker = new DatabaseInvoker();
        invoker.addCommand(new LookForCodesCommand(new HanWen(),code, new TravelCode(),activity,imageView,seed));
        invoker.assignCommand();
    }


    @Override
    public int getItemCount() {
        return travelList.size();
    }

    public class TravelViewHolder extends RecyclerView.ViewHolder {
        CardView box;
        View grey;
        ImageView image;
        TextView title;
        TextView price;
        TextView status;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            grey = itemView.findViewById(R.id.grey);
            status = itemView.findViewById(R.id.status_travel);
            image = itemView.findViewById(R.id.image_row_travel);
            box = itemView.findViewById(R.id.box_travel);
            title = itemView.findViewById(R.id.title_row_travel);
            price = itemView.findViewById(R.id.price_row_travel);
        }
    }
}
