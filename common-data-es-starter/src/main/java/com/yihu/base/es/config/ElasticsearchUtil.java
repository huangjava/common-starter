package com.yihu.base.es.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.ElasticSearchDruidDataSourceFactory;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.parser.SQLExprParser;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.nlpcn.es4sql.domain.Select;
import org.nlpcn.es4sql.jdbc.ObjectResult;
import org.nlpcn.es4sql.jdbc.ObjectResultsExtractor;
import org.nlpcn.es4sql.parse.ElasticSqlExprParser;
import org.nlpcn.es4sql.parse.SqlParser;
import org.nlpcn.es4sql.query.AggregationQueryAction;
import org.nlpcn.es4sql.query.DefaultQueryAction;
import org.nlpcn.es4sql.query.SqlElasticSearchRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by chenweida on 2017/7/17.
 * SELECT town,townName,sum(result1) result1 FROM wlyy_quota_test
 * where quotaCode='1'
 * group by town,townName ， date_histogram(field='quotaDate','interval'='week')
 */
public class ElasticsearchUtil {
    private Logger logger = LoggerFactory.getLogger(ElasticsearchUtil.class);

    private Client elasticFactory;


    /**
     * 执行sql查询es
     *
     * @param sql
     * @return
     */
    public List<Map<String, Object>> excuteDataModel(String sql) {
        List<Map<String, Object>> returnModels = new ArrayList<>();
        try {
            SQLExprParser parser = new ElasticSqlExprParser(sql);
            SQLExpr expr = parser.expr();
            SQLQueryExpr queryExpr = (SQLQueryExpr) expr;
//            if (parser.getLexer().token() != Token.EOF) {
//                throw new ParserException("illegal sql expr : " + sql);
//            }
            Select select = null;
            select = new SqlParser().parseSelect(queryExpr);

            //通过抽象语法树，封装成自定义的Select，包含了select、from、where group、limit等
            AggregationQueryAction action = null;
            DefaultQueryAction queryAction = null;
            SqlElasticSearchRequestBuilder requestBuilder = null;
            if (select.isAgg) {
                //包含计算的的排序分组的
                action = new AggregationQueryAction(elasticFactory, select);
                requestBuilder = action.explain();
            } else {
                //封装成自己的Select对象
                Client client = elasticFactory;
                queryAction = new DefaultQueryAction(client, select);
                requestBuilder = queryAction.explain();
            }
            SearchResponse response = (SearchResponse) requestBuilder.get();
            Object queryResult = null;
            if (sql.toUpperCase().indexOf("GROUP") != -1 || sql.toUpperCase().indexOf("SUM") != -1|| sql.toUpperCase().indexOf("COUNT(") != -1) {
                queryResult = response.getAggregations();
            } else {
                queryResult = response.getHits();
            }
            ObjectResult temp = new ObjectResultsExtractor(true, true, true).extractResults(queryResult, true);
            List<String> heads = temp.getHeaders();
            temp.getLines().stream().forEach(one -> {
                try {
                    Map<String, Object> oneMap = new HashMap<String, Object>();
                    for (int i = 0; i < one.size(); i++) {
                        String key = null;
                        Object value = one.get(i);
//                        if(value instanceof Double){
//                            Double valueTemp =Double.valueOf((Double)value);
//                            DecimalFormat df = new DecimalFormat("######0");
//                            value=df.format(valueTemp);
//                        }
                        key = heads.get(i);
                        oneMap.put(key, value);
                    }
                    returnModels.add(oneMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnModels;
    }

    /**
     * 只执行count语句
     * @param sql
     * @return
     */
    public Integer count(String sql) {
        List<Map<String, Object>> returnModels = new ArrayList<>();
        try {
            SQLExprParser parser = new ElasticSqlExprParser(sql);
            SQLExpr expr = parser.expr();
            SQLQueryExpr queryExpr = (SQLQueryExpr) expr;
//            if (parser.getLexer().token() != Token.EOF) {
//                throw new ParserException("illegal sql expr : " + sql);
//            }
            Select select = null;
            select = new SqlParser().parseSelect(queryExpr);

            //通过抽象语法树，封装成自定义的Select，包含了select、from、where group、limit等
            AggregationQueryAction action = null;
            DefaultQueryAction queryAction = null;
            SqlElasticSearchRequestBuilder requestBuilder = null;
            if (select.isAgg) {
                //包含计算的的排序分组的
                action = new AggregationQueryAction(elasticFactory, select);
                requestBuilder = action.explain();
            } else {
                //封装成自己的Select对象
                Client client = elasticFactory;
                queryAction = new DefaultQueryAction(client, select);
                requestBuilder = queryAction.explain();
            }
            SearchResponse response = (SearchResponse) requestBuilder.get();
            Object queryResult = null;
            if (sql.toUpperCase().indexOf("GROUP") != -1 || sql.toUpperCase().indexOf("SUM") != -1|| sql.toUpperCase().indexOf("COUNT(") != -1) {
                queryResult = response.getAggregations();
            } else {
                queryResult = response.getHits();
            }
            ObjectResult temp = new ObjectResultsExtractor(true, true, true).extractResults(queryResult, true);
            List<String> heads = temp.getHeaders();
            for(int j=0;j<temp.getLines().size();j++){
              List<Object>  one= temp.getLines().get(j);
                try {
                    Map<String, Object> oneMap = new HashMap<String, Object>();
                    for (int i = 0; i < one.size(); i++) {
                        String key = null;
                        Object value = one.get(i);
                        if(value instanceof Double){
                            Double valueTemp =Double.valueOf((Double)value);
                            DecimalFormat df = new DecimalFormat("######0");
                            value=df.format(valueTemp);
                            return Integer.parseInt(df.format(valueTemp));
                        }
                        key = heads.get(i);
                        oneMap.put(key, value);
                    }
                    returnModels.add(oneMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public Client getElasticFactory() {
        return elasticFactory;
    }

    public void setElasticFactory(Client elasticFactory) {
        this.elasticFactory = elasticFactory;
    }

    public static void main(String[] args) {
        Double a =Double.valueOf(("3.3863237E7"));
        DecimalFormat df = new DecimalFormat("######0");
        System.out.println(Integer.parseInt(df.format(a)));
    }
}
