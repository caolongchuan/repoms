package cn.reebtech.repoms.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by shq on 2017/5/17.
 * 这里是监听pc端传输数据的端口
 */
public class SocketService extends Service {

    public static final String TAG = "shq";
    public static Boolean mainThreadFlag = true;
    public static Boolean ioThreadFlag = true;
    ServerSocket serverSocket = null;
    final int SERVER_PORT = 12580;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"androidService--onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"androidService--onStartCommand()");
        mainThreadFlag = true;
        new Thread(){
            @Override
            public void run() {
                doListen();
            }
        }.start();
        return START_NOT_STICKY;
    }

    /**
     * 开启子线程监听端口
     */
    private void doListen() {
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            Log.e(TAG,"doListen()");
            while (mainThreadFlag){
                Socket socket = serverSocket.accept();
                Log.e(TAG,"doListen()mainThread");
                new Thread(new ThreadReadWriterIOSocket(this, socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭线程
        mainThreadFlag = false;
        ioThreadFlag = false;
        Log.e(TAG, Thread.currentThread().getName() + "--"
                + "serverSocket.close()");
        //关闭服务
        if (serverSocket != null){
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, Thread.currentThread().getName() + "--onDestroy()");
    }
}