package cn.swufe.vine.secondapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.swufe.vine.secondapp.dataBase.RateDataBase;

//public class MainActivity extends AppCompatActivity implements Runnable{
public class MainActivity extends ListActivity implements Runnable, AdapterView.OnItemClickListener {

    TextView textView1;
    public static String KEY_USD = "cn.swufe.vine.Secondapp.usd";
    public static String KEY_EURO = "cn.swufe.vine.Secondapp.euro";
    public static String KEY_KRW = "cn.swufe.vine.Secondapp.krw";
    public float [] rates = {0, 0, 0};
    public String[] exchange_rates = {"USD", "EUR", "KRW"};
    public Handler handler;
    public String TAG = "Good_Luck";
    Timer timer = new Timer();

    int UPDATE_TIME = 60*60*24*1000;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        装配从网上获得的信息
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 5){
//                    SharedPreferences sp = getSharedPreferences("rates", Activity.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putFloat("usd_rate", rates[0]);
//                    editor.putFloat("euro_rate", rates[1]);
//                    editor.putFloat("krw_rate", rates[2]);

                    List<HashMap<String, String>> ls_of_rate = (List<HashMap<String, String>>) msg.obj;
                    SimpleAdapter listItemAdapter = new SimpleAdapter(MainActivity.this,
                            ls_of_rate,
                            R.layout.item_of_ratelist,
                            new String[] {"currency", "rate"},
                            new int[] {R.id.item_currency_name, R.id.item_rate}
                    );

//                    ListAdapter adapter = new ArrayAdapter<String>(MainActivity.this,
//                            android.R.layout.simple_list_item_1,
//                            ls_of_rate);

                    setListAdapter(listItemAdapter);

                }else if (msg.what == 1){

                }
                super.handleMessage(msg);
            }

        };
//        创建一个新线程
        Thread t = new Thread(this);
//        开启线程，该线程等待操作系统调度执行run函数
        t.start();

//      给每一个item绑定事件
        getListView().setOnItemClickListener(this);


    }

    public void conversionOfExchangeRate(View btn_exchange) {
        textView1 = findViewById(R.id.editTextTextPersonName);

        if (textView1.getText() == null){
            // input is null
            Toast.makeText(this, "the first input is empty !", Toast.LENGTH_SHORT);
        }else {
            // log.i(TAG, " msg" )
            String str_cny = textView1.getText().toString();
            // 异常处理
            double cny = Float.parseFloat(str_cny);

            double exchanged_cny = 0.0;
            
            if (btn_exchange.getId() == R.id.btn_usd){
                exchanged_cny = cny*rates[0];
            }else if (btn_exchange.getId() == R.id.btn_euro){
                exchanged_cny = cny*rates[1];
            }else if (btn_exchange.getId() == R.id.btn_krw){
                exchanged_cny = cny*rates[2];
            }
            String str_exchanged_cny = String.valueOf(exchanged_cny);
            TextView textView2 = findViewById(R.id.text_of_exchanged);
            textView2.setText(str_exchanged_cny);


        }


    }

    public void configClick(View btn_config) {
//        intent作为两个activity之间的一种单向绑定，当然需要知道是哪两个activity要进行这种绑定
//        this ： 当前界面
//        第二个属性作为第二个界面，需要先被创建。
        Intent intent = new Intent(this, MainActivity2.class);
        textView1 = findViewById(R.id.editTextTextPersonName);
        String str_cny = textView1.getText().toString();
        // 异常处理
        double cny = Float.parseFloat(str_cny);
        double rate_of_usd = 0.1477;
        double rate_of_euro = 0.1256;
        double rate_of_krw = 171.3421;

        // intent只能传递成string类型？
        String intend_str_usd = Double.toString(rate_of_usd*cny);
        String intend_str_euro = Double.toString(cny*rate_of_euro);
        String intend_str_krw = Double.toString(cny*rate_of_krw);
        intent.putExtra(KEY_USD, intend_str_usd);
        intent.putExtra(KEY_EURO, intend_str_euro);
        intent.putExtra(KEY_KRW, intend_str_krw);
        startActivity(intent);


    }

//     从B返回到A，A如何做动作的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


// 爬取汇率方法
    public List<HashMap<String, String>> getRateFromWeb() throws IOException {
        String pre_url = "https://qq.ip138.com/hl.asp?from=REPLACE&to=CNY&q=100";
        StringBuilder root_of_url = new StringBuilder(pre_url);

        String replace_str = "REPLACE";

        List<HashMap<String, String>> list_of_rate = new ArrayList<HashMap<String, String>>();

        for (int i=0; i<exchange_rates.length; i++){
            int index_start = root_of_url.indexOf(replace_str);
            int index_end = index_start + replace_str.length();
            root_of_url.replace(index_start, index_end, exchange_rates[i]);
            String url = root_of_url.toString();
//               爬取信息, 提取汇率
            Document doc = (Document) Jsoup.connect(url).get();
            Elements trs = doc.select("tbody > tr");
            Element tr = trs.get(2);
            Elements tds = tr.getElementsByTag("td");
            Element td = tds.get(1);
            String str_rate = td.text();
//                存入汇率
            rates[i] = Float.parseFloat(str_rate);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("currency", exchange_rates[i]);
            map.put("rate", str_rate);
            list_of_rate.add(map);
//                ls_of_rates.add(str_rate);
//                修改替换字符串
            replace_str = exchange_rates[i];
        }

        return list_of_rate;
    }


// 将汇率存入本地数据库
    public void refreshRateDataBase(List<HashMap<String, String>> rateList){
        RateDataBase rdb = Room.databaseBuilder(getApplicationContext(),
                RateDataBase.class,
                "rateDataBase").build();

//        rdb.rateDao().insertAllRates(rateList);
    }

//    runnable必须实现的方法
    @Override
    public void run() {
//        取出handler
        Message rate_msg = handler.obtainMessage();
        try {
            List<HashMap<String, String>> list_of_rate = getRateFromWeb();
            // 消息标识
            rate_msg.what = 5;
            // 消息内容
            rate_msg.obj = list_of_rate;
            Log.i(TAG, Float.toString(rates[0]));
            // 将消息推送至消息栈
            handler.sendMessage(rate_msg);


        }catch (Exception e) {
            e.printStackTrace();
            rate_msg.what = 1;
            Log.i(TAG, "Worry !!!");
        }

        // 定时重新获取汇率
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message rate_msg = handler.obtainMessage();
                List<HashMap<String, String>> list_of_rate = null;
                try {
                    list_of_rate = getRateFromWeb();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rate_msg.what = 5;
                rate_msg.obj = list_of_rate;
                Log.i(TAG, Float.toString(rates[0]));
                handler.sendMessage(rate_msg);

            }
        };
        timer.schedule(timerTask, UPDATE_TIME);

    }


//    AdapterView.OnItemClickListener必须实现的方法
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object item = getListView().getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) item;
        String currency = map.get("currency");
        String rate = map.get("rate");

        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("currency", currency);
        intent.putExtra("rate", rate);
        startActivity(intent);
    }
}