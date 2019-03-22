package virgil.dailycost.page;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import virgil.dailycost.R;
import virgil.dailycost.ROOM.AppDatabase;
import virgil.dailycost.ROOM.Category_Record;
import virgil.dailycost.ROOM.Record;
import virgil.dailycost.adapter.SearchResultAdapter;

public class SearchActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<String> categoryName = new ArrayList<>();
    List<Category_Record> categories = new ArrayList<>();

    TextView emptyTextView;
    TextView totalTextView;
    Button button_newSearch;
    RecyclerView recyclerView;
    SearchResultAdapter searchResultAdapter;
    List<Record> records = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        button_newSearch = (Button) findViewById(R.id.searchActivity_newSearch);
        emptyTextView = (TextView) findViewById(R.id.searchActivity_empty_indicator);
        totalTextView = (TextView) findViewById(R.id.searchActivity_total_field);

        getCategoryList();

        button_newSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflatAlertDialog_SEARCH_DIALOG();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.searchActivity_recyclerView_result);
        recyclerView.setNestedScrollingEnabled(false);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        updateRecyclerView(records);
    }

    void updateRecyclerView(List<Record> records){
        if(records.size() == 0){
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            totalTextView.setVisibility(View.GONE);
        } else if(records.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
            totalTextView.setVisibility(View.VISIBLE);
            float resultTotalCost = 0f;
            for (Record each : records) resultTotalCost = each.getHKD() != null ? ( resultTotalCost + each.getHKD() ) : resultTotalCost;
            totalTextView.setText(String.valueOf(resultTotalCost));
            searchResultAdapter = new SearchResultAdapter(records, categories, SearchActivity.this);
            recyclerView.setAdapter(searchResultAdapter);
            searchResultAdapter.notifyDataSetChanged();
        }


    }

    void inflatAlertDialog_SEARCH_DIALOG() {
        AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(SearchActivity.this);
        final View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.dialog_searchconfig, null);
        alertDialog_Builder.setView(view);

        final RadioButton radioButton_COST = (RadioButton) view.findViewById(R.id.searchDialog_radioButton_COST);
        final RadioButton radioButton_REMARK = (RadioButton) view.findViewById(R.id.searchDialog_radioButton_REMARK);
        radioButton_COST.setChecked(true);

        final EditText editText_KEYWORD = (EditText) view.findViewById(R.id.searchDialog_editText_KEYWORD);
        editText_KEYWORD.setText("");

        final Spinner spinner_Category = (Spinner) view.findViewById(R.id.searchDialog_spinner_CATEGORY);
        final SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item, categoryName);
        spinner_Category.setAdapter(spinnerAdapter);

        final CheckBox checkBox_isByTime = (CheckBox) view.findViewById(R.id.searchDialog_checkBox_BY_TIME);

        final LinearLayout linearLayout_FROM = (LinearLayout) view.findViewById(R.id.searchDialog_linearLayout_FROM_DATE);
        final EditText editText_FROM = (EditText) view.findViewById(R.id.searchDialog_editText_FROM_DATE);
        editText_FROM.setEnabled(false);
        final LinearLayout linearLayout_TO = (LinearLayout) view.findViewById(R.id.searchDialog_linearLayout_TO_DATE);
        final EditText editText_TO = (EditText) view.findViewById(R.id.searchDialog_editText_TO_DATE);
        editText_TO.setEnabled(false);


        final Button button_OK = (Button) view.findViewById(R.id.searchDialog_button_OK);
        final Button button_CANCEL = (Button) view.findViewById(R.id.searchDialog_button_CANCEL);

        final AlertDialog alertDialog = alertDialog_Builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.show();

        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = "%" + editText_KEYWORD.getText().toString() + "%";

                Integer categoryID = 0;
                if(spinner_Category.getSelectedItemPosition() != 0)
                    categoryID = categories.get(spinner_Category.getSelectedItemPosition() - 1).getId();

                if(radioButton_COST.isChecked()) {
                    if(spinner_Category.getSelectedItemPosition() != 0){
                        compositeDisposable.add(
                            AppDatabase.getInstance(SearchActivity.this).daoAccess().fetchCostRecord(keyword, categoryID)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<List<Record>>() {
                                @Override
                                public void accept(List<Record> record) throws Exception {
                                    if(checkIfTimeFieldOK(view)) {
                                        updateRecyclerView(
                                                reverseResult(
                                                    filteredRecord_byDate(view, record)
                                                )
                                        );
                                        alertDialog.dismiss();
                                    }
                                }
                            })
                        );
                    }
                    else if (spinner_Category.getSelectedItemPosition() == 0){
                        compositeDisposable.add(
                            AppDatabase.getInstance(SearchActivity.this).daoAccess().fetchCostRecord(keyword)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<List<Record>>() {
                                        @Override
                                        public void accept(List<Record> record) throws Exception {
                                            if(checkIfTimeFieldOK(view)) {
                                                updateRecyclerView(
                                                        reverseResult(
                                                                filteredRecord_byDate(view, record)
                                                        )
                                                );
                                                alertDialog.dismiss();
                                            }
                                        }
                                    })
                        );
                    }
                }else if(radioButton_REMARK.isChecked()){

                }
            }
        });
        button_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        radioButton_REMARK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_Category.setEnabled(false);
            }
        });

        radioButton_COST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_Category.setEnabled(true);
            }
        });

        checkBox_isByTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox_isByTime.isChecked()){
                    editText_FROM.setEnabled(true);
                    editText_TO.setEnabled(true);
                }else if (!checkBox_isByTime.isChecked()){
                    editText_FROM.setEnabled(false);
                    editText_TO.setEnabled(false);
                }
            }
        });

    }

    void getCategoryList(){
        compositeDisposable.add(
                AppDatabase.getInstance(SearchActivity.this).daoAccess().getAllCategories()
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Category_Record>>() {
                            @Override
                            public void accept(List<Category_Record> category_records) throws Exception {
                                categoryName.add("(empty)");
                                for (Category_Record each : category_records) categoryName.add(each.getCategoryName());
                                categories = category_records;
                            }
                        })
        );
    }

    List<Record> reverseResult(List<Record> records){
        List<Record> reversedList = new ArrayList<>();
        for(int i = records.size() - 1 ; i >= 0 ; i--) reversedList.add(records.get(i));
        return reversedList;
    }

    Boolean checkIfTimeFieldOK(View view){
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.searchDialog_checkBox_BY_TIME);
        EditText editText_FROM = (EditText) view.findViewById(R.id.searchDialog_editText_FROM_DATE);
        EditText editText_TO = (EditText) view.findViewById(R.id.searchDialog_editText_TO_DATE);
        if(!checkBox.isChecked()) {
            return true;
        }
        else if(checkBox.isChecked()){
            if(editText_FROM.getText().length() < 8) return false;
            if(editText_TO.getText().length() < 8) return false;
            if(Integer.parseInt(editText_FROM.getText().toString()) > Integer.parseInt(editText_TO.getText().toString())) return false;
        }
        return true;
    }

    List<Record> filteredRecord_byDate (View view, List<Record> records){
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.searchDialog_checkBox_BY_TIME);
        EditText editText_FROM = (EditText) view.findViewById(R.id.searchDialog_editText_FROM_DATE);
        EditText editText_TO = (EditText) view.findViewById(R.id.searchDialog_editText_TO_DATE);

        if(!checkBox.isChecked()) return records;

        List<Record> sortedList = new ArrayList<>();
        for(Record each : records){
            String year = String.valueOf(each.getYear());
            String month = each.getMonth() < 10 ? "0" + String.valueOf(each.getMonth()) : String.valueOf(each.getMonth());
            String day = each.getDay() <10 ? "0" + String.valueOf(each.getDay()) : String.valueOf(each.getDay());

            String s = year + month + day;

            if(Integer.parseInt(editText_FROM.getText().toString()) < Integer.parseInt(s) &&
                    Integer.parseInt(s) < Integer.parseInt(editText_TO.getText().toString())){
                sortedList.add(each);
            }
        }
        return sortedList;
    }

}
