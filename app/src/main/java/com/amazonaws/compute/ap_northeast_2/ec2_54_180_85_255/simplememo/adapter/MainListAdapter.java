package com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.R;
import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.bus.BusProvider;
import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.data.Memo;
import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.event.Delcheck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainListAdapter extends BaseAdapter {
    ArrayList<Memo> items;

    public MainListAdapter(ArrayList<Memo> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_lv_layout, parent, false);
            holder.main_lv_content = convertView.findViewById(R.id.main_lv_content);
            holder.main_lv_date = convertView.findViewById(R.id.main_lv_date);
            holder.main_lv_check_layout = convertView.findViewById(R.id.main_lv_check_layout);
            holder.main_lv_check = convertView.findViewById(R.id.main_lv_check);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Memo item = (Memo)getItem(position);
        holder.main_lv_content.setText(item.getContent());
        //SimpleDateFormat 월을 kk로 하면 24시간 표기로 된다 hh으로 할 경우 12시간 표기로 된다.
        SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy.MM.dd   kk-mm" );
        Date d = new Date(item.getTime());
        holder.main_lv_date.setText(sdf.format(d));

        final Holder finalHolder = holder;
        holder.main_lv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.main_lv_check.isChecked()) {
                    Delcheck event = new Delcheck(position, true);
                    BusProvider.getInstance().getBus().post(event);
                }
                else {
                    Delcheck event = new Delcheck(position, false);
                    BusProvider.getInstance().getBus().post(event);
                }
            }
        });

        return convertView;
    }

    public class Holder {
        TextView main_lv_content;
        TextView main_lv_date;
        LinearLayout main_lv_check_layout;
        CheckBox main_lv_check;
    }

}
