package virgil.dailycost.helper;

import java.util.ArrayList;

import virgil.dailycost.models.MonthDay;
import virgil.dailycost.models.MonthYear;

/**
 * Created by VIRGILH on 4/30/2018.
 */

public class ListSortting {

    public static ArrayList<MonthDay> sortting_Day(final ArrayList<MonthDay> number) {

        ArrayList<MonthDay> sortedList = new ArrayList<>();

        Integer index = 0;
        while (number.size() > 0) {
            Integer bigger = 0;
            int i = 0;
            for (MonthDay each : number) {
                if (each.getDay() >= bigger || number.size() == 1) {
                    bigger = each.getDay();
                    index = i;
                }
                i++;
            }
            sortedList.add(number.get(index));
            number.remove(number.get(index));
        }

        return sortedList;
    }

    public static ArrayList<MonthYear> sortting_Month(final ArrayList<MonthYear> number) {

        ArrayList<MonthYear> sortedList = new ArrayList<>();

        Integer index = 0;
        while(number.size() > 0) {
            Integer bigger = 0;
            int i = 0;
            for (MonthYear each : number) {
                if (each.getYear()*100 + each.getMonth() >= bigger || number.size() == 1) {
                    bigger = each.getYear()*100 + each.getMonth();
                    index = i;
                }
                i++;
            }
            sortedList.add(number.get(index));
            number.remove(number.get(index));
        }

        return sortedList;
    }

}
