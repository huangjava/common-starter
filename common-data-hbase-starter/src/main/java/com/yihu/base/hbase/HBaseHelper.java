package com.yihu.base.hbase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Hbase - DML
 * 数据增删改查
 */
public class HBaseHelper extends AbstractHBaseClient {

    private ObjectMapper objectMapper = new ObjectMapper();

    public HBaseHelper(HbaseTemplate hbaseTemplate) {
        super(hbaseTemplate);
    }

    /**
     * 新增数据 - 多列族
     * @param tableName
     * @param rowKey
     * @param family
     * @throws Exception
     */
    public void add(String tableName , String rowKey, Map<String, Map<String, String>> family) {
        hbaseTemplate.execute(tableName, new TableCallback<Void>() {
            @Override
            public Void doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(rowKey.getBytes());
                for (String familyName : family.keySet()) {
                    Map<String, String> map = family.get(familyName);
                    for (String qualifier : map.keySet()) {
                        String value = map.get(qualifier);
                        p.addColumn(familyName.getBytes(), qualifier.getBytes(), value.getBytes());
                    }
                }
                table.put(p);
                return null;
            }
        });
    }

    /**
     * 新增数据 - 单列族
     * @param tableName
     * @param rowKey
     * @param family
     * @param columns
     * @param values
     */
    public void add(String tableName, String rowKey, String family, Object[] columns, Object[] values) {
        hbaseTemplate.execute(tableName, new TableCallback<Void>() {
            @Override
            public Void doInTable(HTableInterface table) throws Throwable {
                Put put = new Put(Bytes.toBytes(rowKey));
                for (int j = 0; j < columns.length; j++) {
                    //为空字段不保存
                    if (values[j] != null) {
                        String column = String.valueOf(columns[j]);
                        String value = String.valueOf(values[j]);
                        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));
                    }
                }
                table.put(put);
                return null;
            }
        });
    }

    /**
     * 删除记录
     * @param tableName
     * @param rowKey
     */
    public void delete(String tableName, String rowKey)  {
        hbaseTemplate.execute(tableName, new TableCallback<Void>() {
            @Override
            public Void doInTable(HTableInterface table) throws Throwable {
                Delete d = new Delete(rowKey.getBytes());
                table.delete(d);
                return null;
            }
        });
    }

    /**
     * 批量删除数据
     * @param tableName
     * @param rowKeys
     * @return
     * @throws Exception
     */
    public Object[] deleteBatch(String tableName, String[] rowKeys) {
        return hbaseTemplate.execute(tableName, new TableCallback<Object[]>() {
            @Override
            public Object[] doInTable(HTableInterface table) throws Throwable {
                List<Delete> deletes = new ArrayList<>(rowKeys.length);
                for (String rowKey : rowKeys) {
                    Delete delete = new Delete(Bytes.toBytes(rowKey));
                    deletes.add(delete);
                }
                Object[] results = new Object[deletes.size()];
                table.batch(deletes, results);
                return results;
            }
        });
    }

    /**
     * 删除列族
     * @param tableName
     * @param rowKey
     * @param familyName
     * @throws Exception
     */
    public void deleteFamily(String tableName, String rowKey, String familyName) throws Exception {
        hbaseTemplate.delete(tableName, rowKey, familyName);
    }

    /**
     * 删除某列
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param columnName
     * @throws Exception
     */
    public void deleteColumn(String tableName, String rowKey, String familyName, String columnName) throws Exception {
        hbaseTemplate.delete(tableName, rowKey, familyName, columnName);
    }

    /**
     * 修改某行某列值
     */
    public void put(String tableName ,String rowKey, String familyName, String qualifier, String value) throws Exception {
        hbaseTemplate.put(tableName, rowKey, familyName, qualifier, value.getBytes());
    }

    /**
     * 模糊匹配rowKey
     * @param tableName 表名
     * @param rowKeyRegEx 表达式
     * @return
     * @throws Exception
     */
    public String[] findRowKeys(String tableName, String rowKeyRegEx) throws Exception {
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes("basic"));
        scan.setStartRow(rowKeyRegEx.substring(1, rowKeyRegEx.length()).getBytes());
        //scan.setStopRow(rowKeyRegEx.substring(1, rowKeyRegEx.length()).getBytes());
        scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(rowKeyRegEx)));
        List<String> list = new LinkedList<>();
        hbaseTemplate.find(tableName, scan, new RowMapper<Void>() {
            @Override
            public Void mapRow(Result result, int rowNum) throws Exception {
                list.add(Bytes.toString(result.getRow()));
                return null;
            }
        });
        return list.toArray(new String[list.size()]);
    }

    /**
     * 表总条数
     * @param tableName
     * @return
     * @throws Exception
     */
    public Integer count(String tableName) throws Exception {
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes("basic"));
        scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^")));
        List<String> list = new LinkedList<>();
        hbaseTemplate.find(tableName, scan, new RowMapper<Void>() {
            @Override
            public Void mapRow(Result result, int rowNum) throws Exception {
                list.add(Bytes.toString(result.getRow()));
                return null;
            }
        });
        return list.size();
    }

    /**
     * 根据rowKey获取一条记录
     * @param tableName
     * @param rowKey
     * @return 字符串
     */
    public String get(String tableName, String rowKey) {
        return hbaseTemplate.get(tableName, rowKey, new RowMapper<String>() {
            @Override
            public String mapRow(Result result, int rowNum) throws Exception {
                if(!result.isEmpty()) {
                    List<Cell> ceList = result.listCells();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("rowkey", rowKey);
                    for (Cell cell : ceList) {
                        // 默认不加列族
                        // Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()) +"_"
                        map.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),
                                Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                    }
                    return objectMapper.writeValueAsString(map);
                }
                else{
                    return "";
                }
            }
        });
    }

    /**
     * 通过rowKey获取某行数据
     * @param tableName
     * @param rowKey
     * @return Map
     */
    public Map<String, Object> getResultMap(String tableName, String rowKey) {
        return hbaseTemplate.get(tableName, rowKey, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(Result result, int rowNum) throws Exception {
                if(!result.isEmpty()) {
                    List<Cell> ceList = result.listCells();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("rowkey", rowKey);
                    for (Cell cell : ceList) {
                        //默认不加列族
                        // Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()) +"_"
                        map.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),
                                Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                    }
                    return map;
                }else {
                    return null;
                }
            }
        });
    }

    /**
     * 通过rowKey获取某行数据
     * @param tableName
     * @param rowKey
     * @return
     * @throws Exception
     */
    public Result getResult(String tableName, String rowKey) throws Exception {
        return hbaseTemplate.get(tableName, rowKey, new RowMapper<Result>() {
            @Override
            public Result mapRow(Result result, int rowNum) throws Exception {
                return result;
            }
        });
    }

    /**
     * 通过表名和rowKey获取指定列族下的值
     * @param tableName 表名
     * @param rowKey rowKey
     * @param familyName 列族
     * @return
     */
    public Map<String, String> get(String tableName, String rowKey, String familyName) {
        return hbaseTemplate.get(tableName, rowKey, familyName, new RowMapper<Map<String, String>>(){
            @Override
            public Map<String, String> mapRow(Result result, int rowNum) throws Exception {
                Map<String, String> map = new HashMap<>();
                NavigableMap<byte[], byte[]> navigableMaps = result.getFamilyMap(familyName.getBytes());
                if(null != navigableMaps) {
                    for (byte[] key : navigableMaps.keySet()) {
                        String keys = new String(key);
                        String values = new String(navigableMaps.get(key));
                        map.put(keys, values);
                    }
                }
                return map;
            }
        });
    }

    /**
     * 通过表名和rowKey获取指定列族下的列名的值
     * @param tableName 表名
     * @param rowKey rowKey
     * @param familyName 列族
     * @param qualifier 列名
     * @return
     */
    public String get(String tableName, String rowKey, String familyName, String qualifier) {
        return hbaseTemplate.get(tableName, rowKey, familyName, qualifier, new RowMapper<String>(){
            @Override
            public String mapRow(Result result, int rowNum) throws Exception {
                Cell cell = result.getColumnLatestCell(familyName.getBytes(), qualifier.getBytes());
                return new String(CellUtil.cloneValue(cell));
            }
        });
    }

    /**
     * 通过rowKey集合获取指定列名下的多条数据
     * @param tableName 表名
     * @param rowKeys rowKeys
     * @param basicFl basic列族下的列名
     * @param dFl d列族下的列名
     * @return
     * @throws Exception
     */
    public Result[] getResultList(String tableName, List<String> rowKeys, String basicFl, String dFl) {
        return hbaseTemplate.execute(tableName, new TableCallback<Result[]>() {
            @Override
            public Result[] doInTable(HTableInterface table) throws Throwable {
                List<Get> list = new ArrayList<Get>();
                for (String rowKey : rowKeys) {
                    Get get = new Get(Bytes.toBytes(rowKey));
                    if (!StringUtils.isEmpty(basicFl)) {
                        String[] basicArr = basicFl.split(",");
                        for (String basicStr : basicArr) {
                            get.addColumn(Bytes.toBytes("basic"), Bytes.toBytes(basicStr));
                        }
                    }
                    if (!StringUtils.isEmpty(dFl)) {
                        String[] dArr = dFl.split(",");
                        for (String dStr : dArr) {
                            get.addColumn(Bytes.toBytes("d"), Bytes.toBytes(dStr));
                        }
                    }
                    list.add(get);
                }
                return table.get(list);
            }
        });
    }

    /************************************* Bean使用原型模式 ***************************************************************/

    /**
     * 保存数据 原型模式
     */
    public void save(String tableName, TableBundle tableBundle) {
        hbaseTemplate.execute(tableName, new TableCallback<Void>() {
            @Override
            public Void doInTable(HTableInterface table) throws Throwable {
                List<Put> puts = tableBundle.putOperations();
                Object[] results = new Object[puts.size()];
                table.batch(puts, results);
                return null;
            }
        });
    }

    /**
     * 删除数据 原型模式
     */
    public void delete(String tableName, TableBundle tableBundle) {
        hbaseTemplate.execute(tableName, new TableCallback<Object[]>() {
            @Override
            public Object[] doInTable(HTableInterface table) throws Throwable {
                List<Delete> deletes = tableBundle.deleteOperations();
                Object[] results = new Object[deletes.size()];
                table.batch(deletes, results);
                return null;
            }
        });
    }

    /**
     * 查询数据 原型模式
     */
    public Object[] get(String tableName, TableBundle tableBundle) {
        return hbaseTemplate.execute(tableName, new TableCallback<Object[]>() {
            @Override
            public Object[] doInTable(HTableInterface table) throws Throwable {
                List<Get> gets = tableBundle.getOperations();
                Object[] results = new Object[gets.size()];
                table.batch(gets, results);
                if (results.length > 0 && results[0].toString().equals("keyvalues=NONE")) {
                    return null;
                }
                return results;
            }
        });
    }
}