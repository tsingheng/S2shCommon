package net.shangtech.ssh.core;

import java.lang.reflect.ParameterizedType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 文件名： BaseAction.java<br/>
 * 作者： 宋相恒<br/>
 * 版本： 2014-1-14 下午9:47:23 v1.0<br/>
 * 日期： 2014-1-14<br/>
 * 描述：
 */
public abstract class BaseAction<T extends IBaseEntity> extends BaseMVC implements ServletRequestAware, ServletResponseAware, Preparable, ModelDriven<T> {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected T entity;
	protected Integer id;
	protected abstract BaseService<T> service();
	protected static final String GET = "GET";
	protected static final String FORM = "form";
	public static final String SUCCESS = "success";
	
	@Override
	public void prepare() throws Exception {
		this.id = super.getId();
	}
	
	/**
	 * 作者： 宋相恒<br/>
	 * 版本： 2014-1-14 下午10:01:25 v1.0<br/>
	 * 日期： 2014-1-14<br/><br/>
	 * 描述：添加或者修改的时候数据准备
	 */
	protected void prepareModel(){
		this.id = super.getId();
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
			return SUCCESS;
		}
		list();
		return null;
	}
	
	protected abstract void list();
	protected String checkSave(){
		return null;
	}
	protected void save(){
		String check = checkSave();
		if(StringUtils.isNotBlank(check)){
			failed(check);
			return;
		}
		if(entity.getId() == null){
			service().add(entity);
		}else{
			service().update(entity);
		}
	}
	
	public void prepareEdit(){
		prepareModel();
	}
	
	public String edit(){
		if(GET.equals(request.getMethod())){
			request.setAttribute("entity", entity);
			return FORM;
		}
		if(entity.getId() == null){
			failed();
			return null;
		}
		save();
		success();
		return null;
	}
	public void prepareAdd(){
		prepareModel();
	}
	public String add(){
		if(GET.equals(request.getMethod()))
			return FORM;
		save();
		success();
		return null;
	}
	
	public void prepareDel(){
		prepareModel();
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
		if(entity == null){
			failed("记录不存在");
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

	public T getModel(){
		return entity;
	}
	
	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
}

  	