package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class Result_User extends AppCompatActivity {
    private RecyclerView recyclerView;
    String uid;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_result_user);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_result_user);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(user_id,"0");
        Check_connectivity check = new Check_connectivity(Result_User.this);
        if(check.getInternetStatus())
        {
            DisplayTask d = new DisplayTask();
            d.execute(uid);
        }
        else{
            Toast.makeText(Result_User.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
        }


    }

    private class DisplayTask extends AsyncTask<String,Void,String> {

        ProgressDialog pd = new ProgressDialog(Result_User.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Loading Your Results");
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
                List<ResultUser_ListItem> data = new ArrayList<>();


                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    //[{"RESULT_ID":"22","name":"test","TEST_NAME":"test 1","TOPIC":"testing","TOTAL":"0","TOTAL_COR":"5","TOTAL_INCOR":"1","SCORE":"4"},

                    String result_id = jo.getString("RESULT_ID");
                    String auth_name = jo.getString("name");
                    String test_name = jo.getString("TEST_NAME");
                    String topic = jo.getString("TOPIC");
                    String total = jo.getString("TOTAL");
                    String total_cor = jo.getString("TOTAL_COR");
                    String total_incor = jo.getString("TOTAL_INCOR");
                    String score = jo.getString("SCORE");
                    //String testId, String testName, String testTopic, String testDuration, String testQBId, String testAvailability
                    ResultUser_ListItem qb = new ResultUser_ListItem(result_id,auth_name,test_name,topic,total,total_cor,total_incor,score);
                    data.add(qb);
                }
                ResultUser_adapter adapter = new ResultUser_adapter(data,Result_User.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Result_User.this));



            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(Result_User.this,"Unable to fetch Data",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Result_User.this,UserActivity.class));//new line
                finish();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(Result_User.this,"Unable to connect",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Result_User.this,UserActivity.class));//new line
                finish();
            }


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String uid = strings[0];

                String data = URLEncoder.encode("user_id", "UTF-8") + "=" +
                        URLEncoder.encode(uid, "UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/get_all_results.php?" + data);
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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Result_User.this,UserActivity.class));
        finish();
    }
}
