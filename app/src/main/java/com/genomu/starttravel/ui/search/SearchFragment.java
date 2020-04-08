package com.genomu.starttravel.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genomu.starttravel.MainActivity;
import com.genomu.starttravel.R;
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
                RecyclerView recyclerView = view.findViewById(R.id.result_search);
                DatabaseInvoker invoker = new DatabaseInvoker();
                GetTravelsResultCommand command = new GetTravelsResultCommand(new HanWen(),10);
                TravelsDBObserver observer = new TravelsDBObserver(recyclerView,getActivity());
                switch (sorting){
                    case "price:descending":
                        command.attach(observer,DBAspect.PRICE_D);
                        break;
                    case "price:ascending":
                        command.attach(observer,DBAspect.PRICE_A);
                        break;
                }
                invoker.addCommand(command);
                invoker.assignCommand();
            }
        });
        return view;
    }


    private void defaultSearchResult() {
        RecyclerView recyclerView = view.findViewById(R.id.result_search);
        DatabaseInvoker invoker = new DatabaseInvoker();
        GetTravelsResultCommand command = new GetTravelsResultCommand(new HanWen(),10);
        TravelsDBObserver observer = new TravelsDBObserver(recyclerView,getActivity());
        command.attach(observer, DBAspect.TRAVELS);
        invoker.addCommand(command);
        invoker.assignCommand();

//        original design
//        TravelParser travelParser = new TravelParser(getActivity());
//        List<Travel> travelList = travelParser.getParsedList();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        TravelAdapter travelAdapter = new TravelAdapter(getActivity(),travelList,true);
//        recyclerView.setAdapter(travelAdapter);
    }

    private void findViews() {
        sorting_spn = view.findViewById(R.id.sorting_search);
        place_spn = view.findViewById(R.id.place_filtering_search);
        edx_bar = view.findViewById(R.id.bar_search);
        im_btn = view.findViewById(R.id.go_search_search);
    }
}
