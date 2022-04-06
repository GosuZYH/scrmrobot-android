package com.scrm.robot.taskmanager.job;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.orhanobut.logger.Logger;
import com.scrm.robot.R;
import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.JobStateViewModel;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.utils.AccessibilityGestureUtil;
import com.scrm.robot.utils.ApplicationUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SopAgentSendMomentJob extends BaseRobotJob {
    private final static String TAG = SopAgentSendMomentJob.class.getName();

    private static String searchSMR = "";
    private static String targetTag = "获取失败";
    private static String tempTag = "";
    public static String deleteTag = "";
    public static Boolean tagFindFlag = true;
    public static Boolean deleteFlag = false;
    public static Boolean selectAllCustomerFlag = false;
    private static int canNotSelectAllCustomerTimes = 0;
    public AccessibilityGestureUtil accessibilityGestureUtil;

    public SopAgentSendMomentJob(){
        super();
        this.setJobType(RobotJobType.SOP_AGENT_SEND_MOMENT);
        this.initTask();
    }

    public void initTask(){
        this.setTaskId(1);
        this.setTaskStatus("INIT_SOP_TASK");
        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
        // TODO NOW FIX HARDCODE
        searchSMR ="SMR-代开发-test" ;//application.getApplicationInfo().metaData.getString("input_text");
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void process() {
        super.process();
        if(!this.canProcess()){
            Logger.d( "任务不可处理: %s", this.toString());
            return;
        }
        Logger.d( "任务处理中: %s", this.toString());

        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
//        RobotAccessibilityContext robotAccessibilityContext = application.getRobotAccessibilityContext();
        RobotAccessibilityContext robotAccessibilityContext = this.getRobotAccessibilityContext();

        this.accessibilityGestureUtil=new AccessibilityGestureUtil(application.getWeWorkAccessibilityService());
        try {
            tagFindFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;
            selectAllCustomerFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        }catch (Exception e){
            tagFindFlag = true;
            selectAllCustomerFlag = false;
        }

        AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
        SopFriendCircle(rootNodeInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public String SopFriendCircle(AccessibilityNodeInfo rootNodeInfo) {
        switch (this.getTaskStatus()) {
            case "INIT_SOP_TASK":
                initToMain(rootNodeInfo);
                break;
            case "START_SOP_TASK":
                sopTaskStart(rootNodeInfo);
                break;
            case "SCREENSHOT_CV":
                openCv(rootNodeInfo);
                break;
            case "READY_TO_SHARE":
                shareTask(rootNodeInfo);
                break;
            case "CHOOSE_TAG":
                chooseTag(rootNodeInfo);
                break;
            case "CONFIRM_CUSTOMER":
                publishTask(rootNodeInfo);
                break;
            case "TAG_FOUND":
                tagFound(rootNodeInfo);
                break;
            case "REPLY_SOP":
                sopReplied(rootNodeInfo);
                break;
            case "BACK_TO_SOP_LIST":
                backToSopList(rootNodeInfo);
                break;
            case "BACK_TO_SOP_LIST_AND_DELETE":
                backToSopListAndDelete(rootNodeInfo);
                break;
            case "SOP_DELETE":
                sopDelete(rootNodeInfo);
                break;
            case "DELETE_CONFIRM":
                confirmDelete(rootNodeInfo);
                break;
            case "BACK_TO_MAIN":
                backToMainEnd(rootNodeInfo);
                break;
            case "TASK3_END":
                //for loop all task.
                return "START_GROUP_TASK";
        }
        return null;
    }

    private void sopTaskStart(AccessibilityNodeInfo rootNodeInfo) {
        //sop任务进入流程
        canNotSelectAllCustomerTimes = 0;
        uiSearch(rootNodeInfo);       //搜索
        uiSearchLabel(rootNodeInfo);      //搜索框输入
        searchResult(rootNodeInfo);       //搜索结果
        sopClickIn(rootNodeInfo);         //sop任务页
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("SdCardPath")
    private void openCv(AccessibilityNodeInfo rootNodeInfo) {
        switch (JobStateViewModel.sopType.getValue()) {
            case "noneed":
                Log.d(TAG, "CV:当前SOP已回执");
                deleteTag = targetTag;
                JobStateViewModel.isScreenShot.postValue(false);
                JobStateViewModel.sopType.postValue("new");
                this.accessibilityGestureUtil.click((int)(0.3*JobStateViewModel.width.getValue()), (int)(0.35*JobStateViewModel.height.getValue()));
                this.setTaskStatus("BACK_TO_SOP_LIST_AND_DELETE");
                break;
            case "need":
                Log.d(TAG, "CV:当前SOP未回执");
                JobStateViewModel.isScreenShot.postValue(false);
                JobStateViewModel.sopType.postValue("new");
//                this.accessibilityGestureUtil.click((int)(0.5*JobStateViewModel.width.getValue()), (int)(0.968*JobStateViewModel.height.getValue()));
                if(JobStateViewModel.x1.getValue()!=null && JobStateViewModel.y1.getValue()!=null){
                    this.accessibilityGestureUtil.click((int)(JobStateViewModel.x1.getValue()*JobStateViewModel.width.getValue()), (int)(JobStateViewModel.y1.getValue()*JobStateViewModel.height.getValue()));
                }else{
//                    this.accessibilityGestureUtil.click((int)(0.3*JobStateViewModel.width.getValue()), (int)(1.007*JobStateViewModel.height.getValue()));
                    this.accessibilityGestureUtil.click((int)(0.3*JobStateViewModel.width.getValue()), (int)(JobStateViewModel.height.getValue()+150-10));
                }
                this.setTaskStatus("READY_TO_SHARE");
                break;
            case "loading":
                Log.d(TAG, "CV:当前SOP还未加载成功");
                JobStateViewModel.isScreenShot.postValue(false);
                JobStateViewModel.sopType.postValue("new");
//                this.accessibilityGestureUtil.click((int)(0.5*JobStateViewModel.width.getValue()), (int)(0.35*JobStateViewModel.height.getValue()));
                this.accessibilityGestureUtil.swip((int)(JobStateViewModel.width.getValue()*0.5),(int)(JobStateViewModel.height.getValue()*0.5),(int)(JobStateViewModel.width.getValue()*0.5),(int)(JobStateViewModel.height.getValue()*0.4));
                this.setTaskStatus("BACK_TO_SOP_LIST");
                break;
        }
    }

    private void shareTask(AccessibilityNodeInfo rootNodeInfo) {
        //分享之后的流程
        chooseVisibleCustomer(rootNodeInfo);      //选择可见的客户
        choosePartialCustomer(rootNodeInfo);      //部分客户
        tagFilter(rootNodeInfo);      //根据标签筛选
    }

    private void chooseTag(AccessibilityNodeInfo rootNodeInfo){
        //寻找->选择对应标签
        List<AccessibilityNodeInfo> tagName = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PERSONAL_TAG);
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByText(targetTag);
//        System.out.println("标签组ui数量："+tagGroup.size());
        if (!tagFindFlag){
            sysSleep(200);
            return;
        }
        try {
            if(tagName.size()>0){
                for(int i=0;i<tagName.size();i++){
                    for(int k=0;k<tagName.get(i).getChildCount();k++){
                        String tag = tagName.get(i).getChild(k).getText().toString();
                        System.out.println("标签内容："+tag);
                        if(tag.equals(targetTag)){
                            performClick(tagName.get(i).getChild(k));
                            this.setTaskStatus("TAG_FOUND");
                            System.out.println("标签已找到");
                            tempTag = "";
                            return;
                        }
                    }
                }
                if(!tagName.get(0).getParent().getParent().getParent().getParent().getParent().isScrollable()){
                    System.out.println("当前没有多余标签页可翻");
                    performClick(tagName.get(0).getChild(0));
                    this.setTaskStatus("REPLY_SOP");
                    tempTag = "";
                    return;
                }
                //和与缓存标签相同
                if(tempTag.equals(tagName.get(0).getChild(0).getText().toString())){
                    System.out.println("当前页首标签与缓存标签相同");
                    this.setTaskStatus("REPLY_SOP");
                    tempTag = "";
                    performScrollUp(tagName.get(0).getParent().getParent().getParent().getParent().getParent());
                }else {
                    System.out.println("翻一整页");
                    tempTag = tagName.get(0).getChild(0).getText().toString();
                    performScroll(tagName.get(0).getParent().getParent().getParent().getParent().getParent());
                }
            }
        }catch (Exception e){
            Log.d(TAG,"选标签时出了点小错误："+e);
        }
        //当前页没找到
        tagFindFlag = false;
    }

    private void publishTask(AccessibilityNodeInfo rootNodeInfo){
        //发布流程
        confirmCustomer(rootNodeInfo);    //全部客户选择完成->确定
        _confirmCustomer(rootNodeInfo);   //部分可见->确定
        publishFriendCircle(rootNodeInfo); //发表
        friendCircleBack(rootNodeInfo);   //朋友圈返回
    }

    private void tagFound(AccessibilityNodeInfo rootNodeInfo) {
        //找到了标签
        confirmTag(rootNodeInfo);     //标签页->确定
        selectAllCustomer(rootNodeInfo);      //全选客户
    }

    public void performClick(AccessibilityNodeInfo targetInfo) {
        //ui动作:点击
        try {
            targetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }catch (Exception e){
            System.out.println("点击失败");
        }
    }

    public void performLongClick(AccessibilityNodeInfo targetInfo) {
        //ui动作:长按点击
        try {
            targetInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
        }catch (Exception e){
            System.out.println("点击失败");
        }
    }

    public void performScroll(AccessibilityNodeInfo targetInfo) {
        //ui动作:向下滑动
        try {
            targetInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }catch (Exception e){
            System.out.println("滑动失败");
        }
    }

    public void performScrollUp(AccessibilityNodeInfo targetInfo) {
        //ui动作:向上滑动
        try {
            targetInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
        }catch (Exception e){
            System.out.println("滑动失败");
        }
    }

    public void inputText(AccessibilityNodeInfo targetInfo){
        //输入text
        try {
            targetInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, searchSMR);
            targetInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }catch (Exception e){
            System.out.println("输入失败");
        }
    }

    private void uiSearch(AccessibilityNodeInfo rootNodeInfo) {
        //寻找->尝试点击搜索
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEARCH);
//        System.out.println("找到'搜索'ui数量"+targetUis.size());
        if(targetUis.size() > 0){
            System.out.println("点击搜索");
            performClick(targetUis.get(0));
        }
    }

    private void uiSearchLabel(AccessibilityNodeInfo rootNodeInfo) {
        //寻找->搜索框聚焦->输入
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEARCH_LABEL);
//        System.out.println("找到'搜索框'ui数量"+targetUis.size());
        if(targetUis.size()>0){
            System.out.println("输入:"+searchSMR);
            inputText(targetUis.get(0));
        }
    }

    private void searchResult(AccessibilityNodeInfo rootNodeInfo) {
        //寻找->尝试点击搜索结果
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEARCH_RESULT);
        if(targetUis.size()>0){
            System.out.println("找到结果:"+searchSMR);
            performClick(targetUis.get(0).getParent());
            sysSleep(1000);
        }
    }

    @SuppressLint("ResourceType")
    private void sopClickIn(AccessibilityNodeInfo rootNodeInfo) {
        //寻找->sop朋友圈消息->尝试点击进入
        List<AccessibilityNodeInfo> sopTitle = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PAGE_TITLE);
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SOP);
        if (sopTitle.size()>0 && sopTitle.get(0).getText().toString().equals(searchSMR)){
            System.out.println("当前已在SOP页");
            if(targetUis.size()>0){
                Collections.reverse(targetUis);
                for(AccessibilityNodeInfo targetUi:targetUis){
                    if(targetUi.getChildCount()>3){
                        try {
                            String tagText = targetUi.getChild(1).getText().toString();

                            //get current sop task tag
                            List<String> list = Arrays.asList(tagText.split("："));
                            targetTag = list.get(list.size()-1);

                            //get current sop task execute time
                            List<String> list1 = Arrays.asList(tagText.split("\n"));
                            String tagDate = list1.get(0);
                            String tagTime = tagText.substring(tagText.indexOf("当日")+2,tagText.indexOf("执行"));
                            String _tagTime = tagDate+tagTime;

                            //print test
                            System.out.println("点击进入一条SOP,标签为:"+targetTag+",该执行时间为:"+ _tagTime);

                            //get task stop time
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date());
                            RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
                            calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(application.getString(R.integer.sopDay)));  //向前推的天数
                            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(application.getString(R.integer.sopHour)));  //时
                            calendar.set(Calendar.MINUTE, Integer.parseInt(application.getString(R.integer.sopMin)));   //分
                            calendar.set(Calendar.SECOND, 0);   //秒
                            Date flagTime = calendar.getTime();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日HH:mm");
                            Date date = sdf.parse(_tagTime);
                            assert date != null;
                            System.out.println("可发送的时间线为："+flagTime+",执行时间格式为："+ date + "是否可执行："+date.after(flagTime));

                            //for no time limit test
//                        performClick(targetUi);
//                        sysSleep(6000);
//                        // 截图
//                        if(!JobStateViewModel.isScreenShot.getValue()){
//                            this.setJobState(RobotRunState.WAITING);
//                            System.out.println("截图功能开启");
//                            JobStateViewModel.isScreenShot.postValue(true); }
//                        this.setTaskStatus("SCREENSHOT_CV");

                            if(date.after(flagTime)){
                                performClick(targetUi);
                                sysSleep(3000);
                                // 截图
                                if(!JobStateViewModel.isScreenShot.getValue()){
                                    // TODO 暂停任务，不是停止？
                                    this.pause();
                                    Logger.d("截图功能开启");
                                    JobStateViewModel.isScreenShot.postValue(true);
                                }
                                this.setTaskStatus("SCREENSHOT_CV");
                            }else{
                                Log.d(TAG,"SOP已执行到截至时间点");
                                this.setTaskStatus("BACK_TO_MAIN");
                            }
                        } catch (ParseException e) {
                            Logger.e("SOP任务在信息处理中出现错误: %s",e);
                            this.setTaskStatus("BACK_TO_MAIN");
                        }
                        return;
                    }
                }
            }else{
                System.out.println("当前没有任何SOP朋友圈消息");
                this.setTaskStatus("BACK_TO_MAIN");
            }
        }
    }

    private void sopDelete(AccessibilityNodeInfo rootNodeInfo){
        //长按+删除
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SOP);
        List<AccessibilityNodeInfo> deleteUis = rootNodeInfo.findAccessibilityNodeInfosByText("删除");
        if(targetUis.size()>0) {
            Collections.reverse(targetUis);
            for (AccessibilityNodeInfo targetUi : targetUis) {
                if (targetUi.getChildCount() > 3) {
                    String tagText = targetUi.getChild(1).getText().toString();
                    List<String> list = Arrays.asList(tagText.split("："));
                    String sopTag = list.get(list.size()-1);
                    System.out.println("准备删除一条SOP,当前Tag为："+sopTag+",目标Tag为："+deleteTag);
                    if(deleteFlag || sopTag.equals(deleteTag)){
                        deleteFlag = true;
                        performLongClick(targetUis.get(0));
                        if(deleteUis.size()>0){
                            performClick(deleteUis.get(0).getParent());
                            this.setTaskStatus("DELETE_CONFIRM");
                            return;
                        }
                    }else{
                        System.out.println("当前最新的SOP不是要删除的SOP");
                        this.setTaskStatus("START_SOP_TASK");
                        return;
                    }
                }
            }
        }
    }

    private void confirmDelete(AccessibilityNodeInfo rootNodeInfo) {
        //长按删除后的确定
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_3);
        if(confirmUis.size()>0){
            performClick(confirmUis.get(0));
            this.setTaskStatus("START_SOP_TASK");
        }
    }

    private void chooseVisibleCustomer(AccessibilityNodeInfo rootNodeInfo){
        //寻找->可见的客户并点击
        List<AccessibilityNodeInfo> pageUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.VISIBLE_PAGE);
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.VISIBLE_0);
        List<AccessibilityNodeInfo> targetUis1 = rootNodeInfo.findAccessibilityNodeInfosByText("公开");
        if(targetUis.size()>0 && targetUis1.size()>0){
            System.out.println("点击可见的客户");
            performClick(targetUis.get(0));
        }
        if(pageUis.size()>0 && pageUis.get(0).isScrollable()){
            System.out.println("可见的客户页向下翻页");
            performScroll(pageUis.get(0));
        }
    }

    private void choosePartialCustomer(AccessibilityNodeInfo rootNodeInfo){
        //寻找->点击部分可见
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.VISIBLE_1);
        if(targetUis.size()>0 && targetUis.get(0).getChild(2).getChildCount()==0){
            System.out.println("点击部分可见");
            performClick(targetUis.get(0));
        }
    }

    private void tagFilter(AccessibilityNodeInfo rootNodeInfo){
        //寻找->点击根据标签筛选
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByText("根据标签筛选");
        if(targetUis.size()>0){
            System.out.println("点击部分可见");
            performClick(targetUis.get(0).getParent().getParent());
            this.setTaskStatus("CHOOSE_TAG");
        }
    }

    private void confirmTag(AccessibilityNodeInfo rootNodeInfo){
        //确定标签
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_0);
        if(targetUis.size()>0){
            System.out.println("点击标签-确定");
            performClick(targetUis.get(0));
        }
    }

    private void selectAllCustomer(AccessibilityNodeInfo rootNodeInfo){
        //全选客户
        if(!selectAllCustomerFlag){
            return;
        }
        List<AccessibilityNodeInfo> allCustomer = rootNodeInfo.findAccessibilityNodeInfosByText("全部客户");
        List<AccessibilityNodeInfo> confirm = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_1);
