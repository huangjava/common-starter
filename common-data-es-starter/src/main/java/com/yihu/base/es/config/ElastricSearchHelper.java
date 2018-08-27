package com.yihu.base.es.config;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yihu.base.es.config.model.ESIDEntity;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

;

/**
 * Created by chenweida on 2017/6/2.
 */
public class ElastricSearchHelper {

    private ElasticsearchUtil elasticsearchUtil;

    private Logger logger = LoggerFactory.getLogger(ElastricSearchHelper.class);
    @Autowired
    private ElasticFactory elasticFactory;

    /**
     * 新增
     *
     * @param index
     * @param type
     * @param sms
     * @return
     */
    public Boolean save(String index, String type, List<Object> sms) {
        JestClient jestClient = null;
        try {
            //得到链接elasticFactory.getJestClient();
            jestClient = elasticFactory.getJestClient();
            int success = 0;
            int error = 0;
            Bulk.Builder bulk = new Bulk.Builder().defaultIndex(index).defaultType(type);
            for (Object obj : sms) {
                try {
                    Index indexObj = new Index.Builder(obj).build();
                    success++;
                    bulk.addAction(indexObj);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    error++;
                }
            }
            BulkResult br = jestClient.execute(bulk.build());
            logger.debug("save flag:" + br.isSucceeded());
            logger.debug("save success:" + success);
            logger.debug("save error:" + error);
            return br.isSucceeded();
        } catch (Exception e) {
            logger.error(" save error ：" + e.getMessage());
        } finally {
            if (jestClient != null) {
                jestClient.shutdownClient();
            }
        }
        return null;
    }

    public Boolean save(String index, String type, String source) {
        JestClient jestClient = null;
        BulkResult br = null;
        try {
            //得到链接elasticFactory.getJestClient();
            jestClient = elasticFactory.getJestClient();
            int success = 0;
            int error = 0;
            Bulk.Builder bulk = new Bulk.Builder().defaultIndex(index).defaultType(type);
            try {
                Index indexObj = new Index.Builder(source).build();
                success++;
                bulk.addAction(indexObj);
            } catch (Exception e) {
                logger.error(e.getMessage());
                error++;
            }
            br = jestClient.execute(bulk.build());
            logger.debug("save flag:" + br.isSucceeded());
            logger.debug("save success:" + success);
            logger.debug("save error:" + error);
            return br.isSucceeded();
        } catch (Exception e) {
            logger.error(" save error ：" + e.getMessage());
        } finally {
            if (jestClient != null) {
                jestClient.shutdownClient();
            }
        }
        return br.isSucceeded();
    }

    /**
     * 自定义ID
     *
     * @param index
     * @param type
     * @param source
     * @param idFieldString
     * @return
     */
    public Boolean saveWithCustomId(String index, String type, String source, String idFieldString) {
        JestClient jestClient = null;
        BulkResult br = null;
        try {
            //得到链接elasticFactory.getJestClient();
            jestClient = elasticFactory.getJestClient();
            int success = 0;
            int error = 0;
            Bulk.Builder bulk = new Bulk.Builder().defaultIndex(index).defaultType(type);
            try {
                JSONObject jsonObject = (JSONObject) (JSONObject.parse(source));
                Index indexObj = new Index.Builder(source).id(jsonObject.getString(idFieldString)).
                        build();
                success++;
                bulk.addAction(indexObj);
            } catch (Exception e) {
                logger.error(e.getMessage());
                error++;
            }
            br = jestClient.execute(bulk.build());
            logger.debug("save flag:" + br.isSucceeded());
            logger.debug("save success:" + success);
            logger.debug("save error:" + error);
            return br.isSucceeded();
        } catch (Exception e) {
            logger.error(" save error ：" + e.getMessage());
        } finally {
            if (jestClient != null) {
                jestClient.shutdownClient();
            }
        }
        return br.isSucceeded();
    }

