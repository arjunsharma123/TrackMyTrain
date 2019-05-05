package com.example.dell.trackmytrain;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCheckAdapter  extends RecyclerView.Adapter<MyCheckAdapter.MyViewHolder>
{
    private ArrayList<MyData> arrayList;
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tname,tnumber,src,des;
        public MyViewHolder(View view)
        {
            super(view);
            this.tname=(TextView) view.findViewById(R.id.check_train_name);
            this.tnumber=(TextView) view.findViewById(R.id.check_train_number);

            this.src=(TextView)view.findViewById(R.id.check_src_name);
            this.des=(TextView)view.findViewById(R.id.check_des_name);

        }
    }
    public MyCheckAdapter(ArrayList<MyData> arrayList)
    {

        this.arrayList=arrayList;
    }

    @Override
    public MyCheckAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.checklistitem,parent,false);
        MyCheckAdapter.MyViewHolder myViewHolder=new MyCheckAdapter.MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final MyCheckAdapter.MyViewHolder holder, final int position) {
        TextView tname=holder.tname;
        TextView tnumber=holder.tnumber;
        TextView src=holder.src;
        TextView des=holder.des;
        tname.setText(arrayList.get(position).getTname());
        tnumber.setText(arrayList.get(position).getTnumber());
        src.setText(arrayList.get(position).getSrc());
        des.setText(arrayList.get(position).getDes());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