//        int allCustomerUis = allCustomer.size();
//        int confirmUis = confirm.size();
//        System.out.println("'全部客户'数量："+allCustomerUis+"确定数量："+confirmUis);
        if(allCustomer.size()>0 && confirm.size()>0){
            System.out.println("点击'全部客户'");
            performClick(allCustomer.get(0).getParent().getParent().getParent().getParent().getParent().getParent());
            this.setTaskStatus("CONFIRM_CUSTOMER");
        }else {
            canNotSelectAllCustomerTimes ++;
            if (canNotSelectAllCustomerTimes >3){
                Log.d(TAG,"找不到标签对应的客户");
                this.setTaskStatus("REPLY_SOP");
            }
        }
    }

    private void confirmCustomer(AccessibilityNodeInfo rootNodeInfo){
        //全选客户后点击
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_1);
        if(confirmUis.size()>0){
            System.out.println("发现确定按钮："+confirmUis.get(0).getText().toString());
            if(!confirmUis.get(0).getText().toString().equals("确定")){
                System.out.println("点击'确定'");
                performClick(confirmUis.get(0));
            }else {
                this.setTaskStatus("TAG_FOUND");
            }
        }
    }

    private void _confirmCustomer(AccessibilityNodeInfo rootNodeInfo){
        //部分可见->确定
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.VISIBLE_1);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_2);
        if(targetUis.size()>0 && targetUis.get(0).getChild(2).getChildCount()!=0 && confirmUis.size()>0){
            System.out.println("点击'确定'");
            performClick(confirmUis.get(0));
        }
    }

    private void publishFriendCircle(AccessibilityNodeInfo rootNodeInfo){
        //发表
        List<AccessibilityNodeInfo> publishUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PUBLISH);
        if(publishUis.size()>0){
            System.out.println("点击发表");
            performClick(publishUis.get(0));
        }
    }

    private void friendCircleBack(AccessibilityNodeInfo rootNodeInfo){
        //跳转到朋友圈界面就返回
        List<AccessibilityNodeInfo> pyqUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PYQ);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        if(pyqUis.size()>0 && backUis.size()>0){
            System.out.println("点击'返回'");
            performClick(backUis.get(0));
            this.setTaskStatus("REPLY_SOP");
        }
    }

    private void sopReplied(AccessibilityNodeInfo rootNodeInfo){
        //点击回执
        List<AccessibilityNodeInfo> pyqUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PAGE_TITLE);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if(pyqUis.size()>0 && pyqUis.get(0).getText().toString().equals("朋友圈")){
            System.out.println("点击'回执'");
            sysSleep(600);
            if(JobStateViewModel.x2.getValue()!=null && JobStateViewModel.y2.getValue()!=null){
                this.accessibilityGestureUtil.click((int)(JobStateViewModel.x2.getValue()*JobStateViewModel.width.getValue()), (int)(JobStateViewModel.y2.getValue()*JobStateViewModel.height.getValue()));
            }else{
                this.accessibilityGestureUtil.click((int)(0.86*JobStateViewModel.width.getValue()), (int)(0.857*JobStateViewModel.height.getValue()));
            }
            sysSleep(600);
            deleteTag = targetTag;
            this.setTaskStatus("BACK_TO_SOP_LIST_AND_DELETE");
        }else{
            System.out.println("点击'返回'");
            if(backUis.size()>0){
                performClick(backUis.get(0));
            }
            if(confirmUis.size()>0) {
                performClick(confirmUis.get(0));
            }
        }
    }

    public void initToMain(AccessibilityNodeInfo rootNodeInfo) {
        //寻找->点击消息
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BOTTOM_NAVIGATE_BAR);
        List<AccessibilityNodeInfo> chatUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BOTTOM_NAVIGATE_BAR);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (chatUis.size()>0){
            if(targetUis.size() > 0){
                Log.d(TAG,"点击消息");
                performClick(targetUis.get(0).getChild(0));
                this.setTaskStatus("START_SOP_TASK");
            }
        }else if(backUis.size()>0){
            performClick(backUis.get(0));
        }else if(confirmUis.size()>0){
            performClick(confirmUis.get(0));
        }
    }

    public void backToMainEnd(AccessibilityNodeInfo rootNodeInfo) {
        //返回主界面
        List<AccessibilityNodeInfo> chatUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BOTTOM_NAVIGATE_BAR);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (chatUis.size()>0){
            Log.d(TAG,"已返回到主界面，task3 end..");
            this.setTaskStatus("TASK3_END");
        }else if(backUis.size()>0){
            performClick(backUis.get(0));
        }else if(confirmUis.size()>0){
            performClick(confirmUis.get(0));
        }
    }

    public void backToSopList(AccessibilityNodeInfo rootNodeInfo) {
        //返回到SOP列表
        List<AccessibilityNodeInfo> sopTitle = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PAGE_TITLE);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (sopTitle.size()>0 && sopTitle.get(0).getText().toString().equals(searchSMR)){
            setTaskStatus("START_SOP_TASK");
        }else {
            if(backUis.size()>0){
                performClick(backUis.get(0));
            }
            if(confirmUis.size()>0){
                performClick(confirmUis.get(0));
            }
        }
    }

    public void backToSopListAndDelete(AccessibilityNodeInfo rootNodeInfo) {
        //返回到SOP列表
        List<AccessibilityNodeInfo> sopTitle = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PAGE_TITLE);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (sopTitle.size()>0 && sopTitle.get(0).getText().toString().equals(searchSMR)){
            deleteFlag = true;
            this.setTaskStatus("SOP_DELETE");
        }else {
            if(backUis.size()>0){
                performClick(backUis.get(0));
            }
            if(confirmUis.size()>0){
                performClick(confirmUis.get(0));
            }
        }
    }

    private void sysSleep(int msecond) {
        //睡眠 param:seconds
        try {
            System.out.println("睡眠一秒");
            Thread.sleep(msecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //for RGB test


}