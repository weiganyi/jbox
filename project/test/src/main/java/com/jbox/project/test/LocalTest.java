package com.jbox.project.test;

import com.jbox.common.base.MailUtils;
import com.jbox.common.base.TimeUtils;
import com.jbox.common.base.XmlConfiger;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.sql.*;

/**
 * Hello world!
 *
 */
public class LocalTest {
    private static Logger logger = Logger.getLogger(LocalTest.class);

    private static XmlConfiger configer;

    public static void MysqlTest() {
        int ret = 0;

        logger.info("MysqlTest start");

        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = configer.GetValue("test.db_info.driver");
        //URL指向要访问的数据库名
        String url = configer.GetValue("test.db_info.url");
        //MySQL配置时的用户名
        String user = configer.GetValue("test.db_info.user");
        //MySQL配置时的密码
        String password = configer.GetValue("test.db_info.passwd");
        //MySQL表名
        String table = configer.GetValue("test.db_info.table");

        try {
            //加载驱动程序
            Class.forName(driver);
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed()) {
                logger.info("Succeeded connecting to the Database!");
            }

            //查询数据
            Statement statement = con.createStatement();
            String sql = "select * from "+table;
            ResultSet rs = statement.executeQuery(sql);
            logger.info("查询结果如下所示:");
            logger.info("t_key" + "\t" + "t_value");
            while(rs.next()){
                String key = rs.getString("t_key");
                String value = rs.getString("t_value");
                logger.info(key + "\t" + value);
            }

            java.sql.PreparedStatement psql;
            //增加数据
            psql = con.prepareStatement("insert into "+table+" values(?,?)");
            psql.setString(1, "刘明");
            psql.setString(2, "总裁");
            ret = psql.executeUpdate();
            logger.info("insert ret:"+ret);

            //更新数据
            psql = con.prepareStatement("update "+table+" set t_value=? where t_key=?");
            psql.setString(1, "副总裁");
            psql.setString(2, "刘明");
            ret = psql.executeUpdate();
            logger.info("update ret:"+ret);

            //删除数据
            psql = con.prepareStatement("delete from "+table+" where t_key=?");
            psql.setString(1, "刘明");
            ret = psql.executeUpdate();
            logger.debug("delete ret:"+ret);
            psql.close();

            rs.close();
            con.close();
        } catch (Exception e) {
            logger.error("catch a Exception:"+e.getMessage());
        }

        logger.info("MysqlTest end");
    }

    public static void main( String[] args ) {
        //加载log4j配置文件
        PropertyConfigurator.configure("log4j.properties");

        //加载项目xml配置文件
        try {
            configer = new XmlConfiger("./test.xml");
        }catch (IOException e1) {
            logger.error("new XmlConfiger catch a IOException:"+e1.getMessage());
            return;
        }catch (DocumentException e1) {
            logger.error("new XmlConfiger catch a DocumentException:"+e1.getMessage());
            return;
        }

        //时间函数测试
        logger.info("GetNowFormatTime:"+TimeUtils.GetNowFormatTime());
        logger.info("GetNowTimeStamp:"+TimeUtils.GetNowTimeStamp());
        logger.info("Date2TimeStamp:"+TimeUtils.Date2TimeStamp("2018-01-19 05:28:07"));

        //mysql测试
        //MysqlTest();

        //邮件通知测试
        MailUtils.notice(logger, "this is a test fail", "");
    }
}
