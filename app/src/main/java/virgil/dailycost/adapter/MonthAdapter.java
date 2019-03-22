package virgil.dailycost.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import virgil.dailycost.R;
import virgil.dailycost.models.MonthYear;



/**
 * Created by VIRGILH on 4/25/2018.
 */

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MyViewHolder>{

    private List<MonthYear> MonthList;
    private String monthEng;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView monthNYear;
        public TextView monthNYear_cost;



        public MyViewHolder(View view) {
            super(view);
            monthNYear = (TextView) view.findViewById(R.id.MMMYY);
            monthNYear_cost = (TextView) view.findViewById(R.id.MMMYY_cost);

        }
    }

    public MonthAdapter(List<MonthYear> MonthList) {
        this.MonthList = MonthList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_month_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MonthYear monthYear = MonthList.get(position);
        monthConverter(monthYear.getMonth());

        holder.monthNYear.setText(monthEng + " " + monthYear.getYear());
        String i;
        if(MonthList.get(position).getCost() == null) i = "0.00";
        else i = new DecimalFormat("0.00").format(MonthList.get(position).getCost());
        holder.monthNYear_cost.setText("$" + i);


    }

    @Override
    public int getItemCount() {
        return MonthList.size();
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
