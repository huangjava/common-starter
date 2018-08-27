package com.yihu.base.router.rule.manager;

import com.yihu.base.router.model.RuleRoute;
import com.yihu.base.router.rule.storage.IRuleStorage;

import java.util.List;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public class SimpleRuleManager extends AbstractRuleManager {

    private IRuleStorage ruleStorage;

    @Override
    public List<RuleRoute> getAllRules() {
        return ruleStorage.listRuleStorage();
    }

    public IRuleStorage getRuleStorage() {
        return ruleStorage;
    }

    public void setRuleStorage(IRuleStorage ruleStorage) {
        this.ruleStorage = ruleStorage;
    }
}
