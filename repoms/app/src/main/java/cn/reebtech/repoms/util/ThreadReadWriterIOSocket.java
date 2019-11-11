package cn.reebtech.repoms.util;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import cn.reebtech.repoms.model.greendao.Order_InDao;

/**
 * Created by shq on 2017/5/17.
 */
public class ThreadReadWriterIOSocket implements Runnable{

    private Socket client;
    private Context context;
    private byte[] bufferdIn;

    public ThreadReadWriterIOSocket(Context context, Socket client) {
        this.client = client;
        this.context = context;
        try{
            this.client.setTcpNoDelay(true);
            // 设置客户端 socket 关闭时，close() 方法起作用时延迟 30 秒关闭，如果 30 秒内尽量将未发送的数据包发送出去
            this.client.setSoLinger(true, 30);
            // 设置输出流的发送缓冲区大小，默认是4KB，即4096字节
            this.client.setSendBufferSize(4096);
            // 设置输入流的接收缓冲区大小，默认是4KB，即4096字节
            this.client.setReceiveBufferSize(40960);
            // 作用：每隔一段时间检查服务器是否处于活动状态，如果服务器端长时间没响应，自动关闭客户端socket
            // 防止服务器端无效时，客户端长时间处于连接状态
            this.client.setKeepAlive(true);
        }
        catch(Exception e){}
    }

