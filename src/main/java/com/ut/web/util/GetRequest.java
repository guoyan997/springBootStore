package com.ut.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


/*
 * GET请求类,绕过https证书，发送https请求
 */
public class GetRequest {
    private String url = "https://b2b.10086.cn/b2b/main/viewNoticeContent.html?noticeBean.id=";
//    private Logger logger;
//    public GetRequest() {
//        logger = Logger.getLogger(GetRequest.class);
//    }
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
    //为更好的演示，去掉了不相关的代码
    public  void getData(String urlStr,String id) {
        this.url = urlStr + id;
        BufferedReader in = null;
        HttpURLConnection conn = null;
        String result = "";
        try {
              //该部分必须在获取connection前调用
            trustAllHttpsCertificates();
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
             //       logger.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            conn = (HttpURLConnection)new URL(url).openConnection();
            // 发送GET请求必须设置如下两行
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            // flush输出流的缓冲
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
   //         logger.error("发送 GET 请求出现异常！\t请求ID:"+id+"\n"+e.getMessage()+"\n");
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
    //            logger.error("关闭数据流出错了！\n"+ex.getMessage()+"\n");
            }
        }
        // 获得相应结果result,可以直接处理......
        System.out.println(result);
    }
    static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
    
    public static void main(String[] args){
    	GetRequest getReq = new GetRequest();
    	getReq.getData("https://192.168.60.46:8443/SpringMVC_UpdateApp/appUpdate/checkUpdateApp?appId=","2fb3e8bd-52ed-4c91-9026-2b16f2675981");
    	
    }
    
    
}