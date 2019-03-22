package virgil.dailycost.models;

/**
 * Created by VIRGILH on 4/25/2018.
 */

public class MonthYear{

    private Integer month;
    private Integer year;
    private Float cost;

    public MonthYear(Integer month, Integer year, Float cost) {
        this.month = month;
        this.year = year;
        this.cost = cost;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
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
}
