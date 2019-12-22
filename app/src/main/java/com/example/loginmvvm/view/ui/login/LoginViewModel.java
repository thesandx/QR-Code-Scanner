package com.example.loginmvvm.view.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.loginmvvm.utils.SingleLiveEvent;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<String> email;
    private MutableLiveData<String> password;
    private SingleLiveEvent<String> message;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        email = new MutableLiveData<>();
        password = new MutableLiveData<>();
        message = new SingleLiveEvent<>();

    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void loginButtonClicked(){
            message.setValue("Button Clicked");
    }

}
