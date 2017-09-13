package com.sen.bluetooth.bre.sockets;

import com.sen.bluetooth.Error;
import com.sen.bluetooth.bre.DataPackage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 陈森华 on 2017/8/24.
 * 功能：用一句话描述
 */

 class SendDataThread extends Thread {
    private boolean isActive;
    private OutputStream outputStream = null;
    private Queue<DataPackage> queue;

    public boolean isActive() {
        return isActive;
    }

    public SendDataThread(OutputStream outputStream) {
        this.outputStream = outputStream;
        setName("SendDataThread");
        queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        isActive = true;
        try {
            while (isActive) {
                if (queue.isEmpty()) {
                    Thread.sleep(100);
                } else {
                    outputStream.write(queue.peek().getData());
                    queue.poll().getSendResponse().onRespond(Error.DATA_SEND_SUCCESS);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            isActive = false;
        } catch (IOException e) {
            e.printStackTrace();
            queue.poll().getSendResponse().onRespond(Error.DATA_SEND_FAILED);
        }
    }

    void sendData(DataPackage dataPackage) {
        queue.add(dataPackage);
    }
}