package com.jbox.project.task_btc_trade.trade_stratage.follow;

import com.jbox.common.base.CommonUtils;
import com.jbox.project.task_btc_trade.trade_client.OKExClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;


/**
 * @author ganyiwei
 */
public class FollowStratage1 {
	private static Logger logger = Logger.getLogger(FollowStratage1.class);

	private OKExClient client = new OKExClient();

	private String shutdownFile;

	public FollowStratage1() {
		client.DoInit();

		//创建控制退出的本地文件
		try {
			shutdownFile = CommonUtils.getClassName(this.getClass().getName())+".shutdown";

			File f = new File(shutdownFile);
			f.createNewFile();
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
		}
	}

	private boolean run() {
		while (true) {
			File f = new File(shutdownFile);    //判断是否退出
			if (!f.exists()) {
				break;
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

		//运行任务
		while(true) {
			FollowStratage1 task = new FollowStratage1();
			boolean ret = task.run();
			if (ret) {
				break;
			}
		}

		logger.info("end!!!");
	}
}

