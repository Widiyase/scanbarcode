package com.example.widiyaqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

        //scan object
        qrScan = new IntentIntegrator(this);

        //implementation OnClickListener
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
            Intent intent;
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil SCAN tidak ada", Toast.LENGTH_LONG).show();
            }


            //phone call
            String number;
            number = new String(result.getContents());

            if(number.matches("^[0-9]*$") && number.length() > 10){
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + number));
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
                startActivity(dialIntent);

            }

            //web
            else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);

            }


            //maps

            String uriMaps = new String(result.getContents());
            String maps = "http://maps.google.com/maps?q=bekasi:" + uriMaps;
            String testDoubleData1 = ",";
            String testDoubleData2 = ".";

            boolean b = uriMaps.contains(testDoubleData1) && uriMaps.contains(testDoubleData2);
            if (b) {
                Intent mapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(maps));
                mapsIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapsIntent);

            }

            //email
            String email = new String(result.getContents());
            String at = "@";

            if (email.contains(at))

            {
                Intent mail = new Intent(Intent.ACTION_SEND);
                String[] recipients = {email.replace("http://","")};
                mail.putExtra(Intent.EXTRA_EMAIL, recipients);
                mail.putExtra(Intent.EXTRA_SUBJECT, "Subject Email");
                mail.putExtra(Intent.EXTRA_TEXT, "Type Here");
                mail.putExtra(Intent.EXTRA_CC, "");
                mail.setType("text/html");
                mail.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(mail, "Send mail"));
            }


            else{

                try {
                    //json
                    JSONObject obj = new JSONObject(result.getContents());

                    textViewName.setText(obj.getString("nama"));
                    textViewClass.setText(obj.getString("kelas"));
                    textViewNim.setText(obj.getString("nim"));

                }

                catch (JSONException e) {
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
        //initialising scanning qr code
        qrScan.initiateScan();
    }


}
