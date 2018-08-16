package com.example.saika.qrgenerateorreaderdemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class generate extends AppCompatActivity {
EditText text;
Button generate;
ImageView qrcode;
String texttoqr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        text= findViewById(R.id.text_id);
        generate= findViewById(R.id.generatebtn);
        qrcode= findViewById(R.id.qrview);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               texttoqr=text.getText().toString();
                MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix=multiFormatWriter.encode(texttoqr, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                    Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                    qrcode.setImageBitmap(bitmap);
                }
                catch (WriterException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
