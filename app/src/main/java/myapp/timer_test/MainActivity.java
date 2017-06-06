package myapp.timer_test;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    WebView wv1;

    Button btn_start_main_timer;
    Button btn_stop_main_timer;
    Button btn_click_counter;

    TextView my_text_log;
    boolean timer_enabled = false;

    private PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start_main_timer = (Button) findViewById(R.id.b_timer_main_start);
        btn_stop_main_timer = (Button) findViewById(R.id.b_timer_main_stop);
        btn_click_counter = (Button) findViewById(R.id.b_3);

        my_text_log = (TextView) findViewById(R.id.text_log);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");

        wv1 = (WebView) findViewById(R.id.webview_main);
        wv1.setWebChromeClient(new WebChromeClient());
        wv1.getSettings().setJavaScriptEnabled(true);

    }

    //Timer
    int main_counter = 0;
    int total_count = 0;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            if (main_counter >= 10) {main_counter = 0;} else main_counter = main_counter + 1;
            btn_click_counter.setText(String.valueOf(main_counter));

            total_count = total_count+1;

            String wakelock_status = " wl OFF";
            if(wl.isHeld()) wakelock_status = " wl ON";

            my_text_log.setText(String.valueOf(total_count)+wakelock_status);

      /* and here comes the "trick" */
            if(timer_enabled){
                handler.postDelayed(this, 1000);
            } else{
                btn_start_main_timer.setEnabled(true);
                btn_start_main_timer.setBackgroundResource(android.R.drawable.btn_default);
                wl.release();
            }
        }
    };

    //Button clicks
    public void buttonOnClick(View view) {
        int the_id = view.getId();

        if (the_id == R.id.b_timer_main_start) {
            if(timer_enabled == false) {
                btn_start_main_timer = (Button) findViewById(the_id);
                btn_start_main_timer.setBackgroundColor(Color.GREEN);
                btn_start_main_timer.setEnabled(false);

                timer_enabled = true;
                handler.postDelayed(runnable, 100);
                wl.acquire();
            }
        }
        if (the_id == R.id.b_timer_main_stop) {
            timer_enabled = false;
            btn_stop_main_timer.setBackgroundColor(Color.MAGENTA);
        }
        if (the_id == R.id.b_1){
            wv1.loadUrl("http://www.autojacker.com");
        }
        if (the_id == R.id.b_2){

            wv1.evaluateJavascript("(function(){return document.getElementById('secretcode').value})();",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String html) {
                        my_text_log.setText(html);
                        }
                    });
        }
    }
}
