package virgil.dailycost.ROOM;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity(tableName = "categories")
public class Category_Record {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    private Integer id;

    @ColumnInfo(name = "category_name")
    private String categoryName;

    @ColumnInfo(name = "lastUsedTimeIndex")
    private long lastUsedTimeIndex;

    @Ignore
    public Category_Record() {
    }

    public Category_Record(Integer id, String categoryName, long lastUsedTimeIndex) {
        this.id = id;
        this.categoryName = categoryName;
        this.lastUsedTimeIndex = lastUsedTimeIndex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getLastUsedTimeIndex() {
        return lastUsedTimeIndex;
    }

    public void setLastUsedTimeIndex(long lastUsedTimeIndex) {
        this.lastUsedTimeIndex = lastUsedTimeIndex;
    }
}
