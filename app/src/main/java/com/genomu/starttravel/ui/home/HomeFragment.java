package com.genomu.starttravel.ui.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genomu.starttravel.MainActivity;
import com.genomu.starttravel.R;
import com.genomu.starttravel.ui.search.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private EditText ed_tx;
    private ImageButton btn;
    private List<GalleryPhoto> photos;
    private View view;
    private ImageView cover;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        findViews();

        ed_tx.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    goSearch();
                    return true;
                }
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                getDatabaseTest();
                goSearch();
            }
        });

        setupPhotos();
        RecyclerView recyclerView = view.findViewById(R.id.travellist_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        PhotoAdapter p_ad = new PhotoAdapter();
        recyclerView.setAdapter(p_ad);
//        Log.d(TAG, "cover initial height"+cover.getMeasuredHeight());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>20){
                    ValueAnimator anim = ValueAnimator.ofInt(cover.getMeasuredHeight(), 480);
//                    Log.d(TAG, "cover height:"+cover.getMeasuredHeight());
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
                            layoutParams.height = val;
                            cover.setLayoutParams(layoutParams);
                        }
                    });
                    anim.setDuration(500);
                    anim.start();
                }else if(dy<-15){
                    ValueAnimator anim = ValueAnimator.ofInt(cover.getMeasuredHeight(), 720);
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
                            layoutParams.height = val;
                            cover.setLayoutParams(layoutParams);
                        }
                    });
                    anim.setDuration(500);
                    anim.start();
                }
                Log.d(TAG, "onScrolled: "+dy);
            }
        });
        return view;
    }


    private void findViews() {
        ed_tx = view.findViewById(R.id.search_bar_home);
        btn = view.findViewById(R.id.search_bar_btn_home);
        cover = view.findViewById(R.id.cover_home);
    }

    /*private void closeKeyboard(View currentFocusView){
            if(currentFocusView != null){
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(),0);
            }
    }*/

    private void goSearch(){
        EditText edx = getEd_tx();
        MainActivity.setSearch_content(edx.getText().toString());
        MainActivity.navGto(R.id.navigation_search);
    }

    private void setupPhotos() {
        photos = new ArrayList<>();
        String[] photo_titles = getResources().getStringArray(R.array.photo_titles);
        photos.add(new GalleryPhoto(photo_titles[0],R.drawable.gallery_ph01));
        photos.add(new GalleryPhoto(photo_titles[1],R.drawable.gallery_ph02));
        photos.add(new GalleryPhoto(photo_titles[2],R.drawable.gallery_ph03));
        photos.add(new GalleryPhoto(photo_titles[3],R.drawable.gallery_ph04));
    }

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{
        @NonNull
        @Override
        public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.gallery_photo,parent,false);
            return new PhotoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
            GalleryPhoto photo = photos.get(position);
            holder.title_text.setText(photo.getTitle());
            holder.photo_image.setImageResource(photo.getImage());
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        public class PhotoViewHolder extends RecyclerView.ViewHolder {
            ImageView photo_image;
            TextView title_text;
            public PhotoViewHolder(@NonNull View itemView) {
                super(itemView);
                photo_image = itemView.findViewById(R.id.gallery_photo);

                title_text = itemView.findViewById(R.id.gallery_name);

            }
        }
    }

    public EditText getEd_tx() {
        return ed_tx;
    }

}
