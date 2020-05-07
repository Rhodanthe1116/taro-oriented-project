package com.genomu.starttravel.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.genomu.starttravel.R;
import com.genomu.starttravel.util.OnOneOffClickListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private List<GalleryPhoto> photos;
    private List<ImageView> hots;
    private List<ImageView> txs;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        findViews();
        setupPhotos();
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new HomeSliderAdapter());
        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();
        return view;
    }


    private void findViews() {
        setUpHots();
        setUpTxs();
        for(int i = 0;i<4;i++){
            final int finalI = i;
            hots.get(i).setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    clickingHot(finalI);
                }
            });
        }
    }

    private void clickingHot(int i){
        switch (i){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    private void setUpTxs() {
        txs = new ArrayList<>();
        txs.add((ImageView) view.findViewById(R.id.ph_tx1));
        txs.add((ImageView) view.findViewById(R.id.ph_tx2));
        txs.add((ImageView) view.findViewById(R.id.ph_tx3));
        txs.add((ImageView) view.findViewById(R.id.ph_tx4));
    }

    private void setUpHots() {
        hots = new ArrayList<>();
        hots.add((ImageView) view.findViewById(R.id.ph_hot1));
        hots.add((ImageView) view.findViewById(R.id.ph_hot2));
        hots.add((ImageView) view.findViewById(R.id.ph_hot3));
        hots.add((ImageView) view.findViewById(R.id.ph_hot4));
    }

    private void setupPhotos() {
        photos = new ArrayList<>();
        String[] photo_titles = getResources().getStringArray(R.array.photo_titles);
        photos.add(new GalleryPhoto(photo_titles[0],R.drawable.gallery_ph01));
        photos.add(new GalleryPhoto(photo_titles[1],R.drawable.gallery_ph02));
        photos.add(new GalleryPhoto(photo_titles[2],R.drawable.gallery_ph03));
        photos.add(new GalleryPhoto(photo_titles[3],R.drawable.gallery_ph04));
    }

    public class HomeSliderAdapter extends SliderViewAdapter<HomeSliderAdapter.HomeViewHolder> {

        @Override
        public HomeViewHolder onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item,null);
            return new HomeViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(HomeViewHolder viewHolder, int position) {
            GalleryPhoto photo = photos.get(position);
            viewHolder.image.setImageResource(photo.getImage());
        }

        @Override
        public int getCount() {
            return photos.size();
        }

        class HomeViewHolder extends SliderViewAdapter.ViewHolder{
            ImageView image;
            public HomeViewHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image_slide_item);
            }
        }
    }

}
