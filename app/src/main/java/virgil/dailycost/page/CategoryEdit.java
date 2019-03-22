package virgil.dailycost.page;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import virgil.dailycost.R;
import virgil.dailycost.ROOM.AppDatabase;
import virgil.dailycost.ROOM.Category_Record;
import virgil.dailycost.adapter.CategoryEditAdapter;
import virgil.dailycost.listeners.CategoryItemListener;

public class CategoryEdit extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CategoryItemListener categoryItemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        Button button_addCategoryItem = (Button) findViewById(R.id.button_addCategoryItem);
        button_addCategoryItem.setText("ADD A CATEGORY");
        button_addCategoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflatAlertDialog_ADD();
            }
        });

        initialize();

    }

    void initialize(){

        compositeDisposable.add(
                AppDatabase.getInstance(this).daoAccess().getAllCategories()
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category_Record>>() {
                    @Override
                    public void accept(List<Category_Record> category_records) throws Exception {
                        CategoryEditAdapter mAdapter = new CategoryEditAdapter(category_records);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_category_item);
                        recyclerView.setNestedScrollingEnabled(false);
                        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);

                        mAdapter.setCategoryItemRemoveListener(new CategoryItemListener() {
                            @Override
                            public void categoryItemRenameListener(Category_Record record) {
                                inflatAlertDialog_RENAME(record);
                            }
                            @Override
                            public void categoryItemRemoveListener(final Category_Record record) {
                                inflatAlertDialog_DELETE(record);
                            }
                        });
                    }
                })
        );
    }

    void inflatAlertDialog_ADD() {
        AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(CategoryEdit.this);
        View view = LayoutInflater.from(CategoryEdit.this).inflate(R.layout.dialog_insert_categoryitem, null);
        alertDialog_Builder.setView(view);
        final EditText editText_ENTER_NEW_CATEGORY_ITEM = (EditText) view.findViewById(R.id.editText_enterNewCategoryItemName);
        Button button_OK = (Button) view.findViewById(R.id.insert_categoryitem_button_OK);
        Button button_CANCEL = (Button) view.findViewById(R.id.insert_categoryitem_button_CANCEL);

        final AlertDialog alertDialog = alertDialog_Builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.show();

        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_ENTER_NEW_CATEGORY_ITEM.getText() != null) {
                    if (editText_ENTER_NEW_CATEGORY_ITEM.getText().length() > 0) {
                        final Category_Record categoryRecord = new Category_Record(null, editText_ENTER_NEW_CATEGORY_ITEM.getText().toString(), 0);
                        compositeDisposable.add(
                                Completable.fromAction(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        AppDatabase.getInstance(CategoryEdit.this).daoAccess().insertNewCategory(categoryRecord);
                                    }
                                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        initialize();
                                    }
                                })
                        );
                        alertDialog.dismiss();
                    } else {
                        alertDialog.dismiss();
                        Toast.makeText(CategoryEdit.this, "no category is created", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        button_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    void inflatAlertDialog_RENAME(final Category_Record record) {
        AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(CategoryEdit.this);
        View view = LayoutInflater.from(CategoryEdit.this).inflate(R.layout.dialog_insert_categoryitem, null);
        alertDialog_Builder.setView(view);
        final EditText editText_ENTER_NEW_CATEGORY_ITEM = (EditText) view.findViewById(R.id.editText_enterNewCategoryItemName);
        editText_ENTER_NEW_CATEGORY_ITEM.setText(record.getCategoryName());
        Button button_OK = (Button) view.findViewById(R.id.insert_categoryitem_button_OK);
        Button button_CANCEL = (Button) view.findViewById(R.id.insert_categoryitem_button_CANCEL);

        final AlertDialog alertDialog = alertDialog_Builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.show();

        button_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_ENTER_NEW_CATEGORY_ITEM.getText() != null) {
                    if (editText_ENTER_NEW_CATEGORY_ITEM.getText().length() > 0) {
                        compositeDisposable.add(
                                Completable.fromAction(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        AppDatabase.getInstance(CategoryEdit.this).daoAccess().updateCategoryNameByID(record.getId(), editText_ENTER_NEW_CATEGORY_ITEM.getText().toString());
                                    }
                                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        initialize();
                                    }
                                })
                        );
                        alertDialog.dismiss();
                    } else {
                        alertDialog.dismiss();
                        Toast.makeText(CategoryEdit.this, "New Name cannot be empty", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        button_CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    void inflatAlertDialog_DELETE(final Category_Record record) {

        AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(CategoryEdit.this);
        alertDialog_Builder.setMessage("delete Catagory " + record.getCategoryName() + "?");

        alertDialog_Builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                compositeDisposable.add(
                        Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                AppDatabase.getInstance(CategoryEdit.this).daoAccess().deleteCategoryByID(record.getId());
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                initialize();
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


}


