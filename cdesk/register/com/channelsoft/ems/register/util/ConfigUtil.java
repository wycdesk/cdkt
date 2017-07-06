package com.channelsoft.ems.register.util;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {
	private static Logger log = LoggerFactory.getLogger(ConfigUtil.class);
	private static Properties properties = null;
	
	public final static String AGENT_APPKEY = "agent.appKey";
	public final static String CUSTOM_APPKEY = "custom.appKey";
	public final static String BUTEL_WEBSERVICE = "butel.webservice";
	public final static String BUTEL_WEBSERVICE_SETMULTAGENT = "setMultAgent";
	public final static String EUC_WEBSERVICE = "euc.webservice";
	public final static String EUC_WEBSERVICE_REGISTERUSER = "registerUser";
	public final static String DOWNLOAD_URL = "downloadUrl";
	
	public final static String CCBMS_ADDENT_URL = "ccbms.addentUrl";
	
	public final static String INTERGRATION_URL = "intergration.url";
	
//	//增加了技能组的排队策略配置之后，没有用了
//	public final static String BUTEL_QUEUE_STRATEGY = "butel.queue.strategy";
//	
	//用户报表数据的前缀
	public static String RECORD_DB_PREFIX = null;
	
//	//需要初始化的技能组用”;“隔开
//	public final static String INIT_SKILLGROUPS = "init.skillGroups"; 
//	
//	//需要初始化的坐席列表用”;“隔开，每组之间的坐席用”，“隔开
//	public final static String INIT_AGENTIDS = "init.agentIds";
//	
//	public static List<SkillGroupConfig> SKILL_GROUP_CONFIG_LIST;
	
	static {
		loadProperties();
		RECORD_DB_PREFIX = getString("record.db.prefix");
	}

	private static void loadProperties() {
		InputStream istream = null;
		try {
			istream = ConfigUtil.class.getResourceAsStream("/config.properties");
			properties = new Properties();
			properties.load(istream);
		} catch (Exception e) {
			log.error("读取属性文件[config.properties]失败.", e);
			return;
		} finally {
			if (istream != null) {
				try {
					istream.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	public static void reloadProperties() {
		loadProperties();
	}

	private ConfigUtil() {
	}

	public static String getString(String key) {
		return properties.getProperty(key);
	}

	public static String getString(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static int getInt(String key) {
		return Integer.parseInt(getString(key));
	}

	public static int getInt(String key, int defaultValue) {
		return Integer.parseInt(getString(key, String.valueOf(defaultValue)));
	}

	public static long getLong(String key, long defaultValue) {
		return Long.parseLong(getString(key, String.valueOf(defaultValue)));
	}

	public static Date getDate(String key, Date defaultValue) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(getString(key));
		} catch (ParseException e) {
			return defaultValue;
		} catch (NullPointerException e) {
			return defaultValue;
		}
	}
	
//	public synchronized static List<SkillGroupConfig> loadInitConfig() {
//		if (SKILL_GROUP_CONFIG_LIST == null) {
//			String sgs = getString(INIT_SKILLGROUPS);
//			String[] asgs = sgs.split(";");
//			String as = getString(INIT_AGENTIDS);
//			String[] aas = as.split(";");
//			SKILL_GROUP_CONFIG_LIST = new ArrayList<SkillGroupConfig>();
//			for (int i=0;i<asgs.length;i++) {
//				List<Agent> agentList = new ArrayList<Agent>();
//				String[] agents = aas[i].split(",");
//				for (String agentStr : agents) {
//					//遍历坐席
//					Agent a = new Agent();
//					String[] aaa = agentStr.split("\\|");
//					a.setAgentId(aaa[0]);
//					a.setAgentName(aaa[1]);
//					agentList.add(a);
//				}
//				SkillGroupConfig sgc = new SkillGroupConfig(asgs[i], Constants.TskillGroupConstants.COMMON_TYPE, agentList);
//				SKILL_GROUP_CONFIG_LIST.add(sgc);
//			}
//		}
//		return SKILL_GROUP_CONFIG_LIST;
//	}
	
	
}
