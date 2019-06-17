/**
 * 
 */
package com.ut.web.util.iospng;

import java.io.File;

/**
 *
 * @Version: 1.0
 * @Date: May 22, 2013
 */
public class PNGTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File srcFile = new File("D:\\Mytools\\apache-tomcat-7.0.57\\webapps\\SpringMVC_UpdateApp\\upload\\2375f3d4-691a-4f3a-a596-d3b35fa8f998\\1209\\AppIcon60x60@2x.png");
		File destFile = new File("D:\\Mytools\\apache-tomcat-7.0.57\\webapps\\SpringMVC_UpdateApp\\upload\\2375f3d4-691a-4f3a-a596-d3b35fa8f998\\1209\\icon@2x-decoded.png");
		File upFile = new File("D:\\Mytools\\apache-tomcat-7.0.57\\webapps\\SpringMVC_UpdateApp\\upload\\2375f3d4-691a-4f3a-a596-d3b35fa8f998\\1209\\icon@2x-updecoded.png");
		File downFile = new File("D:\\Mytools\\apache-tomcat-7.0.57\\webapps\\SpringMVC_UpdateApp\\upload\\2375f3d4-691a-4f3a-a596-d3b35fa8f998\\1209\\icon@2x-downdecoded.png");
		
		ConvertHandler handler = new ConvertHandler();
		handler.convertPNGFile(srcFile, destFile, upFile, downFile);
		upFile.delete();
		downFile.delete();
	}

}
