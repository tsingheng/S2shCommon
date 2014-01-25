package net.shangtech.ssh.core.sort;

import org.apache.commons.lang.ArrayUtils;

import net.shangtech.ssh.core.BaseService;

/**
 * 文件名： SortableService.java<br/>
 * 作者： 宋相恒<br/>
 * 版本： 2014-1-15 下午8:34:03 v1.0<br/>
 * 日期： 2014-1-15<br/>
 * 描述：
 */
public abstract class SortableService<T extends Sortable> extends BaseService<T> {
	public void add(T entity, Object...values){
		try{
			int count = dao().count(entity.getSortHql(), values);
			if(entity.getSort() == null || entity.getSort() > count+1){
				entity.setSort(count+1);
			}else{
				//其他情况属于在中间插入,要把原来排在该位置及以上的所有数据上移一个单位
				String queryString = "update " + getEntityClass().getSimpleName() + " o set sort=sort+1 " + entity.getSortHql() + " and sort>=?";
				dao().execute(queryString, ArrayUtils.add(values, entity.getSort()));
			}
			dao().insert(entity);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void update(T entity, Object...values){
		try{
			T old = dao().find(entity.getId());
			int count = dao().count(entity.getSortHql(), values);
			if(entity.getSort() < 1){
				entity.setSort(1);
			}else if(entity.getSort() > count){
				entity.setSort(count);
			}
			String queryString = "update " + getEntityClass().getSimpleName() + " o ";
			//如果是下移,要把新位置与旧位置之间的数据全部上移一个单位,不包括自己
			//如果是上移,要把新位置与旧位置之间的数据全部下移一个单位,不包括自己
			if(entity.getSort() < old.getSort()){
				queryString += "set sort=sort+1 " + entity.getSortHql() + " and sort>=? and sort<?";
			}else if(entity.getSort() > old.getSort()){
				queryString += "set sort=sort-1 " + entity.getSortHql() + " and sort<=? and sort>?";
			}
			if(!entity.getSort().equals(old.getSort()))
				dao().execute(queryString, ArrayUtils.addAll(values, new Object[]{entity.getSort(), old.getSort()}));
			dao().update(entity);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void delete(int id, Object...values){
		try{
			T entity = dao().find(id);
			//删除之后要把排在上面的数据全部都下移一个单位
			String queryString = "update " + getEntityClass().getSimpleName() + " o set sort=sort-1 " + entity.getSortHql() + " and sort>?";
			dao().execute(queryString, ArrayUtils.add(values, entity.getSort()));
			dao().delete(id);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}

  	