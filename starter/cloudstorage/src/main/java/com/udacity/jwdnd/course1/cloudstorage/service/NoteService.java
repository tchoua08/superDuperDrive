package com.udacity.jwdnd.course1.cloudstorage.service;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotesByUserId(Integer userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public int saveNote(NoteForm noteForm, Integer userId) {
        if (noteForm.getNoteId() == null) {
            Note note = new Note();
            note.setNoteTitle(noteForm.getNoteTitle());
            note.setNoteDescription(noteForm.getNoteDescription());
            note.setUserId(userId);
            return noteMapper.insert(note);
        }

        Note note = new Note();
        note.setNoteId(noteForm.getNoteId());
        note.setNoteTitle(noteForm.getNoteTitle());
        note.setNoteDescription(noteForm.getNoteDescription());
        note.setUserId(userId);
        return noteMapper.update(note);
    }

    public int deleteNote(Integer noteId, Integer userId) {
        return noteMapper.delete(noteId, userId);
    }
}
