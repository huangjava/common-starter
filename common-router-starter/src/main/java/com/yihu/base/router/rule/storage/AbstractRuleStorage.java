package com.yihu.base.router.rule.storage;

import com.yihu.base.router.model.RuleRoute;

import java.util.List;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public class AbstractRuleStorage implements IRuleStorage {
    @Override
    public Boolean saveRuleStorage(RuleRoute ruleRoute) {
        return null;
    }

    @Override
    public Boolean deleteRuleStorage(RuleRoute ruleRoute) {
        return null;
    }

    @Override
    public Boolean deleteRuleStorageById(String id) {
        return null;
    }

    @Override
    public Boolean updateRuleStorage(RuleRoute ruleRoute) {
        return null;
    }

    @Override
    public RuleRoute getRuleStorageById(String id) {
        return null;
    }

    @Override
    public List<RuleRoute> listRuleStorage() {
        return null;
    }

    @Override
    public Integer countRuleStorage() {
        return null;
    }
}
