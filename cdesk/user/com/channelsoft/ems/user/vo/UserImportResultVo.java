package com.channelsoft.ems.user.vo;

import java.util.List;

/**
 * 批量导入用户返回对象
 * @author wangjie
 * @time 2015年11月20日下午4:40:53
 */
public class UserImportResultVo {

	private String totalNum;//总条数
	private double time;//使用时间
	private String importSuccessNum;//导入成功条数
	private String updateSuccessNum;//更新成功条数
	private String importFailNum;//失败条数
	private List<String> failList;//失败信息列表
	private String resultFilePath;//结果文件路径
	
	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public String getImportSuccessNum() {
		return importSuccessNum;
	}
	public void setImportSuccessNum(String importSuccessNum) {
		this.importSuccessNum = importSuccessNum;
	}
	public String getUpdateSuccessNum() {
		return updateSuccessNum;
	}
	public void setUpdateSuccessNum(String updateSuccessNum) {
		this.updateSuccessNum = updateSuccessNum;
	}
	public String getImportFailNum() {
		return importFailNum;
	}
	public void setImportFailNum(String importFailNum) {
		this.importFailNum = importFailNum;
	}
	public List<String> getFailList() {
		return failList;
	}
	public void setFailList(List<String> failList) {
		this.failList = failList;
	}
	public String getResultFilePath() {
		return resultFilePath;
	}
	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}
	
}
