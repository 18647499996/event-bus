package com.liudonghan.event_bus;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.reactivex.rxjava3.core.Observable;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/11/23
 */
public class BaseMain {

    private static volatile BaseMain instance = null;

    private BaseMain() {
    }

    public static BaseMain getInstance() {
        //single chcekout
        if (null == instance) {
            synchronized (BaseMain.class) {
                // double checkout
                if (null == instance) {
                    instance = new BaseMain();
                }
            }
        }
        return instance;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public UserService baseUserService() {
        return init(UserService.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private UserModel baseUserModel() {
        return init(UserModel.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private <T> T init(final Class<T> userServiceClass) {
        return (T) Proxy.newProxyInstance(
                userServiceClass.getClassLoader(),
                new Class<?>[]{userServiceClass},
                (o, method, objects) -> {
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    return lookup.unreflectSpecial(method, userServiceClass).bindTo(o).invokeWithArguments(objects);
                });
    }
}
