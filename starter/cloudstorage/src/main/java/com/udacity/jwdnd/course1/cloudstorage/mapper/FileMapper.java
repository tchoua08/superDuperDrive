package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.FileRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT fileid, filename, contenttype, filesize, userid, filedata FROM FILES WHERE userid = #{userId}")
    List<FileRecord> getFilesByUserId(Integer userId);

    @Select("SELECT fileid, filename, contenttype, filesize, userid, filedata FROM FILES WHERE fileid = #{fileId}")
    FileRecord getFileById(Integer fileId);

    @Select("SELECT fileid, filename, contenttype, filesize, userid, filedata FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    FileRecord getFileByNameAndUserId(@Param("fileName") String filename, @Param("userId") Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(FileRecord fileRecord);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId} AND userid = #{userId}")
    int delete(@Param("fileId") Integer fileid, @Param("userId") Integer userId);
}
