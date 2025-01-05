package notesmaking.service;

import notesmaking.model.Note;

import java.util.List;

public interface NoteService {
    Note createNote(Note note);

    List<Note> getAllNotes();

    Note updateNote(long id, Note updatedNote);

    void deleteNoteById(long id);

    Note getNoteById(long id);

    List<Note> getNotesByUserId(String userId);

    List<Note> getNotesByUserEmail(String email);

    // New method for filtering by category
    List<Note> getNotesByCategory(String userId, String category);
}
