package notesmaking.service;

import notesmaking.model.Note;
import notesmaking.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    @Transactional
    public Note createNote(Note note) {
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    @Transactional
    public Note updateNote(long id, Note updatedNote) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());
        existingNote.setUpdatedAt(LocalDateTime.now());
        existingNote.setVersion(existingNote.getVersion() + 1);

        // Add logic to update collaborators if provided
        if (updatedNote.getCollaborators() != null) {
            existingNote.setCollaborators(updatedNote.getCollaborators());
        }
        if (updatedNote.getCategory() != null) {
            existingNote.setCategory(updatedNote.getCategory());
        }

        return noteRepository.save(existingNote);
    }

    @Override
    @Transactional
    public void deleteNoteById(long id) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        noteRepository.delete(existingNote);
    }

    @Override
    public Note getNoteById(long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }

    @Override
    public List<Note> getNotesByUserId(String userId) {
        return noteRepository.findByCreatorOrCollaboratorsContains(userId, userId);
    }

    @Override
    public List<Note> getNotesByUserEmail(String email) {
        return noteRepository.findByCreatorOrCollaboratorsContains(email, email);
    }

    @Override
    public List<Note> getNotesByCategory(String userId, String category) {
        List<Note> notesByCreator = noteRepository.findByCreatorAndCategory(userId, category);
        List<Note> notesByCollaborator = noteRepository.findByCollaboratorsContainsAndCategory(userId, category);
        notesByCreator.addAll(notesByCollaborator);
        return notesByCreator;
    }
}
