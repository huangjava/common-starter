package com.yihu.base.router.rule.storage;

import com.yihu.base.router.model.RuleRoute;

import java.util.List;

/**
 * Created by chenweida on 2018/4/27 0027.
 */
public interface IRuleStorage {
    /**
     * 保存规则
     *
     * @param ruleRoute
     * @return
     */
    Boolean saveRuleStorage(RuleRoute ruleRoute);

    /**
     * 删除规则
     *
     * @param ruleRoute
     * @return
     */
    Boolean deleteRuleStorage(RuleRoute ruleRoute);
    /**
     * 删除规则
     *
     * @param id
     * @return
     */
    Boolean deleteRuleStorageById(String id);

    /**
     * 更新规则
     *
     * @param ruleRoute
     * @return
     */
    Boolean updateRuleStorage(RuleRoute ruleRoute);

    /**
     * 根据ID或者规则
     *
     * @param
     * @return
     */
    RuleRoute getRuleStorageById(String id);
    /**
     * 获取全部的规则
     *
     * @param
     * @return
     */
    List<RuleRoute> listRuleStorage();
    /**
     * 获取规则总数
     *
     * @param
     * @return
     */
    Integer countRuleStorage();
}
