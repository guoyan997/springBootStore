package com.ut.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ut.web.model.relation.Relation;

public interface AppRelationMapper {
	
	   // 创建关联
	    @Insert("INSERT INTO RELATION (ANDROIDKEY, IOSKEY) VALUES (#{args[0]},#{args[1]})")
		public int createRelation(@Param("args")Object[] args);
		// 删除关联
	    @Delete("DELETE FROM RELATION WHERE ANDROIDKEY = #{args[0]} and IOSKEY = #{args[1]}")
		public int deleteRelation(@Param("args")Object[] args);
		// 修改关联
	    @Update("UPDATE RELATION SET IOSKEY = #{args[0]} WHERE ANDROIDKEY = #{args[1]}")
		public int updateRelationByAndroid(String sql, @Param("args")Object[] args);
		
		// 修改关联
	    @Update("UPDATE RELATION SET ANDROIDKEY = #{args[0]} WHERE IOSKEY = #{args[1]}")
		public int updateRelationByIos(String sql, @Param("args")Object[] args);
		
		// 查找关联
		@Select("<script>"
				 +"<if test='platform == \"Android\"'>" +
				 "SELECT A.PROJECTID AS IOSPROJECTID, A.PROJECTNAME AS IOSPROJECTNAME, B.BRANCHID AS IOSBRANCHID,"
					+ " B.BRANCHNAME AS IOSBRANCHNAME, B.IOSKEY FROM PROJECT A, BRANCH B "+
				" WHERE A.PROJECTID = B.PROJECTID AND B.BRANCHID = (SELECT BRANCHID FROM BRANCH WHERE IOSKEY = (SELECT IOSKEY FROM RELATION WHERE ANDROIDKEY = #{versionKey} ))</if>"
				 +"<if test='platform == \"iOS\"'>" +
				 "SELECT A.PROJECTID AS ANDROIDPROJECTID, A.PROJECTNAME AS ANDROIDPROJECTNAME, B.BRANCHID AS ANDROIDBRANCHID, B.BRANCHNAME AS ANDROIDBRANCHNAME,"
					+ " B.ANDROIDKEY FROM PROJECT A, BRANCH B "+
					" WHERE A.PROJECTID = B.PROJECTID AND  B.BRANCHID = (SELECT BRANCHID FROM BRANCH WHERE ANDROIDKEY = (SELECT ANDROIDKEY FROM RELATION WHERE IOSKEY = #{versionKey}))</if>"
				+ "</script>")
		public Relation findRelationByKey(@Param("versionKey")String versionKey, @Param("platform")String platform);
	   
		// 查找可以关联的列表
		@Select("<script>"
				 +"<if test='platform == \"Android\"'>" +
				 "SELECT A.PROJECTID AS IOSPROJECTID, A.PROJECTNAME AS IOSPROJECTNAME, B.BRANCHID AS IOSBRANCHID, B.BRANCHNAME AS IOSBRANCHNAME,"
					+ " B.IOSKEY FROM PROJECT A, BRANCH B WHERE A.PROJECTID = B.PROJECTID AND B.IOSKEY NOT IN (SELECT IOSKEY FROM RELATION ) AND A.CREATORID = #{creatorId} </if>"
				 +"<if test='platform == \"iOS\"'>" +
				 "SELECT A.PROJECTID AS ANDROIDPROJECTID, A.PROJECTNAME AS ANDROIDPROJECTNAME, B.BRANCHID AS ANDROIDBRANCHID, B.BRANCHNAME AS ANDROIDBRANCHNAME,"
					+ " B.ANDROIDKEY FROM PROJECT A, BRANCH B WHERE A.PROJECTID = B.PROJECTID AND B.ANDROIDKEY NOT IN (SELECT ANDROIDKEY FROM RELATION ) AND A.CREATORID = #{creatorId} </if>"
				+ "</script>")
		public List<Relation> findRelationListForSelect(@Param("versionKey")String versionKey, @Param("platform")String platform, @Param("creatorId")String creatorId);
		
		
		// 删除关联通过分支id
		
		@Delete("DELETE FROM RELATION WHERE ANDROIDKEY  = (SELECT ANDROIDKEY FROM BRANCH WHERE BRANCHID = #{args[0]})"
				+ " OR IOSKEY =(SELECT IOSKEY FROM BRANCH WHERE BRANCHID = #{args[1]})")
		public int deleteRelationByBranchId(@Param("args")Object[] args);
		
		
		//删除关联，通过工程id
		@Delete(" DELETE FROM RELATION WHERE ANDROIDKEY IN (SELECT ANDROIDKEY FROM BRANCH WHERE BRANCHID IN "+
				 " (SELECT BRANCHID FROM BRANCH WHERE PROJECTID = #{args[0]})) OR IOSKEY IN (SELECT IOSKEY FROM BRANCH WHERE BRANCHID IN "+
				 " (SELECT BRANCHID FROM BRANCH WHERE PROJECTID = #{args[1]})) ")
		public int deleteRelationByProjectId(@Param("args")Object[] args);
	

}
