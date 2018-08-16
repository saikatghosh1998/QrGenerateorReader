package com.example.saika.qrgenerateorreaderdemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission_group.CAMERA;

public class Read extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA=1;
            private ZXingScannerView ScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView=new ZXingScannerView(this);
        setContentView(ScannerView);

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                Toast.makeText(Read.this,"Permission granted", Toast.LENGTH_LONG).show();
            }
            else
                {
                    requestPermission();
                }
        }
    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(Read.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED);

    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }

    public  void onRequestPermissionResult(int requestCode,String Permission[],int grantResults[])
    {
        switch (requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length>0)
                {
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted)
                    {
                        Toast.makeText(Read.this,"Permission Granted",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(Read.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M)
                        {
                            if(shouldShowRequestPermissionRationale(CAMERA))
                            {
                                displayAlertMessage("you need to grant permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA},REQUEST_CAMERA);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }
@Override
public void onResume()
{
    super.onResume();
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
    {
        if(checkPermission())
        {
            if(ScannerView==null)
            {
                ScannerView=new ZXingScannerView(this);
                setContentView(ScannerView);
            }
            ScannerView.setResultHandler(this);
            ScannerView.startCamera();
        }
        else {
            requestPermission();
        }
    }
}
@Override
public  void onDestroy() {
    super.onDestroy();
    ScannerView.stopCamera();
}
    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(Read.this)
                .setMessage(message)
                .setPositiveButton("ok",listener)
                .setNegativeButton("cancel",null)
                .create()
                .show();
    }
    @Override
    public void handleResult(Result result) {
final String scanResult=result.getText();
AlertDialog.Builder builder=new AlertDialog.Builder(this);
builder.setTitle("Scam Result");
builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        ScannerView.resumeCameraPreview(Read.this);
    }
});
builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
        startActivity(intent);
    }
});
builder.setMessage(scanResult);
AlertDialog alertDialog=builder.create();
alertDialog.show();
    }
}
