package io.tatsuki.wifiscan;

import android.app.Activity;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private long startTime;
    private long stopTime;
    Some_param Someparam = new Some_param();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
        if(manager.isWifiEnabled()) {

        } else {
            Toast.makeText(getApplicationContext(),"WiFiがOFFになっています。", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //Log.d("TouchEvent", "X:" + event.getX() + ",Y:" + event.getY());

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
                // 変更したいレイアウトを取得する(mainとthirdのLinearLayoutのIDをあえて同じにする)
                LinearLayout layout = (LinearLayout)findViewById(R.id.layout1);
                // レイアウトのビューをすべて削除する
                layout.removeAllViews();
                // レイアウトをR.layout.secondに変更する
                getLayoutInflater().inflate(R.layout.second, layout);

                // WiFiManagerのインスタンス
                WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
                if(manager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                    // WiFiスポットの検索
                    manager.startScan();
                    // 検索結果が0件でなければ
                    if (manager.getScanResults() != null) {
                        // スキャン結果を格納する配列(検索結果分)
                        String[] result = new String[manager.getScanResults().size()];
                        String[] ssid = new String[manager.getScanResults().size()];
                        int[] frequency = new int[manager.getScanResults().size()];
                        int[] level = new int[manager.getScanResults().size()];
                        // SSIDなどを格納
                        // for (int i=0; i < manager.getScanResults().size; i++) {
                        for (int i = 0; i < result.length; i++) {
                            ssid[i] = manager.getScanResults().get(i).SSID;
                            frequency[i] = manager.getScanResults().get(i).frequency;
                            level[i] = manager.getScanResults().get(i).level;
                            //result[i] = String.format("SSID:%s\n周波数:%sch\n信号レベル:%sdBm", manager.getScanResults().get(i).SSID, manager.getScanResults().get(i).frequency, manager.getScanResults().get(i).level);
                            result[i] = String.format("SSID:%s\n周波数:%sch\n信号レベル:%sdBm", ssid[i], frequency[i], level[i]);
                        }
                        Someparam.setResult(result);
                        Someparam.setFrequency(frequency);
                        Someparam.setLevel(level);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                // タップ時間を計測し、円弧が一周しなければ更新しない
                stopTime = System.currentTimeMillis();
                long time = stopTime - startTime;
                int second = (int) (time/1000);
                int comma = (int) (time % 1000);
                //Log.d("Second", String.valueOf(second));
                //Toast.makeText(getApplication(), String.valueOf(second), Toast.LENGTH_SHORT).show();
                Log.d("TouchEvent", "getAction()" + "ACTION_UP");

                if (second >= 3) {
                    // 以下はthrid.xmlをjavaで書き直したもの
                    LinearLayout linearLayout = new LinearLayout(this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    setContentView(linearLayout);

                    final LinearLayout layout1 = new LinearLayout(this);               // 画面上半分のLinearLayout
                    layout1.setId(R.id.layout1);

                    // この形式ならresultを受け取れる
                    // ゆえにxml形式ではなくjavaで直接レイアウトを記述する(10/18)
                    // viewのインスタンス
                    final MarkSurfaceView mark = new MarkSurfaceView(this);
                    mark.SomeParam.setFrequency(Someparam.getFrequency());
                    mark.SomeParam.setLevel(Someparam.getLevel());

                    layout1.addView(mark);

                    LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    param1.weight = 1.0f;
                    linearLayout.addView(layout1, param1);

                    LinearLayout layout2 = new LinearLayout(this);               // 画面下半分のLinearLayout
                    layout2.setOrientation(LinearLayout.VERTICAL);
                    layout2.setId(R.id.layout2);
                    layout2.setBackgroundColor(Color.BLACK);

                    TextView text1 = new TextView(this);
                    LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    text_params.setMargins(15, 0, 0, 0);
                    text1.setLayoutParams(text_params);
                    text1.setText("WiFi一覧");
                    text1.setTextColor(Color.GREEN);
                    text1.setTextSize(16);

                    // WiFiリスト
                    final ListView wifi_list = new ListView(this);
                    wifi_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    if (Someparam.getResult() != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.row_textview1, Someparam.getResult());
                        // リストにアダプターセット
                        wifi_list.setAdapter(adapter);

                        wifi_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //ここに処理を書く
                                String item = (String) wifi_list.getItemAtPosition(position);
                                // 以下で文字列操作
                                String[] strs = item.split("\n");

                                int ssid_index = strs[0].indexOf(":");
                                int frequency_index = strs[1].indexOf(":");
                                int level_index = strs[2].indexOf(":");
                                String ssid_name = strs[0].substring(ssid_index+1);
                                String fre_str = strs[1].substring(frequency_index+1);
                                String level_str = strs[2].substring(level_index+1);

                                String[] fre_strs = fre_str.split("c");
                                int fre_value = Integer.valueOf(fre_strs[0]);

                                String[] level_strs = level_str.split("d");
                                int level_value = Integer.valueOf(level_strs[0]);

                                //Log.v("OnItemClick", item);
                                //Log.v("OnItemClick", ssid_name);
                                //Log.v("OnItemClick", String.valueOf(fre_value));
                                //Log.v("OnItemClick", String.valueOf(level_value));
                                // MarkSurfaceViewを削除
                                layout1.removeView(mark);
                                mark.SomeParam.setFre_value(fre_value);
                                mark.SomeParam.setLevel_value(level_value);
                                // MarkSurfaceViewを追加＝更新
                                layout1.addView(mark);
                            }
                        });

                        /* 保留(2015/10/24)
                        // リストが長押しされた時
                        wifi_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                //ここに処理を書く(コンテキストメニュー表示)
                                ListView listView = (ListView) parent;
                                registerForContextMenu(listView);
                                String item = (String) wifi_list.getItemAtPosition(position);
                                // 以下で文字列操作
                                String[] strs = item.split("\n");
                                Someparam.setStr_ssid(strs[0]);
                                return false;
                            }
                        });
                        */
                    }

                    layout2.addView(text1);
                    layout2.addView(wifi_list);

                    LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    param2.weight = 1.0f;
                    linearLayout.addView(layout2, param2);

                } else {
                    Toast.makeText(getApplicationContext(), "タップ時間が短すぎます。", Toast.LENGTH_SHORT).show();
                    setContentView(R.layout.third);
                    // リストビューのインスタンス
                    //final ListView wifi_list = (ListView) findViewById(R.id.third_wifi_list);
                    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row, R.id.row_textview1, Someparam.getResult());
                    // リストにアダプターセット
                    //wifi_list.setAdapter(adapter);
                }
                break;
        }
       return true;
    }
    /* 保留(2015/10/24)
    // コンテキストメニュー
    @Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context, menu);
        menu.setHeaderTitle(Someparam.getStr_ssid());
    }
    */
}

