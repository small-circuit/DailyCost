package virgil.dailycost.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import virgil.dailycost.R;
import virgil.dailycost.models.MonthDay;

import static virgil.dailycost.page.DayList.YEAR;


/**
 * Created by VIRGILH on 4/27/2018.
 */

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.MyViewHolder>{

    private List<MonthDay> DayList;
    private String monthEng;
    private Integer year;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dayNMonth;
        public TextView dayNMonth_cost;



        public MyViewHolder(View view) {
            super(view);
            dayNMonth = (TextView) view.findViewById(R.id.DDDMM);
            dayNMonth_cost = (TextView) view.findViewById(R.id.DDDMM_cost);
        }
    }

    public DayAdapter(List<MonthDay> DayList, Integer year) {
        this.DayList = DayList;
        this.year = year;
    }


    @Override
    public DayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_day_list, parent, false);

        return new DayAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DayAdapter.MyViewHolder holder, int position) {
        MonthDay monthDay = DayList.get(position);
        monthConverter(monthDay.getMonth());

        String s = monthDay.getDay() + " " + monthEng;
        holder.dayNMonth.setText(s);

        if(monthDay.getCost() !=null){
            String r = "$" + monthDay.getCost().toString();
            holder.dayNMonth_cost.setText(r);
        } else if(monthDay.getCost() == null){
            holder.dayNMonth_cost.setText("$0");
        }

        holder.dayNMonth.setBackgroundResource(R.color.COLORLESS);

        try {
            if (weekDayDetermine(monthDay.getDay(),monthDay.getMonth(),year) == "RED"){
                holder.dayNMonth.setBackgroundResource(R.color.RED);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return DayList.size();
    }

    private String weekDayDetermine(int day, int month, int year) throws ParseException {

        String input_date = String.format("%02d",day) + "/" + String.format("%02d",month) + "/" + String.format("%04d",year);
        SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
        Date dt1 = format1.parse(input_date);
        DateFormat format2=new SimpleDateFormat("EEEE");
        String finalDay=format2.format(dt1);

        if (finalDay.equals("Sunday") || finalDay.equals("Saturday") || finalDay.equals("星期日") || finalDay.equals("星期六") ){

            return "RED";
        }

        return "BLACK";

    }

    private void monthConverter(int month){

        switch (month){

            default:
                monthEng = "No Data";
                break;

            case 1:
                monthEng = "January";
                break;

            case 2:
                monthEng = "February";
                break;

            case 3:
                monthEng = "March";
                break;

            case 4:
                monthEng = "April";
                break;

            case 5:
                monthEng = "May";
                break;

            case 6:
                monthEng = "June";
                break;

            case 7:
                monthEng = "July";
                break;

            case 8:
                monthEng = "August";
                break;

            case 9:
                monthEng = "September";
                break;

            case 10:
                monthEng = "October";
                break;

            case 11:
                monthEng = "November";
                break;

            case 12:
                monthEng = "December";
                break;

        }

    }

}
