package com.example.studentapp.view.ui.qrscan;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.util.List;

public class QrCodeAnalyzer implements ImageAnalysis.Analyzer {
    onQrCodeDetected onQrCodeDetected;
    int count = 0;

    public QrCodeAnalyzer(onQrCodeDetected qrCodeDetected) {
        this.onQrCodeDetected = qrCodeDetected;

    }

    @Override
    public void analyze(ImageProxy imageProxy, int rotationDegrees) {
        if (imageProxy == null || imageProxy.getImage() == null) {
            return;
        }


        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_QR_CODE)
                        .build();


        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
        Image mediaImage = imageProxy.getImage();
        int rotation = degreesToFirebaseRotation(rotationDegrees);
        FirebaseVisionImage image = FirebaseVisionImage.fromMediaImage(mediaImage, rotation);


        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        //Task Completed Sucessfully
                        if (firebaseVisionBarcodes.size() > 0 && count == 0) {
                            onQrCodeDetected.onSuccess(firebaseVisionBarcodes);
                            count++;
                        }

                        Log.e("thesandx", "Success" + count);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //something went wrong
                        Log.e("thesandx", "Something went wrong");
                        onQrCodeDetected.onFailure("failure");

                    }
                });
    }

    private int degreesToFirebaseRotation(int degrees) {
        switch (degrees) {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException(
                        "Rotation must be 0, 90, 180, or 270.");
        }
    }


    interface onQrCodeDetected {
        void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes);

        void onFailure(String msg);
    }
}
