package bak.hdfs.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.URI;

/**
 * Created by chenweida on 2018/2/26.
 */
public class HDFSUtil {

    private static Logger logger = LoggerFactory.getLogger(HDFSUtil.class);

    private static String uri = "hdfs://192.168.131.240:9000/";


    /**
     * @param uri     hdfs地址
     * @param message 追加的内容
     * @param path    文件路径
     *                <p/>
     *                <p/>
     *                追加文件内容
     *                上面的解释明显的提到如果需要使用append操作，需要升级到hadoop 2.x版本。并且需要在Conf的hdfs.site.xml文件中加入如下配置：
     *                <p/>
     *                <property>
     *                <name>dfs.support.append</name>
     *                <value>true</value>
     *                </property>
     *                Hadoop的API中也提供了设置项来支持内容追加，代码如下：
     *                <p/>
     *                Configuration conf = new Configuration();
     *                conf.setBoolean("dfs.support.append", true);
     *                https://www.cnblogs.com/flyeast/archive/2014/07/20/3856356.html
     */
    public static void appendFile(String uri, String path, String message) {
        try {
            Configuration conf = new Configuration();
            conf.setBoolean("dfs.support.append", true);//开启文件追加模式
            FileSystem fileSystem = FileSystem.get(URI.create(uri), conf);
            if (exsit(fileSystem, uri, path)) {

            } else {
                //如果不存在就创建文件
                fileSystem.create(new Path(path));
            }
            //直接追加
            append(fileSystem, uri, message, path);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * @param uri     hdfs地址
     * @param pathStr 文件路径
     *                判断文件是否存在
     */
    private static boolean exsit(FileSystem fileSystem, String uri, String pathStr)
            throws Exception {
        try {
            Path path = new Path(pathStr);
            return fileSystem.exists(path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 追加文件
     *
     * @param fileSystem
     * @param uri
     * @param message
     * @param path
     * @throws Exception
     */
    private static void append(FileSystem fileSystem, String uri, String message, String path)
            throws Exception {
        //如果存在就直接追加
        OutputStream out = fileSystem.append(new Path(path));

        out.write((message + "\r\n").getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    public static void main(String[] args) throws Exception {
        while (true) {
            String uri = "hdfs://172.17.110.20:8020/";
            String message = "ceshi";
            String path = "/user/root/ceshi123.log";
            HDFSUtil.appendFile(uri, path, message);
        }
    }
}
