package com.ICT.NAVJOT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ICT.NAVJOT.R;

/**
 * Created by Android on 10-10-2017.
 */

public class Adapter_history extends BaseAdapter {
        LayoutInflater layoutInflater;
        Adapter_history(Context context){layoutInflater=LayoutInflater.from(context);}
@Override
public int getCount() {
        return    History_list.blist.size();
        }

@Override
public Object getItem(int position) {
        return position;
        }

@Override
public long getItemId(int position) {
        return position;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        View view=layoutInflater.inflate(R.layout.history_layout,null);
        TextView name=(TextView)view.findViewById(R.id.textView12);
        TextView jod=(TextView)view.findViewById(R.id.textView10);
        TextView date=(TextView)view.findViewById(R.id.textView11);
        TextView daid=(TextView)view.findViewById(R.id.textView22);
        Customer temp=History_list.blist.get(position);
        name.setText(temp.Title);
        jod.setText(temp.Place);
        date.setText(temp.Date);
        daid.setText(temp.ID);
        return view;
        }
        }
