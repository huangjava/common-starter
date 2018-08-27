package com.yihu.admin.server.websocket.store;

import com.yihu.admin.server.properties.AdminServerProperties;
import com.yihu.base.es.config.ElastricSearchHelper;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by chenweida on 2018/5/23 0023.1
 */
public class ElasticSearchStore implements EventStore {

    private Logger logger = LoggerFactory.getLogger(ElasticSearchStore.class);


    private EsEventBuffer esEventBuffer;


    @Override
    public Boolean saveEvent(JSONObject jo) {
        try {
            esEventBuffer.addLogEvent(jo.toString());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }


    public EsEventBuffer getEsEventBuffer() {
        return esEventBuffer;
    }

    public void setEsEventBuffer(EsEventBuffer esEventBuffer) {
        this.esEventBuffer = esEventBuffer;
    }
}
