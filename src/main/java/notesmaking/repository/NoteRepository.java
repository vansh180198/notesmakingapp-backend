package notesmaking.repository;

import notesmaking.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByCreatorOrCollaboratorsContains(String creator, String collaborator);

    // Find notes by creator and category
    List<Note> findByCreatorAndCategory(String creator, String category);

    // Find notes by collaborator and category
    List<Note> findByCollaboratorsContainsAndCategory(String collaborator, String category);
}
