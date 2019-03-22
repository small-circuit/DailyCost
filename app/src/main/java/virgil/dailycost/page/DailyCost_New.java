package virgil.dailycost.page;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import virgil.dailycost.R;
import virgil.dailycost.ROOM.AppDatabase;
import virgil.dailycost.ROOM.Category_Record;
import virgil.dailycost.ROOM.Record;
import virgil.dailycost.adapter.DailyCostEditorAdapter;
import virgil.dailycost.helper.RecyclerItemClickListener;
import virgil.dailycost.listeners.DailyListListener;

import static virgil.dailycost.Constant.INTENT_DAY;
import static virgil.dailycost.Constant.INTENT_MONTH;
import static virgil.dailycost.Constant.INTENT_YEAR;
import static virgil.dailycost.ROOM.Record.COST;
import static virgil.dailycost.ROOM.Record.REMARK;

/**
 * Created by VIRGILH on 4/30/2018.
 */

public class DailyCost_New extends AppCompatActivity {

    private DailyCostEditorAdapter mAdapter;
    private List<Record> records = new ArrayList<>();
    private RecyclerView recyclerView;
    private EditText remark;
    private View button_Add;
    private View button_Back;
    private List<Category_Record> categories = new ArrayList<>();
    private Integer ASNY_CODE = 0;

    private Integer day, month, year;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Record remarkRecord = new Record();

    private DailyListListener dailyListListener;

    @Override
    public void onBackPressed() {}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_cost_new);

        if(getIntent().hasExtra(INTENT_DAY)) day = getIntent().getIntExtra(INTENT_DAY,-1);
        if(getIntent().hasExtra(INTENT_MONTH)) month = getIntent().getIntExtra(INTENT_MONTH,-1);
        if(getIntent().hasExtra(INTENT_YEAR)) year = getIntent().getIntExtra(INTENT_YEAR,-1);

        //Fetch Cost Record
        compositeDisposable.add(
                readCostRecord().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Record>>() {
                    @Override
                    public void accept(List<Record> returnRecord) throws Exception {
                        ASNY_CODE = ASNY_CODE + 1;
                        records = returnRecord;
                        if(ASNY_CODE == 3) prepareUI();
                    }
                })
        );

        //Fetch Remark Record
        compositeDisposable.add(
                readDairyRecord().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Record>>() {
                    @Override
                    public void accept(List<Record> records) throws Exception {
                        ASNY_CODE = ASNY_CODE + 1;
                        if(records.size() == 0){
                            compositeDisposable.add(
                                    Completable.fromAction(new Action() {
                                        @Override
                                        public void run() throws Exception {
                                            Record dailyRecord = new Record(null, year, month, day, null, null, null, Record.REMARK, null, null, null );
                                            dailyRecord.setId(Integer.parseInt(AppDatabase.getInstance(DailyCost_New.this).daoAccess().insertANewRecord(dailyRecord).toString()));
                                            remarkRecord = dailyRecord;
                                        }
                                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                                        @Override
                                        public void run() throws Exception {
                                            if(ASNY_CODE == 3) prepareUI();
                                        }
                                    })
                            );
                        }else {
                            remarkRecord = records.get(0);
                            if(ASNY_CODE == 3) prepareUI();
                        }
                    }
                })
        );

        //Fetch Category List
        compositeDisposable.add(
                getCategoryList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Category_Record>>() {
                    @Override
                    public void accept(List<Category_Record> category_records) throws Exception {
                        ASNY_CODE = ASNY_CODE + 1;
                        categories = category_records;
                        if(ASNY_CODE == 3) prepareUI();
                    }
                })
        );

    }

    private void prepareUI(){

        button_Add = (Button) findViewById(R.id.daily_cost_new_button_add);
        button_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer orderOfRecycleView = mAdapter.getItemCount();
                final Record record = new Record(null,year,month,day,null,null,null,COST,null,null,null);
                compositeDisposable.add(
                        io.reactivex.Observable.fromCallable(new Callable<Long>() {
                            @Override
                            public Long call() throws Exception {
                                return AppDatabase.getInstance(DailyCost_New.this).daoAccess().insertANewRecord(record);
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                record.setId(Integer.parseInt(aLong.toString()));
                            }
                        })
                );
                records.add(record);
                updateRecycleView();
            }
        });


        button_Back = (Button) findViewById(R.id.daily_cost_new_button_back);
        button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compositeDisposable.add(
                        Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                writeFile(records);
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                Intent intent = new Intent(DailyCost_New.this, DayList.class);
                                intent.putExtra(INTENT_YEAR, year);
                                intent.putExtra(INTENT_MONTH, month);
                                startActivity(intent);
                                finish();
                            }
                        })
                );
            }
        });

        mAdapter = new DailyCostEditorAdapter(records,categories,DailyCost_New.this);

        mAdapter.setDailyListListener(new DailyListListener() {
            @Override
            public void itemDeleteListener(Record record) {
                inflatAlertDialog_DELETE(record);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.daily_cost_new_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        remark = (EditText) findViewById(R.id.daily_cost_new_remark);
        if(remarkRecord.getStringSpare() != null) remark.setText(remarkRecord.getStringSpare());
    }

    private void updateRecycleView() {
        mAdapter = new DailyCostEditorAdapter(records,categories,DailyCost_New.this);
        mAdapter.setDailyListListener(new DailyListListener() {
            @Override
            public void itemDeleteListener(Record record) {
                inflatAlertDialog_DELETE(record);
            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private Maybe<List<Record>> readCostRecord() {
        return AppDatabase.getInstance(DailyCost_New.this).daoAccess().fetchRecordsOnOneDay(year, month, day, COST);
    }

    private Maybe<List<Record>> readDairyRecord() {
        return AppDatabase.getInstance(DailyCost_New.this).daoAccess().fetchRecordsOnOneDay(year, month, day, REMARK);
    }

    private Maybe<List<Category_Record>> getCategoryList(){
        return AppDatabase.getInstance(DailyCost_New.this).daoAccess().getAllCategories();
    }

    private void writeFile(List<Record> records) {
        for (Record each : records) {
            AppDatabase.getInstance(DailyCost_New.this).daoAccess().updateOneRecordById(each.getId(),each.getYear(),each.getMonth(),each.getDay(),each.getDescription(),each.getCategory(),each.getHKD(),each.getType(),each.getPhotoString(),each.getStringSpare(),each.getIntegerSpare());
        }
        if(remark.getText() == null) remark.setText("");
        AppDatabase.getInstance(DailyCost_New.this).daoAccess().updateOneRecordById(remarkRecord.getId(), remarkRecord.getYear(), remarkRecord.getMonth(), remarkRecord.getDay(), null, null, null, Record.REMARK, null, remark.getText().toString(), null);
    }

    void inflatAlertDialog_DELETE(final Record record) {

        AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(DailyCost_New.this);
        alertDialog_Builder.setMessage("delete?");

        alertDialog_Builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                compositeDisposable.add(
                        Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                AppDatabase.getInstance(DailyCost_New.this).daoAccess().deleteOneRecordByID(record.getId());
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                records.remove(record);
                                updateRecycleView();
                            }
                        })
                );
            }
        });
        alertDialog_Builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialog_Builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private Long createRecord(Record record){
        return AppDatabase.getInstance(DailyCost_New.this).daoAccess().insertANewRecord(record);
    }
}