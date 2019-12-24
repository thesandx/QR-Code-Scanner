package com.example.studentapp.view.ui.qrscan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentapp.databinding.QrScanResultFragmentBinding;

public class QrScanResult extends Fragment {

    QrScanResultFragmentBinding binding;
    String result;
    private QrScanResultViewModel mViewModel;

    public static QrScanResult newInstance() {
        return new QrScanResult();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = QrScanResultFragmentBinding.inflate(inflater, container, false);
        result = getArguments().getString("result");
        binding.qrResultText.setText(result);

        binding.included.toolbarTitle.setText("Result");
        binding.included.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireFragmentManager().popBackStack();
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(QrScanResultViewModel.class);
        // TODO: Use the ViewModel
    }

}
