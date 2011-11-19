package com.pandawork.core.exception;

/**
 * 业务异常处理接口
 * 
 * @author Lionel
 * @date 2010-12-7
 */
public interface IBizExceptionMes {
    /**
     * 获取异常信息
     * @return
     */
    public String getMes();
    
    /**
     * 获取异常代码
     * @return
     */
    public int getCode();
}
