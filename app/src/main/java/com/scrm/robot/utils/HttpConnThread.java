package com.scrm.robot.utils;

import com.scrm.robot.LoginActivity;

//import org.json.JSONObject;

import com.alibaba.fastjson.JSONObject;
import com.scrm.robot.R;
import com.scrm.robot.RobotApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class HttpConnThread extends Thread{

    private static final String prodServer = "https://web.luoshurobot.cn/";
    private static final String testServer = "http://test.luoshuscrm.com/";
    private static String loginUrl = "api/account/v1/login";
    private static String userVerifyUrl = "api/wecom/user/profile";
    private static String companyVerifyUrl = "api/system/v1/tenant/profile";
    public static String token;
    public static String id;
    public static String userName;
    public static String outTime;

    @Override
    public void run(){
        loginUrl = "api/account/v1/login";
        userVerifyUrl = "api/wecom/user/profile";
        companyVerifyUrl = "api/system/v1/tenant/profile";
        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
        String loginServer = application.getString(R.string.loginServer);
        if(loginServer.equals("test")){
            loginUrl = testServer + loginUrl;
            userVerifyUrl = testServer + userVerifyUrl;
            companyVerifyUrl = testServer + companyVerifyUrl;
        }else{
            loginUrl = prodServer + loginUrl;
            userVerifyUrl = prodServer + userVerifyUrl;
            companyVerifyUrl = prodServer + companyVerifyUrl;
        }
        doPost(LoginActivity.Account,LoginActivity.PWd);
        doGetUserInfo();
        doGetCompanyInfo();
    }

    public String doPost(String account,String password){
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try{
            URL url = new URL(loginUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //发送POST请求必须设置为true
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            //获取输出流
            out = new OutputStreamWriter(conn.getOutputStream());
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("account", account);
            jsonParam.put("password", password);
            out.write(String.valueOf(jsonParam));
            out.flush();
            out.close();
            //取得输入流，并使用Reader读取
            if (200 == conn.getResponseCode()){
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = in.readLine()) != null){
                    result.append(line);
                    System.out.println(line);
                    JSONObject jsonObject = JSONObject.parseObject(line);
                    token = jsonObject.getString("token");
                }
            }else{
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return result.toString();
    }

    public String doGetUserInfo(){
        HttpURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try{
            //创建远程url连接对象
            URL url = new URL(userVerifyUrl);
            //通过远程url连接对象打开一个连接，强转成HTTPURLConnection类
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(60000);
//            conn.setRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Authorization","Bearer "+token);
            //发送请求
            conn.connect();
            //通过conn取得输入流，并使用Reader读取

            if (200 == conn.getResponseCode()){
                is = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line;
                while ((line = br.readLine()) != null){
                    result.append(line);
                    System.out.println(line);
                    JSONObject jsonObject = JSONObject.parseObject(line);
                    String data =  jsonObject.getString("data");
                    JSONObject dataJson = JSONObject.parseObject(data);
                    id = dataJson.getString("id");
                    userName = dataJson.getString("userName");
                }
            }else{
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(br != null){
                    br.close();
                }
                if(is != null){
                    is.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
            assert conn != null;
            conn.disconnect();
        }
        return result.toString();
    }

    public String doGetCompanyInfo(){
        HttpURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try{
            //创建远程url连接对象
            URL url = new URL(companyVerifyUrl);
            //通过远程url连接对象打开一个连接，强转成HTTPURLConnection类
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(60000);
            conn.addRequestProperty("Authorization","Bearer "+token);
            conn.connect();

            if (200 == conn.getResponseCode()){
                is = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                String line;
                while ((line = br.readLine()) != null){
                    result.append(line);
                    System.out.println(line);
                    JSONObject jsonObject = JSONObject.parseObject(line);
                    String data =  jsonObject.getString("data");
                    JSONObject dataJson = JSONObject.parseObject(data);
                    outTime = dataJson.getString("expireAt");
                    List<String> list = Arrays.asList(outTime.split("T"));
                    outTime = list.get(0);
                }
            }else{
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(br != null){
                    br.close();
                }
                if(is != null){
                    is.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
            assert conn != null;
            conn.disconnect();
        }
        return result.toString();
    }

}
