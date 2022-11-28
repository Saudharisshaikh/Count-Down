package com.example.countingdays;

import static java.util.Calendar.*;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.countingdays.Model.Schedule;
import com.example.countingdays.UI.Base.AboutUsFragment;
import com.example.countingdays.UI.Base.HomeFragment;
import com.example.countingdays.UI.Base.PrivacyFragment;
import com.example.countingdays.UI.Base.RatingFragment;
import com.example.countingdays.Utils.AlarmBroadcastReceiver;
import com.example.countingdays.Utils.AppConstant;
import com.example.countingdays.Utils.ScheduleService;
import com.example.countingdays.ViewModels.HomeViewModel;
import com.example.countingdays.ViewModels.MainActivityViewModel;
import com.google.android.material.navigation.NavigationView;

import org.joda.time.DateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout showNavigaton;
    boolean isActivityStop;
    AlarmManager alarmManager ;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    private void createNotificationChannel() {


        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel(
                    AppConstant.NOTIFICATION_CHANNEL_ID,
                    AppConstant.NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        createNotificationChannel();

        Intent intent = new Intent(this,ScheduleService.class);
        intent.putExtra("notiText","dash dash");
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            Log.d("--Oreo", "onCreate: ");
            startForegroundService(intent);
        }
        else{
            Log.d("--Oreo2", "onCreate: ");
            startService(intent);
        }

        List<Schedule> myScheduleList =new ArrayList<>();
        MainActivityViewModel mainActivityViewModel = new  ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.getScheduleList.observe(this,
                schedules -> {

                     setAlarm(schedules);
                     for(Schedule schedule:schedules){
                        Log.d("--MainSchedules", "startScheduleService: "+schedule.getScheduleName());

                     }
                }
        );


        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.main_toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setSupportActionBar(toolbar);
        setFragment(R.id.home,null);




      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                setFragment(item.getItemId(),null);


                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAlarm(List<Schedule> schedules) {



        for(Schedule schedule:schedules){
            LocalDateTime time = LocalDateTime.parse(schedule.getDateTime());

            final Date fromDate = Date.from(time.toInstant(ZoneOffset.UTC));
            Calendar calendar = getInstance();

            calendar.set(Calendar.HOUR, time.getHour());
            calendar.set(Calendar.MINUTE,time.getMinute());
            calendar.set(Calendar.SECOND,0);
            calendar.set(calendar.MILLISECOND,0);





//            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
//            AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingIntent
//
//        );
        }
    }




    public void setFragment(@IdRes int id, Bundle bundle) {

        bundle = bundle == null? new Bundle():bundle;
        Fragment fragment = null;
        switch (id) {
            case R.id.About:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new AboutUsFragment();
                break;
            case R.id.home:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new HomeFragment();
                break;

            case R.id.policy:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new PrivacyFragment();
                break;

            case R.id.rating:
                 drawerLayout.closeDrawer(GravityCompat.START);
                 fragment = new RatingFragment();
                 break;

            default:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new HomeFragment();
        }


            if (fragment != null) {
                handleFragmentTransaction(fragment, bundle);
            }

}

    public void handleFragmentTransaction(Fragment fragment, Bundle bundle) {

        if (fragment != null && !isActivityStop) {
            if (bundle != null)
                fragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        isActivityStop = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityStop = true;
    }
}