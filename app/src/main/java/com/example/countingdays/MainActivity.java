package com.example.countingdays;

import static java.util.Calendar.*;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.countingdays.Model.Schedule;
import com.example.countingdays.UI.Base.AboutUsFragment;
import com.example.countingdays.UI.Base.EditIndividualFragment;
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
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout showNavigaton;
    boolean isActivityStop;
    AlarmManager alarmManager ;
    Fragment currentFragment;

    private Fragment lastFragmentInQueue = null;

    MainActivityViewModel mainActivityViewModel;
    Bundle bundle = null;
    int id = 0;
    String nameS = "",dateTimeS = "",colorS = "",startTimeS = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        id = getIntent().getIntExtra(AppConstant.BUNDLE_SCHEDULE_ID,0);
        nameS = getIntent().getStringExtra(AppConstant.BUNDLE_SCHEDULE_NAME);
        dateTimeS = getIntent().getStringExtra(AppConstant.BUNDLE_SCHEDULE_DATE_TIME);
        colorS = getIntent().getStringExtra(AppConstant.BUNDLE_SCHEDULE_COLOR);
        startTimeS = getIntent().getStringExtra(AppConstant.BUNDLE_SCHEDULE_STARTIME);

        mainActivityViewModel = new  ViewModelProvider(this).get(MainActivityViewModel.class);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        //toolbar.setTitleTextAppearance(this,R.style.MyTitleTextApperance);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setSupportActionBar(toolbar);


        if(id != 0){
            EditIndividualFragment fragment = new EditIndividualFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt(AppConstant.BUNDLE_SCHEDULE_ID,id);
                bundle1.putString(AppConstant.BUNDLE_SCHEDULE_NAME,nameS);
                bundle1.putString(AppConstant.BUNDLE_SCHEDULE_DATE_TIME,dateTimeS);
                bundle1.putString(AppConstant.BUNDLE_SCHEDULE_STARTIME,startTimeS);
                bundle1.putString(AppConstant.BUNDLE_SCHEDULE_COLOR,colorS);

                fragment.setArguments(bundle1);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

        }

        else{
            setFragment(R.id.home,null);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                setFragment(item.getItemId(),null);


                return true;
            }
        });
    }


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
    public void setViewModelData(){

        ArrayList<Schedule> deleteSchedules = new ArrayList<>();

        mainActivityViewModel.getScheduleList.observe(this,
                schedules -> {

                    LocalDateTime nowTime = LocalDateTime.now();
                    for (Schedule schedule : schedules) {

                        LocalDateTime time = LocalDateTime.parse(schedule.getDateTime());
                        ZonedDateTime zdt = ZonedDateTime.of(time, ZoneId.systemDefault());
                        long datess = zdt.toInstant().toEpochMilli();
                        Date dateS = new Date(datess);
                        Date dateN = new Date();
                        if (time.isBefore(LocalDateTime.now())) {

                            deleteSchedules.add(schedule);
                            Log.d("--previousDate ", "setViewModelData: " + schedule.getDateTime() + "     "+dateS+"  "+dateN);
                        }

                    }
                    if (schedules.size() > 0){
                        Schedule[] newDelteList = new Schedule[deleteSchedules.size()];

                    for (int i = 0; i < deleteSchedules.size()-1; i++) {

                        newDelteList[i] = schedules.get(i);
                        Log.d("--inArray", "setViewModelData: " + newDelteList[i]);
                    }

                    mainActivityViewModel.deleteSelectedSchedules(newDelteList);
                }
        }
        );
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


    @Override
    public void onBackPressed() {
        if(!(currentFragment instanceof  HomeFragment)){

            Log.d("--backPress", "onBackPressed: ");
            setFragment(R.id.home,null);
            return;
        }
        super.onBackPressed();
    }

    public void handleFragmentTransaction(Fragment fragment, Bundle bundle) {

        if (fragment != null && !isActivityStop) {
            if (bundle != null)
                fragment.setArguments(bundle);
        //    lastFragmentInQueue = fragment;
            currentFragment = fragment;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        isActivityStop = false;
//        if(lastFragmentInQueue != null){
//            handleFragmentTransaction(lastFragmentInQueue,null);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        isActivityStop = true;
    }
}