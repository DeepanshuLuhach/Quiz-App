package com.deepanshu.quiz;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepanshu on 27/7/17.
 */

class ResultUser_adapter extends RecyclerView.Adapter<ResultUser_adapter.ViewHolder> {
    private List<ResultUser_ListItem> dt_listItems;
    Context context;
    ResultUser_adapter(List<ResultUser_ListItem> listItems, Context context) {
        this.dt_listItems = listItems;
        this.context = context;
    }

    @Override
    public ResultUser_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_result_user_listitem,parent,false);

        return new ResultUser_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ResultUser_adapter.ViewHolder holder, int position) {
        ResultUser_ListItem listItem = dt_listItems.get(position);

        final String id = listItem.getResultId();
        String pass;
        pass = "TestName : "+listItem.getTestname();
        holder.Testname.setText(pass);
        pass = "Authname : "+listItem.getName();
        holder.mAuthname.setText(pass);
        pass = "Topic : "+listItem.getTopic();
        holder.Topicname.setText(pass);
        pass = "Score : "+listItem.getScore();
        holder.mScore.setText(pass);
        holder.pieChart.setUsePercentValues(true);

        ArrayList<Entry> yvalues = new ArrayList<>();
        int x = Integer.parseInt(listItem.getTotalCor());
        int y = Integer.parseInt(listItem.getTotalIncor());
        int z = Integer.parseInt(listItem.getTotal())-Integer.parseInt(listItem.getTotalCor())-Integer.parseInt(listItem.getTotalIncor());
        if(x>0)
            yvalues.add(new Entry(x, 0));//correct
        if(y>0)
            yvalues.add(new Entry(x, 1));//incorrect
        if(z>0)
            yvalues.add(new Entry(x, 2));//not attempted

        PieDataSet dataSet;
        dataSet = new PieDataSet(yvalues, "");
        ArrayList<String> xvalues = new ArrayList<>();
        if(x>0)
            xvalues.add("Correct");
        if(y>0)
            xvalues.add("InCorrect");
        if(z>0)
            xvalues.add("Not Attempted");

        PieData data = new PieData(xvalues, dataSet);
        data.setValueFormatter(new PercentFormatter());
        holder.pieChart.setData(data);
        holder.pieChart.setDescription("Test Result");

        holder.pieChart.setDrawHoleEnabled(true);
        holder.pieChart.setTransparentCircleRadius(50f);
        holder.pieChart.setHoleRadius(50f);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.BLACK);
        holder.pieChart.animateXY(1400, 1400);



    }

    @Override
    public int getItemCount() {
        return dt_listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView Testname;
        TextView Topicname;
        TextView mAuthname;
        TextView mScore;
        PieChart pieChart;


        ViewHolder(View itemView) {
            super(itemView);

            Testname = itemView.findViewById(R.id.tv_test_name_result);
            Topicname = itemView.findViewById(R.id.topic_name_result);
            mAuthname = itemView.findViewById(R.id.auth_name_result);
            mScore =  itemView.findViewById(R.id.score_result);
            pieChart = itemView.findViewById(R.id.piechart2);

        }
    }

}
