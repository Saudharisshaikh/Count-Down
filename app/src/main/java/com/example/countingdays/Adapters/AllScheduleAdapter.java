package com.example.countingdays.Adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countingdays.Model.Schedule;
import com.example.countingdays.R;
import com.example.countingdays.UI.Base.EditCountdownitems;
import com.example.countingdays.Utils.AppConstant;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class AllScheduleAdapter extends RecyclerView.Adapter<AllScheduleAdapter.AllScheduleViewHolder> {

    Context context;
    List<Schedule> scheduleList;

    public AllScheduleAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public AllScheduleAdapter.AllScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.home_items,parent,false);
        return new AllScheduleViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AllScheduleAdapter.AllScheduleViewHolder holder, int position) {


        Schedule schedule = scheduleList.get(position);
        LocalDateTime time = changeIntoDate(schedule.getDateTime());
         long duration = ChronoUnit.DAYS.between(LocalDate.now(), time);
        holder.scheduleName.setText(schedule.getScheduleName());
        LocalDateTime timeNew = LocalDateTime.parse(schedule.getDateTime());
        ZonedDateTime zdt = ZonedDateTime.of(timeNew, ZoneId.systemDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("dd MMMM yyyy");
       long datess = zdt.toInstant().toEpochMilli();
        Date date = new Date(datess);
        String dateText = df2.format(date);
        holder.dateSchedule.setText(dateText);
        //holder.dateSchedule.setText(String.valueOf(time.getDayOfMonth()+"-"+time.getMonthValue()+"-"+time.getYear()));
        String dayStrings = duration == 1 || duration == 0? "day Left":"days Left";
        String prceede = duration < 10 && duration > 0? "0":"";
       // duration = duration < 0 ? 0 : duration;


        if(duration < 0){

           holder.daysRemain.setText("Completed");
        }
        else{
            holder.daysRemain.setText(String.valueOf(prceede+duration+" "+dayStrings));
        }


        Log.d("--allScheduleAdapter", "onBindViewHolder: called");

        switch (schedule.getScheduleColor()) {
            case AppConstant.COLOR_PINK:
                holder.constraintLayout.setBackgroundResource(R.drawable.rectangle_home_pink);
                Log.d("--selectedColor", "onBindViewHolder: pink");
                break;
            case AppConstant.COLOR_ORANGE:
                holder.constraintLayout.setBackgroundResource(R.drawable.rectangle_home_orange);
                Log.d("--selectedColor", "onBindViewHolder: oorage");
                break;
            case AppConstant.COLOR_YELLOW:

                holder.constraintLayout.setBackgroundResource(R.drawable.rectangle_home_yellow);
                Log.d("--selectedColor", "onBindViewHolder: yellow");
                break;
            case AppConstant.COLOR_GREEN:
                holder.constraintLayout.setBackgroundResource(R.drawable.rectangle_home_green);
                Log.d("--selectedColor", "onBindViewHolder: green");
                break;
            case AppConstant.COLOR_PURPLE:
                holder.constraintLayout.setBackgroundResource(R.drawable.rectangle_home_purple);
                Log.d("--selectedColor", "onBindViewHolder: purple");
                break;
            case AppConstant.COLOR_BLUE:
                holder.constraintLayout.setBackgroundResource(R.drawable.rectangle_home_blue);
                Log.d("--selectedColor", "onBindViewHolder: accent");
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();

                bundle.putInt("scheduleId",schedule.getId());
                bundle.putString("scheduleName",schedule.getScheduleName());
                bundle.putString("scheduleDateTime",schedule.getDateTime());
                bundle.putString("scheduleColor",schedule.getScheduleColor());
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                EditCountdownitems fragment = new EditCountdownitems();
                fragment.setArguments(bundle);
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime changeIntoDate(String dateString){
        return   LocalDateTime.parse(dateString);
    }



    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class AllScheduleViewHolder extends RecyclerView.ViewHolder {

        TextView daysRemain,dateSchedule,scheduleName;
        ConstraintLayout constraintLayout;

        public AllScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            daysRemain = itemView.findViewById(R.id.days);
            dateSchedule = itemView.findViewById(R.id.schedule_date);
            scheduleName = itemView.findViewById(R.id.schedule_name);
            constraintLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
