package com.example.studentapp.view.ui.qrscan;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.example.studentapp.R;
import com.example.studentapp.view.ui.NavigationActivity;

public class QrScanActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);
        /*LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_qr_scan, null, true);
        FrameLayout frameLayout = findViewById(R.id.frameContainerqr);
        frameLayout.addView(view);*/
        replaceFragment();
    }


    public void replaceFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameContainerqr,new QrScanFragment()).commit();
    }


}
