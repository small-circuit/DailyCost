package virgil.dailycost.page;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import virgil.dailycost.R;
import virgil.dailycost.ROOM.AppDatabase;
import virgil.dailycost.ROOM.Record;
import virgil.dailycost.adapter.DayAdapter;
import virgil.dailycost.helper.RecyclerItemClickListener;
import virgil.dailycost.models.MonthDay;

import static virgil.dailycost.Constant.INTENT_DAY;
import static virgil.dailycost.Constant.INTENT_MONTH;
import static virgil.dailycost.Constant.INTENT_YEAR;

public class DayList extends AppCompatActivity {

    private View mButton_Add;
    private View mButton_Back;

    private DayAdapter mAdapter;
    private ArrayList<MonthDay> dayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private String enterDay;
    public static Integer YEAR;

    private Integer month;
    private Integer year;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DayList.this,MonthList.class));
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);

        if(getIntent().hasExtra(INTENT_MONTH)) month = getIntent().getIntExtra(INTENT_MONTH,-1);
        if(getIntent().hasExtra(INTENT_YEAR)) year = getIntent().getIntExtra(INTENT_YEAR, -1);

        mButton_Back = (View) findViewById(R.id.day_list_button_back);
        mButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DayList.this,MonthList.class));
                finish();
            }
        });

        mButton_Add = (View) findViewById(R.id.day_list_button_ADD);
        mButton_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DayList.this);
                dialog.setContentView(R.layout.dialog_enter_month_day);
                dialog.setTitle("Please enter day");
                final EditText mEditText_Day = (EditText) dialog.findViewById(R.id.enter_month_day_edittext_day);
                dialog.findViewById(R.id.enter_month_day_button_OK).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mEditText_Day.getText().toString().equals("") && checkConsistent(Integer.valueOf(mEditText_Day.getText().toString())) == "OK") {

                            dayList.add(0, new MonthDay(Integer.valueOf(month), Integer.valueOf(mEditText_Day.getText().toString()),0f));
                            updateRecycleView();
                            enterDay = mEditText_Day.getText().toString();

                            compositeDisposable.add(
                                    Completable.fromAction(new Action() {
                                        @Override
                                        public void run() throws Exception {
                                            createOneDayRecordOnDataBase(Integer.parseInt(enterDay));
                                        }
                                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action() {
                                        @Override
                                        public void run() throws Exception {
                                            dialog.cancel();
                                        }
                                    })
                            );
                        }else if(checkConsistent(Integer.valueOf(mEditText_Day.getText().toString())) == "DUPLICATED"){
                            Toast.makeText(DayList.this,"Entered Day is depulicated with others.",Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(DayList.this,"Please enter Day.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.findViewById(R.id.enter_month_day_button_CANCEL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        compositeDisposable.add(
                readDataBase().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Record>>() {
                    @Override
                    public void accept(List<Record> records) throws Exception {
                        System.out.println(records.size());
                        for (Record each : records){
                            dayList.add(new MonthDay(month,each.getDay(),each.getHKD()));
                        }
                        prepareRecycleView();
                    }
                }));
    }

    private void prepareRecycleView(){
        mAdapter = new DayAdapter(dayList,year);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_day);
        recyclerView.setNestedScrollingEnabled(false);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Integer selectedDay = dayList.get(position).getDay();
                Intent intent = new Intent(DayList.this,DailyCost_New.class);
                intent.putExtra(INTENT_DAY,selectedDay);
                intent.putExtra(INTENT_MONTH,month);
                intent.putExtra(INTENT_YEAR,year);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongItemClick(View view, final int position) {
                final Dialog dialog = new Dialog(DayList.this);
                dialog.setContentView(R.layout.dialog_yes_no);
                dialog.setTitle("Delete?");
                final EditText mEditText_Month = (EditText) dialog.findViewById(R.id.enter_month_year_edittext_month);
                final EditText mEditText_Year = (EditText) dialog.findViewById(R.id.enter_month_year_edittext_year);
                dialog.findViewById(R.id.yes_no_button_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Integer selectedDay = dayList.get(position).getDay();
                        compositeDisposable.add(
                                Completable.fromAction(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        deleteOneDayRecord(selectedDay);
                                    }
                                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        dayList.remove(position);
                                        updateRecycleView();
                                        dialog.cancel();
                                    }
                                })
                        );
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
        mAdapter = new DayAdapter(dayList,year);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void deleteOneDayRecord(Integer selectedDay){
        AppDatabase.getInstance(DayList.this).daoAccess().deleteOneDayRecord(year,month,selectedDay);
    }

    private void createOneDayRecordOnDataBase (Integer day) {

        Record record = new Record(null,year,month,day,null,null,null,Record.COST,null,null,null);
        AppDatabase.getInstance(DayList.this).daoAccess().insertANewRecord(record);

        Record dailyRecord = new Record(null, year, month, day, null, null, null, Record.REMARK, null, null, null );
        AppDatabase.getInstance(DayList.this).daoAccess().insertANewRecord(dailyRecord);

    }

    private Maybe<List<Record>> readDataBase(){
        return AppDatabase.getInstance(DayList.this).daoAccess().getDayList(year,month);
    }

    private String checkConsistent(int day){
        for (MonthDay each:dayList){
            if(each.getDay() == day){
            return "DUPLICATED";
        }
        }
        return "OK";
    }

    }



