package com.example.callback.workClass;

import com.example.callback.callBackInterface.StartFinishCallBackInterface;

/**
 * Created by lenovo on 2018-4-27.
 */
public class WorkClass {

    public void write(final StartFinishCallBackInterface callBackInterface) {
        callBackInterface.onStartWork();
        try {
            Thread.sleep(5000); // do some thing cost a lot of time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callBackInterface.onFinishWork();
    }
}
