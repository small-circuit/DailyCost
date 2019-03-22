package virgil.dailycost.models;

import android.content.Intent;

/**
 * Created by VIRGILH on 4/26/2018.
 */

public class MonthDay {

    private Integer month;
    private Integer day;
    private Float cost;

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

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public MonthDay(Integer month, Integer day, Float cost) {

        this.month = month;
        this.day = day;
        this.cost = cost;
    }
}
