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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

class MyAddapter extends RecyclerView.Adapter<MyAddapter.ViewHolder>{

    private List<ListItem> listItems;
    private Context context;

    MyAddapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.textViewQbid.setText(listItem.getQbid());
        final String id = holder.textViewQbid.getText().toString();
        holder.textViewQbname.setText(listItem.getQbname());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteTask d = new deleteTask();
                d.execute(id);
            }
        });
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gb = new Intent(context,Add_Questions.class);
                Bundle bundle = new Bundle();
                bundle.putString("Parent","DisplayQB");
                bundle.putString("qbid",id);
                gb.putExtras(bundle);
                gb.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                gb.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(gb);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewQbid;
        TextView textViewQbname;
        ImageButton delete, show;


        ViewHolder(View itemView) {
            super(itemView);

            textViewQbid = itemView.findViewById(R.id.textViewQbid);
            textViewQbname = itemView.findViewById(R.id.textViewQbname);
            delete = itemView.findViewById(R.id.img_delete);
            show = itemView.findViewById(R.id.img_show);
        }
    }
    private class deleteTask extends AsyncTask<String,String,String>
    {

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pd=new ProgressDialog(context);
            pd.setTitle("Deleting Question Bank");
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
                Toast.makeText(context,"Delete Successful",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Check your internet connectivity!!!",Toast.LENGTH_LONG).show();
            }
            Intent gb = new Intent(context,DisplayQB.class);
            gb.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            gb.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(gb);

        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                String qb_id = strings[0];
                String data= URLEncoder.encode("qb_id","UTF-8") + "=" +
                        URLEncoder.encode(qb_id,"UTF-8") ;

                URL url = new URL("https://contests.000webhostapp.com/php/delete_qb.php?"+data);
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