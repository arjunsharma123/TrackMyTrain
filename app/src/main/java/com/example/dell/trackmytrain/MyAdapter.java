package com.example.dell.trackmytrain;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 16-Jan-19.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
private ArrayList<Details> arrayList;
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

      TextView tname,tnumber,fair,src,des;
        public MyViewHolder(View view)
        {
            super(view);
            this.tname=(TextView) view.findViewById(R.id.show_train_name);
            this.tnumber=(TextView) view.findViewById(R.id.show_train_number);
            this.fair=(TextView) view.findViewById(R.id.show_train_fair);
            this.src=(TextView)view.findViewById(R.id.show_src_time);
            this.des=(TextView)view.findViewById(R.id.show_des_time);

        }
    }
    public MyAdapter(ArrayList<Details> arrayList)
    {

        this.arrayList=arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,parent,false);
        view.setOnClickListener(ShowActivity.myOnClickListener);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        TextView tname=holder.tname;
        TextView tnumber=holder.tnumber;
        TextView fair=holder.fair;
        TextView src=holder.src;
        TextView des=holder.des;
        tname.setText(arrayList.get(position).getTname());
        tnumber.setText(arrayList.get(position).getTnumber());
        fair.setText(arrayList.get(position).getFair());
        src.setText(arrayList.get(position).getSrc());
        des.setText(arrayList.get(position).getDes());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
