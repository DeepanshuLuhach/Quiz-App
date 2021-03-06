package com.deepanshu.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

class DisplayTest_adapter extends RecyclerView.Adapter<DisplayTest_adapter.ViewHolder> {
    private List<DisplayTest_ListItem> dt_listItems;
    Context context;

    DisplayTest_adapter(List<DisplayTest_ListItem> listItems, Context context) {
        this.dt_listItems = listItems;
        this.context = context;
    }

    @Override
    public DisplayTest_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_test__list_item,parent,false);

        return new DisplayTest_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DisplayTest_adapter.ViewHolder holder, int position) {
        DisplayTest_ListItem listItem = dt_listItems.get(position);

        final String id = listItem.getTestId();
        String pass;
        pass = "Id : "+listItem.getTestId()+"    Name : "+listItem.getTestName();
        holder.mTestName.setText(pass);
        pass = "Topic : "+listItem.getTestTopic();
        holder.mTopic.setText(pass);
        pass = "Time : "+listItem.getTestDuration()+" Hrs";
        holder.mDuration.setText(pass);
        pass = "QB Id : "+listItem.getTestQBId();
        holder.mqbid.setText(pass);
        int status = Integer.parseInt(listItem.getTestStatus());
        String temp = "";
        if(status == 1){
            //Test is currently online Click stop to stop the test
            temp = "Stop";
            holder.mStatus.setText(temp);
            holder.mStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int temp = 0;
                    updateStatusTask u = new updateStatusTask();
                    u.execute(id, String.valueOf(temp));
                    Intent gb = new Intent(context,DisplayTest.class);
                    gb.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    gb.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(gb);

                }
            });
        }
        else {
            //Test is currently offline Click start to start the test
            temp = "Start";
            holder.mStatus.setText(temp);
            holder.mStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int temp = 1;
                    updateStatusTask u = new updateStatusTask();
                    u.execute(id, String.valueOf(temp));

                    Intent gb = new Intent(context,DisplayTest.class);
                    gb.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    gb.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(gb);

                }
            });
        }

        holder.mResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AdminResults.class);
                Bundle bundle = new Bundle();
                bundle.putString("TestId",id);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask d = new deleteTask();
                d.execute(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dt_listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mTestName;
        TextView mTopic;
        TextView mDuration;
        TextView mqbid;
        Button mStatus, mResult;
        ImageButton delete;

        ViewHolder(View itemView) {
            super(itemView);

            mTestName = itemView.findViewById(R.id.tv_test_name);
            mDuration = itemView.findViewById(R.id.tv_duration);
            mqbid =  itemView.findViewById(R.id.tv_qb_id);
            mTopic = itemView.findViewById(R.id.tv_topic);
            mStatus = itemView.findViewById(R.id.btn_start_stop);
            mResult = itemView.findViewById(R.id.btn_admin_result);
            delete = itemView.findViewById(R.id.btn_delete_test);
        }
    }

    private class updateStatusTask extends AsyncTask<String,String,String>
    {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd=new ProgressDialog(context);
            pd.setTitle("Updating Test Status");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            pd.dismiss();

            if (!("error".equals(s.trim()))){
                Toast.makeText(context,"Update Successful",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Check your internet connectivity!!!",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String test_id = strings[0];
                String status = strings[1];
                String data= URLEncoder.encode("test_id","UTF-8") + "=" +
                        URLEncoder.encode(test_id,"UTF-8") + "&" +
                        URLEncoder.encode("status","UTF-8") + "=" +
                        URLEncoder.encode(status,"UTF-8") ;

                URL url = new URL("https://contests.000webhostapp.com/php/update_test_status.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result;

                result = bufferedReader.readLine();
                return result;



            }catch(Exception e){
                e.printStackTrace();
            }

            return "error";
        }
    }

    private class deleteTask extends AsyncTask<String,String,String>
    {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd=new ProgressDialog(context);
            pd.setTitle("Deleting Test");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            pd.dismiss();

           if (!("error".equals(s.trim()))){
                Toast.makeText(context,"Delete Successful",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Check your internet connectivity!!!",Toast.LENGTH_LONG).show();
            }
            Intent gb = new Intent(context,DisplayTest.class);
            gb.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            gb.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(gb);

        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String test_id = strings[0];
                String data= URLEncoder.encode("test_id","UTF-8") + "=" +
                        URLEncoder.encode(test_id,"UTF-8") ;

                URL url = new URL("https://contests.000webhostapp.com/php/delete_test.php?"+data);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String result;

                result = bufferedReader.readLine();
                if(result == null)
                    return "";
                return result;



            }catch(Exception e){
                e.printStackTrace();
            }

            return "error";
        }
    }
}
