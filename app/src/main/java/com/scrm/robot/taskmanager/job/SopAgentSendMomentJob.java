package com.scrm.robot.taskmanager.job;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.JobStateViewModel;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.AccessibilityGestureUtil;
import com.scrm.robot.utils.ApplicationUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SopAgentSendMomentJob extends BaseRobotJob {
    private final static String TAG = SopAgentSendMomentJob.class.getName();

    private static String targetTag = "获取失败";
    private static String tempTag = "";
    private static Boolean tagFindFlag = true;
    private static Boolean selectAllCustomerFlag = false;
    private static int canNotSelectAllCustomerTimes = 0;
    private AccessibilityGestureUtil accessibilityGestureUtil;
    private final static String packageName="com.tencent.wework";

    public SopAgentSendMomentJob(){
        super();
        this.setTaskStatus("START_SOP");
        RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
    }

    @Override
    public void run() {
        Log.d(TAG, String.format("%s start run", this.getJobId()));
        this.setJobState(RobotRunState.STARTED);
        this.setStartTime(new Date());
    }

    @Override
    public void stop() {
        Log.d(TAG, String.format("%s stop", this.getJobId()));
        super.stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void process() {
        if(this.getJobState()==RobotRunState.STOPPED){
            Log.d(TAG, String.format("%s processing is [stopped]", this.getJobId()));
            return;
        }


        if(this.getJobState()==RobotRunState.WAITING){
            Log.d(TAG, String.format("%s processing is [waiting]", this.getJobId()));
            return;
        }

        Log.d(TAG, String.format("%s processing", this.getJobId()));
        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
        RobotAccessibilityContext robotAccessibilityContext = application.getRobotAccessibilityContext();

        this.accessibilityGestureUtil=new AccessibilityGestureUtil(robotAccessibilityContext.getWeWorkAccessibilityService());
        tagFindFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;
        selectAllCustomerFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
        SopFriendCircle(rootNodeInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void SopFriendCircle(AccessibilityNodeInfo rootNodeInfo) {
        switch (this.getTaskStatus()) {
            case "START_SOP":
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
                backToMain(rootNodeInfo);
                break;
        }
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
        //点击之后的截图识别 ——> JobSchedulerMessageReceiver
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream("/sdcard/test.png");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        Bitmap bitmap  = BitmapFactory.decodeStream(fis);
//        Color color = bitmap.getColor(540,2070);
//        int pixel = bitmap.getPixel(540,2070);
//        System.out.println("red:"+color.red());
//        System.out.println("green:"+color.green());
//        System.out.println("blue:"+color.blue());
//        System.out.println("to ARGB:"+color.toArgb());
//        System.out.println("获取图片中该像素Color:"+color);
//        System.out.println("获取图片中该像素Pixel:"+pixel);

        switch (JobStateViewModel.sopType.getValue()) {
            case "noneed":
                Log.d(TAG, "CV:当前SOP已回执");
                this.setTaskStatus("BACK_TO_SOP_LIST_AND_DELETE");
                JobStateViewModel.isScreenShot.postValue(false);
                JobStateViewModel.sopType.postValue("new");
                break;
            case "need":
                Log.d(TAG, "CV:当前SOP未回执");
                //小米
//                this.accessibilityGestureUtil.click(100, 1550);
//                this.accessibilityGestureUtil.click(360, 1550);
//                this.accessibilityGestureUtil.click(360, 1550);
                //AVD
                this.accessibilityGestureUtil.click(540, 2070);
                JobStateViewModel.isScreenShot.postValue(false);
                JobStateViewModel.sopType.postValue("new");
                this.setTaskStatus("READY_TO_SHARE");
                break;
            case "loading":
                Log.d(TAG, "CV:当前SOP还未加载成功");
                JobStateViewModel.isScreenShot.postValue(false);
                JobStateViewModel.sopType.postValue("new");
                backToSopList(rootNodeInfo);
                break;
        }
        //加载成功且未回执的流程
//        System.out.println("准备点击一键分享");
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
                            return;
                        }
                    }
                }
                //和与缓存标签相同
                if(tempTag.equals(tagName.get(0).getChild(0).getText().toString())){
                    this.setTaskStatus("REPLY_SOP");
                }else {
                    tempTag = tagName.get(0).getChild(0).getText().toString();
                    performScroll(tagName.get(0).getParent().getParent().getParent().getParent().getParent());
                    System.out.println("翻一整页");
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

    public void inputText(AccessibilityNodeInfo targetInfo){
        //输入text
        try {
            targetInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, ResourceId.testServer);
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
            System.out.println("输入:"+ResourceId.testServer);
            inputText(targetUis.get(0));
        }
    }

    private void searchResult(AccessibilityNodeInfo rootNodeInfo) {
        //寻找->尝试点击搜索结果
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEARCH_RESULT);
        if(targetUis.size()>0){
            System.out.println("找到结果:"+ResourceId.testServer);
            performClick(targetUis.get(0).getParent());
        }
    }

    private void sopClickIn(AccessibilityNodeInfo rootNodeInfo) {
        //寻找->sop朋友圈消息->尝试点击进入
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SOP);
        if(targetUis.size()>0){
            Collections.reverse(targetUis);
            for(AccessibilityNodeInfo targetUi:targetUis){
                if(targetUi.getChildCount()>3){
                    String tagText = targetUi.getChild(1).getText().toString();
                    List<String> list = Arrays.asList(tagText.split("："));
                    targetTag = list.get(list.size()-1);
                    System.out.println("点击进入一条SOP,标签为:"+targetTag);
                    performClick(targetUi);
                    sysSleep(7000);
                    // 截图
                    if(!JobStateViewModel.isScreenShot.getValue()){
                        this.setJobState(RobotRunState.WAITING);

                        System.out.println("截图功能开启");
                        JobStateViewModel.isScreenShot.postValue(true); }
                    this.setTaskStatus("SCREENSHOT_CV");
                    return;
                }
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
                    System.out.println("删除一条SOP");
                    performLongClick(targetUis.get(0));
                    if(deleteUis.size()>0){
                        performClick(deleteUis.get(0).getParent());
                        this.setTaskStatus("DELETE_CONFIRM");
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
            this.setTaskStatus("START_SOP");
        }
    }

    private void chooseVisibleCustomer(AccessibilityNodeInfo rootNodeInfo){
        //寻找->可见的客户并点击
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.VISIBLE_0);
        List<AccessibilityNodeInfo> targetUis1 = rootNodeInfo.findAccessibilityNodeInfosByText("公开");
        if(targetUis.size()>0 && targetUis1.size()>0){
            System.out.println("点击可见的客户");
            performClick(targetUis.get(0));
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
            this.accessibilityGestureUtil.click(620, 1350);
            sysSleep(1000);
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

    public void backToMain(AccessibilityNodeInfo rootNodeInfo) {
        //返回主界面
        List<AccessibilityNodeInfo> userUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.USER);
        List<AccessibilityNodeInfo> chatUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CHAT);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (userUis.size()>0 && chatUis.size()>0){
            // TODO 任务完成
        }else {
            if(backUis.size()>0){
                performClick(backUis.get(0));
            }
            if(confirmUis.size()>0){
                performClick(confirmUis.get(0));
            }
        }
    }

    public void backToSopList(AccessibilityNodeInfo rootNodeInfo) {
        //返回到SOP列表
        List<AccessibilityNodeInfo> sopTitle = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PAGE_TITLE);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (sopTitle.size()>0 && sopTitle.get(0).getText().toString().equals(ResourceId.testServer)){
            setTaskStatus("START_SOP");
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
        if (sopTitle.size()>0 && sopTitle.get(0).getText().toString().equals(ResourceId.testServer)){
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