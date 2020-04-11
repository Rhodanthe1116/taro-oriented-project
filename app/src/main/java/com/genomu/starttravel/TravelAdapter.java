package com.genomu.starttravel;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.genomu.starttravel.travel_data.TravelCode;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.travel_data.Travel;
import com.genomu.starttravel.util.LookForCodesCommand;

import java.util.List;

import static com.genomu.starttravel.TravelDetailActivity.FUNC_TRA;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {

    public static long getSeed(Travel travel){
        return Long.parseLong(travel.getProduct_key().substring(4))
                +Long.parseLong(travel.getEnd_date().substring(8))*51;
    }

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
        long seed = getSeed(travel);
        // waiting GUI here
        holder.image.setImageResource(R.drawable.alert);
        parseCountryName(travel.getTravel_code(),activity,holder.image,seed);
        if(travel.getTitle().length()>20){
            holder.title.setText(travel.getTitle().substring(0,20)+"...");
        }else {
            holder.title.setText(travel.getTitle());
        }
        holder.price.setText(travel.getPrice() + "元");
        holder.lower.setText("最少" + travel.getLower_bound() + "人成行");
        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,TravelDetailActivity.class);
                intent.putExtra("travel",travel);
                activity.startActivityForResult(intent,FUNC_TRA);
            }
        });

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
