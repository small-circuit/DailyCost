package virgil.dailycost.page;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import virgil.dailycost.R;
import virgil.dailycost.ROOM.AppDatabase;
import virgil.dailycost.ROOM.Record;
import virgil.dailycost.adapter.MonthAdapter;
import virgil.dailycost.helper.RecyclerItemClickListener;
import virgil.dailycost.models.MonthYear;

import static virgil.dailycost.Constant.INTENT_MONTH;
import static virgil.dailycost.Constant.INTENT_YEAR;

public class MonthList extends AppCompatActivity implements View.OnClickListener{

    private MonthYear monthYear;
    private ArrayList<MonthYear> monthList = new ArrayList<>();
    private MonthAdapter mAdapter;
    private RecyclerView recyclerView;
    private String enterMonth;
    private String enterYear;
    private View mButton_ADD;
    private View mButton_OK;
    private View mButton_CANCEL;
    private View mButton_CATEGORY_MANAGEMENT;
    private View mButton_SEARCH_COST;
    private EditText mEditText_Year;
    private EditText mEditText_Month;
    private String SEPERATOR = "-";

    public CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        setTitle("MySpend");

        mButton_ADD = (Button) findViewById(R.id.month_list_button_ADD);
        mButton_OK = (Button) findViewById(R.id.enter_month_year_button_OK);
        mButton_CANCEL = (Button) findViewById(R.id.enter_month_year_button_CANCEL);
        mButton_CATEGORY_MANAGEMENT = (Button) findViewById(R.id.month_list_button_CATEGORY_MANAGEMENT);
        mButton_SEARCH_COST =(Button) findViewById(R.id.month_list_button_SEARCH_ACTIVITY);


        compositeDisposable.add(AppDatabase.getInstance(MonthList.this).daoAccess()
                .getMonthList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Record>>() {
                    @Override
                    public void accept(List<Record> records) throws Exception {
                        for (Record each : records) {
                            monthList.add(new MonthYear(each.getMonth(), each.getYear(), each.getHKD()));
                        }
                        initialize();
                    }
                }));

    }

    private void initialize(){
        mAdapter = new MonthAdapter(monthList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_month);
        recyclerView.setNestedScrollingEnabled(false);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Integer choosedMonth = monthList.get(position).getMonth();
                Integer choosedYear = monthList.get(position).getYear();
                Intent intent = new Intent(MonthList.this, DayList.class);
                intent.putExtra(INTENT_MONTH, choosedMonth);
                intent.putExtra(INTENT_YEAR, choosedYear);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongItemClick(View view, final int position) {
                final Dialog dialog = new Dialog(MonthList.this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle("Delete?");
                final EditText mEditText_Month = (EditText) dialog.findViewById(R.id.enter_month_year_edittext_month);
                final EditText mEditText_Year = (EditText) dialog.findViewById(R.id.enter_month_year_edittext_year);
                dialog.findViewById(R.id.yes_no_button_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        compositeDisposable.add(Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                deleteOneMonthOnDataBase(monthList.get(position).getMonth(), monthList.get(position).getYear());
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                monthList.remove(position);
                                updateRecycleView();
                                dialog.cancel();
                            }
                        }));
                    }
                });
                dialog.findViewById(R.id.yes_no_button_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        }));
    }

    private void updateRecycleView(){
        mAdapter = new MonthAdapter(monthList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.month_list_button_ADD:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_enter_month_year);
                dialog.setTitle("Please enter month and year");
                final EditText mEditText_Month = (EditText) dialog.findViewById(R.id.enter_month_year_edittext_month);
                final EditText mEditText_Year = (EditText) dialog.findViewById(R.id.enter_month_year_edittext_year);
                dialog.findViewById(R.id.enter_month_year_button_OK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mEditText_Year.getText().toString().equals("")  && !mEditText_Month.getText().toString().equals("")) {
                        monthList.add(0, new MonthYear(Integer.valueOf(mEditText_Month.getText().toString()), Integer.valueOf(mEditText_Year.getText().toString()),0f));
                        updateRecycleView();
                        enterMonth = mEditText_Month.getText().toString();
                        enterYear = mEditText_Year.getText().toString();

                        compositeDisposable.add(Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                createOneMonthRecord(Integer.parseInt(enterYear),Integer.parseInt(enterMonth));
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                dialog.cancel();
                            }
                        }));

                    } else{
                        Toast.makeText(MonthList.this,"Please enter Month and Year.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
                dialog.findViewById(R.id.enter_month_year_button_CANCEL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                break;

            case R.id.month_list_button_CATEGORY_MANAGEMENT:
                startActivity(new Intent(MonthList.this, CategoryEdit.class));
                break;

            case R.id.month_list_button_SEARCH_ACTIVITY:
                startActivity(new Intent(MonthList.this, SearchActivity.class));
                break;

            default:
                break;

            }

        }

    private void createOneMonthRecord(Integer year, Integer month){
        Record costRecord = new Record(null, year, month, 1, null, null, null, Record.COST,null,null,null);
        AppDatabase.getInstance(MonthList.this).daoAccess().insertANewRecord(costRecord);
        Record dairyRecord = new Record(null, year, month, 1, null, null, null, Record.REMARK,null,null,null);
        AppDatabase.getInstance(MonthList.this).daoAccess().insertANewRecord(dairyRecord);
    }

    private void deleteOneMonthOnDataBase(Integer month, Integer year){
        AppDatabase.getInstance(MonthList.this).daoAccess().deleteOneMonthRecord(year, month);
    }

    }










