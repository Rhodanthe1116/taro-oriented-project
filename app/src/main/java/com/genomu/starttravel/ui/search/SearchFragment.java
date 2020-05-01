package com.genomu.starttravel.ui.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.genomu.starttravel.MainActivity;
import com.genomu.starttravel.R;
import com.genomu.starttravel.travel_data.PlaceCounselor;
import com.genomu.starttravel.travel_data.PlaceSuggestion;
import com.genomu.starttravel.ui.DatePickerFragment;
import com.genomu.starttravel.util.DBAspect;
import com.genomu.starttravel.util.DatabaseInvoker;
import com.genomu.starttravel.util.GetTravelsResultCommand;
import com.genomu.starttravel.util.HanWen;
import com.genomu.starttravel.util.TravelsDBObserver;

import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private View view;
    private FloatingSearchView searchView;
    private Button start_btn;
    private Button end_btn;
    private String sorting;
    private ProgressBar bar;

    private String lastQuery = "";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search,container,false);
        boolean remind = getActivity().getSharedPreferences("StartTravel", Context.MODE_PRIVATE)
                .getBoolean("remind_search",true);
        Log.d(TAG, "remind: "+remind);
        if(remind){
            new AlertDialog.Builder(getActivity())
                    .setTitle("搜尋須知")
                    .setMessage("長按日期鍵可以取消日期條件")
                    .setView(R.layout.dialog_remind)
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CheckBox checkBox = ((AlertDialog)dialog).findViewById(R.id.check_box_remind);
                            Log.d(TAG, "isChecked: "+checkBox.isChecked());
                            getActivity().getSharedPreferences("StartTravel",Context.MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("remind_search",!checkBox.isChecked())
                                    .apply();
                        }
                    })
                    .show();
        }
        findViews();
        defaultSearchResult();
        setUpSearchView();
        setUpMenuSorting();
        setBtn();

        return view;
    }

    private void setUpMenuSorting() {
        sorting = getString(R.string.price_d);
        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                Log.d(TAG, "onActionMenuItemSelected: "+item.getTitle());
                if(item.getTitle().equals(getString(R.string.price_d))){
                    sorting = getString(R.string.price_d);
                    searchPlace(lastQuery);
                }else if(item.getTitle().equals(getString(R.string.price_a))){
                    sorting = getString(R.string.price_a);
                    searchPlace(lastQuery);
                }
            }
        });
    }

    private void setUpSearchView() {
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")||newQuery.equals(lastQuery)) {
                    searchView.clearSuggestions();
                } else {
                    searchView.showProgress();
                    final long delay = 50;
                    findPlaceSuggestion(newQuery, delay);
                }
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                PlaceSuggestion suggestion = (PlaceSuggestion) searchSuggestion;
                lastQuery = suggestion.getBody();
                searchView.setSearchText(lastQuery);
                searchView.clearSearchFocus();
                searchPlace(lastQuery);

            }

            @Override
            public void onSearchAction(String currentQuery) {
                lastQuery = currentQuery;
                searchPlace(lastQuery);
            }
        });
    }

    private void findPlaceSuggestion(String newQuery, long delay) {
        PlaceCounselor counselor = new PlaceCounselor(getResources().getStringArray(R.array.place_filtering));
        counselor.findSuggestions(newQuery, 5, delay, new PlaceCounselor.OnFindSuggestionsListener() {
            @Override
            public void onResults(List<PlaceSuggestion> results) {
                searchView.swapSuggestions(results);
                searchView.hideProgress();
            }
        });
    }

    private void setBtn() {
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isStartBtn = true;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getParentFragmentManager(),"start picker");
            }
        });
        start_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.isStartBtn = true;
                start_btn.setText(R.string.start_date_btn);
                return true;
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
        end_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.isStartBtn = false;
                end_btn.setText(R.string.end_date_btn);
                return true;
            }
        });
        start_btn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "start_onTextChanged: "+s);
                searchPlace(lastQuery);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        end_btn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "end_onTextChanged: "+s);
                searchPlace(lastQuery);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void searchPlace(String place) {
        String start = start_btn.getText().toString();
        String end = end_btn.getText().toString();
        Log.d(TAG, "search range: "+start+","+end);
        RecyclerView recyclerView = view.findViewById(R.id.result_search);
        DatabaseInvoker invoker = new DatabaseInvoker();
        GetTravelsResultCommand command = new GetTravelsResultCommand(new HanWen(),20,start,end,place);
        TravelsDBObserver observer = new TravelsDBObserver(recyclerView,getActivity(),bar);
        DBAspect aspect = DBAspect.TRAVELS;
        aspect = getDbAspect(sorting, aspect);
        command.attach(observer,aspect);
        invoker.addCommand(command);
        invoker.assignCommand();
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
        final RecyclerView recyclerView = view.findViewById(R.id.result_search);
        DatabaseInvoker invoker = new DatabaseInvoker();
        GetTravelsResultCommand command = new GetTravelsResultCommand(new HanWen(),20);
        TravelsDBObserver observer = new TravelsDBObserver(recyclerView,getActivity(),bar);
        command.attach(observer, DBAspect.TRAVELS);
        invoker.addCommand(command);
        invoker.assignCommand();

    }

    private void findViews() {
        start_btn = view.findViewById(R.id.start_date_search);
        end_btn = view.findViewById(R.id.end_date_btn);
        bar = view.findViewById(R.id.progress_search);
        searchView = view.findViewById(R.id.floating_search_view);
    }
}
