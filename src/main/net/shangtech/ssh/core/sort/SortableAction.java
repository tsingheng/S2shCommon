package net.shangtech.ssh.core.sort;

import net.shangtech.ssh.core.BaseAction;


/**
 * 文件名： SortableAction.java<br/>
 * 作者： 宋相恒<br/>
 * 版本： 2014-1-15 下午8:00:27 v1.0<br/>
 * 日期： 2014-1-15<br/>
 * 描述：支持排序的action,对于排序,更新,删除,添加操作需要调整顺序
 * 		<p>sort值越大的排序越靠前</p>
 */
public abstract class SortableAction<T extends Sortable> extends BaseAction<T> {

	public String sort(){
		id = super.getId();
		if(id == null){
			return failed();
		}
		entity = service().find(id);
		if(entity == null){
			return failed();
		}
		String sortType = request.getParameter("sortType");
		if("down".equals(sortType) || "last".equals(sortType)){
			if(entity.getSort() <= 1)
				return failed("该记录已经是最后一条");
			if("down".equals(sortType))
				entity.setSort(entity.getSort()-1);
			else
				entity.setSort(1);
			service().update(entity, getValues());
			return success();
		}
		if("up".equals(sortType) || "first".equals(sortType)){
			int count = service().count(entity.getSortHql(), getValues());
			if(entity.getSort() >= count)
				return failed("该记录已经是第一条");
			if("up".equals(sortType))
				entity.setSort(entity.getSort()+1);
			else
				entity.setSort(count);
			service().update(entity, getValues());
			return success();
		}
		return null;
	}
	
	@Override
	protected void save(){
		if(id == null){
			service().add(entity, getValues());
		}else{
			service().update(entity, getValues());
		}
	}
	
	/**
	 * 添加前设置默认排序值,最大值跟默认值相等
	 */
	@Override
	public String add(){
		String result = super.add();
		if(result != null){
			int count = service().count(entity.getSortHql(), getValues());
			request.setAttribute("max", count+1);
			entity.setSort(count+1);
			request.setAttribute("entity", entity);
		}
		return result;
	}
	
	/**
	 * 修改前设置排序最大值
	 */
	@Override
	public String edit(){
		String result = super.edit();
		if(result != null){
			int count = service().count(entity.getSortHql(), getValues());
			request.setAttribute("max", count);
		}
		return result;
	}
	
	protected Object[] getValues(){
		return null;
	}
	
	@Override
	protected abstract SortableService<T> service();
	
}

  	