package virgil.dailycost.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import virgil.dailycost.R;
import virgil.dailycost.ROOM.AppDatabase;
import virgil.dailycost.ROOM.Category_Record;
import virgil.dailycost.ROOM.Record;
import virgil.dailycost.listeners.DailyListListener;
import virgil.dailycost.models.SubjectSpend;



/**
 * Created by VIRGILH on 4/30/2018.
 */

public class DailyCostEditorAdapter extends RecyclerView.Adapter<DailyCostEditorAdapter.EditTextViewHolder>{
    private List<Record> records;
    private List<Category_Record> categories;
    private Context mContext;

    private DailyListListener dailyListListener;

    public void setDailyListListener(DailyListListener dailyListListener) {
        this.dailyListListener = dailyListListener;
    }

    @NonNull
    @Override
    public EditTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_daily_cost, parent, false);

        return new EditTextViewHolder(itemView);
    }

    public DailyCostEditorAdapter(List<Record> records, List<Category_Record> categories, Context mContext) {
        this.records = records;
        this.categories = categories;
        this.mContext = mContext;
    }

    class EditTextViewHolder extends RecyclerView.ViewHolder {
        private Record record;

        private EditText editHKD;
        private EditText editDescription;
        private Spinner spinnerCategory;
        private ImageButton imageButtonDeleteCostRecord;

        EditTextViewHolder(View itemView) {
            super(itemView);

            editHKD = (EditText) itemView.findViewById(R.id.daily_cost_edittext_HKD);
            editDescription = (EditText) itemView.findViewById(R.id.daily_cost_edittext_DESCRIPTION);
            spinnerCategory = (Spinner) itemView.findViewById(R.id.daily_cost_spinner_CATELOGY);
            imageButtonDeleteCostRecord = (ImageButton) itemView.findViewById(R.id.imageButton_deleteCostRecord);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final EditTextViewHolder holder, final int position) {
        holder.record = records.get(position);

        if(holder.record.getDescription() != null) {
            holder.editDescription.setText(holder.record.getDescription());
        }
        if(holder.record.getHKD() !=null) {
            holder.editHKD.setText(String.valueOf(holder.record.getHKD()));
        }

        holder.editHKD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    try {
                        if (!holder.editHKD.getText().toString().equals("")) {
                            holder.record.setHKD(Float.valueOf(holder.editHKD.getText().toString()));
                        } else {
                            holder.record.setHKD(null);
                        }
                    } catch(NumberFormatException e){
                        e.printStackTrace();
                    }
            }
        });

        holder.editDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                holder.record.setDescription(holder.editDescription.getText().toString());
            }
        });

        Category_Record categoryWithBiggerTimeIndex;
        List<Category_Record> draftSortedCategories = new ArrayList<>();
        while (draftSortedCategories.size() != categories.size()) {
            categoryWithBiggerTimeIndex = new Category_Record(null,null,0);
            for (Category_Record eachCategoryRecord : categories) {
                if (categoryWithBiggerTimeIndex.getLastUsedTimeIndex() <= eachCategoryRecord.getLastUsedTimeIndex()
                        && !draftSortedCategories.contains(eachCategoryRecord))
                    categoryWithBiggerTimeIndex = eachCategoryRecord;
            }
            draftSortedCategories.add(categoryWithBiggerTimeIndex);
        }

        final List<Category_Record> sortedCategories = draftSortedCategories;
        List<String> categoryNames = new ArrayList<>();
        for (Category_Record eachCategoryRecord : sortedCategories) categoryNames.add(eachCategoryRecord.getCategoryName());

        final SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, categoryNames);
        holder.spinnerCategory.setAdapter(spinnerAdapter);
        if(records.get(position).getCategory() != null) holder.spinnerCategory.setSelection(getSelectedPositionInListSortedCategories(records, position, sortedCategories));
        holder.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int spinnerPosition, long id) {
                records.get(position).setCategory(sortedCategories.get(spinnerPosition).getId());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(mContext).daoAccess().updateLastUsedIndexByID(sortedCategories.get(spinnerPosition).getId(), System.currentTimeMillis());
                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.imageButtonDeleteCostRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyListListener.itemDeleteListener(records.get(position));
            }
        });

    }



    @Override
    public int getItemCount() {
        return records.size();
    }

    Integer getSelectedPositionInListSortedCategories(List<Record> record, Integer position, List<Category_Record> sortedCategorie){
        Integer selectedPositionInListSortedCategories = 0;
            for (Category_Record each_categoryRecord : sortedCategorie){
                if(record.get(position).getCategory() == each_categoryRecord.getId())
                    selectedPositionInListSortedCategories = sortedCategorie.indexOf(each_categoryRecord);
            }
        return selectedPositionInListSortedCategories;
    }


}
