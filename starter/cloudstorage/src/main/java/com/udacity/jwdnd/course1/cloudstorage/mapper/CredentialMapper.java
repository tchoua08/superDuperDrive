package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT credentialid, url, username, key, password, userid FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credential> getCredentialsByUserId(Integer userId);

    @Select("SELECT credentialid, url, username, key, password, userid FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
    Credential getCredentialByIdAndUserId(@Param("credentialId") Integer credentialId, @Param("userId") Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{userName}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{userName}, key = #{key}, password = #{password} WHERE credentialid = #{credentialId} AND userid = #{userId}")
    int update(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId} AND userid = #{userId}")
    int delete(@Param("credentialId") Integer credentialId, @Param("userId") Integer userId);
}
