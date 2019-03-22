package virgil.dailycost.ROOM;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "records")
public class Record {

    final public static String COST = "COST";
    final public static String REMARK = "REMARK";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "record_id")
    private Integer id;

    @ColumnInfo(name = "year")
    private Integer year;

    @ColumnInfo(name = "month")
    private Integer month;

    @ColumnInfo(name = "day")
    private Integer day;

    @ColumnInfo(name = "description")
    @Nullable
    private String description;

    @ColumnInfo(name = "category")
    @Nullable
    private Integer category;

    @ColumnInfo(name = "HKD")
    @Nullable
    private Float HKD;

    @ColumnInfo(name = "type")
    @Nullable
    private String type;

    @ColumnInfo(name = "photos")
    @Nullable
    private String photoString;

    @ColumnInfo(name = "stringSpare")
    @Nullable
    private String stringSpare;

    @ColumnInfo(name = "integerSpare")
    @Nullable
    private Integer integerSpare;

    @Ignore
    public Record(){}

    public Record(Integer id, Integer year, Integer month, Integer day, String description, Integer category, Float HKD, String type, String photoString, String stringSpare, Integer integerSpare) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.description = description;
        this.category = category;
        this.HKD = HKD;
        this.type = type;
        this.photoString = photoString;
        this.stringSpare = stringSpare;
        this.integerSpare = integerSpare;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public Integer getCategory() {
        return category;
    }

    public void setCategory(@Nullable Integer category) {
        this.category = category;
    }

    @Nullable
    public Float getHKD() {
        return HKD;
    }

    public void setHKD(@Nullable Float HKD) {
        this.HKD = HKD;
    }

    @Nullable
    public String getType() {
        return type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    @Nullable
    public String getPhotoString() {
        return photoString;
    }

    public void setPhotoString(@Nullable String photoString) {
        this.photoString = photoString;
    }

    @Nullable
    public String getStringSpare() {
        return stringSpare;
    }

    public void setStringSpare(@Nullable String stringSpare) {
        this.stringSpare = stringSpare;
    }

    @Nullable
    public Integer getIntegerSpare() {
        return integerSpare;
    }

    public void setIntegerSpare(@Nullable Integer integerSpare) {
        this.integerSpare = integerSpare;
    }
}