    @Override
    public void run() {
        Log.e(SocketService.TAG, "a client has connected to server!");
        BufferedOutputStream out;
        BufferedInputStream in;
        /*pc端发来的数据msg*/
        String currCMD = "";
        try {
            out = new BufferedOutputStream(client.getOutputStream());
            in = new BufferedInputStream(client.getInputStream());
            SocketService.ioThreadFlag = true;
            while (SocketService.ioThreadFlag){
                if (!client.isConnected()){
                    break;
                }
                /*接收pc端发来的数据*/
                Log.e(SocketService.TAG, Thread.currentThread().getName()
                        + "--is readingData......");
                /*读取的操作*/
                try{
                    currCMD = readCMDFromSocket(in);
                    Log.e(SocketService.TAG, Thread.currentThread().getName()
                            + "---->" + "**currCMD ==== " + currCMD);
                    /*根据cmd命令处理数据*/
                    if (currCMD.equals("1")||currCMD.equals("2")||currCMD.equals("3")) {
                        out.write("OK".getBytes());
                        out.flush();
                    }else if (currCMD.equals("4")){
                        /**
                         * 这里将json对象传输到pc端
                         */
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name","android");
                        map.put("img","to");
                        map.put("op","pc");
                        //将json转化为String类型
                        JSONObject json = new JSONObject(map);
                        String jsonString = "";
                        jsonString = json.toString();
                        //将String转化为byte[]
                        byte[] jsonByte = jsonString.getBytes();
                        out.write(jsonByte);
                        out.flush();
                    }else if (currCMD.equalsIgnoreCase("exit")){
                        out.write("exit ok".getBytes());
                        out.flush();
                    }
                    else if(currCMD.equalsIgnoreCase("dlvalid")){
                        //检查是否有未上传的数据
                        DataSyncUtils util = new DataSyncUtils();
                        if(util.chkDlvalid()){
                            out.write("valid".getBytes());
                            out.flush();
                        }
                        else{
                            out.write("invalid".getBytes());
                            out.flush();
                        }
                    }
                    else if(currCMD.equalsIgnoreCase("complete")){
                        String msg = new String(bufferdIn, 0, bufferdIn.length, "UTF-8");
                        JSONObject json = new JSONObject(msg);
                        JSONArray data = json.getJSONArray("result");
                        Log.i("complete rec data-" + bufferdIn.length + "字节", "记录条数：" + data.length() + "");
                        new DataSyncUtils().saveDownloadData(json);
                    }
                    else if(currCMD.equalsIgnoreCase("order_in:OK")){
                        //更新入库单
                        new DataSyncUtils().updateOrderList("order_in");
                        Log.i("updateData", currCMD);
                        //Log.i("data length","rec data length" + currCMD.length());
                        out.write("OK".getBytes());
                        out.flush();
                    }
                    else if(currCMD.equalsIgnoreCase("order_out:OK")){
                        //更新出库单
                        new DataSyncUtils().updateOrderList("order_out");
                        Log.i("updateData", currCMD);
                        //Log.i("data length","rec data length" + currCMD.length());
                        out.write("OK".getBytes());
                        out.flush();
                    }
                    else if(currCMD.equalsIgnoreCase("order_invt:OK")){
                        //更新盘点单
                        new DataSyncUtils().updateOrderList("order_invt");
                        Log.i("updateData", currCMD);
                        //Log.i("data length","rec data length" + currCMD.length());
                        out.write("OK".getBytes());
                        out.flush();
                    }
                    else if(currCMD.equalsIgnoreCase("order_in")){
                        //读取数据
                        DataSyncUtils dataSyncUtils = new DataSyncUtils();
                        JSONArray data = dataSyncUtils.readUploadData("order_in");
                        //发送
                        String jsonString = data.toString();
                        byte[] jsonBytes = jsonString.getBytes("UTF-8");
                        out.write(jsonBytes);
                        out.flush();
                        //更新数据
                        //dataSyncUtils.updateData("order_in");
                        //Log.i("hello", "hello");
                    }
                    else if(currCMD.equalsIgnoreCase("order_out")){
                        //读取数据
                        DataSyncUtils dataSyncUtils = new DataSyncUtils();
                        JSONArray data = dataSyncUtils.readUploadData("order_out");
                        //发送
                        String jsonString = data.toString();
                        byte[] jsonBytes = jsonString.getBytes("UTF-8");
                        out.write(jsonBytes);
                        out.flush();
                        //更新数据
                        //dataSyncUtils.updateData("order_out");
                        //Log.i("hello", "hello");

                    }
                    else if(currCMD.equalsIgnoreCase("order_invt")){
                        //读取数据
                        DataSyncUtils dataSyncUtils = new DataSyncUtils();
                        JSONArray data = dataSyncUtils.readUploadData("order_invt");
                        //发送
                        String jsonString = data.toString();
                        byte[] jsonBytes = jsonString.getBytes("UTF-8");
                        out.write(jsonBytes);
                        out.flush();
                        //更新数据
                        //dataSyncUtils.updateData("order_invt");
                        //Log.i("hello", "hello");

                    }
                    else{
                        Log.i("rec data", currCMD);
                        //Log.i("data length","rec data length" + currCMD.length());
                        out.write("OK".getBytes());
                        out.flush();
                    }
                }
                catch(Exception e){
                    Log.i("socket error:", e.getMessage());
                }

            }
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (client != null) {
                    Log.e(SocketService.TAG, Thread.currentThread().getName()
                            + "--client.close()");
                    client.close();
                }
            } catch (IOException e) {
                Log.e(SocketService.TAG, Thread.currentThread().getName()
                        + "--read write error");
                e.printStackTrace();
            }
        }
    }
    /**
     * 读取命令
     * @param in
     * @return
     */
    private String readCMDFromSocket(BufferedInputStream in) {
        int MAX_BUFFER_BYTES = 4096;
        String msg = "";
        byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
        try {
            int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
            Log.i("rec bytes","rec bytes:" + numReadedBytes);
            if(numReadedBytes != -1){
                int bufferdCount = bufferdIn == null ? 0 : bufferdIn.length;
                byte[] tmpBufferd = new byte[numReadedBytes + bufferdCount];
                if(bufferdCount > 0){
                    System.arraycopy(bufferdIn, 0, tmpBufferd, 0, bufferdCount);
                }
                System.arraycopy(tempbuffer, 0, tmpBufferd, bufferdCount, numReadedBytes);
                bufferdIn = new byte[tmpBufferd.length];
                //Log.i("新长度", tmpBufferd.length+"");
                System.arraycopy(tmpBufferd, 0, bufferdIn, 0, tmpBufferd.length);
                msg = new String(tempbuffer,0, numReadedBytes, "UTF-8");
                tempbuffer = null;
                //Log.e(SocketService.TAG, Thread.currentThread().getName()+ "--readFromSocket error");
                //保存到数据库中


            }
            else{
                msg = "complete";
                SocketService.ioThreadFlag = false;
                Log.i("complete", "recComplete");
            }
        } catch (IOException e) {
            Log.e(SocketService.TAG, Thread.currentThread().getName()
                    + "--readFromSocket error" + e.getMessage());
            e.printStackTrace();
        }
        return msg;
    }
}
