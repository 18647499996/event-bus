package com.liudonghan.event_bus;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liudonghan.event.ADBus;
import com.liudonghan.event.ADEvent;
import com.liudonghan.event.ADEventSubscriber;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import rx.Observable;


public class MainActivity extends AppCompatActivity {

    private Button button;
    private Observable<ADEvent> tag;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.activity_main_tv);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ADBus.get().post("TAG",new ADEvent(1,"我通知更新了"));
            }
        });

//        BaseMain.getInstance()
//                .baseUserService()
//                .login()
//                .subscribe(new Observer<UserModel>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull UserModel userModel) {
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//        io.reactivex.rxjava3.core.Observable.unsafeCreate(new io.reactivex.rxjava3.core.Observable<Object>() {
//            @Override
//            protected void subscribeActual(@NonNull Observer<? super Object> observer) {
//
//            }
//        }).subscribe(new Observer<Object>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(@NonNull Object o) {
//
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

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