    /**
     * 自定义ID
     *
     * @param index
     * @param type
     * @param sources
     * @param idFieldString
     * @return
     */
    public Boolean saveBulkWithCustomId(String index, String type, List<String> sources, String idFieldString) {
        JestClient jestClient = null;
        BulkResult br = null;
        try {
            //得到链接elasticFactory.getJestClient();
            jestClient = elasticFactory.getJestClient();
            int success = 0;
            int error = 0;
            Bulk.Builder bulk = new Bulk.Builder().defaultIndex(index).defaultType(type);
            try {
                for (String source : sources) {
                    JSONObject jsonObject = (JSONObject) (JSONObject.parse(source));
                    Index indexObj = new Index.Builder(source).id(jsonObject.getString(idFieldString)).
                            build();
                    success++;
                    bulk.addAction(indexObj);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                error++;
            }
            br = jestClient.execute(bulk.build());
            logger.debug("save flag:" + br.isSucceeded());
            logger.debug("save success:" + success);
            logger.debug("save error:" + error);
            return br.isSucceeded();
        } catch (Exception e) {
            logger.error(" save error ：" + e.getMessage());
        } finally {
            if (jestClient != null) {
                jestClient.shutdownClient();
            }
        }
        return br.isSucceeded();
    }
    /**
     * 更新
     *
     * @param index
     * @param type
     * @param sms
     * @return
     */
    public Boolean update(String index, String type, List<Object> sms) {
        JestClient jestClient = null;
        BulkResult br = null;
        try {
            //得到链接
            jestClient = elasticFactory.getJestClient();

            int success = 0;
            int error = 0;
            boolean isSuccessed = true;
            Bulk.Builder bulk = new Bulk.Builder().defaultIndex(index).defaultType(type);
            for (Object obj : sms) {
                try {
                    JSONObject jo = new JSONObject();
                    jo.put("doc", obj);
                    Update indexObj = new Update.Builder(jo.toString()).index(index).type(type).id(((ESIDEntity) obj).getId()).build();
                    bulk.addAction(indexObj);
                    success++;
                } catch (Exception e) {
                    error++;
                    isSuccessed = false;
                }
            }

            br = jestClient.execute(bulk.build());
            logger.debug("update flag:" + br.isSucceeded());
            logger.debug("update success:" + success);
            logger.debug("update error:" + error);
            return isSuccessed;
        } catch (Exception e) {
            logger.error(" update error ：" + e.getMessage());
        } finally {
            if (jestClient != null) {
                jestClient.shutdownClient();
            }
        }
        return br.isSucceeded();
    }


    /**
     * 跟新
     *
     * @param index
     * @param type
     * @param _id
     * @param source
     * @return
     */
    public boolean update(String index, String type, String _id, JSONObject source) {
        JestClient jestClient = null;
        JestResult jestResult = null;
        try {
            jestClient = elasticFactory.getJestClient();
            JSONObject docSource = new JSONObject();
            docSource.put("doc", source);
            Update update = new Update.Builder(docSource).index(index).type(type).id(_id).build();
            jestResult = jestClient.execute(update);
            logger.debug("update info:" + jestResult.isSucceeded());
        } catch (Exception e) {
            logger.error("update fail:" + _id, e.getMessage());
            return false;
        } finally {
            if (jestClient != null) {
                jestClient.shutdownClient();
            }
        }
        return true;
    }

    /**
     * 删除
     */
    public boolean delete(String index, String type, List<Map<String, Object>> datas) {
        JestClient jestClient = null;
        BulkResult br = null;
        try {
            jestClient = elasticFactory.getJestClient();

            //根据id批量删除
            Bulk.Builder bulk = new Bulk.Builder().defaultIndex(index).defaultType(type);
            for (Map map : datas) {
                if (!map.containsKey("id") || !map.containsKey("_id")) {
                    continue;
                }
                Delete indexObj = null;
                if (null != map.get("_id")) {
                    indexObj = new Delete.Builder(map.get("_id").toString()).build();
                } else if (null != map.get("id")) {
                    indexObj = new Delete.Builder(map.get("id").toString()).build();
                }
                bulk.addAction(indexObj);
            }
            br = jestClient.execute(bulk.build());
            logger.debug("delete data count:" + datas.size());
            logger.debug("delete flag:" + br.isSucceeded());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jestClient != null) {
                jestClient.shutdownClient();
            }
        }
        return br.isSucceeded();
    }


    /**
     * 执行sql
     *
     * @param sql
     * @return
     */
    public List<Map<String, Object>> excuceSQL(String sql) {
        return elasticsearchUtil.excuteDataModel(sql);
    }

    /**
     * 执行sql
     *
     * @param sql
     * @return
     */
    public Integer excuceCountSQL(String sql) {
        return elasticsearchUtil.count(sql);
    }

    public ElasticsearchUtil getElasticsearchUtil() {
        return elasticsearchUtil;
    }

    public void setElasticsearchUtil(ElasticsearchUtil elasticsearchUtil) {
        this.elasticsearchUtil = elasticsearchUtil;
    }

}
