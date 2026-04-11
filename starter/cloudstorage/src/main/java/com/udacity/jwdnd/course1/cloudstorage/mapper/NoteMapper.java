package com.udacity.jwdnd.course1.cloudstorage.mapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT noteid, notetitle, notedescription, userid FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotesByUserId(Integer userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId} AND userid = #{userId}")
    int update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId} AND userid = #{userId}")
    int delete(@Param("noteId") Integer noteid, @Param("userId") Integer userId);
}