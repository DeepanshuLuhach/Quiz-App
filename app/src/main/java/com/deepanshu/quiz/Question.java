package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.ArrayList;
import java.util.List;

public class Question extends AppCompatActivity {

    TextView mques;
    RadioButton mopA, mopB, mopC, mopD;
    Button mnext;
    List <QuestionDetails> quesList;
    int index = 0;
    int quescount = 0;
    int right = 0, wrong = 0;
    QuestionDetails currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Bundle bundle = getIntent().getExtras();
        String qbid = bundle.getString("QBId");
        FetchTask f = new FetchTask();
        f.execute(qbid);

        //get all questions in an list and set first question

        mques = (TextView) findViewById(R.id.tv_ques);
        mopA = (RadioButton) findViewById(R.id.rb_opA);
        mopB = (RadioButton) findViewById(R.id.rb_opB);
        mopC = (RadioButton) findViewById(R.id.rb_opC);
        mopD = (RadioButton) findViewById(R.id.rb_opD);
        mnext = (Button) findViewById(R.id.btn_next);
        mnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup grp=(RadioGroup)findViewById(R.id.rg_options);
                RadioButton ans=(RadioButton)findViewById(grp.getCheckedRadioButtonId());
                Toast.makeText(Question.this,ans.getText(),Toast.LENGTH_SHORT).show();
                if(currentQuestion.getAnswer().equals(ans.getText())){
                    right++;
                }
                else{
                    wrong++;
                }

                if(index < quescount){
                    currentQuestion=quesList.get(index);
                    setQuestionView();
                    index++;
                }else{
                    Intent intent = new Intent(Question.this, UserActivity.class);
                   /* Bundle b = new Bundle();
                    b.putInt("score", score); //Your score
                    intent.putExtras(b); //Put your score to your next Intent
                    */
                    Toast.makeText(Question.this,right+" "+wrong,Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            }
        });



    }

    private void setQuestionView() {
        mques.setText(currentQuestion.getQuestion());
        mopA.setText(currentQuestion.getA());
        mopB.setText(currentQuestion.getB());
        mopC.setText(currentQuestion.getC());
        mopD.setText(currentQuestion.getD());

    }

    private class FetchTask extends AsyncTask<String,String,String>
    {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd=new ProgressDialog(Question.this);
            pd.setTitle("Fetching Questions");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            pd.dismiss();
            int flag = 1;
            if (!("error".equals(s.trim()))){

                //set the data list of questions
                //{"QUESTION_ID":"42","QUESTION":"first","A":"a","B":"b","C":"c","D":"d","CORRECT":"A"}
                try {
                    quesList = new ArrayList<>();
                    quesList.clear();
                    JSONArray ja = new JSONArray(s);
                    JSONObject jo;
                    quescount = ja.length();
                    if(quescount == 0){
                        Toast.makeText(Question.this,"No questions added in the test",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Question.this,StartTest.class));
                        finish();
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        QuestionDetails qd = new QuestionDetails();
                        String temp = jo.getString("QUESTION_ID");
                        qd.setQid(temp);
                        temp = jo.getString("QUESTION");
                        qd.setQuestion(temp);
                        temp = jo.getString("A");
                        qd.setA(temp);
                        temp = jo.getString("B");
                        qd.setB(temp);
                        temp = jo.getString("C");
                        qd.setC(temp);
                        temp = jo.getString("D");
                        qd.setD(temp);
                        temp = jo.getString("CORRECT");
                        qd.setAnswer(temp);
                        quesList.add(qd);

                    }
                    currentQuestion = quesList.get(index);
                    setQuestionView();
                    index++;

                } catch (JSONException e) {
                    e.printStackTrace();
                    flag = 0;
                }
            }
            else
            {
                flag = 0;
                Toast.makeText(getBaseContext(),"Error : "+s,Toast.LENGTH_LONG).show();
            }
            if(flag == 0)
            {
                Toast.makeText(Question.this,"Error while fetching questions",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Question.this,StartTest.class));
                finish();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String qbid = strings[0];
                String data= URLEncoder.encode("qb_id","UTF-8") + "=" +
                        URLEncoder.encode(qbid,"UTF-8");

                URL url = new URL("https://contests.000webhostapp.com/php/get_questions.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                StringBuilder jsonData = new StringBuilder();

                //READ
                while ((line = bufferedReader.readLine()) != null) {
                    jsonData.append(line).append("\n");
                }

                System.out.println(jsonData.toString());
                return jsonData.toString();


            }catch(Exception e){
                e.printStackTrace();
            }

            return "error";
        }
    }
}
