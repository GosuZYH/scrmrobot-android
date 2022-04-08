package com.scrm.robot.taskmanager.job;

import com.scrm.robot.taskmanager.JobStateViewModel;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class ResourceId {
    public final static String WEWORK_MAIN_UI_CLASS_NAME="com.tencent.wework.launch.WwMainActivity";
    public static Map<String, String> ResourceIdModel = new HashMap<String,String>();

    public static void reloadResourceId(){
        ResourceIdModel.clear();
        String weworkVersion = JobStateViewModel.weworkVersion.getValue();
        if (weworkVersion.equals("v4.0.0")){
            ResourceIdModel.put("USER","com.tencent.wework:id/kcw");
            ResourceIdModel.put("USER_NAME","com.tencent.wework:id/jpy");
            ResourceIdModel.put("SETTING","com.tencent.wework:id/j5e");
            ResourceIdModel.put("PAGE_TITLE","com.tencent.wework:id/kbp");
            ResourceIdModel.put("BACK","com.tencent.wework:id/kbo");
            ResourceIdModel.put("BACK_1","com.tencent.wework:id/kbo");
            //SOP朋友圈
            ResourceIdModel.put("SEARCH","com.tencent.wework:id/kci");
            ResourceIdModel.put("SEARCH_LABEL","com.tencent.wework:id/iqz");
            // 底部导航，ViewGroup
            ResourceIdModel.put("BOTTOM_NAVIGATE_BAR","com.tencent.wework:id/js1");
            ResourceIdModel.put("WORKSTATION","com.tencent.wework:id/fz4");
            ResourceIdModel.put("SKIP","com.tencent.wework:id/anv");
            ResourceIdModel.put("TEXT","android.widget.TextView");
            ResourceIdModel.put("SEARCH_RESULT","com.tencent.wework:id/esq");
            ResourceIdModel.put("SMR_TITLE","com.tencent.wework:id/kbp");
            ResourceIdModel.put("SOP","com.tencent.wework:id/gb8");
            ResourceIdModel.put("SOP_TYPE","com.tencent.wework:id/ggh");
            ResourceIdModel.put("PUBLISH","com.tencent.wework:id/kc5");
            ResourceIdModel.put("VISIBLE_PAGE","com.tencent.wework:id/iop");
            ResourceIdModel.put("VISIBLE_0","com.tencent.wework:id/ci8");
            ResourceIdModel.put("VISIBLE_1","com.tencent.wework:id/hb6");
            ResourceIdModel.put("SELECT_TAG","com.tencent.wework:id/jul");
            ResourceIdModel.put("TAG_TITLE","com.tencent.wework:id/kbp");
            ResourceIdModel.put("TAG_GROUP","com.tencent.wework:id/c8z");
            ResourceIdModel.put("PERSONAL_TAG","com.tencent.wework:id/cfo");
            ResourceIdModel.put("SELECTED","com.tencent.wework:id/bta");
            ResourceIdModel.put("CONFIRM_0","com.tencent.wework:id/bzb");
            ResourceIdModel.put("CONFIRM_1","com.tencent.wework:id/ivc");
            ResourceIdModel.put("CONFIRM_2","com.tencent.wework:id/kc8");
            ResourceIdModel.put("CONFIRM_3","com.tencent.wework:id/ceq");
            ResourceIdModel.put("CONFIRM_4","com.tencent.wework:id/cen");
            ResourceIdModel.put("PYQ","com.tencent.wework:id/i5r");
            //企业朋友圈
            ResourceIdModel.put("CUSTOMER_PYQ","com.tencent.wework:id/grd");
            ResourceIdModel.put("PYQ_MSG","com.tencent.wework:id/kc8");
            ResourceIdModel.put("PYQ_MSG1","com.tencent.wework:id/kca");
            ResourceIdModel.put("COMPANY_NOTIFICATION","com.tencent.wework:id/fz4");
            ResourceIdModel.put("PUBLISH_1","com.tencent.wework:id/idv");
            ResourceIdModel.put("NO_CUSTOMER_PYQ","com.tencent.wework:id/d9i");
            ResourceIdModel.put("COMPANY_TASK","com.tencent.wework:id/i50");
            ResourceIdModel.put("COMPANY_TASK_TIME","com.tencent.wework:id/j_9");
            ResourceIdModel.put("GROUP_SEND_HELP","com.tencent.wework:id/i50");
            ResourceIdModel.put("GROUP_SEND","com.tencent.wework:id/g16");
            ResourceIdModel.put("GROUP_SEND_TIME","com.tencent.wework:id/k44");
            ResourceIdModel.put("SEND_FLAG","com.tencent.wework:id/k1m");
            ResourceIdModel.put("SEND_FLAG1","com.tencent.wework:id/ijd");
            ResourceIdModel.put("MSG_NUM_TITLE","com.tencent.wework:id/kap");
            ResourceIdModel.put("NEW_USER","com.tencent.wework:id/jh2");
            ResourceIdModel.put("CLEAR_TEXT","com.tencent.wework:id/iq1");
            ResourceIdModel.put("SEND_PAGE","com.tencent.wework:id/i5r");
            ResourceIdModel.put("NO_MSG_PAGE","com.tencent.wework:id/d9i");
            ResourceIdModel.put("SEND_TIME","com.tencent.wework:id/ja6");
            ResourceIdModel.put("START_TIME","com.tencent.wework:id/k44");
            ResourceIdModel.put("MSG_HEIGHT","com.tencent.wework:id/c5i");
            ResourceIdModel.put("SEND_BUTTON","com.tencent.wework:id/ien");
        }
        if (weworkVersion.equals("v4.0.3")){
            ResourceIdModel.put("USER","com.tencent.wework:id/kcw");
            ResourceIdModel.put("USER_NAME","com.tencent.wework:id/jpy");
            ResourceIdModel.put("SETTING","com.tencent.wework:id/j5e");
            ResourceIdModel.put("PAGE_TITLE","com.tencent.wework:id/kbp");
            ResourceIdModel.put("BACK","com.tencent.wework:id/kbo");
            ResourceIdModel.put("BACK_1","com.tencent.wework:id/kbo");
            //SOP朋友圈
            ResourceIdModel.put("SEARCH","com.tencent.wework:id/kci");
            ResourceIdModel.put("SEARCH_LABEL","com.tencent.wework:id/iqz");
            // 底部导航，ViewGroup
            ResourceIdModel.put("BOTTOM_NAVIGATE_BAR","com.tencent.wework:id/js1");
            ResourceIdModel.put("WORKSTATION","com.tencent.wework:id/fz4");
            ResourceIdModel.put("SKIP","com.tencent.wework:id/anv");
            ResourceIdModel.put("TEXT","android.widget.TextView");
            ResourceIdModel.put("SEARCH_RESULT","com.tencent.wework:id/esq");
            ResourceIdModel.put("SMR_TITLE","com.tencent.wework:id/kbp");
            ResourceIdModel.put("SOP","com.tencent.wework:id/gb8");
            ResourceIdModel.put("SOP_TYPE","com.tencent.wework:id/ggh");
            ResourceIdModel.put("PUBLISH","com.tencent.wework:id/kc5");
            ResourceIdModel.put("VISIBLE_PAGE","com.tencent.wework:id/iop");
            ResourceIdModel.put("VISIBLE_0","com.tencent.wework:id/ci8");
            ResourceIdModel.put("VISIBLE_1","com.tencent.wework:id/hb6");
            ResourceIdModel.put("SELECT_TAG","com.tencent.wework:id/jul");
            ResourceIdModel.put("TAG_TITLE","com.tencent.wework:id/kbp");
            ResourceIdModel.put("TAG_GROUP","com.tencent.wework:id/c8z");
            ResourceIdModel.put("PERSONAL_TAG","com.tencent.wework:id/cfo");
            ResourceIdModel.put("SELECTED","com.tencent.wework:id/bta");
            ResourceIdModel.put("CONFIRM_0","com.tencent.wework:id/bzb");
            ResourceIdModel.put("CONFIRM_1","com.tencent.wework:id/ivc");
            ResourceIdModel.put("CONFIRM_2","com.tencent.wework:id/kc8");
            ResourceIdModel.put("CONFIRM_3","com.tencent.wework:id/ceq");
            ResourceIdModel.put("CONFIRM_4","com.tencent.wework:id/cen");
            ResourceIdModel.put("PYQ","com.tencent.wework:id/i5r");
            //企业朋友圈
            ResourceIdModel.put("CUSTOMER_PYQ","com.tencent.wework:id/grd");
            ResourceIdModel.put("PYQ_MSG","com.tencent.wework:id/kc8");
            ResourceIdModel.put("PYQ_MSG1","com.tencent.wework:id/kca");
            ResourceIdModel.put("COMPANY_NOTIFICATION","com.tencent.wework:id/fz4");
            ResourceIdModel.put("PUBLISH_1","com.tencent.wework:id/idv");
            ResourceIdModel.put("NO_CUSTOMER_PYQ","com.tencent.wework:id/d9i");
            ResourceIdModel.put("COMPANY_TASK","com.tencent.wework:id/i50");
            ResourceIdModel.put("COMPANY_TASK_TIME","com.tencent.wework:id/j_9");
            ResourceIdModel.put("GROUP_SEND_HELP","com.tencent.wework:id/i50");
            ResourceIdModel.put("GROUP_SEND","com.tencent.wework:id/g16");
            ResourceIdModel.put("GROUP_SEND_TIME","com.tencent.wework:id/k44");
            ResourceIdModel.put("SEND_FLAG","com.tencent.wework:id/k1m");
            ResourceIdModel.put("SEND_FLAG1","com.tencent.wework:id/ijd");
            ResourceIdModel.put("MSG_NUM_TITLE","com.tencent.wework:id/kap");
            ResourceIdModel.put("NEW_USER","com.tencent.wework:id/jh2");
            ResourceIdModel.put("CLEAR_TEXT","com.tencent.wework:id/iq1");
            ResourceIdModel.put("SEND_PAGE","com.tencent.wework:id/i5r");
            ResourceIdModel.put("NO_MSG_PAGE","com.tencent.wework:id/d9i");
            ResourceIdModel.put("SEND_TIME","com.tencent.wework:id/ja6");
            ResourceIdModel.put("START_TIME","com.tencent.wework:id/k44");
            ResourceIdModel.put("MSG_HEIGHT","com.tencent.wework:id/c5i");
            ResourceIdModel.put("SEND_BUTTON","com.tencent.wework:id/ien");
        }
    }

    //server
    public static final String testServer = "SMR-代开发-test";
    public static final String prodServer = "SMR-代开发";
    //v4.0.0
    public static final String USER = "com.tencent.wework:id/kcw";
    public static final String USER_NAME = "com.tencent.wework:id/jpy";
    public static final String SETTING = "com.tencent.wework:id/j5e";
    public static final String PAGE_TITLE = "com.tencent.wework:id/kbp";
    public static final String BACK = "com.tencent.wework:id/kbo";
    public static final String BACK_1 = "com.tencent.wework:id/kbo";
    //SOP朋友圈
    public static final String SEARCH = "com.tencent.wework:id/kci";
    public static final String SEARCH_LABEL = "com.tencent.wework:id/iqz";
    // 底部导航，ViewGroup
    public static final String BOTTOM_NAVIGATE_BAR = "com.tencent.wework:id/js1";
    public static final String WORKSTATION = "com.tencent.wework:id/fz4";
    public static final String SKIP = "com.tencent.wework:id/anv";
    public static final String TEXT = "android.widget.TextView";
    public static final String SEARCH_RESULT = "com.tencent.wework:id/esq";
    public static final String SMR_TITLE = "com.tencent.wework:id/kbp";
    public static final String SOP = "com.tencent.wework:id/gb8";
    public static final String SOP_TYPE = "com.tencent.wework:id/ggh";
    public static final String PUBLISH = "com.tencent.wework:id/kc5";
    public static final String VISIBLE_PAGE = "com.tencent.wework:id/iop";
    public static final String VISIBLE_0 = "com.tencent.wework:id/ci8";
    public static final String VISIBLE_1 = "com.tencent.wework:id/hb6";
    public static final String SELECT_TAG = "com.tencent.wework:id/jul";
    public static final String TAG_TITLE = "com.tencent.wework:id/kbp";
    public static final String TAG_GROUP = "com.tencent.wework:id/c8z";
    public static final String PERSONAL_TAG = "com.tencent.wework:id/cfo";
    public static final String SELECTED = "com.tencent.wework:id/bta";
    public static final String CONFIRM_0 = "com.tencent.wework:id/bzb";
    public static final String CONFIRM_1 = "com.tencent.wework:id/ivc";
    public static final String CONFIRM_2 = "com.tencent.wework:id/kc8";
    public static final String CONFIRM_3 = "com.tencent.wework:id/ceq";
    public static final String CONFIRM_4 = "com.tencent.wework:id/cen";
    public static final String PYQ = "com.tencent.wework:id/i5r";
    //企业朋友圈
    public static final String CUSTOMER_PYQ = "com.tencent.wework:id/grd";
    public static final String PYQ_MSG = "com.tencent.wework:id/kc8";
    public static final String PYQ_MSG1 = "com.tencent.wework:id/kca";
    public static final String COMPANY_NOTIFICATION = "com.tencent.wework:id/fz4";
    public static final String PUBLISH_1 = "com.tencent.wework:id/idv";
    public static final String NO_CUSTOMER_PYQ = "com.tencent.wework:id/d9i";
    public static final String COMPANY_TASK = "com.tencent.wework:id/i50";
    public static final String COMPANY_TASK_TIME = "com.tencent.wework:id/j_9";
    public static final String GROUP_SEND_HELP = "com.tencent.wework:id/i50";
    public static final String GROUP_SEND = "com.tencent.wework:id/g16";
    public static final String GROUP_SEND_TIME = "com.tencent.wework:id/k44";
    public static final String SEND_FLAG = "com.tencent.wework:id/k1m";
    public static final String SEND_FLAG1 = "com.tencent.wework:id/ijd";
    public static final String MSG_NUM_TITLE = "com.tencent.wework:id/kap";
    public static final String NEW_USER = "com.tencent.wework:id/jh2";
    public static final String CLEAR_TEXT = "com.tencent.wework:id/iq1";
    public static final String SEND_PAGE = "com.tencent.wework:id/i5r";
    public static final String NO_MSG_PAGE = "com.tencent.wework:id/d9i";
    public static final String SEND_TIME = "com.tencent.wework:id/ja6";
    public static final String START_TIME = "com.tencent.wework:id/k44";
    public static final String MSG_HEIGHT ="com.tencent.wework:id/c5i";
    public static final String SEND_BUTTON = "com.tencent.wework:id/ien";
}