package com.jbox.project.task_okex;

import com.alibaba.fastjson.JSONArray;
import com.jbox.common.base.CommonUtils;
import com.jbox.common.base.MailUtils;
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

	private boolean GetKline(String symbol) {
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
			return false;
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
					return false;
				}

				//写入mysql
				String table = tablePrefix+"kline_"+symbol;
				return InsertRecord(table, bean);
			}else {
				logger.error(symbol+" jsonResult.size():"+jsonResult.size());
				return false;
			}
		}else {
			logger.error(symbol+" jsonResults.size():"+jsonResults.size());
			return false;
		}
	}

	private boolean run() {
		int iMaxFailNum = 10;
		int iLtcBtcFailNum = 0;
		int iEthBtcFailNum = 0;
		int iEtcBtcFailNum = 0;
		int iBchBtcFailNum = 0;
		int iEosBtcFailNum = 0;

		logger.info("enter");
		while (true) {
			File f = new File(shutdownFile);    //判断是否退出
			if (!f.exists()) {
				break;
			}

			if (!GetKline("ltc_btc")) {
				if (++iLtcBtcFailNum > iMaxFailNum) {
					MailUtils.notice(logger, "TaskOkexKLine ltc_btc fail", "");
					iLtcBtcFailNum = 0;
				}
			}else {
				iLtcBtcFailNum = 0;
			}

			if (!GetKline("eth_btc")) {
				if (++iEthBtcFailNum > iMaxFailNum) {
					MailUtils.notice(logger, "TaskOkexKLine eth_btc fail", "");
					iEthBtcFailNum = 0;
				}
			}else {
				iEthBtcFailNum = 0;
			}

			if (!GetKline("etc_btc")) {
				if (++iEtcBtcFailNum > iMaxFailNum) {
					MailUtils.notice(logger, "TaskOkexKLine etc_btc fail", "");
					iEtcBtcFailNum = 0;
				}
			}else {
				iEtcBtcFailNum = 0;
			}

			if (!GetKline("bch_btc")) {
				if (++iBchBtcFailNum > iMaxFailNum) {
					MailUtils.notice(logger, "TaskOkexKLine bch_btc fail", "");
					iBchBtcFailNum = 0;
				}
			}else {
				iBchBtcFailNum = 0;
			}

			if (!GetKline("eos_btc")) {
				if (++iEosBtcFailNum > iMaxFailNum) {
					MailUtils.notice(logger, "TaskOkexKLine eos_btc fail", "");
					iEosBtcFailNum = 0;
				}
			}else {
				iEosBtcFailNum = 0;
			}

			try {
				Thread.sleep(10*1000); //10s
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

