package com.yihu.admin.client.websocket.sender;


import com.yihu.admin.client.websocket.event.IEvent;

/**
 * Created by chenweida on 2018/5/23 0023.1
 */
public interface EventSender {

    Boolean send(IEvent event);
}
