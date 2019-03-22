package virgil.dailycost.adapter;

import android.content.Context;
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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import virgil.dailycost.R;
import virgil.dailycost.ROOM.AppDatabase;
import virgil.dailycost.ROOM.Category_Record;
import virgil.dailycost.ROOM.Record;
import virgil.dailycost.listeners.DailyListListener;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.EditTextViewHolder>{
    private List<Record> records;
    private Context mContext;
    private List<Category_Record> categories;


    @NonNull
    @Override
    public SearchResultAdapter.EditTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search_result, parent, false);

        return new SearchResultAdapter.EditTextViewHolder(itemView);
    }

    public SearchResultAdapter(List<Record> records, List<Category_Record> categories, Context mContext) {
        this.records = records;
        this.categories = categories;
        this.mContext = mContext;
    }

    class EditTextViewHolder extends RecyclerView.ViewHolder {

        private TextView dateView;
        private TextView descriptionView;
        private TextView costView;
        private TextView categoryView;

        EditTextViewHolder(View itemView) {
            super(itemView);

            dateView = (TextView) itemView.findViewById(R.id.row_search_result_DATE);
            descriptionView = (TextView) itemView.findViewById(R.id.row_search_result_DESCRIPTION);
            costView = (TextView) itemView.findViewById(R.id.row_search_result_COST);
            categoryView = (TextView) itemView.findViewById(R.id.row_search_result_CATEGORY);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchResultAdapter.EditTextViewHolder holder, final int position) {

        holder.dateView.setVisibility(View.GONE);
        holder.descriptionView.setVisibility(View.GONE);
        holder.costView.setVisibility(View.GONE);
        holder.categoryView.setVisibility(View.GONE);

        if(records.get(position).getYear() != null ||
                records.get(position).getMonth() != null ||
                records.get(position).getDay() != null){
            holder.dateView.setVisibility(View.VISIBLE);
            holder.dateView.setText(mContext.getResources().getString(R.string.DD_MM_YYYY,
                    records.get(position).getDay(),
                    records.get(position).getMonth(),
                    records.get(position).getYear()));
        }
        if(records.get(position).getCategory() != null){
            holder.categoryView.setVisibility(View.VISIBLE);
            holder.categoryView.setText(getCategoryNmaeByCategoryID(records.get(position).getCategory()));
        }

        if(records.get(position).getDescription() != null){
            holder.descriptionView.setVisibility(View.VISIBLE);
            holder.descriptionView.setText(records.get(position).getDescription());
        }

        if(records.get(position).getHKD() != null){
            holder.costView.setVisibility(View.VISIBLE);
            holder.costView.setText(records.get(position).getHKD().toString());
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    String getCategoryNmaeByCategoryID(Integer ID){
        for (Category_Record each : categories){
            if (ID == each.getId()) return each.getCategoryName();
        }
        return null;
    }

}
