package com.genomu.starttravel.nav_pages.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.genomu.starttravel.R;
import com.genomu.starttravel.activity.ScenicSpotActivity;
import com.genomu.starttravel.util.OnOneOffClickListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.genomu.starttravel.activity.ScenicSpotActivity.FUNC_SCS;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private List<GalleryPhoto> photos;
    private List<GalleryPhoto> ads;
    private List<ImageView> hots;
    private List<ImageView> txs;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        findViews();
        setupPhotos();
        setupAds();
        setUpSlider();
        return view;
    }

    private void setUpSlider() {
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new HomeSliderAdapter());
        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();
    }

    private void setupAds() {
        ads = new ArrayList<>();
        String[] adTitles = getResources().getStringArray(R.array.advertise_titles);
        ads.add(new GalleryPhoto(adTitles[0],R.drawable.advertise_00));
        ads.add(new GalleryPhoto(adTitles[1],R.drawable.advertise_01));
        ads.add(new GalleryPhoto(adTitles[2],R.drawable.advertise_02));
        ads.add(new GalleryPhoto(adTitles[2],R.drawable.advertise_03));
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
            txs.get(i).setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    clickingTx(finalI);
                }
            });
        }
    }

    private void clickingTx(int i) {
        String[] txTitles = getResources().getStringArray(R.array.tx_titles);
        goSceneDetail(i,txTitles);
    }

    private void clickingHot(int i){
        String[] hotTitles = getResources().getStringArray(R.array.photo_titles);
        goSceneDetail(i,hotTitles);
    }
    private void goSceneDetail(int i,String[] titles){
        Intent intent = new Intent(getActivity(), ScenicSpotActivity.class);
        intent.putExtra("spots",titles[i]);
        startActivityForResult(intent,FUNC_SCS);
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
            GalleryPhoto ad = ads.get(position);
            viewHolder.image.setImageResource(ad.getImage());
        }

        @Override
        public int getCount() {
            return ads.size();
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
