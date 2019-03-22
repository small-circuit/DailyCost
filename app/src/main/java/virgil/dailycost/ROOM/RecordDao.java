package virgil.dailycost.ROOM;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Maybe;
@Dao
public interface RecordDao {

    @Query("SELECT * FROM `records`")
    Maybe<List<Record>> fetchAllRecord();

    @Query("SELECT * FROM `records` WHERE `record_id` = :id")
    Maybe<Record> fetchOneRecordById (Integer id);

    // for DailyCost_New
    @Query("UPDATE `records` SET `year` = :year, `month` = :month, `day` = :day, `description` = :description, `category` = :catagory, `HKD` = :HKD, `type` = :type, `photos` = :photoString, `stringSpare` = :stringSpare, `integerSpare` = :integerSpare  WHERE `record_id` = :id")
    void updateOneRecordById (Integer id, Integer year, Integer month, Integer day, String description, Integer catagory, Float HKD, String type, String photoString, String stringSpare, Integer integerSpare);

    @Query("SELECT * FROM `records` WHERE `year` = :year AND `month` = :month AND `day` = :day AND `type` = :type")
    Maybe<List<Record>> fetchRecordsOnOneDay(Integer year, Integer month, Integer day, String type);

    // for MonthList & DayList
    @Insert
    Long insertANewRecord(Record record);  // Set this method as Long will return id.

    // for MonthList
    @Query("SELECT `year`, `month`, sum(`HKD`) AS `HKD` FROM `records` GROUP BY `year`, `month` ORDER BY `year` DESC, `month` DESC")
    Maybe<List<Record>> getMonthList();

    @Query("DELETE FROM `records` WHERE `month` = :month AND `year` = :year")
    void deleteOneMonthRecord(Integer year, Integer month);

    @Query("DELETE FROM `records` WHERE `record_id` = :record_id")
    void deleteOneRecordByID(Integer record_id);

    // for DayList
    @Query("SELECT `day`, sum(`HKD`) AS `HKD` FROM `records` WHERE `year` = :year AND `month` = :month GROUP BY `day` ORDER BY `day` DESC")
    Maybe<List<Record>> getDayList(Integer year, Integer month);

    @Query("DELETE FROM `records` WHERE `year` = :year AND `month` = :month AND `day` = :day")
    void deleteOneDayRecord(Integer year, Integer month, Integer day);

    //for Category
    @Insert
    void insertNewCategory(Category_Record category_record);

    @Query("SELECT * FROM `categories`")
    Maybe<List<Category_Record>> getAllCategories();

    @Query("DELETE FROM `categories` WHERE `category_id` = :id")
    void deleteCategoryByID(Integer id);

    @Query("UPDATE  `categories` SET `category_name` = :newName WHERE `category_id` = :categoryID")
    void updateCategoryNameByID(Integer categoryID, String newName);

    @Query("UPDATE `categories` SET `lastUsedTimeIndex` = :lastUsedTimeIndex WHERE `category_id` = :categoryID")
    void updateLastUsedIndexByID(Integer categoryID, long lastUsedTimeIndex);

    //for Search Activity
    @Query ("SELECT * FROM `records` WHERE `description` LIKE :keyword AND `category` = :keyword_ID")
    Maybe<List<Record>> fetchCostRecord (String keyword, Integer keyword_ID);

    @Query ("SELECT * FROM `records` WHERE `description` LIKE :keyword ")
    Maybe<List<Record>> fetchCostRecord (String keyword);

    @Query ("SELECT * FROM `records` WHERE `category` = :keyword_ID")
    Maybe<List<Record>> fetchCostRecord (Integer keyword_ID);

}
