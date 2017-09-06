package com.deepanshu.quiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

class AdminResults_adapter extends RecyclerView.Adapter<AdminResults_adapter.ViewHolder> {
    private List<AdminResults_ListItem> dt_listItems;
    Context context;

    AdminResults_adapter(List<AdminResults_ListItem> listItems, Context context) {
        this.dt_listItems = listItems;
        this.context = context;
    }

    @Override
    public AdminResults_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_result_list_item,parent,false);
        return new AdminResults_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AdminResults_adapter.ViewHolder holder, int position) {
        AdminResults_ListItem listItem = dt_listItems.get(position);
        String pass;
        pass = "Name : "+listItem.getName();
        holder.mname.setText(pass);
        pass = "Score : " + listItem.getScore();
        holder.mscore.setText(pass);
        pass = "Percentage : " + listItem.getPercentage();
        holder.mpercentage.setText(pass);
        int correct = Integer.parseInt(listItem.getCorrect());
        int incorrect = Integer.parseInt(listItem.getCorrect());
        int notAtttempted = Integer.parseInt(listItem.getTotal())-Integer.parseInt(listItem.getCorrect())-Integer.parseInt(listItem.getIncorect());

        ArrayList<BarEntry> yvalue= new ArrayList<>();
        yvalue.add(new BarEntry(correct, 0));
        yvalue.add(new BarEntry(incorrect, 1));
        yvalue.add(new BarEntry(notAtttempted, 2));

        BarDataSet dataSet = new BarDataSet(yvalue, " Number of Question");
        ArrayList<String> xvalues = new ArrayList<>();
        xvalues.add(0,"Correct");
        xvalues.add(1,"InCorrect");
        xvalues.add(2,"Not Attempted");

        BarData data = new BarData(xvalues, dataSet);
        holder.barChart.setData(data);
        holder.barChart.setDescription("");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);


    }

    @Override
    public int getItemCount() {
        return dt_listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mname, mscore, mpercentage;
        HorizontalBarChart barChart;
        ViewHolder(View itemView) {
            super(itemView);

            mname = itemView.findViewById(R.id.tv_name);
            mscore = itemView.findViewById(R.id.tv_score_admin);
            mpercentage = itemView.findViewById(R.id.tv_percentage_admin);
            barChart = itemView.findViewById(R.id.horizontal_chart_student);

        }
    }

}
