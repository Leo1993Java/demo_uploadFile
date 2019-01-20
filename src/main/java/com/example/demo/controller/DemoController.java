package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.dao.javaBean.UserInfo;
import com.example.demo.util.FileUtil;
import com.example.demo.util.ObjectUtil;
import com.example.demo.util.StrUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DemoController {

    /**
     * 文件类型 初始化
     * 如果增加了多功能，则可以在此处进行初始化
     */
    public Map<String,String> ContentType = new HashMap<String,String>(){{
        put("text/plain",".txt");
    }};

    @RequestMapping("index")
    public String index(){
        return "欢迎来到 梦想追逐者 后台管理！";
    }

    @RequestMapping("test")
    public String test(){
        return "hello-world 你好！";
    }

//    @RequestMapping("getUser")
//    public String getUser(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("username","李国强");
//        map.put("role","超级管理员");
//        return JSON.toJSONString(map);
//    }

    /**
     * {"globalData":{"userInfo":{"nickName":"Leo","gender":1,"language":"zh_CN","city":"Maoming","province":"Guangdong","country":"China","avatarUrl":"https://wx.qlogo.cn/mmopen/vi_32/4s4CMBAOMvoHFnDIcnsRK1tRYvw9hBxsBKxngNuak6eh24rSBucNEcr8qAZPcbJrSfYWTKNQibomrzjnUN4ZkBA/132"}}}
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getUser")
    public UserInfo getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,Object> map = new HashMap<>();
        String userInfoStr = request.getParameter("userInfo");
        Map<String,Object> _map = (Map<String, Object>) JSON.parse(String.valueOf(userInfoStr));
        Map<String,Object> global_map = (Map<String, Object>) JSON.parse(String.valueOf(_map.get("globalData")));
        Map<String,Object> userInfo_map = (Map<String, Object>) JSON.parse(String.valueOf(global_map.get("userInfo")));
        UserInfo userInfo = (UserInfo) ObjectUtil.mapToObject(userInfo_map, UserInfo.class);
        return userInfo;
    }

    /**
     * 文件上传
     * 进行保存
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "fileUpload",method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(HttpServletRequest request, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        long size = file.getSize();
        byte[] buffers = file.getBytes();
        System.out.println(StrUtil.toHexString(buffers));
        InputStream in = file.getInputStream();

        byte[] buffer = new byte[1024];
        int len = 0;
        filename = "/Users/leo/Downloads/" + filename + "_" +  new Date();//文件最终上传的位
        System.out.println("文件大小：" + size +",文件将会存放在：" + filename);
        OutputStream out = new FileOutputStream(filename);

        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.close();
        in.close();
        return "上传成功";
    }

    /**
     * 解析文件
     * @param request
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "fileResolve",method = RequestMethod.POST)
    @ResponseBody
    public String fileResolve(HttpServletRequest request, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        long size = file.getSize();
        String chSize = FileUtil.getPrintSize(size);
        String contentType = file.getContentType();
        if(size <= 0) {
            return "文件必须要有内容，否则不进行解析！";
        }
        if(contentType==null || !ContentType.containsKey(contentType) || "".equals(contentType)){
            return "该文件类型目前，目前能支持的文件类型：" + ContentType.keySet().toString() + ",后缀为：" + ContentType.values().toString();
        }
        System.out.println("即将解析文件：" + filename +"文件类型：" + contentType + ",文件大小：" + chSize);
        byte[] buffers = file.getBytes();
        System.out.println(StrUtil.toHexString(buffers));//字节码
        System.out.println(new String(buffers));//字节码对于的中文
        InputStream in = file.getInputStream();
        List<String> list = StrUtil.readTxtFileIntoStringArrList(in);
        System.out.println(list.toString());
        return "解析完成";
    }

    /**
     * 页面跳转类
     */
    @RequestMapping("fileUpload.html")
    public String fileUpload(){
        return "fileUpload.html";
    }
}
