package com.pandawork.core.exception;

/**
 * 异常信息记录
 * 
 * @author Lionel pang
 * @date 2010-3-19
 */
public enum ExceptionMes implements IBizExceptionMes{
	SYSEXCEPTION(0, "系统内部异常"),
	SYS_NOT_FOUND_OBJECT(1, "所要查询的对象不存在"),
	// 上传图片异常
	IMAGEDEALEXCEPTION(1, "图片处理异常"),
	//选景车异常
	CAR_BEYOND_MAX_NUMBER(2,"景区超过选景车最大数量"),
	CAR_REPEAT_ERROR(3,"该景区已经添加到选景车中"),
	SCENIC_SHOWORDER(4, "反射处理权限错误"),
	SEARCH_ERROR(5,"系统查询异常,请重试!"),
	CAR_SCENIC_ID_ERROR(6, "所请求的景区ID不存在"),
	CAR_DELETE_ZERO_SCENIC(7, "已选景区为0，无法删除"),
	ADMIN_PASSWORD_ERROR(8, "密码错误，请重试"),
	USER_NOT_EXIST(9, "用户不存在，请重试"),
	
	EXCEL_TEA_EXIST(10, "导入的Excel文件中不能存在相同的登录名或者教师编码！"),
	EXCEL_TEA_EXIST_INDB(11, "导入的Excel文件中已经在数据库中存在相同的登录名或者教师编码！"),
	EXCEL_STU_EXIST(12, "导入的Excel文件中不能存在相同的登录名或者学生学号！"),
    EXCEL_STU_EXIST_INDB(13, "导入的Excel文件中已经在数据库中存在相同的登录名或者学生学号！"),
    
    EXCEL_EDU_EXIST(14, "导入的Excel文件中不能存在相同的学生学号！"),
    EXCEL_EDU_EXIST_INDB(15, "导入的Excel文件中已经在数据库中存在相同考试科目的相同学生学号！"),
	
	REGEDIT_ERROR(20, "注册信息有错！"),
	
	OPTION_SIZE(30, "投票选项的个数小于默认的选项个数"),
	VOTE_NUM(31,"问卷投票的个数小于默认的投票个数"),
	VOTE_PERMISSION(32,"该用户没有投票的权限");
	
	// 异常信息
	private String mes;
	// 异常编码
	private int code;
	ExceptionMes(int code, String mes){
		this.mes = mes;
		this.code = code;
	}
	
	@Override
	public String getMes() {
		return mes;
	}
	
	@Override
	public int getCode() {
		return code;
	}
	
	
}
