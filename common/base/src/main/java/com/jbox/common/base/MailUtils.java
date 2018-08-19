package com.jbox.common.base;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ganyiwei
 */
public class MailUtils {
	public static void notice(Logger logger, String title, String content) {
		String message = "/bin/echo \"" + content + "\" | /bin/mail -s \"" + title + "\" weiganyi@yeah.net";
		try {
			Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", message}).waitFor();
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
	}
}
