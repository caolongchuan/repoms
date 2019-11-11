package cn.reebtech.repoms.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceBroadcastReceiver extends BroadcastReceiver {

    private static String START_ACTION = "NotifyServiceStart";
    private static String STOP_ACTION = "NotifyServiceStop";
    private static boolean running = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!running && START_ACTION.equalsIgnoreCase(action)){
            running = true;
            context.startService(new Intent(context, SocketService.class));
            Log.e(SocketService.TAG,Thread.currentThread().getName()+"------>"
                    + "onReceive start");
        }else if (running && STOP_ACTION.equalsIgnoreCase(action)){
            running = false;
            context.stopService(new Intent(context, SocketService.class));
            Log.e(SocketService.TAG,Thread.currentThread().getName()+"------>"
                    + "onReceive stop");
        }
    }
}
