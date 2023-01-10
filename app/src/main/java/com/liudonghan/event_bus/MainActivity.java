package com.liudonghan.event_bus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liudonghan.event.ADBus;
import com.liudonghan.event.ADEvent;
import com.liudonghan.event.ADEventSubscriber;

import rx.Observable;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Observable<ADEvent> tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.activity_main_tv);
        button.setOnClickListener(view -> ADBus.get().post("TAG",new ADEvent(1,"我通知更新了")));

        tag = ADBus.get().register("TAG", new ADEventSubscriber<ADEvent>() {
            @Override
            public void onEvent(ADEvent adEvent) {
                button.setText((String) adEvent.obj);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ADBus.get().unregister("TAG",tag);
    }
}