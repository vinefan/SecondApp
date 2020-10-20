package cn.swufe.vine.secondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        TextView text_usd = findViewById(R.id.text_usd);
        TextView text_euro = findViewById(R.id.text_euro);
        TextView text_krw = findViewById(R.id.text_krw);
        String usd = intent.getStringExtra(MainActivity.KEY_USD);
        String euro = intent.getStringExtra(MainActivity.KEY_EURO);
        String krw = intent.getStringExtra(MainActivity.KEY_KRW);


        text_usd.setText(usd);
        text_euro.setText(euro);
        text_krw.setText(krw);

    }


}