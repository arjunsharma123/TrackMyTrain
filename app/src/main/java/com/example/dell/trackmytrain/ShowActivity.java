package com.example.dell.trackmytrain;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

public class ShowActivity extends AppCompatActivity {
private Toolbar mToolbar;
private DatabaseReference mDatabase,mDatabaseSecond;
private static Dialog myDialog;
private static RecyclerView.Adapter adapter;
private static RecyclerView recyclerView;
private static RecyclerView.LayoutManager layoutManager;
private static ArrayList<Details> arrayList;
static View.OnClickListener myOnClickListener;
private ProgressDialog progressDialog;
DatePickerDialog datePickerDialog;
int year,month,dayOfMonth;
Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        myOnClickListener=new MyOnClickListener(ShowActivity.this);
        recyclerView=findViewById(R.id.mRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        arrayList=new ArrayList<>();
        mToolbar=findViewById(R.id.show_toolbar);
        adapter = new MyAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        progressDialog=new ProgressDialog(ShowActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.create();
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        LayoutInflater inflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.myactionbar,null);
        actionBar.setCustomView(v);
        final LinearLayout linearLayout=(LinearLayout) v.findViewById(R.id.actionlayout);
        final TextView textView=v.findViewById(R.id.actionText);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog=new Dialog(ShowActivity.this);
                myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myDialog.setContentView(R.layout.mycalendarview);
                myDialog.setCanceledOnTouchOutside(false);
                RadioGroup radioGroup=(RadioGroup) myDialog.findViewById(R.id.RadioGroupDate);
                Button cancel=(Button) myDialog.findViewById(R.id.btnCancel);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId)
                    {
                    switch (checkedId)
                    {
                        case R.id.RadioBtnAll:
                            textView.setText("All Dates");
                            break;
                        case R.id.RadioBtnToday:
                             textView.setText("Today");
                            break;
                        case R.id.RadioBtnTomorrow:
                            textView.setText("Tomorrow");
                            break;
                        case R.id.RadioBtnCalendar:
                            calendar=Calendar.getInstance();
                            year=calendar.get(Calendar.YEAR);
                            month=calendar.get(Calendar.MONTH);
                            dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
                            datePickerDialog = new DatePickerDialog(ShowActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                           textView.setText(day+"-"+(month+1)+"-"+year);
                                        }
                                    }, year, month, dayOfMonth);
                            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                            datePickerDialog.show();
                            break;
                    }
                        myDialog.cancel();
                    }
                });
                myDialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        myDialog.cancel();
                    }
                });


            }
        });


    }

public class Mytask extends AsyncTask<String,String, JSONObject>
{

    @Override
    protected void onPreExecute() {
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        JSONObject myJsonObject=null;
        HttpURLConnection httpURLConnection=null;
        BufferedReader reader=null;
        String JsonStr = null;
        try {
            URL url = new URL(strings[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            InputStream inputStream=httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Toast.makeText(ShowActivity.this, "Connection Error ", Toast.LENGTH_SHORT).show();
                progressDialog.hide();

            }
            else {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    Toast.makeText(ShowActivity.this, "Connection Error ", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();

                } else {
                    JsonStr = buffer.toString();
                }
            }
        }
        catch (IOException i)
        {
            i.printStackTrace();

        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (final IOException j) {
                    j.printStackTrace();

                }
            }
        }
        try
        {
            myJsonObject=new JSONObject(JsonStr);
            return myJsonObject;
        }
        catch (JSONException k)
        {
            k.printStackTrace();
        }

      return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(jsonObject==null)
        {
                Toast.makeText(ShowActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            if(progressDialog.isShowing()) {
                progressDialog.hide();
            }

        }
        else {
            if(progressDialog.isShowing()) {
                progressDialog.hide();
            }
            String tnumber, tname, src, des;
            try {
                int total = Integer.parseInt(jsonObject.getString("total"));
                JSONArray jsonArray = jsonObject.getJSONArray("trains");
                for (int i = 0; i < total; i++) {
                    tname = jsonArray.getJSONObject(i).getString("name");
                    tnumber = jsonArray.getJSONObject(i).getString("number");
                    src = jsonArray.getJSONObject(i).getString("src_departure_time");
                    des = jsonArray.getJSONObject(i).getString("dest_arrival_time");
                    String[] sarr = src.split(":");
                    String[] darr = des.split(":");
                    int shr = Integer.parseInt(sarr[0]);
                    int dhr = Integer.parseInt(darr[0]);
                    int smm = Integer.parseInt(sarr[1]);
                    int dmm = Integer.parseInt(darr[1]);
                    if (shr > 12) {
                        src = String.valueOf(shr - 12) + ":" + String.valueOf(smm) + " PM";
                    } else if (shr == 12) {
                        src += " PM";
                    } else {
                        src += " AM";
                    }
                    if (dhr > 12) {
                        des = String.valueOf(dhr - 12) + ":" + String.valueOf(dmm) + " PM";
                    } else if (dhr == 12) {
                        des += " PM";
                    } else {
                        des += " AM";
                    }
                    Details details = new Details(tname, tnumber, " ", src, des);
                    arrayList.add(details);
                    adapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.hide();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(ShowActivity.this, "Error", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                e.printStackTrace();

            }
        }
        }


    }


   private class MyOnClickListener implements View.OnClickListener {
        private Context context;
        public MyOnClickListener(Context context) {
         this.context=context;
        }

        @Override
        public void onClick(View view)
        {
           TextView tnumber=(TextView)view.findViewById(R.id.show_train_number);
           TextView tname=view.findViewById(R.id.show_train_name);
           TextView tsrc=view.findViewById(R.id.show_src_time);
           TextView tdes=view.findViewById(R.id.show_des_time);
           String number=tnumber.getText().toString();
           String name=tname.getText().toString();
           String srctime=tsrc.getText().toString();
           String destime=tdes.getText().toString();
          Intent trainDetailIntent=new Intent(context,TrainDetailActivity.class);
          trainDetailIntent.putExtra("tnumber",number );
          trainDetailIntent.putExtra("tname",name);
          trainDetailIntent.putExtra("tsrc",srctime);
          trainDetailIntent.putExtra("tdes",destime);
          trainDetailIntent.putExtra("src",getIntent().getStringExtra("sname"));
          trainDetailIntent.putExtra("des",getIntent().getStringExtra("dname"));
          startActivity(trainDetailIntent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        String Source=getIntent().getStringExtra("source");
        String Destination=getIntent().getStringExtra("destination");
      SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar=Calendar.getInstance();
      // String url="https://api.railwayapi.com/v2/between/source/"+Source+"/dest/"+Destination+"/date/"+dateFormat.format(calendar.getTime())+"/apikey/o471ijfegw/";
       String url="http://api.railwayapi.com/v2/between/source/"+Source+"/dest/"+Destination+"/date/"+dateFormat.format(calendar.getTime())+"/apikey/o471ijfegw/";
        Mytask mytask=new Mytask();
        mytask.execute(url);
    }
}
