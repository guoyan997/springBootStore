package com.ut.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ut.web.model.User;


public interface AppUserMapper {
//	
//	@Select("select a.id, a.cat_name, a.cat_age from cat a, demo b where a.id = b.id ")
//	public List<Cat> getCat();
//	
//	
//	@Select("select * from cat where id = #{id}")
//	public Cat getById(long id);
//	
//	@Insert("insert into cat(cat_name, cat_age) values(#{cat_name},#{cat_age})")
//	@Options(useGeneratedKeys=true,keyProperty="id",keyColumn="id")
//	public void save(Cat demo);
//	
//	@Delete("delete from cat where id = #{id}")
//	public Cat deleteById(long id);
//	
//	@Update("update cat set cat_name =#{cat_name}, cat_age=#{cat_age}  where id = #{id}")
//	public Cat updateById(Cat demo,long id);
	
	@Select("SELECT ID, USERNAME, PASSWORD, USERTYPE FROM USER WHERE USERNAME = #{username}")
	public User findUserByName(String username);
	
	
	@Update("UPDATE USER SET PASSWORD = #{args[0]}  where USERNAME = #{args[1]}")
	public int changeUserPsw(@Param("args")Object[] args);
	
	
	@Select(" SELECT ID, USERNAME, USERTYPE, CREATORID,CREATETIME, '' AS CREATOR  FROM USER  WHERE ID = #{creatorId} "+
			 "	UNION  "+
			 "	SELECT A.ID, A.USERNAME, A.USERTYPE, A.CREATORID, A.CREATETIME, B.USERNAME AS CREATOR FROM  "+
			 "	(SELECT ID, USERNAME, USERTYPE, CREATORID,CREATETIME  FROM USER WHERE CREATORID = #{creatorId}  ORDER BY ID ASC) A ,   "+
			 "	USER B WHERE A.CREATORID = B.ID ")
	public List<User> findUserListByCreatorId(String creatorId);
	
	
	@Update("UPDATE USER SET USERNAME = #{args[0]}, PASSWORD = #{args[1]} WHERE ID= #{args[2]}")
	public int updateUser(@Param("args")Object[] args);
	
	// 在sql中需要多参数的时候，要用@Param("args") 把参数修饰一下，还可以使用 对象实例
	@Insert("INSERT INTO USER (USERNAME, PASSWORD, USERTYPE, CREATORID, CREATETIME) VALUES (#{args[0]},#{args[1]},#{args[2]},#{args[3]},#{args[4]})")
	public int createUser(@Param("args")Object[] args);
	
	
	 
	@Delete("DELETE FROM USER WHERE ID = #{id}")
	public int deleteUserById(String id);
	

}
