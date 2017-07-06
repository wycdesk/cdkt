package com.channelsoft.ems.api.po;

import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.channelsoft.cri.util.JsonUtils;
/**
 * json 接口成功返回对象
  *<dl>
  *<dt>类名：AjaxResultPo</dt>
  *<dd>描述: </dd>
  *<dd>公司: 青牛（北京）技术有限公司</dd>
  *<dd>创建时间：2010-7-23  下午01:48:13</dd>
  *<dd>创建人： 韦水生 </dd>
  *</dl>
 */
public class ResultPo implements Serializable {
	
	private static final long serialVersionUID = -4148768233386711389L;
	private boolean success;
	private String msg;
	private long total = -1;
	private Object rows;
	
	/**
	 * 返回一个失败的消息对象
	 */
	public static ResultPo failed(Exception e) {
		return new ResultPo(false, e.getMessage());
	}
	/**
	 * 返回一个成功的消息对象
	 * @param message
	 * @param data
	 * @return
	 * @CreateDate: 2013年11月7日 下午5:20:31
	 * @author 魏铭
	 */
	public static ResultPo success(String message, long total, Object rows){
		return new ResultPo(true, message, total, rows);
	}
	
	/**
	 * 缺省的成功消息对象
	 * @param data
	 * @return
	 * @CreateDate: 2013年11月7日 下午5:20:20
	 * @author 魏铭
	 */
	public static ResultPo successDefault (){
		return new ResultPo(true, "操作成功", 0, null);
	}


	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public Object getRows() {
		return rows;
	}
	public void setRows(Object rows) {
		this.rows = rows;
	}

	public ResultPo(boolean b){
		setSuccess(b);
		setMsg("");
	}
	
	public ResultPo(boolean success, String meassage) {
		super();
		this.success = success;
		this.msg = meassage;
	}
	
	public ResultPo(boolean success, Object rows) {
		super();
		this.success = success;
		this.rows = rows;
	}
	public ResultPo(boolean b, String msg, long total, Object rows){
		setSuccess(b);
		setMsg(msg);
		setTotal(total);
		setRows(rows);
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * ajax jsonp 跨域返回
	 *
	 * @return
	 * @CreateDate: 2015/11/17 下午20:57
	 * @author zhouzy
	 */
	public void returnJSONP(HttpServletRequest request,HttpServletResponse response) {
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			String callback = request.getParameter("callback");
            StringBuffer data = new StringBuffer();
			String rows = JsonUtils.toJson(this.rows);
            data.append("{")
                .append("success:"+this.success).append(",")
                .append("rows:"+rows).append(",")
                .append("total:"+this.total).append(",")
                .append("msg:\""+this.msg+"\"").append("}");
            if(StringUtils.isNotBlank(callback)){
            	out.write(callback + "("+data+")");
            }
            else{
            	out.write(data+"");
            }
			
			out.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out !=null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ajax jsonp 跨域返回
	 *
	 * @return
	 * @CreateDate: 2015/11/17 下午20:57
	 * @author zhouzy
	 */
	public String returnJSONP(HttpServletRequest request) {
		try {
			String callback = request.getParameter("callback");
            StringBuffer data = new StringBuffer();
			String rows = JsonUtils.toJson(this.rows);
            data.append("{")
                .append("success:"+this.success).append(",")
                .append("rows:"+rows).append(",")
                .append("total:"+this.total).append(",")
                .append("msg:\""+this.msg+"\"").append("}");
            if(StringUtils.isNotBlank(callback)){
              return callback + "("+data+")";
            }
            else{
            	return data+"";
            }
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
		return "";
	}
}
