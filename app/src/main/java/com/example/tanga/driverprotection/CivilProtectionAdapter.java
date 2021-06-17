package com.example.tanga.driverprotection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CivilProtectionAdapter extends BaseAdapter {


    private List<Accidents> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CivilProtectionAdapter(Context aContext, ArrayList<Accidents> listData)
    {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }


        private int retour;

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.protection_civil_listrow, null);
                holder = new ViewHolder();
                holder.AccidentTime = (TextView) convertView.findViewById(R.id.crashtime);
                holder.AccidentLong = (TextView) convertView.findViewById(R.id.crashlong);
                holder.AccidentLati = (TextView) convertView.findViewById(R.id.crashlati);
                holder.AccidentMap = (ImageButton) convertView.findViewById(R.id.openmaps);
                //holder.FreelancersWorkingOn = (GridView) convertView.findViewById(R.id.gridview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Accidents p = this.listData.get(position);

            holder.AccidentTime.setText(p.getCrashTime());
            holder.AccidentLong.setText(p.getLongitude());
            holder.AccidentLati.setText(p.getLatitude());
            holder.AccidentMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String locationurl = "https://www.google.com/maps?q="+p.getLongitude()+","+p.getLatitude()+"(Pointer)";
                    Log.d("LOG", locationurl);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationurl));
                    context.startActivity(browserIntent);
                }
            });

            final View finalConvertView = convertView;

            return convertView;
        }


        static class ViewHolder {
            TextView AccidentTime;
            TextView AccidentLong;
            TextView AccidentLati;
            ImageButton AccidentMap;


        }
    }
