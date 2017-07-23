package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.deepanshu.quiz.Login.MyPREFERENCES;
import static com.deepanshu.quiz.Login.qb_id;
import static com.deepanshu.quiz.Login.user_id;

public class QBDetails extends AppCompatActivity {

    private EditText mname, mposmarks, mnegmarks;
    private Button mfinish, maddques;
    private TextView mhead;

    SharedPreferences sharedPreferences;

    public int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qbdetails);

        mname = (EditText) findViewById(R.id.et_QBname);
        mhead = (TextView) findViewById(R.id.head1);
        mposmarks = (EditText) findViewById(R.id.et_posMarks);
        mnegmarks = (EditText) findViewById(R.id.et_negMarks);
        mfinish = (Button) findViewById(R.id.btn_finish);
        maddques = (Button) findViewById(R.id.btn_addQuestions);

        i = 0;

        mhead.setText("Enter the details of your Question Bank :");

        mfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save data here and go to main_Activity
                Check_connectivity check = new Check_connectivity(QBDetails.this);
                if(check.getInternetStatus())
                {
                    i = 1;
                    SaveTask s = new SaveTask();
                    s.execute(mname.getText().toString(),mposmarks.getText().toString(),mnegmarks.getText().toString());
                    startActivity(new Intent(QBDetails.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(QBDetails.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                }


            }
        });

        maddques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save data here and go to Add_Question activity

                Check_connectivity check = new Check_connectivity(QBDetails.this);
                if(check.getInternetStatus())
                {
                    i = 0;
                    SaveTask s = new SaveTask();
                    s.execute(mname.getText().toString(),mposmarks.getText().toString(),mnegmarks.getText().toString());
                    startActivity(new Intent(QBDetails.this, Add_Questions.class));
                    finish();
                }
                else{
                    Toast.makeText(QBDetails.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public class SaveTask extends AsyncTask<String,String,String>
    {
       // ProgressDialog progressDialog = new ProgressDialog(QBDetails.this);

        @Override
        protected void onPreExecute() {

        //   progressDialog.setMessage("Saving... Please Wait");
        //    progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            //Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
            if(s.trim() == null){
                Toast.makeText(getBaseContext(),"Check your internet connectivity!!!",Toast.LENGTH_LONG).show();
            }
            else if (!("error".equals(s.trim()))){

                if(i == 1)  //Clicked on Finish
                {
                   // Toast.makeText(getBaseContext(),"Main",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(qb_id, "0");
                    editor.apply();
                }
                else
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(qb_id, s.trim());
                    editor.apply();
                    Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(getBaseContext(),"Error : "+s,Toast.LENGTH_LONG).show();
            }
            //progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String qbname = strings[0];
                String pmarks = strings[1];
                String nmarks = strings[2];
                sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String uid = sharedPreferences.getString(user_id,"0");
                String data= URLEncoder.encode("qbname","UTF-8") + "=" +
                        URLEncoder.encode(qbname,"UTF-8") + "&" +
                        URLEncoder.encode("user_id","UTF-8") + "=" +
                        URLEncoder.encode(uid,"UTF-8") + "&" +
                        URLEncoder.encode("pmarks","UTF-8") + "=" +
                        URLEncoder.encode(pmarks,"UTF-8") + "&" +
                        URLEncoder.encode("nmarks","UTF-8") + "=" +
                        URLEncoder.encode(nmarks,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/addtoqb.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result;

                result = bufferedReader.readLine();

                return result;

            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

}
