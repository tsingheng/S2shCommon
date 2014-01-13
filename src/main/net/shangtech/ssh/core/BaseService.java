package net.shangtech.ssh.core;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 文件名： BaseService.java<br/>
 * 作者： 宋相恒<br/>
 * 版本： 2014-1-13 下午8:55:34 v1.0<br/>
 * 日期： 2014-1-13<br/>
 * 描述：
 */
public abstract class BaseService<T> {
	/**
	 * 作者： 宋相恒<br/>
	 * 版本： 2014-1-13 下午8:59:30 v1.0<br/>
	 * 日期： 2014-1-13<br/>
	 * @param entity<br/>
	 * 描述：存储数据
	 */
	public void add(T entity){
		try{
			dao().insert(entity);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
			//throw new RuntimeException("保存对象" + entity + "时出错");
		}
	}
	
	public T find(int id){
		try{
			return dao().find(id);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
			//throw new RuntimeException("查询[ID="+id+"]的类型" + getEntityClass().getName() + "时出错");
		}
	}
	
	public void delete(int id){
		try{
			dao().delete(id);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
			//throw new RuntimeException("删除[ID="+id+"]的类型" + getEntityClass().getName() + "时出错");
		}
	}
	
	public List<T> findAll(){
		try{
			return dao().findAll();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 作者： 宋相恒<br/>
	 * 版本： 2014-1-12 下午2:31:51 v1.0<br/>
	 * 日期： 2014-1-12<br/>
	 * @return<br/>
	 * 描述：获取实体T的类型
	 */
	@SuppressWarnings("unused")
	private Class<?> getEntityClass() {
		Class<?> entityClass = (Class<?>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		return entityClass;
	}
	protected abstract BaseDao<T> dao();
}

  	