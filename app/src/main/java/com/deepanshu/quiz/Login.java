package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        musername = (EditText) findViewById(R.id.etusername);
        mpassword = (EditText) findViewById(R.id.etpassword);
        mlogin = (Button) findViewById(R.id.btnlogin);
        mgoRegister = (Button) findViewById(R.id.btnGoToRegister);

        Check_connectivity check = new Check_connectivity(this);
        if(!check.getInternetStatus()){
            Toast.makeText(Login.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
        }

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check_connectivity check = new Check_connectivity(Login.this);
                if(fieldsvalidation())
                {
                    if(check.getInternetStatus())
                    {
                        loginTask o = new loginTask();
                        o.execute(musername.getText().toString(),mpassword.getText().toString());
                    }
                    else{
                        Toast.makeText(Login.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(Login.this,"Field cannot be left blank",Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(Login.this,"Register here",Toast.LENGTH_LONG).show();;
            }
        });

        mgoRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Check_connectivity check = new Check_connectivity(Login.this);
                if(check.getInternetStatus()){
                    Intent register = new Intent(Login.this,Register.class);
                    startActivity(register);
                }
                else{
                    Toast.makeText(Login.this,"Internet Connection Problem",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean fieldsvalidation() {
        return !musername.getText().toString().equals("") && !mpassword.getText().toString().equals("");
    }

    public class loginTask extends AsyncTask<String,String,String>
    {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd=new ProgressDialog(Login.this);
            pd.setTitle("Logging In");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            pd.dismiss();
            try {
                if (!("not valid".equals(s.trim()))) {
                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(user_id, s.trim());
                    editor.apply();
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Unable to connect!!!", Toast.LENGTH_LONG).show();
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
