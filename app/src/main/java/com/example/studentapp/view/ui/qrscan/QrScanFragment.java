package com.example.studentapp.view.ui.qrscan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentapp.R;
import com.example.studentapp.databinding.QrScanFragmentBinding;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import java.util.List;

public class QrScanFragment extends Fragment {


    private final int REQUEST_CAMERA_PERMISSION = 10;

    private QrScanViewModel mViewModel;
    private QrScanFragmentBinding qrScanFragmentBinding;

    public static QrScanFragment newInstance() {
        return new QrScanFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        qrScanFragmentBinding = qrScanFragmentBinding.inflate(inflater, container, false);

        qrScanFragmentBinding.included.toolbarTitle.setText("QR Scan");
        qrScanFragmentBinding.included.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        if (isCameraPermissionGranted()) {
            qrScanFragmentBinding.textureView.post(this::startCamera);
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }


        return qrScanFragmentBinding.getRoot();

    }

    private boolean isCameraPermissionGranted() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void startCamera() {

        CameraX.unbindAll();
        //qrScanFragmentBinding.textureView.setEnabled(true);

        Rational aspectRatio = new Rational(qrScanFragmentBinding.textureView.getWidth(), qrScanFragmentBinding.textureView.getHeight());
        Size screen = new Size(qrScanFragmentBinding.textureView.getWidth(), qrScanFragmentBinding.textureView.getHeight()); //size of the screen


        PreviewConfig pConfig = new PreviewConfig.Builder().setLensFacing(CameraX.LensFacing.BACK).setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);

        //to update the surface texture we  have to destroy it first then re-add it
        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    @Override
                    public void onUpdated(Preview.PreviewOutput output) {
                        ViewGroup parent = (ViewGroup) qrScanFragmentBinding.textureView.getParent();
                        parent.removeView(qrScanFragmentBinding.textureView);
                        parent.addView(qrScanFragmentBinding.textureView, 0);

                        qrScanFragmentBinding.textureView.setSurfaceTexture(output.getSurfaceTexture());
                        QrScanFragment.this.updateTransform();
                    }
                });


        ImageAnalysisConfig config =
                new ImageAnalysisConfig.Builder()
                        .setTargetResolution(screen)
                        .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                        .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis(config);

        QrCodeAnalyzer qrCodeAnalyzer = new QrCodeAnalyzer(new QrCodeAnalyzer.onQrCodeDetected() {

            @Override
            public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                if (firebaseVisionBarcodes.size() > 0) {
                    FirebaseVisionBarcode barcode = firebaseVisionBarcodes.get(0);
                    Rect bounds = barcode.getBoundingBox();
                    Point[] corners = barcode.getCornerPoints();

                    String rawValue = barcode.getRawValue();
                    Toast.makeText(requireContext(), rawValue, Toast.LENGTH_SHORT).show();
                    vibratePhone();
                    openResultFragment(rawValue);
                    //qrScanFragmentBinding.textureView.setEnabled(false);
                    //qrScanFragmentBinding.textureView.getSurfaceTexture().release();
                    CameraX.unbindAll();

                    int valueType = barcode.getValueType();
                    // See API reference for complete list of supported types
                    switch (valueType) {
                        case FirebaseVisionBarcode.TYPE_WIFI:
                            String ssid = barcode.getWifi().getSsid();
                            String password = barcode.getWifi().getPassword();
                            int type = barcode.getWifi().getEncryptionType();
                            break;
                        case FirebaseVisionBarcode.TYPE_URL:
                            String title = barcode.getUrl().getTitle();
                            String url = barcode.getUrl().getUrl();
                            break;
                    }

                }
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        imageAnalysis.setAnalyzer(qrCodeAnalyzer);


        //bind to lifecycle:
        CameraX.bindToLifecycle((LifecycleOwner) requireContext(), imageAnalysis, preview);
    }

    private void updateTransform() {
        Matrix mx = new Matrix();
        float w = qrScanFragmentBinding.textureView.getMeasuredWidth();
        float h = qrScanFragmentBinding.textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int) qrScanFragmentBinding.textureView.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float) rotationDgr, cX, cY);
        qrScanFragmentBinding.textureView.setTransform(mx);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                qrScanFragmentBinding.textureView.post(this::startCamera);
            } else {
                Toast.makeText(requireContext(), "Camera permission is requried", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(QrScanViewModel.class);
        // TODO: Use the ViewModel
    }

    public void openResultFragment(String qrResult) {
        QrScanResult qrScanResult = new QrScanResult();
        Bundle bundle = new Bundle();
        bundle.putString("result", qrResult);
        qrScanResult.setArguments(bundle);
        requireFragmentManager()
                .beginTransaction()
                .replace(R.id.frameContainerqr, qrScanResult)
                .commit();
    }

    public void vibratePhone() {
        Vibrator v = (Vibrator) getActivity().getSystemService(requireContext().VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}
