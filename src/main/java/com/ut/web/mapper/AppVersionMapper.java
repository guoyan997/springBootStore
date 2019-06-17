package com.ut.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ut.web.model.version.Version;

public interface AppVersionMapper {
	
	 
	    @Insert("INSERT INTO VERSION (BRANCHID,VERSIONCODE,VERSIONNAME,PLATFORM,DOWNLOADNUM,PACKAGENAME,FILESIZE,FILEPATH" +
				" ,PLISTfILEPATH,EWMIMGPATH,ICONPATH, DESCRIPTION, CREATORID,CREATETIME)" +
				" values (#{args[0]},#{args[1]},#{args[2]},#{args[3]},#{args[4]},#{args[5]},#{args[6]},#{args[7]},#{args[8]},#{args[9]},#{args[10]},#{args[11]},#{args[12]},#{args[13]})" )
	    public int createVersion(@Param("args")Object[] args);
		
	    // 查找摸个分支下的所有
	    @Select( "SELECT VERSIONID,BRANCHID, VERSIONCODE, VERSIONNAME,PACKAGENAME,DESCRIPTION AS VERSIONDESCRIPTION,PLATFORM, IFNULL(DOWNLOADNUM,0) AS DOWNLOADNUM,FILESIZE , CREATETIME, FILEPATH, "+ 
	            " ICONPATH from VERSION where BRANCHID=#{branchId}  ORDER BY CREATETIME DESC ")
		public List<Version> findReleaseListByBranchId(String branchId);
	    
	     
	    @Update("UPDATE VERSION SET DESCRIPTION =#{args[0]} WHERE VERSIONID=#{args[1]}")
		public int updateVersion(String sql, @Param("args")Object[] args);
		
	   
	    @Delete( " DELETE FROM VERSION WHERE VERSIONID= #{versionId} ")
		public int deleteVersion(String versionId);
		
	    
	    @Delete("DELETE FROM VERSION WHERE BRANCHID= #{branchId}")
		public int deleteAllVersions(String branchId);
		
		//查找最新的版本  String branchId, String platForm
	    @Select( "SELECT VERSIONID,BRANCHID, VERSIONCODE, VERSIONNAME,PACKAGENAME,DESCRIPTION AS VERSIONDESCRIPTION,PLATFORM, DOWNLOADNUM,FILESIZE , CREATETIME, FILEPATH, "+ 
	            " ICONPATH, EWMIMGPATH from VERSION where BRANCHID=#{branchId} AND PLATFORM = #{platForm} ORDER BY CREATETIME DESC LIMIT 1 ")
		public Version findNewstVersion(@Param("branchId")String branchId, @Param("platForm")String platForm);
		
		//查找某一个版本
		 @Select("SELECT A.VERSIONID,A.BRANCHID, B.BRANCHNAME, A.VERSIONCODE, A.VERSIONNAME,A.PACKAGENAME,A.DESCRIPTION AS VERSIONDESCRIPTION,A.PLATFORM, A.DOWNLOADNUM,A.FILESIZE , A.CREATETIME, A.FILEPATH, "
				 +" A.ICONPATH , A.EWMIMGPATH from VERSION A , BRANCH B  WHERE A.BRANCHID = B.BRANCHID AND VERSIONID = #{versionId} ORDER BY CREATETIME DESC ")
		 public Version findOneRelease(String versionId);
		
		//更新某个版本的下载量
		 @Update( " update version set  downloadnum= downloadnum +1  where versionId = #{versionId} ")
		public int updateDownLoadNum(String versionId);
		 
		
		//查询版本列表通过工程id
		
