package com.example.countingdays.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.countingdays.Application.App;
import com.example.countingdays.Preferences.AppPreferenceHelper;


import java.io.Closeable;
import java.lang.ref.WeakReference;

public class BaseViewModel<N> extends AndroidViewModel {

    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private AppPreferenceHelper appPreferenceHelper;
    private WeakReference<N> mNavigator;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }


    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }

    public LiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading.setValue(isLoading);
    }

    public AppPreferenceHelper getAppPreferenceHelper(){
       appPreferenceHelper =  AppPreferenceHelper.getInstance(App.getContext());

        return appPreferenceHelper;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
     }


}
