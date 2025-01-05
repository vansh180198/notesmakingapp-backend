package notesmaking.controller;

import notesmaking.model.Note;
import notesmaking.service.NoteService;
import notesmaking.emitter.SseEmitterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final SseEmitterService sseEmitterService;

    public NoteController(NoteService noteService, SseEmitterService sseEmitterService) {
        this.noteService = noteService;
        this.sseEmitterService = sseEmitterService;
    }

    @GetMapping("/stream")
    public SseEmitter streamUpdates() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return sseEmitterService.addEmitter(userId);
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        note.setCreator(userId);
        Note createdNote = noteService.createNote(note);
        System.out.println(note);
        // Broadcast note creation to collaborators and the creator
        List<String> recipients = new ArrayList<>(createdNote.getCollaborators());
        recipients.add(createdNote.getCreator());
        sseEmitterService.broadcastToCollaborators(recipients, "note-created", createdNote);

        return createdNote;
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<Note> notes = noteService.getNotesByUserId(userId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Note>> getNotesByCategory(@PathVariable String category) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<Note> notes = noteService.getNotesByCategory(userId, category);
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable long id, @RequestBody Note updatedNote) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Note existingNote = noteService.getNoteById(id);

        // Authorization check
        if (!existingNote.getCreator().equals(currentUserId) &&
                !existingNote.getCollaborators().contains(currentUserId)) {
            throw new RuntimeException("You are not authorized to update this note");
        }

        Note updated = noteService.updateNote(id, updatedNote);

        // Broadcast note update to collaborators and the creator
        List<String> recipients = new ArrayList<>(updated.getCollaborators());
        recipients.add(updated.getCreator());
        sseEmitterService.broadcastToCollaborators(recipients, "note-updated", updated);

        return updated;
    }

    @DeleteMapping("/{id}")
    public void deleteNoteById(@PathVariable long id) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Note existingNote = noteService.getNoteById(id);

        // Authorization check
        if (!existingNote.getCreator().equals(currentUserId)) {
            throw new RuntimeException("You are not authorized to delete this note");
        }

        noteService.deleteNoteById(id);

        // Broadcast note deletion to collaborators and the creator
        List<String> recipients = new ArrayList<>(existingNote.getCollaborators());
        recipients.add(existingNote.getCreator());
        sseEmitterService.broadcastToCollaborators(recipients, "note-deleted", Map.of("id", id));
    }
}