		 @Select( "SELECT C.VERSIONID FROM PROJECT A, BRANCH B, VERSION C WHERE A.PROJECTID = B.PROJECTID AND B.BRANCHID = C.BRANCHID  AND A.PROJECTID =#{projectId} ")
		 public List<Version> findReleaseListByProjectId(String projectId);
		
		 
		// 通过versionKey和versionCode去查找一个版本的信息
		@Select("<script>"
				+ "SELECT A.VERSIONID,A.BRANCHID, B.BRANCHNAME, C.PROJECTNAME, A.VERSIONCODE, A.VERSIONNAME,A.PACKAGENAME,A.DESCRIPTION AS VERSIONDESCRIPTION,A.PLATFORM, A.DOWNLOADNUM,A.FILESIZE , A.CREATETIME, A.FILEPATH, "
				 +" A.ICONPATH , A.EWMIMGPATH, A.PLISTFILEPATH FROM VERSION A , BRANCH B, PROJECT C  WHERE A.BRANCHID = B.BRANCHID AND B.PROJECTID = C.PROJECTID AND A.VERSIONCODE = #{versionCode} AND A.PLATFORM = #{platform} AND A.BRANCHID = ("
				 +"<if test='platform == \"Android\"'>" +
				 "SELECT BRANCHID FROM BRANCH WHERE ANDROIDKEY = #{versionKey}</if>"
				 +"<if test='platform == \"iOS\"'>" +
				 "SELECT BRANCHID FROM BRANCH WHERE IOSKEY = #{versionKey}</if>"
				 + ") </script>") 
		public Version findOneVersionByKey(@Param("versionKey")String versionKey, @Param("versionCode")String versionCode,@Param("platform")String platform);
		
//		@Select("SELECT A.VERSIONID,A.BRANCHID, B.BRANCHNAME, C.PROJECTNAME, A.VERSIONCODE, A.VERSIONNAME,A.PACKAGENAME,A.DESCRIPTION AS VERSIONDESCRIPTION,A.PLATFORM, A.DOWNLOADNUM,A.FILESIZE , A.CREATETIME, A.FILEPATH, "
//				 +" A.ICONPATH , A.EWMIMGPATH, A.PLISTFILEPATH FROM VERSION A , BRANCH B, PROJECT C  WHERE A.BRANCHID = B.BRANCHID AND B.PROJECTID = C.PROJECTID AND A.VERSIONCODE = #{versionCode} AND A.PLATFORM = #{platform} AND A.BRANCHID = (SELECT BRANCHID FROM BRANCH WHERE IOSKEY = #{versionKey}) ")
//		public Version findOneVersionByKey_Ios(@Param("versionKey")String versionKey, @Param("versionCode")String versionCode,@Param("platform")String platform);
		
		// 通过versionKey和PLATFORM去查找一个最新的版本的信息
		@Select("<script>"
				 + "SELECT A.VERSIONID,A.BRANCHID, B.BRANCHNAME, C.PROJECTNAME, A.VERSIONCODE, A.VERSIONNAME,A.PACKAGENAME,A.DESCRIPTION AS VERSIONDESCRIPTION,A.PLATFORM, A.DOWNLOADNUM,A.FILESIZE , A.CREATETIME, A.FILEPATH, "
				 +" A.ICONPATH , A.EWMIMGPATH, A.PLISTFILEPATH FROM VERSION A , BRANCH B, PROJECT C  WHERE A.BRANCHID = B.BRANCHID  AND B.PROJECTID = C.PROJECTID AND A.PLATFORM = #{platform} AND A.BRANCHID = ("
				 + "<if test='platform == \"Android\"'>" 
				 + " SELECT BRANCHID FROM BRANCH WHERE ANDROIDKEY = #{versionKey}</if>"
				 + "<if test='platform == \"iOS\"'>" 
				 + " SELECT BRANCHID FROM BRANCH WHERE IOSKEY = #{versionKey}</if>"
				 + " ) ORDER BY A.VERSIONCODE DESC LIMIT 1  </script>")
		public Version findOneNewstVersionByKey(@Param("versionKey")String versionKey, @Param("platform")String platform);
		
//		@Select("SELECT A.VERSIONID,A.BRANCHID, B.BRANCHNAME, C.PROJECTNAME, A.VERSIONCODE, A.VERSIONNAME,A.PACKAGENAME,A.DESCRIPTION AS VERSIONDESCRIPTION,A.PLATFORM, A.DOWNLOADNUM,A.FILESIZE , A.CREATETIME, A.FILEPATH, "
//				 +" A.ICONPATH , A.EWMIMGPATH, A.PLISTFILEPATH FROM VERSION A , BRANCH B, PROJECT C  WHERE A.BRANCHID = B.BRANCHID  AND B.PROJECTID = C.PROJECTID AND A.PLATFORM = #{platform} AND A.BRANCHID = ( SELECT BRANCHID FROM BRANCH WHERE IOSKEY = #{versionKey} ) ORDER BY A.VERSIONCODE DESC LIMIT 1 ")
//		public Version findOneNewstVersionByKey_Ios(@Param("versionKey")String versionKey, @Param("platform")String platform);

}
