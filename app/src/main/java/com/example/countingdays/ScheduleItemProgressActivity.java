package com.example.countingdays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.countingdays.UI.Base.EditIndividualFragment;
import com.example.countingdays.UI.Base.HomeFragment;
import com.example.countingdays.Utils.AppConstant;
import com.example.countingdays.Utils.Utils;
import com.example.countingdays.databinding.ActivityScheduleItemProgressBinding;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ScheduleItemProgressActivity extends AppCompatActivity {

    ActivityScheduleItemProgressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScheduleItemProgressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();
                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

        Bundle bundle = getIntent().getExtras().getBundle(AppConstant.BUNDLE_KEY);
        int id = 0;
        String name = null;
        String dateTime = null;
        String startTime = null;
        String scheduleColor = null;
        if(bundle != null){
            id = bundle.getInt("scheduleId");
            name = bundle.getString("scheduleName");
            dateTime = bundle.getString("scheduleDateTime");
            scheduleColor = bundle.getString("scheduleColor");
            startTime = bundle.getString("startTime");
            Log.d("--BundleHere", "onViewCreated: "+id+" "+dateTime+" "+name+"  "+startTime);
        }


        binding.backClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivity(new Intent(ScheduleItemProgressActivity.this,MainActivity.class));
            }
        });


        int finalId = id;
        String finalName = name;
        String finalDateTime = dateTime;
        String finalColor = scheduleColor;
        String finalStartTime = startTime;
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ScheduleItemProgressActivity.this,MainActivity.class);
                intent.putExtra("scheduleId",finalId);
                intent.putExtra("scheduleName",finalName);
                intent.putExtra("scheduleDateTime",finalDateTime);
                intent.putExtra("scheduleColor",finalColor);
                intent.putExtra("startTime",finalStartTime);
                startActivity(intent);


            }
        });


        LocalDateTime time = LocalDateTime.parse(dateTime);

        LocalDateTime startTimeSchedule = LocalDateTime.parse(startTime);

        double newDuration = ChronoUnit.MINUTES.between(startTimeSchedule,time);

        double timeGone = ChronoUnit.MINUTES.between(startTimeSchedule,LocalDateTime.now());

        double percentage = (timeGone / newDuration) * 100;

        if(percentage < 0.0 ){
           percentage = 100;
        }

        Log.d("--newDuration ", "onViewCreated: "+newDuration + "    "+timeGone+"   "+percentage);

        long minutesRemain = ChronoUnit.MINUTES.between(LocalDateTime.now(),time);

//        long monthcountdown = (int)(minutesRemain/43200);
//        minutesRemain = (int)(minutesRemain%43200);
//        long weekcountdown = (int)(minutesRemain/10080);
//        minutesRemain = (int)(minutesRemain%10080);
        long daycountdown= (int)(minutesRemain/1440);
        minutesRemain = (int)(minutesRemain%1440);
        long hourcountdown = (int)(minutesRemain/60);
        minutesRemain = (int)(minutesRemain%60);

        daycountdown = daycountdown <0 ? 0 :daycountdown;
        hourcountdown = hourcountdown < 0 ? 0 :hourcountdown;
        minutesRemain = minutesRemain < 0 ? 0:minutesRemain;

        String dayExtra   = daycountdown <10 ? "0":"";
        String hourExtra = hourcountdown < 10 ? "0":"";
        String minuteExtra = minutesRemain < 10 ? "0":"";

        Log.d("--monthValue", "onViewCreated: "+" "+daycountdown+"  "+hourcountdown+"  "+minutesRemain);
        if(daycountdown <= 0){
            binding.dayDigit.setText(String.valueOf(hourExtra+hourcountdown));
            binding.dayLabel.setText("hours");
            binding.minuteDigit.setText(minuteExtra+minutesRemain);
            binding.hourdots.setVisibility(View.GONE);
            binding.hourDigit.setVisibility(View.GONE);
            binding.hourLabel.setVisibility(View.GONE);
        }
        else if(daycountdown <= 0 && hourcountdown <= 0){
            Log.d("--hourwala", "onCreate: ");
            binding.dayDigit.setText(String.valueOf(minuteExtra+minutesRemain));
            binding.dayLabel.setText("minutes");
            binding.minuteDots.setVisibility(View.GONE);
            binding.hourdots.setVisibility(View.GONE);
            binding.hourLabel.setVisibility(View.GONE);
            binding.hourLabel.setVisibility(View.GONE);
            binding.minuteDigit.setVisibility(View.GONE);
            binding.minuteLabel.setVisibility(View.GONE);
            binding.secondDigit.setVisibility(View.GONE);
            binding.secondsLabel.setVisibility(View.GONE);
        }
     else  if(daycountdown <= 0 && hourcountdown <= 0 && minutesRemain <= 0){
            binding.dayDigit.setText("Completed");
            binding.dayDigit.setTextSize(30);
            binding.dayLabel.setVisibility(View.GONE);
            binding.minuteDots.setVisibility(View.GONE);
            binding.hourdots.setVisibility(View.GONE);
            binding.hourLabel.setVisibility(View.GONE);
            binding.hourLabel.setVisibility(View.GONE);
            binding.minuteDigit.setVisibility(View.GONE);
            binding.minuteLabel.setVisibility(View.GONE);
            binding.secondDigit.setVisibility(View.GONE);
            binding.secondsLabel.setVisibility(View.GONE);
        }
        else{

            binding.dayDigit.setText(String.valueOf(dayExtra+daycountdown));
            binding.hourDigit.setText(String.valueOf(hourExtra+hourcountdown));
            binding.minuteDigit.setText(String.valueOf(minuteExtra+minutesRemain));
        }

