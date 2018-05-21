package com.jbox.project.task_okex;

import com.alibaba.fastjson.JSONArray;
import com.jbox.common.base.CommonUtils;
import com.jbox.common.base.TimeUtils;
import com.jbox.common.base.XmlConfiger;
import com.jbox.project.task_okex.bean.KLineBean;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.DocumentException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * @author ganyiwei
 */
public class TaskOkexKLine {
	private static Logger logger = Logger.getLogger(TaskOkexKLine.class);

	private static XmlConfiger configer;

	private OKExClient client = new OKExClient();

	private String shutdownFile;

	private String driver;
	private String url;
	private String user;
	private String password;
	private String tablePrefix;

	public TaskOkexKLine() {
		client.DoInit();

		//创建控制退出的本地文件
		try {
			shutdownFile = CommonUtils.getClassName(this.getClass().getName())+".shutdown";

			File f = new File(shutdownFile);
			f.createNewFile();
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}

		//驱动程序名
		driver = configer.GetValue("task_okex.db_info.driver");
		//URL指向要访问的数据库名
		url = configer.GetValue("task_okex.db_info.url");
		//MySQL配置时的用户名
		user = configer.GetValue("task_okex.db_info.user");
		//MySQL配置时的密码
		password = configer.GetValue("task_okex.db_info.passwd");
		//MySQL表前缀
		tablePrefix = configer.GetValue("task_okex.db_info.table_prefix");
	}

	private boolean InsertRecord(String table, KLineBean bean) {
		//声明Connection对象
		Connection con;

		try {
			//加载驱动程序
			Class.forName(driver);
			con = DriverManager.getConnection(url,user,password);
			if(con.isClosed()) {
				logger.error("failure connecting to the Database!");
				return false;
			}

			//查询数据
			Statement statement = con.createStatement();
			String sql = "select * from "+table+" where data_time="+bean.getlDataTime();
			ResultSet rs = statement.executeQuery(sql);
			rs.last();
			if (rs.getRow() == 0) {
				java.sql.PreparedStatement psql;
				//增加数据
				psql = con.prepareStatement("insert into "+table+" values(default,?,?,?,?,?,?,default,default)");
				psql.setLong(1, bean.getlDataTime());
				psql.setFloat(2, bean.getfOpen());
				psql.setFloat(3, bean.getfHigh());
				psql.setFloat(4, bean.getfLow());
				psql.setFloat(5, bean.getfEnd());
				psql.setFloat(6, bean.getfVol());
				int ret = psql.executeUpdate();
				if (ret == 0) {
					logger.error("insert return failure");
				}
				psql.close();
			}else {
				logger.info("rs.getRow():"+rs.getRow());
			}

			rs.close();
			con.close();
		} catch (Exception e) {
			logger.error("catch a Exception:"+e.getMessage());
		}

		return true;
	}

	private void GetKline(String symbol) {
		//获取kline数据
		String result;
		try {
			//String strTime = TimeUtils.GetNowTimeStamp();
			//Long lTime = Long.valueOf(strTime)*1000;	//ms
			Long lTime = 1516310887000L;
			logger.info(symbol+" lTime:"+lTime);
			result = client.GetKline(symbol, "1min", 3, lTime);
		}catch (Exception e) {
			logger.error(symbol+" catch a Exception:"+e.getMessage());
			return;
		}
		logger.info(symbol+" result:"+result);

		//解析kline数据
		JSONArray jsonResults = JSONArray.parseArray(result);
		if (jsonResults.size() == 3) {
			JSONArray jsonResult = jsonResults.getJSONArray(0);
			if (jsonResult.size() == 6) {
				KLineBean bean = new KLineBean();
				try {
					bean.setlDataTime(jsonResult.getLong(0));
					bean.setfOpen(jsonResult.getFloat(1));
					bean.setfHigh(jsonResult.getFloat(2));
					bean.setfLow(jsonResult.getFloat(3));
					bean.setfEnd(jsonResult.getFloat(4));
					bean.setfVol(jsonResult.getFloat(5));
				}catch (Exception e) {
					logger.error(symbol+" catch a Exception:"+e.getMessage());
					return;
				}

				//写入mysql
				String table = tablePrefix+"kline_"+symbol;
				InsertRecord(table, bean);
			}else {
				logger.error(symbol+" jsonResult.size():"+jsonResult.size());
			}
		}else {
			logger.error(symbol+" jsonResults.size():"+jsonResults.size());
		}
	}

	private boolean run() {
		logger.info("enter");

		while (true) {
			File f = new File(shutdownFile);    //判断是否退出
			if (!f.exists()) {
				break;
			}

			GetKline("ltc_btc");
			GetKline("eth_btc");
			GetKline("etc_btc");
			GetKline("bch_btc");
			GetKline("eos_btc");

			try {
				Thread.sleep(5*1000); //5s
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}

		return true;
	}

	public static void main(String[] args) {
		logger.info("start!!!");

		//加载log4j配置文件
		PropertyConfigurator.configure("log4j.properties");

		//加载项目xml配置文件
		try {
			configer = new XmlConfiger("./task_okex.xml");
		}catch (IOException e1) {
			logger.error("new XmlConfiger catch a IOException:"+e1.getMessage());
			return;
		}catch (DocumentException e1) {
			logger.error("new XmlConfiger catch a DocumentException:"+e1.getMessage());
			return;
		}

		//运行任务
		while(true) {
			TaskOkexKLine task = new TaskOkexKLine();
			boolean ret = task.run();
			if (ret) {
				break;
			}
		}

		logger.info("end!!!");
	}
}

