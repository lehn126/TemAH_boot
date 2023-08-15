package com.temah.common.web;

import java.io.Serializable;

/**
 * 统一接口返回格式
 * @author win10
 *
 */
public class RestResult implements Serializable{

	private static final long serialVersionUID = -2938534102033696405L;

	public static final String SUCCESS = "0";

	private String errCode;
	private Object data;

	public RestResult() {
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 返回
	 * @param errCode
	 */
	public RestResult(String errCode) {
		this.errCode = errCode;
	}

	/**
	 * 成功返回
	 * @param errCode
	 * @param data
	 */
	public RestResult(String errCode, Object data) {
		this.errCode = errCode;
		this.data = data;
	}
	
	/**
	 * 异常返回
	 * @param errCode
	 * @param data
	 */
	public RestResult(String errCode, Throwable data) {
		this.errCode = errCode;
		if(data != null){
			this.data = data.getMessage();
		}
	}

	public static RestResult success() {
		return new RestResult(SUCCESS);
	}

	public static RestResult success(Object data) {
		return new RestResult(SUCCESS,data);
	}

	public static RestResult fail(String errCode) {
		return new RestResult(errCode);
	}

	public static RestResult fail(String errCode, Object data) {
		return new RestResult(errCode,data);
	}
	
	public static RestResult fail(String errCode, Throwable errData) {
		return new RestResult(errCode,errData);
	}

}
