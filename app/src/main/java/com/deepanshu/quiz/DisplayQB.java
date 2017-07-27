package com.deepanshu.quiz;

import android.app.ProgressDialog;
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

public class DisplayQB extends AppCompatActivity{

    private RecyclerView recyclerView;
    String uid;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qb);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_qb);
        uid = "1";
        DisplayTask d = new DisplayTask();
        d.execute(uid);

    }

    private class DisplayTask extends AsyncTask<String,Void,String>{

        ProgressDialog pd = new ProgressDialog(DisplayQB.this);

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
                List<ListItem> data = new ArrayList<>();


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



            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(DisplayQB.this,"Unable to fetch Data",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(DisplayQB.this,"Unable to fetch Data",Toast.LENGTH_SHORT).show();
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
                    jsonData.append(line + "\n");
                }

                return jsonData.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}