package com.example.dell.trackmytrain;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static RecyclerView.LayoutManager layoutManager;
    private static ArrayList<MyData> arrayList;
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;
     static ArrayList<String> name,code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        name=getIntent().getStringArrayListExtra("name");
        code=getIntent().getStringArrayListExtra("code");
        String source=getIntent().getStringExtra("val");
        int index=code.indexOf(source);
        mToolbar=findViewById(R.id.show_toolbar);
        recyclerView=findViewById(R.id.mRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        arrayList=new ArrayList<>();
        adapter = new MyCheckAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        progressDialog=new ProgressDialog(CheckActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.create();
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(name.get(index)+" : Upcoming trains");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    public class task extends AsyncTask<String,String, JSONObject> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject myJsonObject = null;
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;
            String JsonStr = null;
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    Toast.makeText(CheckActivity.this, "Connection Error ", Toast.LENGTH_SHORT).show();
                }
                JsonStr = buffer.toString();
                myJsonObject = new JSONObject(JsonStr);
                reader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (IOException i)
            {
                 progressDialog.dismiss();
                i.printStackTrace();

            } catch (JSONException j) {
                progressDialog.dismiss();

                j.printStackTrace();

            }
            return myJsonObject;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(jsonObject!=null) {
                String tnumber, tname, src, des;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("Trains");
                    progressDialog.dismiss();
                    for (int i = 0; i <jsonArray.length(); i++) {
                        tname = jsonArray.getJSONObject(i).getString("Name");
                        tnumber = jsonArray.getJSONObject(i).getString("Number");
                        src = jsonArray.getJSONObject(i).getString("Source");
                        des = jsonArray.getJSONObject(i).getString("Destination");

                            MyData myData = new MyData(tname, tnumber, src, des);
                            arrayList.add(myData);
                            adapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(CheckActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String Source=getIntent().getStringExtra("val");
       String url="https://indianrailapi.com/api/v2/LiveStation/apikey/be69708c9b48d59f63e8b0e959de0a19/StationCode/"+Source+"/hours/2/";
        task mytask=new task();
        mytask.execute(url);
    }
}
