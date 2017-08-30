package com.deepanshu.quiz;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class TestResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        Bundle bundle = getIntent().getExtras();
        int total = Integer.parseInt(bundle.getString("Total"));
        int correct = Integer.parseInt(bundle.getString("Correct"));
        int incorrect = Integer.parseInt(bundle.getString("Incorrect"));
        int score = Integer.parseInt(bundle.getString("Score"));
        double percent = Double.parseDouble(bundle.getString("Percentage"));

        TextView scored = (TextView) findViewById(R.id.score_testresult);
        scored.setText("You scored "+score+" marks.\n Percentage : " + percent +" %") ;

        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        Button button = (Button) findViewById(R.id.gotouser);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TestResult.this,UserActivity.class);
                startActivity(i);
                finish();
            }
        });
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(50f);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        if(correct>0)
            yvalues.add(new Entry(correct,0));//correct
        if(incorrect>0)
            yvalues.add(new Entry(incorrect, 1));//incorrect
        if((total-correct-incorrect)>0)
            yvalues.add(new Entry(total-correct-incorrect,2));//not attempted

        PieDataSet dataSet;
        dataSet = new PieDataSet(yvalues, "");
        ArrayList<String> xvalues = new ArrayList<>();
        xvalues.add("Correct");
        xvalues.add("InCorrect");
        xvalues.add("Not Attempted");

        PieData data = new PieData(xvalues, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(13f);
        pieChart.setDescription("Test Result");
        data.setValueTextColor(Color.BLACK);
        pieChart.animateXY(1400, 1400);

    }

}
