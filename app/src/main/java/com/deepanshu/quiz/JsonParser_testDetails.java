package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import static android.R.attr.data;


/**
 * Created by deepanshu on 25/7/17.
 */

public class JsonParser_testDetails extends AsyncTask<String,Void,String> {

    //JSON DOWNLOADER

    Context c;
    Spinner sp;
    ProgressDialog pd;
    int flag;

    public JsonParser_testDetails(Context c, Spinner sp) {
        this.c = c;
        this.sp = sp;
        this.flag = 1;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Download QB");
        pd.setMessage("Downloading...Please wait");
        pd.show();

    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String uid = strings[0];

            String data = URLEncoder.encode("user_id", "UTF-8") + "=" +
                    URLEncoder.encode(uid, "UTF-8");

            URL url = new URL("https://contests.000webhostapp.com/php/spinner_get_qb.php?" + data);
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

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);
        new Parser(jsonData).execute();
    }


    //JSON PARSER
    public class Parser extends AsyncTask<Void, Void, Boolean> {

        String jsonData;
        ArrayList<String> qbName = new ArrayList<>();
        ArrayList<String> qbId = new ArrayList<>();

        public Parser(String jsonData) {
            this.jsonData = jsonData;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                if(jsonData == null)
                    return false;
                JSONArray ja = new JSONArray(jsonData);
                JSONObject jo;
                int count = ja.length();
                if(count == 0){
                    flag = 0;
                }

                qbName.clear();
                qbId.clear();

                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);

                    String name = jo.getString("QB_NAME");
                    qbName.add(name);
                    String id = jo.getString("QB_ID");
                    qbId.add(id);
                }

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isParsed) {
            super.onPostExecute(isParsed);
            pd.dismiss();
            if (isParsed) {
                //BIND
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, qbName);
                sp.setAdapter(adapter);

                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        TestDetails.qbid = qbId.get(pos);
                        //Toast.makeText(c, TestDetails.qbid, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        TestDetails.qbid = qbId.get(0);
                    }
                });

            }
            if(flag == 0){
                Toast.makeText(c,"No Question Banks Available!!!\nAdd Some Question Banks First",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(c,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                c.startActivity(i);
            }
        }

    }

}