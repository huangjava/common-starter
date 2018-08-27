package com.yihu.admin.server.websocket.store;

import net.sf.json.JSONObject;

/**
 * Created by chenweida on 2018/5/23 0023.1
 */
public interface EventStore {

    Boolean saveEvent(JSONObject jo);
}
