package com.deepanshu.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    EditText musername,mpassword;
    Button mlogin,mgoRegister;

    public static final String MyPREFERENCES = "User_Data";
    public static final String user_id = "user_id";
    public static final String qb_id = "question_bank_id";
    public static final String test_id = "test_id";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        musername = (EditText) findViewById(R.id.etusername);
        mpassword = (EditText) findViewById(R.id.etpassword);
        mlogin = (Button) findViewById(R.id.btnlogin);
        mgoRegister = (Button) findViewById(R.id.btnGoToRegister);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTask o = new loginTask();
                o.execute(musername.getText().toString(),mpassword.getText().toString());

                //Toast.makeText(Login.this,"Register here",Toast.LENGTH_LONG).show();;
            }
        });

        mgoRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent register = new Intent(Login.this,Register.class);
                startActivity(register);
            }
        });
    }

    public class loginTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            if(s.trim() == null){
                Toast.makeText(getBaseContext(),"Check your internet connectivity!!!",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
                if (!("not valid".equals(s.trim()))){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(user_id, s.trim());
                    editor.apply();
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }



        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String username = strings[0];
                String password = strings[1];
                String data= URLEncoder.encode("username","UTF-8") + "=" +
                        URLEncoder.encode(username,"UTF-8") + "&" +
                        URLEncoder.encode("password","UTF-8") + "=" +
                        URLEncoder.encode(password,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/login.php?"+data);
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
