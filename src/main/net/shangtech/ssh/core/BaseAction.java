package net.shangtech.ssh.core;

import java.lang.reflect.ParameterizedType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.Preparable;

/**
 * 文件名： BaseAction.java<br/>
 * 作者： 宋相恒<br/>
 * 版本： 2014-1-14 下午9:47:23 v1.0<br/>
 * 日期： 2014-1-14<br/>
 * 描述：
 */
public abstract class BaseAction<T extends IBaseEntity> extends BaseMVC implements ServletRequestAware, ServletResponseAware, Preparable {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected T entity;
	protected Integer id;
	protected abstract BaseService<T> service();
	protected static final String GET = "GET";
	protected static final String FORM = "form";
	protected static final String INDEX = "index";
	
	@Override
	public void prepare() throws Exception {}
	
	/**
	 * 作者： 宋相恒<br/>
	 * 版本： 2014-1-14 下午10:01:25 v1.0<br/>
	 * 日期： 2014-1-14<br/><br/>
	 * 描述：添加或者修改的时候数据准备
	 */
	protected void prepareModel(){
		id = super.getId();
		if(id != null){
			entity = service().find(id);
		}else{
			try {
				entity = getEntityClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String execute(){
		if(GET.equals(request.getMethod())){
			return INDEX;
		}
		list();
		return null;
	}
	
	protected abstract void list();
	protected void save(){
		if(id == null){
			service().add(entity);
		}else{
			service().update(entity);
		}
	}
	
	protected void prepareEdit(){
		prepareModel();
	}
	
	public String edit(){
		if(GET.equals(request.getMethod())){
			request.setAttribute("entity", entity);
			return FORM;
		}
		id = super.getId();
		if(id == null){
			failed();
			return null;
		}
		save();
		return null;
	}
	protected void prepareAdd(){
		prepareModel();
	}
	public String add(){
		if(GET.equals(request.getMethod()))
			return FORM;
		save();
		return null;
	}
	
	/**
	 * 作者： 宋相恒<br/>
	 * 版本： 2014-1-14 下午10:17:40 v1.0<br/>
	 * 日期： 2014-1-14<br/>
	 * @return<br/>
	 * 描述：删除记录,该方法执行对id的检查,id存在时才执行真正的删除delete()操作
	 * 		<p>delete()默认为直接删除,如果要做其他检查要重写delete方法</p>
	 */
	public String del(){
		if(id == null){
			failed();
			return null;
		}
		delete();
		return null;
	}
	protected void delete(){
		service().delete(id);
		success();
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public HttpServletResponse getResponse() {
		return this.response;
	}

	@Override
	public HttpServletRequest getRequest() {
		return this.request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
}

  	