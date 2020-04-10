package com.genomu.starttravel.ui.search;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genomu.starttravel.MainActivity;
import com.genomu.starttravel.R;
import com.genomu.starttravel.ui.DatePickerFragment;
import com.genomu.starttravel.util.DBAspect;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.GetTravelsResultCommand;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.TravelsDBObserver;

public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private EditText edx_bar;
    private View view;
    private ImageButton im_btn;
    private Button start_btn;
    private Button end_btn;
    private Button reset_btn;
    private Spinner sorting_spn;
    private Spinner place_spn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search,container,false);
        findViews();
        if(MainActivity.getSearch_content()!="" && MainActivity.getSearched()){
            edx_bar.setText(MainActivity.getSearch_content());
            MainActivity.setSearched(false);
        }

        ArrayAdapter<CharSequence> spnAdapter =
                ArrayAdapter.createFromResource(getActivity(),R.array.sorting,android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sorting_spn.setAdapter(spnAdapter);
        spnAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.place_filtering,android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        place_spn.setAdapter(spnAdapter);
        defaultSearchResult();
        im_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sorting = sorting_spn.getSelectedItem().toString();
                String place = place_spn.getSelectedItem().toString();
                String start = start_btn.getText().toString();
                String end = end_btn.getText().toString();
                Log.d(TAG, "search range: "+start+","+end);
                RecyclerView recyclerView = view.findViewById(R.id.result_search);
                DatabaseInvoker invoker = new DatabaseInvoker();
                GetTravelsResultCommand command = new GetTravelsResultCommand(new HanWen(),20,start,end,place);
                TravelsDBObserver observer = new TravelsDBObserver(recyclerView,getActivity());
                DBAspect aspect = DBAspect.TRAVELS;
                aspect = getDbAspect(sorting, aspect);
                command.attach(observer,aspect);
                invoker.addCommand(command);
                invoker.assignCommand();
            }
        });

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isStartBtn = true;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getParentFragmentManager(),"start picker");
            }
        });
        end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isStartBtn = false;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getParentFragmentManager(),"end picker");
            }
        });
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_btn.setText(R.string.start_date_btn);
                end_btn.setText(R.string.end_date_btn);
            }
        });
        return view;
    }

    private DBAspect getDbAspect(String sorting, DBAspect aspect) {
        switch (sorting){
            case "price:descending":
                aspect = DBAspect.PRICE_D;
                break;
            case "price:ascending":
                aspect = DBAspect.PRICE_A;
                break;
        }
        return aspect;
    }


    private void defaultSearchResult() {
        RecyclerView recyclerView = view.findViewById(R.id.result_search);
        DatabaseInvoker invoker = new DatabaseInvoker();
        GetTravelsResultCommand command = new GetTravelsResultCommand(new HanWen(),20);
        TravelsDBObserver observer = new TravelsDBObserver(recyclerView,getActivity());
        command.attach(observer, DBAspect.TRAVELS);
        invoker.addCommand(command);
        invoker.assignCommand();
    }

    private void findViews() {
        sorting_spn = view.findViewById(R.id.sorting_search);
        place_spn = view.findViewById(R.id.place_filtering_search);
        edx_bar = view.findViewById(R.id.bar_search);
        im_btn = view.findViewById(R.id.go_search_search);
        start_btn = view.findViewById(R.id.start_date_search);
        end_btn = view.findViewById(R.id.end_date_btn);
        reset_btn = view.findViewById(R.id.reset_date);
    }
}
