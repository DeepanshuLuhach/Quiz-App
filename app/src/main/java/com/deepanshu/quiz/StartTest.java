package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class StartTest extends AppCompatActivity {

    TextView mtestName, mtestTopic, mDuration, mAuthor, mposMarks, mnegMarks;
    Button mstartTest;
    int flag;
    String qbid, mAuthorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        final String testId = bundle.getString("TestId");

        mtestName = (TextView) findViewById(R.id.tv_test_name);
        mtestTopic = (TextView) findViewById(R.id.tv_test_topic);
        mDuration = (TextView) findViewById(R.id.tv_test_duration);
        mAuthor = (TextView) findViewById(R.id.tv_author);
        mposMarks = (TextView) findViewById(R.id.tv_posMarks);
        mnegMarks = (TextView) findViewById(R.id.tv_negMarks);
        mstartTest = (Button) findViewById(R.id.btn_start_test);


        flag = 1;
        FetchTask s = new FetchTask();
        s.execute(testId);


        mstartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check_connectivity check = new Check_connectivity(StartTest.this);
                if(check.getInternetStatus())
                {
                    Intent i = new Intent(StartTest.this, Question.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("QBId",qbid );
                    bundle.putString("posMarks",mposMarks.getText().toString() );
                    bundle.putString("negMarks",mnegMarks.getText().toString() );
                    bundle.putString("mDuration",mDuration.getText().toString() );
                    bundle.putString("TestId",testId );
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(StartTest.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
    private class FetchTask extends AsyncTask<String,String,String>
    {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd=new ProgressDialog(StartTest.this);
            pd.setTitle("Getting Details");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);


            if(s.trim().equals("")){
                flag = 0;
            }
            else if (!("error".equals(s.trim()))){

               //set the data in text views
                try {
                    JSONArray ja = new JSONArray(s);
                    JSONObject jo;
                    jo = ja.getJSONObject(0);

                    String name = jo.getString("TEST_NAME");
                    mtestName.setText(name);
                    name = jo.getString("TOPIC");
                    mtestTopic.setText(name);
                    name = jo.getString("DURATION");
                    mDuration.setText(name);
                    name = jo.getString("USER_ID");
                    mAuthorId = name;
                    name = jo.getString("name");
                    mAuthor.setText(name);
                    name = jo.getString("COR_MARKS");
                    mposMarks.setText(name);
                    name = jo.getString("INCOR_MARKS");
                    mnegMarks.setText(name);
                    qbid = jo.getString("QB_ID");

                } catch (JSONException e) {
                    e.printStackTrace();
                    flag = 0;
                }
            }
            else
            {
                flag = 0;
            }
            pd.dismiss();
            if(flag == 0)
            {
                Toast.makeText(StartTest.this,"Could not start the test",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StartTest.this,UserActivity.class));
                finish();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String test_id = strings[0];
                String data= URLEncoder.encode("test_id","UTF-8") + "=" +
                        URLEncoder.encode(test_id,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/start_test.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                StringBuilder jsonData = new StringBuilder();

                //READ
                while ((line = bufferedReader.readLine()) != null) {
                    jsonData.append(line).append("\n");
                }

                return jsonData.toString();


            }catch(Exception e){
                e.printStackTrace();
            }

            return "error";
        }
    }

}
//[{"QB_ID":"112","USER_ID":"1","ONLINE":"1","TEST_NAME":"test 1","TOPIC":"testing","DURATION":"1","name":"test","COR_MARKS":"5","INCOR_MARKS":"1"}]