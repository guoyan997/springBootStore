package com.ut.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ut.web.model.AppCheckModel;

public interface AppUpdateMapper {
	
	@Select("<script>"
				 +"<if test='platform == \"Android\"'>" +
				 "SELECT VERSIONCODE, VERSIONNAME, PLATFORM, FILEPATH AS DOWNURL FROM VERSION WHERE PLATFORM = #{platform} AND BRANCHID = ( SELECT BRANCHID FROM BRANCH WHERE ANDROIDKEY = #{versionkey} ) ORDER BY CREATETIME DESC LIMIT 1"
				 + "</if>"
				 +"<if test='platform == \"iOS\"'>" 
                 +"SELECT VERSIONCODE, VERSIONNAME, PLATFORM, FILEPATH AS DOWNURL FROM VERSION WHERE PLATFORM = #{platform} AND BRANCHID = ( SELECT BRANCHID FROM BRANCH WHERE IOSKEY = #{versionkey} ) ORDER BY CREATETIME DESC LIMIT 1"
                 + "</if>"
				 + "</script>")
	public List<AppCheckModel> checkUpVersionCode(@Param("versionkey")String versionkey, @Param("platform")String platform);

}
