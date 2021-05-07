package com.huang.nettysync.client;

/**
 * @author: hsz
 * @date: 2021/5/7 09:21
 * @description:
 */

public interface NettyClientService {

    boolean sendMsg(String text, String dataId, String serviceId);

    String sendSyncMsg(String text, String dataId, String serviceId);

    void ackSyncMsg(String msg);
}
