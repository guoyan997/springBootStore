package com.ut.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ut.web.model.project.Project;

public interface AppProjectMapper {
	
	@Insert("INSERT INTO PROJECT(PROJECTNAME,PACKAGENAME,DESCRIPTION,CREATORID,CREATETIME) values (#{args[0]},#{args[1]},#{args[2]},#{args[3]},#{args[4]})")
	public int createProject(@Param("args")Object[] args);
	
	@Select("SELECT A.PROJECTID, A.PROJECTNAME, B.USERNAME AS CREATOR, A.PACKAGENAME, A.CREATETIME, A.DESCRIPTION FROM PROJECT A,"
			+ " USER B WHERE A.CREATORID = B.ID AND A.CREATORID = #{args[0]}   ORDER BY A.CREATETIME DESC")
	public List<Project> findAllProjects(@Param("args")Object[] args);
	
	
	@Delete("DELETE FROM PROJECT WHERE PROJECTID = #{args[0]}")
	public int deleteProject(@Param("args")Object[] args);
	
	
	@Update("UPDATE PROJECT SET PROJECTNAME = #{args[0]}, DESCRIPTION = #{args[1]} WHERE PROJECTID = #{args[2]} ")
	public int updateProject(@Param("args")String sql,@Param("args")Object[] args);
	
	@Update("UPDATE PROJECT SET PACKAGENAME = #{args[0]} WHERE PROJECTID = #{args[1]} ")
	public int updateProjectPackageName(String sql, @Param("args")Object[] args);
	
	
	@Select("SELECT A.PROJECTID, A.PROJECTNAME, B.USERNAME AS CREATOR, A.PACKAGENAME,A.CREATETIME, A.DESCRIPTION FROM PROJECT A, "
			+" USER B WHERE A.CREATORID = B.ID AND A.PROJECTID = #{projectId} ")
	public Project findProjectById(String projectId);
	
	@Select( "SELECT PROJECTID,PROJECTNAME FROM PROJECT WHERE PROJECTNAME=#{projectName}  ")
	public List<Project> findProjectsByName(String projectName);
	

}
