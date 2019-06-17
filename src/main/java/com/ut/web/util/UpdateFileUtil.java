package com.ut.web.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


public class UpdateFileUtil {
	
	private static Logger log = LogManager.getLogger(UpdateFileUtil.class);
	
	public static HashMap<String,String> saveUploadFile(HttpServletRequest request, HttpServletResponse response, String savePath) {
		
		HashMap<String,String> params = new HashMap<String,String>();
		
		long  startTime=System.currentTimeMillis();
		 //消息提示
        String message = "0";		
		File uploadFile = new File(savePath);
        //判断上传文件的保存目录是否存在
        if (!uploadFile.exists() && !uploadFile.isDirectory()) {
            System.out.println(savePath+"目录不存在，需要创建");
            //创建目录
            uploadFile.mkdirs();
        }
		
		
         //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
           //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();
             
            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null)
                {
                	String filename = file.getOriginalFilename();
                    String path=savePath + "/" +filename;
                    String fileSize = file.getSize() + "";
                    //获得文件名和文件大小
                    params.put("fileName", filename);
                    params.put("fileSize", fileSize);
                    //上传
					try {
						file.transferTo(new File(path));
						message = "1";
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						log.error("上传文件失败"+ e.getMessage());
						message = "0";
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.error("上传文件失败"+ e.getMessage());
						message = "0";
					}
                }
            }
        }
        long  endTime=System.currentTimeMillis();
        System.out.println("上传文件运行时间："+String.valueOf(endTime-startTime)+"ms");
		
        params.put("uploadOk", message);
		return params;
		
	}

}
