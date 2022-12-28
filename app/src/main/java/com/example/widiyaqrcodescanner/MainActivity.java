package com.example.widiyaqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity<view> extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    private Button buttonScan;
    private TextView textViewName, textViewClass, textViewNim;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewClass = (TextView) findViewById(R.id.textViewClass);
        textViewNim = (TextView) findViewById(R.id.textViewNim);

        //intialisasi scan object
        qrScan = new IntentIntegrator(this);

        //mengimplentasikan OnClickListener
        buttonScan.setOnClickListener((View.OnClickListener) this);
    }

    //untuk mendapatkan hasil scanning
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
                resultCode, data);
        if (result != null) {
            //jika qrcode tidak ada sama sekali
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil SCAN tidak ada", Toast.LENGTH_LONG).show();
            }


            else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);

            }


            else if (Patterns.PHONE.matcher(result.getContents()).matches()) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + result.getContents()));
                startActivity(intent);
            }

            else {
                //jika qr ada/ditemukan data nya
                try
                {
                    //konversi datanya ke json
                    JSONObject obj = new JSONObject(result.getContents());
                    //di set nilai datanya ke textviews
                    textViewName.setText(obj.getString("nama"));
                    textViewClass.setText(obj.getString("kelas"));
                    textViewNim.setText(obj.getString("nim"));

                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                    //jika kontolling ada di sini
                    //itu berarti format encoded tidak cocok
                    //dalam hal ini kita dapat menampilkan data apapun yg tesedia pada qrcode
                    //untuk di toast
                    Toast.makeText(this, result.getContents(),
                            Toast.LENGTH_LONG).show();
                }
                    }
                            } else
                            {
                    super.onActivityResult(requestCode, resultCode, data);
                            }
    }


    @Override
    public void onClick(View view) {
        //inisialisasi scanning qr code
        qrScan.initiateScan();
    }


}
