package net.shangtech.ssh.core;

import java.util.List;

/**
 * 文件名： Page.java<br/>
 * 作者： 宋相恒<br/>
 * 版本： 2014-1-13 下午9:27:22 v1.0<br/>
 * 日期： 2014-1-13<br/>
 * 描述：分页查询辅助类,分页查询分两种情况:<br/>
 * 		<p>1.使用start+limit组合分页,需要给定start和limit值</p>
 * 		<p>1.使用pageNo+limit组合分页,需要给定pageNo和limit值</p>
 * 		<p>不管是哪种方式,都要先查询出totalCount,之后会根据查询结果对范围进行校正,得出的start与pageNo才是合理的值</p>
 */
public class Page<T> {
	private int start;
	private int limit;
	private int pageNo;
	private int pageSize;
	private int totalCount;
	private List<T> result;
	private String hql;
	private Object[] values;
	public void setQuery(String hql, Object...values){
		this.hql = hql;
		this.values = values;
	}
	public String getHql(){
		return hql;
	}
	public Object[] getValues(){
		return values;
	}
	public Page(int limit){
		this.limit = limit;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		//计算总页数
		this.pageSize = (int) (totalCount/limit);
		pageSize = totalCount%limit==0?pageSize:pageSize+1;
		//如果start为0就认为是用pageNo+limit查询,否则视为start+limit查询
		if(start == 0){
			//当前页码范围应该要大于0且不大于总页数
			if(pageNo > pageSize)
				pageNo = pageSize;
			if(pageNo <= 0)
				pageNo = 1;
		}else{
			//start应该大于0且不大于记录总数
			if(start > totalCount)
				pageNo = pageSize;
			if(start < 0)
				pageNo = 1;
		}
		//计算起始位置
		start = (pageNo-1)*limit;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	
}

  	