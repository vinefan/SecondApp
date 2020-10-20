package cn.swufe.vine.secondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity implements TextWatcher {

    TextView text_currency;
    TextView text_exchanged_money;
    TextView text_input_money;
    Double currency_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_of_item);
        Intent intent = getIntent();

        text_currency = findViewById(R.id.info_currency);
        text_input_money = findViewById(R.id.info_input);
        text_exchanged_money = findViewById(R.id.info_money);

        String currency = intent.getStringExtra("currency");
        String rate = intent.getStringExtra("rate");

        text_currency.setText(currency);
        currency_rate = Double.parseDouble(rate);

        text_input_money.addTextChangedListener(this);


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String content = text_input_money.getText().toString();
        if (content == ""){
            return;
        }else {
            try {
                double rmb = Double.parseDouble(content);
                double money = rmb/currency_rate;
                content = String.valueOf(money);
                text_exchanged_money.setText(content);
            }catch (Exception e){
                text_exchanged_money.setText("请输入数字 O\\.\\O ~");

            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}