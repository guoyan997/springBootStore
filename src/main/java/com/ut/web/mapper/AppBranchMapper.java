package com.ut.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ut.web.model.branch.Branch;
import com.ut.web.model.branch.BranchInfo;
import com.ut.web.model.branch.BranchNewst;

public interface AppBranchMapper {
	
	@Insert("INSERT INTO BRANCH (BRANCHNAME, PROJECTID, ANDROIDKEY, IOSKEY, ICONPATH, DESCRIPTION, "
			+ "CREATORID,  CREATETIME) VALUES (#{args[0]},#{args[1]},#{args[2]},#{args[3]},#{args[4]},#{args[5]},#{args[6]},#{args[7]})")
	public int createBranch(@Param("args")Object[] args);
	
    @Select("SELECT C.BRANCHID, C.BRANCHNAME,C.PROJECTID, C.ANDROIDKEY,C.IOSKEY,C.ICONPATH,C.DESCRIPTION,C.CREATOR, C.CREATETIME, IFNULL(D.DOWNLOADCOUNT,0) AS DOWNLOADCOUNT "+
    		" FROM "+
    		" (SELECT A.BRANCHID, A.BRANCHNAME,A.PROJECTID, A.ANDROIDKEY,A.IOSKEY,A.ICONPATH,A.DESCRIPTION,B.USERNAME AS CREATOR, A.CREATETIME  "+
    		" FROM BRANCH A, USER B WHERE A.CREATORID = B.ID) C LEFT JOIN "+
    		"(SELECT BRANCHID,  SUM(DOWNLOADNUM) AS DOWNLOADCOUNT FROM VERSION  GROUP BY BRANCHID) D ON C.BRANCHID = D.BRANCHID WHERE  C.PROJECTID= #{projectId} ORDER BY C.CREATETIME DESC ")	
	public List<Branch> findAllBranchs(String projectId);
	
    @Delete("DELETE FROM BRANCH WHERE BRANCHID = #{args[0]} ")
	public int deleteBranch(@Param("args")Object[] args);
	
    @Delete("DELETE FROM BRANCH WHERE PROJECTID = #{args[0]} ")
	public int deleteBranchByProjectId(@Param("args")Object[] args);
	
   
    @Update( "UPDATE BRANCH SET BRANCHNAME = #{args[0]}, DESCRIPTION = #{args[1]} WHERE BRANCHID = #{args[2]}  ")
	public int updateBranch(String sql, @Param("args")Object[] args);
    
    @Update( " UPDATE BRANCH SET PACKAGENAME = #{args[0]} WHERE BRANCHID = #{args[1]} ")
    public int updateBranchPackageName(String sql, @Param("args")Object[] args);
    
    @Update( "UPDATE BRANCH SET ICONPATH = #{args[0]}, PACKAGENAME = #{args[1]} WHERE BRANCHID = #{args[2]}")
    public int updateBranchPackageNameAndIcon(String sql, @Param("args")Object[] args);
	
    
    @Update("UPDATE BRANCH SET ICONPATH = #{args[0]} WHERE BRANCHID = #{args[1]}")
	public int updateBranchIconPath(String sql, @Param("args")Object[] args);
	
	
	@Select("SELECT C.BRANCHID, C.BRANCHNAME,C.PROJECTID, C.PACKAGENAME,C.ANDROIDKEY,C.IOSKEY,C.ICONPATH,C.DESCRIPTION,C.CREATOR, C.CREATETIME, IFNULL(D.DOWNLOADCOUNT,0) AS DOWNLOADCOUNT FROM "+
			" (SELECT A.BRANCHID, A.BRANCHNAME,A.PROJECTID, A.PACKAGENAME,A.ANDROIDKEY,A.IOSKEY,A.ICONPATH,A.DESCRIPTION,B.USERNAME AS CREATOR, A.CREATETIME  "+
			" FROM BRANCH A, USER B WHERE A.CREATORID = B.ID) C LEFT JOIN "+
			"(SELECT BRANCHID,  SUM(DOWNLOADNUM) AS DOWNLOADCOUNT FROM VERSION  GROUP BY BRANCHID) D ON C.BRANCHID = D.BRANCHID WHERE C.BRANCHID =#{branchId} ORDER BY C.CREATETIME DESC ")
	public Branch findBranchById(String branchId);
	
	
	
	
	// 这个是为了修改名称时，查找是否重复
	@Select("SELECT BRANCHID,BRANCHNAME FROM BRANCH WHERE BRANCHNAME= #{branchName} AND PROJECTID = #{projectId} ")
	public List<Branch> findBranchsByName(@Param("branchName")String branchName, @Param("projectId")String projectId);
	
	//通过branchId获取工程信息
	@Select("SELECT A.PROJECTNAME, B.BRANCHNAME FROM PROJECT A, BRANCH B WHERE A.PROJECTID = B.PROJECTID AND B.BRANCHID = #{branchId}")
	public BranchInfo findProjectByBranchId(String branchId);
	
	//通过工程id查询最新的分支版本信息列表
	@Select("SELECT A.PROJECTID, A.BRANCHID, A.BRANCHNAME,A.ANDROIDKEY, A.IOSKEY, B.VERSIONCODE,B.PLATFORM,  CONCAT(B.DOWNLOADNUM,'') AS DOWNLOADNUM, B.CREATETIME FROM BRANCH A LEFT JOIN "+
			" (SELECT C.BRANCHID,C.VERSIONID,C.VERSIONCODE,C.PLATFORM,C.DOWNLOADNUM,C.CREATETIME FROM VERSION C, "+
			"	(SELECT BRANCHID, max(VERSIONID) AS VERSIONID FROM "+
			"	VERSION GROUP BY  BRANCHID ) D WHERE C.VERSIONID = D.VERSIONID ) B ON A.BRANCHID = B.BRANCHID WHERE A.PROJECTID = #{projectId} ORDER BY B.VERSIONCODE DESC ")
	public List<BranchNewst> findNewstBranchs(String projectId);
	
	// 通过versionKey查找分支的信息
	@Select(" SELECT BRANCHID, BRANCHNAME, ANDROIDKEY, IOSKEY FROM BRANCH WHERE ANDROIDKEY= #{versionKey} OR IOSKEY = #{versionKey} ")
	public Branch findOneBranchByVersionKey(@Param("versionKey")String versionKey);

	
}
