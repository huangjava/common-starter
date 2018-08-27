package com.yihu.base.router.ratelimit.storage;

import com.yihu.base.router.model.Ratelimit;

import java.util.List;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public class AbstractRateLimitStorage implements IRateLimitStorage {
    @Override
    public Boolean saveRateLimit(Ratelimit ratelimit) {
        return null;
    }


    @Override
    public Boolean updateRateLimit(Ratelimit ratelimit) {
        return null;
    }

    @Override
    public Boolean deleteRateLimitById(String id) {
        return null;
    }

    @Override
    public Ratelimit findRateLimitById(String id) {
        return null;
    }

    @Override
    public List<Ratelimit> findAllRatelimit( ) {
        return null;
    }

    @Override
    public Boolean countDec(String id,String requestIP) {
        return null;
    }

    @Override
    public Integer getCount(String id,String requestIP) {
        return null;
    }

    @Override
    public Boolean isUpperLimit(String id,String requestIP) {
        return null;
    }


}
