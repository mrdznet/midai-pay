package com.midai.pay.common.constants;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Constants {

	public static final int CHANNEL_WEB = 0;
	public static final int CHANNEL_APP =1;
	
	/** 企业代码 */
	public static final String MIFU_INSCODE = "101002";
	public static final String HAIBEI_INSCODE = "101003";
	public static final String ZHANGFU_INSCODE = "101004";
	
	/** 企业小票编号*/
	public static final String MIFU_MERID = "01";
	public static final String HAIBEI_MERID = "02";
	public static final String ZHANGFU_MERID = "03";
	
    /** 状态 设备状态 */
    /* 未开通 */
    public static final Integer DEVICE_NO_OPEN=1;
    /* 已开通 */
    public static final Integer DEVICE_OPEN=2;
    /* 激活 */
    public static final Integer DEVICE_ACTIVATION=3;

    /** 操作状态 */
    /* 出库 */
    public static final Integer OPERATE_STATE_OUTSTORAGE=1;
    /* 变更 */
    public static final Integer OPERATE_STATE_CHANGE=2;

    /** 是否库存设备 1 库存 2已出库 */
    /* 库存 */
    public static final Integer IS_INVENTORY=1;
    /* 已出库-非库存 */
    public static final Integer NOT_INVENTORY=2;

    /** 首选项：1:首选|0:否 */
    /* 是首选 */
    public static final Integer IS_FIRST=1;
    /* 非首选 */
    public static final Integer NOT_FIRST=0;
    
    
    /************************设备类型状态*************************/
    /**
     * 设备类型状态-废除
     */
    public static final Integer DEVICE_TYPE_STATE_ABOLISH = 0;
    
    /**
     * 设备类型状态-启用
     */
    public static final Integer DEVICE_TYPE_STATE_ENABLE = 1;
    
    /**
     * 设备类型状态-暂停
     */
    public static final Integer DEVICE_TYPE_STATE_SUSPEND = 2;
    
    /**
     * 设备类型状态Map
     */
    public static Map<Integer, String> DEVICE_TYPE_STATE_MAP = new LinkedHashMap<Integer, String>();
    static {
    	DEVICE_TYPE_STATE_MAP.put(DEVICE_TYPE_STATE_ABOLISH, "废除");
    	DEVICE_TYPE_STATE_MAP.put(DEVICE_TYPE_STATE_ENABLE, "启用");
    	DEVICE_TYPE_STATE_MAP.put(DEVICE_TYPE_STATE_SUSPEND, "暂停");
    }
    
    
    /************************设备型号状态*************************/
    /**
     * 设备型号状态-废除
     */
    public static final Integer DEVICE_MODE_STATE_ABOLISH = 0;
    
    /**
     * 设备型号状态-启用
     */
    public static final Integer DEVICE_MODE_STATE_ENABLE = 1;
    
    /**
     * 设备型号状态-暂停
     */
    public static final Integer DEVICE_MODE_STATE_SUSPEND = 2;
    
    /**
     * 设备型号状态Map
     */
    public static Map<Integer, String> DEVICE_MODE_STATE_MAP = new LinkedHashMap<Integer, String>();
    static {
    	DEVICE_MODE_STATE_MAP.put(DEVICE_MODE_STATE_ABOLISH, "废除");
    	DEVICE_MODE_STATE_MAP.put(DEVICE_MODE_STATE_ENABLE, "启用");
    	DEVICE_MODE_STATE_MAP.put(DEVICE_MODE_STATE_SUSPEND, "暂停");
    }
    
    
    /***************************设备类型*****************************/
    /**
     * 设备类型-刷卡器
     */
    public static final Integer DEVICE_TYPE_SKQ = 0;
    
    /**
     * 设备类型-刷卡头
     */
    public static final Integer DEVICE_TYPE_SKT = 1;
    
    /**
     * 设备类型名称-刷卡器
     */
    public static final String DEVICE_TYPE_NAME_SKQ = "刷卡器";
    
    /**
     * 设备类型名称-刷卡头
     */
    public static final String DEVICE_TYPE_NAME_SKT = "刷卡头";
    
    public static final int JYSH_MODULE_ID = 36;
    public static final int JYJS_MODULE_ID = 45;
    public static final int QFQS_MODULE_ID = 51;
    public static final int ZDDK_MODULE_ID = 52;
}
