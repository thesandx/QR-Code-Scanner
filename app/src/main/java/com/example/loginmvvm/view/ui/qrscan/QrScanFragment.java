package com.example.loginmvvm.view.ui.qrscan;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loginmvvm.R;
import com.example.loginmvvm.databinding.QrScanFragmentBinding;

public class QrScanFragment extends Fragment {

    private QrScanViewModel mViewModel;
    private QrScanFragmentBinding qrScanFragmentBinding;

    public static QrScanFragment newInstance() {
        return new QrScanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        qrScanFragmentBinding = qrScanFragmentBinding.inflate(inflater,container,false);
        return qrScanFragmentBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(QrScanViewModel.class);
        // TODO: Use the ViewModel
    }

}
