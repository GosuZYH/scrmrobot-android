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
            ResourceIdModel.put("NO_CUSTOMER","com.tencent.wework:id/d_m");
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
            ResourceIdModel.put("PAGE_TITLE",           "com.tencent.wework:id/kk7"); //页面title
            ResourceIdModel.put("BACK",                 "com.tencent.wework:id/kk6"); //任一处左上角返回按钮
            //SOP朋友圈
            ResourceIdModel.put("WEB_VIEW",             "com.tencent.wework:id/m3j"); //webview页面
            ResourceIdModel.put("SEARCH",               "com.tencent.wework:id/kl0"); //搜索按钮
            ResourceIdModel.put("SEARCH_LABEL",         "com.tencent.wework:id/iy7"); //搜索输入框
            // 底部导航，ViewGroup
            ResourceIdModel.put("BOTTOM_NAVIGATE_BAR",  "com.tencent.wework:id/k03"); //首页下方导航栏
            ResourceIdModel.put("SEARCH_RESULT",        "com.tencent.wework:id/evv"); //搜索结果
            ResourceIdModel.put("SOP",                  "com.tencent.wework:id/ggq"); //任务父节点
            ResourceIdModel.put("PUBLISH",              "com.tencent.wework:id/kkm"); //发表按钮
            ResourceIdModel.put("VISIBLE_PAGE",         "com.tencent.wework:id/ivo"); //信息编辑页
            ResourceIdModel.put("VISIBLE_0",            "com.tencent.wework:id/cja"); //可见的客户
            ResourceIdModel.put("VISIBLE_1",            "com.tencent.wework:id/hhl"); //部分可见
            ResourceIdModel.put("PERSONAL_TAG",         "com.tencent.wework:id/cgo"); //标签
            ResourceIdModel.put("CONFIRM_0",            "com.tencent.wework:id/c09"); //标签页确认按钮
            ResourceIdModel.put("CONFIRM_1",            "com.tencent.wework:id/j2v"); //选择可见客户页确认按钮
            ResourceIdModel.put("CONFIRM_2",            "com.tencent.wework:id/kkp"); //选择客户右上角确定
            ResourceIdModel.put("CONFIRM_3",            "com.tencent.wework:id/cfq"); //内容编辑页取消编辑继续编辑按钮
            ResourceIdModel.put("CONFIRM_4",            "com.tencent.wework:id/cfn"); //内容编辑页取消编辑确认按钮
            ResourceIdModel.put("PYQ",                  "com.tencent.wework:id/ibi"); //发送完成朋友圈
            ResourceIdModel.put("NO_CUSTOMER",          "com.tencent.wework:id/dbi"); //标签没可选用户
            //企业朋友圈
            ResourceIdModel.put("NEW_USER",             "com.tencent.wework:id/jox");
            ResourceIdModel.put("PYQ_MSG",              "com.tencent.wework:id/kkp"); //朋友圈页面右上角无红点
            ResourceIdModel.put("PYQ_MSG1",             "com.tencent.wework:id/kks"); //朋友圈页面右上角红点
            ResourceIdModel.put("COMPANY_NOTIFICATION", "com.tencent.wework:id/g3u"); //右上角点开后的企业通知
            ResourceIdModel.put("NO_CUSTOMER_PYQ",      "com.tencent.wework:id/dad"); //没客户朋友圈的空页
            ResourceIdModel.put("SEND_FLAG",            "com.tencent.wework:id/k9v"); //消息通知下方通知
            ResourceIdModel.put("SEND_FLAG1",           "com.tencent.wework:id/iq6"); //消息通知上方通知
            ResourceIdModel.put("SEND_PAGE",            "com.tencent.wework:id/ibi"); //全部消息父节点
            ResourceIdModel.put("NO_MSG_PAGE",          "com.tencent.wework:id/dad"); //没有私聊群发的空页
            ResourceIdModel.put("SEND_TIME",            "com.tencent.wework:id/jhz"); //企业朋友圈任务发送时间
            ResourceIdModel.put("SEND_BUTTON",          "com.tencent.wework:id/ilf"); //客户朋友圈发表按钮
        }
    }
}