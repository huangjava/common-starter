package com.yihu.base.router.ratelimit.storage;

import com.yihu.base.router.model.Ratelimit;

import java.util.List;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public interface IRateLimitStorage {
    /**
     * 新增限制规则
     * @param ratelimit
     * @return
     */
    Boolean saveRateLimit(Ratelimit ratelimit);

    /**
     * 修改限制规则
     * @param ratelimit
     * @return
     */
    Boolean updateRateLimit(Ratelimit ratelimit);

    /**
     * 根据id删除限制规则
     * @param id
     * @return
     */
    Boolean deleteRateLimitById(String id);

    /**
     * 根据id查找限制规则
     * @param id
     * @return
     */
    Ratelimit findRateLimitById(String id);

    /**
     * 获取全部的限制规则
     * @return
     */
    List<Ratelimit> findAllRatelimit();

    /**
     * 总数-1
     * @param id
     * @return
     */
    Boolean countDec(String id,String requestIP);
    /**
     * 获取次数
     * @param id
     * @return
     */
    Integer getCount(String id,String requestIP);

    /**
     * 判断是否超过次数
     * @param id
     * @return
     */
    Boolean isUpperLimit(String id,String requestIP);

}
