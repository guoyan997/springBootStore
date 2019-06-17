package com.ut.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
@Component
public class CommonUtil {
	
	
    
    
	 // 获取项目路径，用于拼接文件和图标的路径
	 public static String getProjectPath(HttpServletRequest request) {
		 String projectPath = request.getScheme()+"://"+request.getRemoteAddr()+":"+request.getServerPort() 
				 +request.getServletContext().getContextPath();
		 return projectPath;
	 }
	 
	 public static String getHttpsUrlAddress(){
		 // 从配置文件中读取参数
		 Properties prop = new Properties();
         InputStream in = CommonUtil.class.getClassLoader().getResourceAsStream("application.properties");
         try {
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         String httpsUrl=prop.getProperty("httpsUrl");
		 return httpsUrl;
	 }
	 
	 public static String getHttpsResourceUrl(String fileUrl){
		 if(fileUrl == null){
			 return null;
		 }
		 String httpsUrl = getHttpsUrlAddress();
		 if(fileUrl.indexOf("output")>=0){
			 String httpsResourceUrl = httpsUrl + "/" +fileUrl.substring(fileUrl.indexOf("output")).replaceAll("\\\\", "/");
			 return httpsResourceUrl;
		 }else{
			return fileUrl;
		 }
//		 String httpsResourceUrl = httpsUrl + "/" +fileUrl.substring(fileUrl.indexOf("upload")).replaceAll("\\\\", "/");
//		 return httpsResourceUrl;
	 }
	 
	// 删除文件夹和该文件夹下的所有内容，用于上传失败的时候 
	 public static void deleteAll(File file) {
		   System.gc();
		  if (!file.exists()) {
	          
	        }else {
		        if (file.isFile() || file.list().length == 0) {
		            file.delete();
		        } else {
		            for (File f : file.listFiles()) {
		                deleteAll(f); // 递归删除每一个文件
		            }
		            file.delete(); // 删除文件夹
		        }
	    }
	 }
	 
	 
	 //拷贝文件夹下所有文件, 
	 public static void copyFiles(File file, File toFile) throws Exception {  
	        byte[] b = new byte[1024];  
	        int a;  
	        FileInputStream fis;  
	        FileOutputStream fos;  
	        if (file.isDirectory()) {  
	            String filepath = file.getAbsolutePath();  
	            filepath=filepath.replaceAll("\\\\", "/");  
	            String toFilepath = toFile.getAbsolutePath();  
	            toFilepath=toFilepath.replaceAll("\\\\", "/");  
	            int lastIndexOf = filepath.lastIndexOf("/");  
	            toFilepath = toFilepath + filepath.substring(lastIndexOf ,filepath.length());  
	            File copy=new File(toFilepath);  
	            //复制文件夹  
	            if (!copy.exists()) {  
	                copy.mkdir();  
	            }  
	            //遍历文件夹  
	            for (File f : file.listFiles()) {  
	            	copyFiles(f, copy);  
	            }  
	        } else {  
	            if (toFile.isDirectory()) {  
	                String filepath = file.getAbsolutePath();  
	                filepath=filepath.replaceAll("\\\\", "/");  
	                String toFilepath = toFile.getAbsolutePath();  
	                toFilepath=toFilepath.replaceAll("\\\\", "/");  
	                int lastIndexOf = filepath.lastIndexOf("/");  
	                toFilepath = toFilepath + filepath.substring(lastIndexOf ,filepath.length());  
	                  
	                //写文件  
	                File newFile = new File(toFilepath);  
	                fis = new FileInputStream(file);  
	                fos = new FileOutputStream(newFile);  
	                while ((a = fis.read(b)) != -1) {  
	                    fos.write(b, 0, a);  
	                }  
	            } else {  
	                //写文件  
	                fis = new FileInputStream(file);  
	                fos = new FileOutputStream(toFile);  
	                while ((a = fis.read(b)) != -1) {  
	                    fos.write(b, 0, a);  
	                }  
	            }  
	  
	        }  
	    }  
	 
	  //利用freemark把数据插入到模板中
	 public static void putDataIntoTemplet(String fileDirPath,String fileName,  Map<String, String> mapData, String outFilePath) throws Exception {
		 
		  //1.创建配置实例Cofiguration  
	        Configuration cfg = new Configuration();  
	        //2.设置模板文件目录  
	        //（1）src目录下的目录（template在src下）  
	        //cfg.setDirectoryForTemplateLoading(new File("src/template"));  
	        //（2）完整路径（template在src下）  
	        //cfg.setDirectoryForTemplateLoading(new File(  
	        //      "D:/cpic-env/workspace/javaFreemarker/src/template"));  
	        //cfg.setDirectoryForTemplateLoading(new File("src/template"));  
	        //（3）工程目录下的目录（template/main在工程下）--推荐  
	        cfg.setDirectoryForTemplateLoading(new File(fileDirPath));  
	        //cfg.setObjectWrapper(new DefaultObjectWrapper());  
	        //获取模板（template）  
	        Template template = cfg.getTemplate(fileName);  
	        //建立数据模型（Map）  
//	        Map<String, String> root = new HashMap<String, String>();  
//	        root.put("name", "cxl");  
//	        root.put("age", "25");  
	        //获取输出流（指定到控制台（标准输出））  
	        // Writer out = new OutputStreamWriter(System.out);  
	        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outFilePath), "UTF-8");  
	        //StringWriter out = new StringWriter();  
	        //System.out.println(out.toString());  
	        //数据与模板合并（数据+模板=输出）  
	        template.process(mapData, out);  
	        out.flush();  
		 
		 
		 
	 }
	 
	 
	 /** 
	  * 将长整型数字(1516672001461这种格式的时间)转换为日期时间格式的字符串 
	  */  
	 public static String convertDateToString(String timeStr) {  
	  if (timeStr!=null && !timeStr.trim().equals("")) {
		   long time = Long.parseLong(timeStr) ;
		   String format = "yyyy-MM-dd HH:mm:ss"; 
		   SimpleDateFormat sf = new SimpleDateFormat(format);  
		   Date date = new Date(time);  
		   return sf.format(date);  
	  }else {
		  return ""; 
	  }
	 } 
	 /**
	  * 将文件大小，转化为mb的格式
	  */
	 public static String convertFileSize(String fileSizeStr) {  	
		 if (fileSizeStr!=null && !fileSizeStr.trim().equals("")) {
			 double filesize = Double.parseDouble(fileSizeStr)/1000000;
			return new java.text.DecimalFormat("#.00").format(filesize);
		  }else {
			  return "0"; 
		  }
		 
	 } 
	 
	 
	 
	 
	  
	 /**
	 public static void copyDir(File fromfile, File toFile) throws Exception {  
		 
	        FileInputStream fis = null;  
	        FileOutputStream fos = null;  
	        //用字节输入输出流  
	        File[] file = fromfile.listFiles();  
	        for(int i=0;i<file.length;i++){  
	            String path = file[i].getAbsolutePath();  
	            String filepath = path.replaceAll("\\\\", "/");
	            String toFilepath = toFile.getAbsolutePath();  
	            toFilepath=toFilepath.replaceAll("\\\\", "/");  
	            int lastIndexOf = filepath.lastIndexOf("/");  
	            toFilepath = toFilepath + filepath.substring(lastIndexOf ,filepath.length());  
	            File AfterFile=new File(toFilepath);  
	            if(!AfterFile.exists()){  
	                AfterFile.createNewFile();  
	            }//如果文件不存在，则进行创建  
	            System.out.println(path);  
	            fis = new FileInputStream(file[i]);  
	            fos = new FileOutputStream(AfterFile);  
	            byte[] buffer = new byte[1024];  
	            int length;  
	            while((length=fis.read(buffer))!=-1){  
	                fos.write(buffer,0,length);  
	            }  
	            if(null != fos) {
		        	  fos.flush();
		        	  fos.close();
		        	  fos = null;
		        }
		        if(null != fis) {
	        		 fis.close();
	        		 fis = null;
		        }
	        }  
	       
	    }  
	    **/
	 
	 public static void copyDir(File fromfile, File file) {  
		  File[] fl = fromfile.listFiles();  
		    if (!file.exists()) // 如果文件夹不存在  
		        file.mkdir(); // 建立新的文件夹  
		    for (int i = 0; i < fl.length; i++) {  
		        if (fl[i].isFile()) { // 如果是文件类型就复制文件  
		            try {  
		                FileInputStream fis = new FileInputStream(fl[i]);  
		                FileOutputStream out = new FileOutputStream(new File(file.getPath()
		                        + File.separator + fl[i].getName()));  
		                 
		                int count = fis.available();  
		                byte[] data = new byte[count];  
		                if ((fis.read(data)) != -1) {  
		                    out.write(data); // 复制文件内容  
		                }  
		                out.close(); // 关闭输出流  
		                fis.close(); // 关闭输入流  
		            } catch (Exception e) {  
		                e.printStackTrace();  
		            }  
		        }  
		        if (fl[i].isDirectory()) { // 如果是文件夹类型  
		            File des = new File(file.getPath() + File.separator  
		                    + fl[i].getName());  
		            des.mkdir(); // 在目标文件夹中创建相同的文件夹  
		            copyDir(fl[i], des); // 递归调用方法本身  
		        }  
		    }  
	 }
	        
	 public static void main(String[] args){
		 Date dd = new Date();
		
		 System.out.println( System.currentTimeMillis());
		 
		//需要复制的目标文件或目标文件夹  
	        String pathname = "F:/1";  
	        File file = new File(pathname);  
	        //复制到的位置  
	        String topathname = "F:/2";  
	        File toFile = new File(topathname);  
	        
	   //     copyDir(file, toFile);
	   /**     
	        try {  
	        //	copyFiles(file, toFile);  
	        	 Map<String, String> root = new HashMap<String, String>();  
	             root.put("downloadUrl", "cxl");  
	             root.put("iconPathSmall", "25");  
	             root.put("iconPathBig", "cxl");  
	             root.put("packageName", "25"); 
	             root.put("versionCode", "cxl");  
	             root.put("appName", "25"); 
	        	
	        	putDataIntoTemplet("F:/1","et.plist",root,"F:/2/et.plist");
	        	
	        } catch (Exception e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        
	     **/
		 /**
	       // getHttpsUrlAddress();
	      // System.out.println(getHttpsResourceUrl("D:\\Mytools\\apache-tomcat-7.0.57\\webapps\\SpringMVC_UpdateApp\\upload\\2e3f1f59-3108-4a4f-9e9d-ba790b0f75d6\\1208\\ewm.png"));
	        long releaseTime = System.currentTimeMillis(); 
	        System.out.println(CommonUtil.convertDateToString(releaseTime+""));
	        
	       String a = "https://d83e551a-c4e7-4039-a54d-f81aadd5fa19/1254/asd.ipa";
	       try {
			System.out.println(URLEncoder.encode("asd asd.ipa", "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	       String downloadUrlStr ="https://d83e551a-c4e7-4039-a54d-f81aadd5fa19/1254/你 还.ipa";
			  String downloadUrl = "";
			  try {
				 downloadUrl = URLEncoder.encode(downloadUrlStr.substring(downloadUrlStr.lastIndexOf('/')+1),"UTF-8");
				 downloadUrl = downloadUrl.replaceAll("\\+","%20");  
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
				 e1.printStackTrace();
				}
	       System.out.println(downloadUrlStr.substring(0, downloadUrlStr.lastIndexOf('/')+1) + downloadUrl);
	        
	        **/
	        
	      System.out.println( getHttpsUrlAddress()); 
	 }
	 

}
