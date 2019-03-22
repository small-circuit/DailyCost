package virgil.dailycost.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import virgil.dailycost.R;
import virgil.dailycost.ROOM.Category_Record;
import virgil.dailycost.listeners.CategoryItemListener;

public class CategoryEditAdapter extends RecyclerView.Adapter<CategoryEditAdapter.MyViewHolder> {

    private List<Category_Record> categoryRecords;

    private CategoryItemListener categoryItemListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryItem;
        public ImageButton renameCategoryItem;
        public ImageButton removeCategoryItem;

        public MyViewHolder(View view) {
            super(view);
            categoryItem = (TextView) view.findViewById(R.id.textView_categoryItem);
            renameCategoryItem = (ImageButton) view.findViewById(R.id.imageButton_categoryItemRename);
            removeCategoryItem = (ImageButton) view.findViewById(R.id.imageButton_categoryItemRemove);
        }
    }

    public CategoryEditAdapter(List<Category_Record> categoryRecords) {
        this.categoryRecords = categoryRecords;
    }

    public void setCategoryItemRemoveListener(CategoryItemListener categoryItemListener) {
        this.categoryItemListener = categoryItemListener;
    }

    public void setCategoryItemRenameListener(CategoryItemListener categoryItemListener){
        this.categoryItemListener = categoryItemListener;
    }

    @Override
    public CategoryEditAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category_edit, parent, false);

        return new CategoryEditAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryEditAdapter.MyViewHolder holder, final int position) {
        holder.categoryItem.setText(categoryRecords.get(position).getCategoryName());
        holder.renameCategoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryItemListener.categoryItemRenameListener(categoryRecords.get(position));
            }
        });
        holder.removeCategoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryItemListener.categoryItemRemoveListener(categoryRecords.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryRecords.size();
    }


}
