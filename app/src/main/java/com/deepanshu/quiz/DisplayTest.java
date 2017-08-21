package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
import static com.deepanshu.quiz.Login.user_id;

public class DisplayTest extends AppCompatActivity {
    private RecyclerView recyclerView;
    String uid;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_test);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_test);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(user_id,"0");
        Check_connectivity check = new Check_connectivity(DisplayTest.this);
        if(check.getInternetStatus())
        {
            DisplayTask d = new DisplayTask();
            d.execute(uid);
        }
        else{
            Toast.makeText(DisplayTest.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
        }


    }

    private class DisplayTask extends AsyncTask<String,Void,String> {

        ProgressDialog pd = new ProgressDialog(DisplayTest.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            pd.dismiss();

            try {
                JSONArray ja = new JSONArray(jsonData);
                JSONObject jo;
                List<DisplayTest_ListItem> data = new ArrayList<>();


                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    //{"TEST_ID":"2","QB_ID":"2","USER_ID":"1","ONLINE":"1","TEST_NAME":"dbms2","TOPIC":"sql","DURATION":"2"},

                    String test_id = jo.getString("TEST_ID");
                    String qb_id = jo.getString("QB_ID");
                    String status = jo.getString("ONLINE");
                    String tname = jo.getString("TEST_NAME");
                    String topic = jo.getString("TOPIC");
                    String duration = jo.getString("DURATION");
                    //String testId, String testName, String testTopic, String testDuration, String testQBId, String testAvailability
                    DisplayTest_ListItem qb = new DisplayTest_ListItem(test_id,tname,topic,duration,qb_id,status);
                    data.add(qb);
                }
                DisplayTest_adapter adapter = new DisplayTest_adapter(data,DisplayTest.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(DisplayTest.this));



            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(DisplayTest.this,"Unable to fetch Data",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(DisplayTest.this,"Unable to connect",Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String uid = strings[0];

                String data = URLEncoder.encode("user_id", "UTF-8") + "=" +
                        URLEncoder.encode(uid, "UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/view_test.php?" + data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                StringBuffer jsonData = new StringBuffer();

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
