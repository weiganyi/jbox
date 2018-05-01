package com.jbox.project.test;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.*;

/**
 * Hello world!
 *
 */
public class LocalTest {
    private static Logger logger = Logger.getLogger(LocalTest.class);

    public static void main( String[] args ) {
        //加载log4j配置文件
        PropertyConfigurator.configure("log4j.properties");

        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名
        String url = "jdbc:mysql://172.16.32.11:3306/weigy";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "Wgy008231";

        try {
            //加载驱动程序
            Class.forName(driver);
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed()) {
                logger.debug("Succeeded connecting to the Database!");
            }

            //查询数据
            Statement statement = con.createStatement();
            String sql = "select * from t_test";
            ResultSet rs = statement.executeQuery(sql);
            logger.debug("执行结果如下所示:");
            logger.debug("t_key" + "\t" + "t_value");
            while(rs.next()){
                String key = rs.getString("t_key");
                String value = rs.getString("t_value");
                logger.debug(key + "\t" + value);
            }

            java.sql.PreparedStatement psql;
            //增加数据
            psql = con.prepareStatement("insert into t_test values(?,?)");
            psql.setString(1, "刘明");
            psql.setString(2, "总裁");
            psql.executeUpdate();

            //更新数据
            psql = con.prepareStatement("update t_test set t_value=? where t_key=?");
            psql.setString(1, "副总裁");
            psql.setString(2, "刘明");
            psql.executeUpdate();

            //删除数据
            psql = con.prepareStatement("delete from t_test where t_key=?");
            psql.setString(1, "刘明");
            psql.executeUpdate();
            psql.close();

            rs.close();
            con.close();
        } catch (Exception e) {
            logger.error("catch a Exception:"+e.getMessage());
        }finally{
            logger.debug("数据库数据成功获取！！");
        }
    }
}
