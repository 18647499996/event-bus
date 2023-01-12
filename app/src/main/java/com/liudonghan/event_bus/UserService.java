package com.liudonghan.event_bus;

import io.reactivex.rxjava3.core.Observable;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/11/23
 */
public interface UserService {

    Observable<UserModel> login();
}
