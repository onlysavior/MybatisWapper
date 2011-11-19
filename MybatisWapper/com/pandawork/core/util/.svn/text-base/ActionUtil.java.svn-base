package com.pandawork.core.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pandawork.core.log.LogClerk;
import com.pandawork.core.sytem.CorePropertiesKey;
import com.pandawork.core.sytem.SystemInstance;

/**
 * Action助手类
 * 
 * @author Lionel pang
 *
 */
public class ActionUtil {
	/**
	 * 检查post
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static boolean checkPost(HttpServletRequest request, HttpServletResponse response) {
		if(!request.getMethod().toLowerCase().equals("post")) {
			LogClerk.webLog.error("checkPost|error|" + "|" + request.getRequestURI() + 
					"|" + request.getQueryString());
			response.setStatus(403);
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 检查条状是否正常
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static boolean checkReferer(HttpServletRequest request, HttpServletResponse response) {
		String referer  = request.getHeader("Referer");
		
		String domain = request.getServerName();
		
		if(null==referer || !referer.matches("^https?://(" + domain + ").*$")) {
			LogClerk.webLog.error("checkReferer|error|" + referer + "|" + request.getAttribute("hostid")
					+ "|" + request.getRequestURI() + "|" + request.getQueryString());
			response.setStatus(403);
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Post方法和跳转连接一起搞
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static boolean checkPostAndReferer(HttpServletRequest request, HttpServletResponse response) {
		return checkPost(request, response) && checkReferer(request, response);
	}
	
	/**
	 * 输出信息
	 * 
	 * @param response
	 * @param str
	 * @param contentType
	 */
	public static void print(HttpServletResponse response, String str, String contentType) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
			LogClerk.webLog.error("print|error|" + str + "|" + contentType, e);
		}
		response.setContentType(contentType);
		if (out != null) {
			out.print(str);
			out.close();
		}
	}
	
	/**
	 * 输出xml文件
	 * 
	 * @param response
	 * @param str
	 */
	public static void printXML(HttpServletResponse response, String str) {
		print(response, str, "text/xml; charset=" + SystemInstance.getIntance().getPropertie(CorePropertiesKey.Encode.getKey(), "UTF-8"));
	}
	
	/**
	 * 输出正常的html文本信息
	 * 
	 * @param response
	 * @param str
	 */
	public static void print(HttpServletResponse response, String str) {
		print(response, str, "text/html; charset=" + SystemInstance.getIntance().getPropertie(CorePropertiesKey.Encode.getKey(), "UTF-8"));
	}
}
