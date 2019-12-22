package com.example.loginmvvm.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginmvvm.view.ui.login.LoginViewModel;

// Factory class for creating viewmodels used in whole application
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;
    private final Application mApplication;

    private ViewModelFactory(Application application) {
        mApplication = application;
    }

    // creates single object for ViewModelFactory through singleton pattern
    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mApplication);
        }
//        } else if (modelClass.isAssignableFrom(AddAppointmentViewModel.class)) {
//            return (T) new AddAppointmentViewModel(mApplication, receptionistRepository);
//        } else if (modelClass.isAssignableFrom(AppointmentViewModel.class)) {
//            return (T) new AppointmentViewModel(mApplication, receptionistRepository);
//        } else if (modelClass.isAssignableFrom(PatientInfoViewModel.class)) {
//            return (T) new PatientInfoViewModel(mApplication, receptionistRepository);
//        } else if (modelClass.isAssignableFrom(VersionCheckViewModel.class)) {
//            return (T) new VersionCheckViewModel(receptionistRepository);
//        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

}