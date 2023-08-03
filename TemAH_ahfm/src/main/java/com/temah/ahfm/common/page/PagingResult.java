package com.temah.ahfm.common.page;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PagingResult implements Serializable{

	private static final long serialVersionUID = 1416867284825483785L;

	private int pageIndex; // 当前是第几页

	private int pageSize; // 每页多少条数据

	private int maxPage; // 一共有多少页

	private Long maxCount; // 有多少条数据

	private List<?> pageData; // 数据对象集合

	public PagingResult() {
		super();
	}

	public PagingResult(int pageIndex, int pageSize) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.maxPage = 0;
		this.maxCount = 0L;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public Long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
	}
	
	@JsonIgnore
	public int getOffset() {
		return (pageIndex - 1)*pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<?> getPageData() {
		return pageData;
	}

	public void setPageData(List<?> pageData) {
		this.pageData = pageData;
	}

	public void countMaxPage() { // 根据总行数计算总页数
		if (this.maxCount % this.pageSize == 0) {
			this.maxPage = (int) (this.maxCount / this.pageSize);
		} else {
			this.maxPage = (int) (this.maxCount / this.pageSize + 1);
		}
	}
}
