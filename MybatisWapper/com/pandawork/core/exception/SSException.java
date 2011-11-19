package com.pandawork.core.exception;


/**
 * 网站内部异常信息
 * 
 * @author Lionel pang
 * @date 2010-3-19
 */
public class SSException extends Exception implements Cloneable {
	private static final long serialVersionUID = -7950591492983519092L;
	// 异常信息
	private String mes;
	// 异常编码
	private int code;
	// 异常信息枚举
	private IBizExceptionMes eMes;
	
	private  SSException(IBizExceptionMes eMes){
		this.eMes = eMes;
		this.mes = eMes.getMes();
		this.code = eMes.getCode();
	}
	
	private  SSException(IBizExceptionMes eMes, Throwable e){
	    super(eMes.getMes(), e);
        this.eMes = eMes;
        this.mes = eMes.getMes();
        this.code = eMes.getCode();
    }
	
	/**
	 * 生成网站内部异常
	 * 
	 * @param eMes
	 * @return
	 */
	public static SSException get(IBizExceptionMes eMes) {
		return new SSException(eMes);
	}
	
	/**
     * 生成网站内部异常
     * 
     * @param eMes
     * @return
     */
    public static SSException get(IBizExceptionMes eMes, Throwable e) {
        return new SSException(eMes, e);
    }

    @Override
    public void printStackTrace() {
    	System.err.println(this.getClass() + " " + this.getMessage());
    }
	
    @Override
    protected SSException clone() {
        return new SSException(this.eMes);
    }
    
	public String getMessage() {
		return mes;
	}

	public int getCode() {
		return code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((eMes == null) ? 0 : eMes.hashCode());
		result = prime * result + ((mes == null) ? 0 : mes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		SSException other = (SSException) obj;
		if (code != other.code) return false;
		if (eMes == null) {
			if (other.eMes != null) return false;
		} else if (!eMes.equals(other.eMes)) return false;
		if (mes == null) {
			if (other.mes != null) return false;
		} else if (!mes.equals(other.mes)) return false;
		return true;
	}
	
	
}
