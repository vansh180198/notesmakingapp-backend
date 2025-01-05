package notesmaking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Note {

    @Id
    private Long id; // Unique identifier for the note

    private String title; // Title of the note

    @Column(columnDefinition = "TEXT") // For storing large content
    private String content; // Content of the note

    private String creator; // The user who created the note (e.g., user ID or email)

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> collaborators; // List of collaborators

    private LocalDateTime createdAt; // Timestamp when the note was created

    private LocalDateTime updatedAt; // Timestamp when the note was last updated

    private String status; // Note status: e.g., "active", "archived", "deleted"

    private int version; // Version number for tracking changes

    private String category; // New field to categorize notes

    public Note() {
        // Default constructor
    }

    public Note(Long id, String title, String content, String creator, List<String> collaborators,
                LocalDateTime createdAt, LocalDateTime updatedAt, String status, int version, String category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creator = creator;
        this.collaborators = collaborators;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.version = version;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
