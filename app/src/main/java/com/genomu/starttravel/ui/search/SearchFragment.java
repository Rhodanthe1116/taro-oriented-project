package com.genomu.starttravel.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.genomu.starttravel.MainActivity;
import com.genomu.starttravel.R;
import com.genomu.starttravel.TravelHandlerActivity;

public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private EditText edx_bar;
    private View view;
    private ImageButton im_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search,container,false);
        findViews();
        if(MainActivity.getSearch_content()!="" && MainActivity.getSearched()){
            edx_bar.setText(MainActivity.getSearch_content());
            MainActivity.setSearched(false);
        }
        im_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TravelHandlerActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void findViews() {
        edx_bar = view.findViewById(R.id.bar_search);
        im_btn = view.findViewById(R.id.go_search_search);
    }
}
