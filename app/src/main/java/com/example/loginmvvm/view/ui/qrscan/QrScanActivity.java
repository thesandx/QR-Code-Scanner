package com.example.loginmvvm.view.ui.qrscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.loginmvvm.R;
import com.example.loginmvvm.view.ui.NavigationActivity;
import com.example.loginmvvm.view.ui.login.LoginFragment;

public class QrScanActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_qr_scan, null, true);
        FrameLayout frameLayout = findViewById(R.id.contentnav);
        frameLayout.addView(view);
        replaceFragment();
    }


    public void replaceFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameContainerqr,new QrScanFragment()).commit();
    }
}
