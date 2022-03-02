package com.android.myappproject.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.android.myappproject.R;
import com.android.myappproject.vo.CustomHospitalVo;

import java.util.List;

public class CustomAdapter extends BaseAdapter
{
    private Activity activity;

    private List<CustomHospitalVo> memberList;

    private CustomHolder holder;

    public class CustomHolder
    {
        private TextView tv_custom_item_name;
        private TextView tv_custom_item_address;
        private TextView tv_custom_item_homepage;
    }

    public CustomAdapter(Activity activity, List<CustomHospitalVo> memberList)
    {
        this.activity = activity;

        this.memberList = memberList;
    }

    @Override
    public int getCount()
    {
        return memberList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_custom_list, parent, false);

            holder = new CustomHolder();

            holder.tv_custom_item_name = convertView.findViewById(R.id.tv_custom_item_name);
            holder.tv_custom_item_address = convertView.findViewById(R.id.tv_custom_item_address);
            holder.tv_custom_item_homepage = convertView.findViewById(R.id.tv_custom_item_homepage);

            convertView.setTag(holder);
        }
        else
        {
            holder = (CustomHolder) convertView.getTag();
        }


        holder.tv_custom_item_address.setText(memberList.get(position).getAddress());
        holder.tv_custom_item_name.setText(memberList.get(position).getName());
        holder.tv_custom_item_homepage.setText(memberList.get(position).getHomepage());


        return convertView;
    }

    public void addItem(CustomHospitalVo customHospitalVo)
    {
        //memberList.add(customMemberVo);
        notifyDataSetChanged();
    }
}
