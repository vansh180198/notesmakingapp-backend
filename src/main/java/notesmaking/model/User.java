package notesmaking.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // Maps to a table named "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Automatically generate unique IDs
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private String id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password; // Stored as a hashed password

    @ElementCollection // Stores a list of note IDs in a separate table
    @CollectionTable(name = "user_notes", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "note_id")
    private List<Long> noteIds;

    // Default constructor (required by JPA)
    public User() {}

    // Parameterized constructor
    public User(String id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getNoteIds() {
        return noteIds;
    }

    public void setNoteIds(List<Long> noteIds) {
        this.noteIds = noteIds;
    }



        @Column(length = 512)
        private String refreshToken;

        // Getters and setters for refreshToken
        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }


}
