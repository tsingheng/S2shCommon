package net.shangtech.ssh.core.sort;

import net.shangtech.ssh.core.IBaseEntity;

/**
 * 文件名： Sortable.java<br/>
 * 作者： 宋相恒<br/>
 * 版本： 2014-1-15 下午7:59:31 v1.0<br/>
 * 日期： 2014-1-15<br/>
 * 描述：
 */
public abstract class Sortable extends IBaseEntity{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么)
	 * @since v1.0
	 */
	private static final long serialVersionUID = 1093664519005485803L;
	public abstract void setSort(Integer sort);
	public abstract Integer getSort();
	public abstract String getSortHql();
}

  	