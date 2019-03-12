package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class EarthquakeAdapter extends ArrayAdapter <Earthquake>{


    public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        View currentView = convertView;

        if(currentView == null){

            currentView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Earthquake currentEarthquake= getItem(position);


        TextView mag = currentView.findViewById(R.id.tv_mag);
        mag.setText(""+currentEarthquake.getMag());

        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();
        int magColor = getMagColor(currentEarthquake.getMag());
        magnitudeCircle.setColor(getContext().getColor(magColor));


        String s = currentEarthquake.getPlace();
        String place1;
        String place2;
        if (s.contains("of")){
            String t[] = s.split("of ");
            place1 = t[0]+"of";
            place2 = t[1];
        }else {
            place1 = "Near the";
            place2 = s;
        }


        TextView place1View = currentView.findViewById(R.id.tv_place1);
        place1View.setText(place1);

        TextView place2View = currentView.findViewById(R.id.tv_place2);
        place2View.setText(place2);

        Date dateObject = new Date(currentEarthquake.getDate());

        TextView date = currentView.findViewById(R.id.tv_date);
        String fDate = formatDate(dateObject);
        date.setText(fDate);

        TextView time = currentView.findViewById(R.id.tv_time);
        String fTime = formatTime(dateObject);
        time.setText(fTime);

        return currentView;
    }

    public String formatDate(Date date){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return simpleDateFormat.format(date);
    }

    public String formatTime(Date date){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        return simpleDateFormat.format(date);
    }

    public int getMagColor(Double mag){
        if (mag>10)
            return R.color.magnitude10plus;
        else if(mag>9)
            return  R.color.magnitude9;
        else if(mag>8)
            return R.color.magnitude8;
        else if(mag>7)
            return R.color.magnitude7;
        else if(mag>6)
            return R.color.magnitude6;
        else if (mag>5)
            return R.color.magnitude5;
        else if (mag>4)
            return R.color.magnitude4;
        else if (mag>3)
            return R.color.magnitude3;
        else if(mag>2)
            return R.color.magnitude2;
        else
            return R.color.magnitude1;
    }

}
