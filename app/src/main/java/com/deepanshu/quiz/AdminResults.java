package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.deepanshu.quiz.Login.MyPREFERENCES;

public class AdminResults extends AppCompatActivity {

    RecyclerView recyclerView;
    String tid;
    TextView mtestname, mtopic;
    BarChart barChart;
    public int x1, x2, x3, x4, x5, x6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_results);
        Bundle bundle = getIntent().getExtras();
        tid = bundle.getString("TestId");
        x1 = x2 = x3 = x4 = x5 = x6 = 0;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_result_admin);
        mtestname = (TextView) findViewById(R.id.tv_test_name_admin_result);
        mtopic = (TextView) findViewById(R.id.tv_topic_admin_result);
        barChart = (BarChart) findViewById(R.id.percentage_chart);
        Check_connectivity check = new Check_connectivity(AdminResults.this);
        if(check.getInternetStatus())
        {
            FetchTask f = new FetchTask();
            f.execute(tid);

        }
        else{
            Toast.makeText(AdminResults.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
        }

    }



    private class FetchTask extends AsyncTask<String,Void,String> {

        ProgressDialog pd = new ProgressDialog(AdminResults.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Getting Test Results");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            pd.dismiss();

            try {
                JSONArray ja = new JSONArray(jsonData);
                JSONObject jo;
                List<AdminResults_ListItem> data = new ArrayList<>();

                int count = ja.length();
                if(count == 0){
                    Toast.makeText(AdminResults.this,"No Result found!!!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminResults.this,DisplayTest.class));
                    finish();
                }

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    //{"RESULT_ID":"36","name":"ajay","TEST_NAME":"OS-TEST-1","TOPIC":"Deadlock","TOTAL":"5","TOTAL_COR":"3","TOTAL_INCOR":"1","SCORE":"11","PERCENTAGE":"0"},
                    if(i == 0)
                    {
                        String tname = jo.getString("TEST_NAME");
                        mtestname.setText(tname);
                        String topic = jo.getString("TOPIC");
                        mtopic.setText(topic);
                    }
                    String name = jo.getString("name");
                    String resultId = jo.getString("RESULT_ID");
                    String total = jo.getString("TOTAL");
                    String correct = jo.getString("TOTAL_COR");
                    String incorrect = jo.getString("TOTAL_INCOR");
                    String score = jo.getString("SCORE");
                    String percentage = jo.getString("PERCENTAGE");
                    int p = (int) Double.parseDouble(percentage);
                    System.out.println(p);
                    if(p >= 90)
                        x6++;
                    else if(p >= 80)
                        x5++;
                    else if(p >= 60)
                        x4++;
                    else if(p >= 40)
                        x3++;
                    else if(p >= 20)
                        x2++;
                    else if(p >= 0)
                        x1++;
                    System.out.println(x1+ " "+x2+ " "+x3+ " "+x4+ " "+x5+ " "+x6);

                    //String testId, String testName, String testTopic, String testDuration, String testQBId, String testAvailability
                    AdminResults_ListItem qb = new AdminResults_ListItem(resultId, name, total, correct, incorrect, score, percentage);
                    data.add(qb);
                }

                ArrayList<BarEntry>  yvalues = new ArrayList<>();
                yvalues.add(new BarEntry(x1,0));
                yvalues.add(new BarEntry(x2,1));
                yvalues.add(new BarEntry(x3,2));
                yvalues.add(new BarEntry(x4,3));
                yvalues.add(new BarEntry(x5,4));
                yvalues.add(new BarEntry(x6,5));
                BarDataSet dataSet = new BarDataSet(yvalues,"Percentage Marks");

                ArrayList<String> xvalues = new ArrayList<>();
                xvalues.add("<20");
                xvalues.add("<40");
                xvalues.add("<60");
                xvalues.add("<80");
                xvalues.add("<90");
                xvalues.add("<=100");
                BarData bardata = new BarData(xvalues, dataSet);
                barChart.setData(bardata);
                barChart.setDescription("Student's Performance");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);


                AdminResults_adapter adapter = new AdminResults_adapter(data,AdminResults.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminResults.this));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(AdminResults.this,"Unable to fetch Data",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminResults.this,DisplayTest.class));
                finish();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(AdminResults.this,"Unable to connect",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminResults.this,DisplayTest.class));
                finish();
            }


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String tid = strings[0];

                String data = URLEncoder.encode("test_id", "UTF-8") + "=" +
                        URLEncoder.encode(tid, "UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php//get_admin_results.php?" + data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                StringBuilder jsonData = new StringBuilder();

                //READ
                while ((line = bufferedReader.readLine()) != null) {
                    jsonData.append(line).append("\n");
                }

                return jsonData.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