//        binding.dayDigit.setText(String.valueOf(dayExtra+daycountdown));
//        binding.hourDigit.setText(String.valueOf(hourExtra+hourcountdown));
//        binding.minuteDigit.setText(String.valueOf(minuteExtra+minutesRemain));

        String checkingColor = finalColor == null ? AppConstant.COLOR_BLUE:finalColor;


        if(checkingColor.equals(AppConstant.COLOR_PINK)){

            Drawable drawable = getResources().getDrawable(R.drawable.circle_progress_pink);
            binding.progressbar.setProgressDrawable(drawable);
            binding.progressbar.setProgress((int) percentage);
        }
        else if(checkingColor.equals(AppConstant.COLOR_ORANGE)){
            Drawable drawable = getResources().getDrawable(R.drawable.circle_progress_orange);
            binding.progressbar.setProgressDrawable(drawable);
            binding.progressbar.setProgress((int) percentage);
        }

        else if(checkingColor.equals(AppConstant.COLOR_YELLOW)){
            Drawable drawable = getResources().getDrawable(R.drawable.circle_progress_yellow);
            binding.progressbar.setProgressDrawable(drawable);
            binding.progressbar.setProgress((int) percentage);
        }

        else if(checkingColor.equals(AppConstant.COLOR_GREEN)){
            Drawable drawable = getResources().getDrawable(R.drawable.circle_progress_green);
            binding.progressbar.setProgressDrawable(drawable);
            binding.progressbar.setProgress((int) percentage);
        }

        else if(checkingColor.equals(AppConstant.COLOR_PURPLE)){
            Drawable drawable = getResources().getDrawable(R.drawable.circle_progress_purple);
            binding.progressbar.setProgressDrawable(drawable);
            binding.progressbar.setProgress((int) percentage);
        }
        else if(checkingColor.equals(AppConstant.COLOR_BLUE)){
            Drawable drawable = getResources().getDrawable(R.drawable.circle_progress_blue);
            binding.progressbar.setProgressDrawable(drawable);
            binding.progressbar.setProgress((int) percentage);
        }
        else {
            Drawable drawable = getResources().getDrawable(R.drawable.circle_progress_default);
            binding.progressbar.setProgressDrawable(drawable);
            binding.progressbar.setProgress((int) percentage);
        }

        ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
        SimpleDateFormat df2 = new SimpleDateFormat("MMMM dd, yyyy");
        long datess = zdt.toInstant().toEpochMilli();
        Date date = new Date(datess);
        String dateText = df2.format(date);
       String newExactDate =  Utils.exactDate(dateText);
        Log.d("--dueDate", "onCreate: "+newExactDate);
        binding.dueDate.setText("Due "+newExactDate);
        binding.ScheduleNameEdit.setText(name);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}