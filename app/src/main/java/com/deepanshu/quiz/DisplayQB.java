package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class DisplayQB extends AppCompatActivity{

    private RecyclerView recyclerView;
    String uid;
    SharedPreferences sharedPreferences;
    int count = 0;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qb);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_qb);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString(user_id,"0");
        Check_connectivity check = new Check_connectivity(DisplayQB.this);
        if(check.getInternetStatus())
        {
            DisplayTask d = new DisplayTask();
            d.execute(uid);
        }
        else{
            Toast.makeText(DisplayQB.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
        }


    }

    private class DisplayTask extends AsyncTask<String,Void,String>{

        ProgressDialog pd = new ProgressDialog(DisplayQB.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Loading Question Bank set");
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
                List<ListItem> data = new ArrayList<>();
                count = ja.length();
                if(count == 0){
                    Toast.makeText(DisplayQB.this,"No Question Banks Available!!!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DisplayQB.this,MainActivity.class));
                    finish();
                }

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    String name = jo.getString("QB_NAME");
                    String id = jo.getString("QB_ID");
                    ListItem qb = new ListItem(id,name);
                    data.add(qb);
                }
                MyAddapter addapter = new MyAddapter(data,DisplayQB.this);
                recyclerView.setAdapter(addapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(DisplayQB.this));

            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(DisplayQB.this,"Unable to fetch Data",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DisplayQB.this,MainActivity.class));//new line
                finish();
            }


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String uid = strings[0];

                String data = URLEncoder.encode("user_id", "UTF-8") + "=" +
                        URLEncoder.encode(uid, "UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/view_qb.php?" + data);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DisplayQB.this,MainActivity.class));//new line
        finish();
    }
}