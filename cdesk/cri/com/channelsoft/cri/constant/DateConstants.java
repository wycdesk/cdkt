package com.channelsoft.cri.constant;

import java.text.SimpleDateFormat;
/**
 * 时间格式常量类。
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:SimpleDateFormat线程不安全</dd>
 * <dd>不能直接使用静态常量</dd>
 * <dd>需要借助于ThreadLocal实现线程安全。</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-26</dd>
 * </dl>
 * @author 魏铭
 */
public class DateConstants {
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_NUM  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("yyyyMMddHHmmss");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_NUM() {   
		return (SimpleDateFormat) DATE_FORMAT_NUM.get();   
	}    
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_NUM_SHORT  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("yyyyMMdd");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_NUM_SHORT() {   
		return (SimpleDateFormat) DATE_FORMAT_NUM_SHORT.get();   
	} 
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_NUM_YM  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("yyyyMM");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_NUM_YM() {   
		return (SimpleDateFormat) DATE_FORMAT_NUM_YM.get();   
	} 
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_NUM_HMS  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("HHmmss");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_NUM_HMS() {   
		return (SimpleDateFormat) DATE_FORMAT_NUM_HMS.get();   
	} 
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT() {   
		return (SimpleDateFormat) DATE_FORMAT.get();   
	} 
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_YMDH  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("yyyy-MM-dd HH");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_YMDH() {   
		return (SimpleDateFormat) DATE_FORMAT_YMDH.get();   
	} 
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_SHORT  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("yyyy-MM-dd");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_SHORT() {   
		return (SimpleDateFormat) DATE_FORMAT_SHORT.get();   
	} 
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_CH_SHORT  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("MM月dd日");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_CH_SHORT() {   
		return (SimpleDateFormat) DATE_FORMAT_CH_SHORT.get();   
	} 
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_MD  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("MM-dd");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_MD() {   
		return (SimpleDateFormat) DATE_FORMAT_MD.get();   
	}
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_HM  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("HH:mm");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_HM() {   
		return (SimpleDateFormat) DATE_FORMAT_HM.get();   
	} 
	
	@SuppressWarnings("rawtypes")
	private static ThreadLocal DATE_FORMAT_HMS  = new ThreadLocal() {   
		protected synchronized Object initialValue() {   
			return new SimpleDateFormat("HH:mm:ss");   
		}   
	};   
	public static SimpleDateFormat DATE_FORMAT_HMS() {   
		return (SimpleDateFormat) DATE_FORMAT_HMS.get();   
	} 
}
