package notesmaking.controller;



import notesmaking.model.EmailRequest;
import notesmaking.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // Endpoint to send invitation emails
    @PostMapping("/sendinvite")
    public ResponseEntity<?> sendInvitationEmail(@RequestBody EmailRequest emailRequest) {
        boolean isSent = emailService.sendInvitationEmail(emailRequest);
        if (isSent) {
            return ResponseEntity.ok("Invitation email sent successfully.");
        } else {
            return ResponseEntity.status(500).body("Failed to send the invitation email.");
        }
    }

    // Additional endpoints for other email types can be added here
}

