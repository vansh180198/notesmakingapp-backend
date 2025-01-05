package notesmaking.emitter;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEmitterService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter addEmitter(String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(userId, emitter);
        System.out.println("Added emitter for user: " + userId);

        emitter.onCompletion(() -> {
            removeEmitter(userId);
            broadcastOnlineUsers();
        });

        emitter.onTimeout(() -> {
            removeEmitter(userId);
            broadcastOnlineUsers();
        });

        emitter.onError(e -> {
            removeEmitter(userId);
            broadcastOnlineUsers();
        });

        broadcastOnlineUsers();
        return emitter;
    }

    public void broadcastToCollaborators(List<String> recipients, String eventType, Object data) {
        for (String recipient : recipients) {
            if (emitters.containsKey(recipient)) {
                try {
                    emitters.get(recipient).send(SseEmitter.event()
                            .name(eventType)
                            .data(data));
                    System.out.println("Broadcasted " + eventType + " to " + recipient);
                } catch (IOException e) {
                    removeEmitter(recipient);
                    System.err.println("Failed to send event to " + recipient + ": " + e.getMessage());
                }
            }
        }
    }

    public void broadcastOnlineUsers() {
        List<String> onlineUsers = getActiveUsers();
        for (Map.Entry<String, SseEmitter> entry : emitters.entrySet()) {
            try {
                entry.getValue().send(SseEmitter.event()
                        .name("online-users")
                        .data(onlineUsers));
                System.out.println("Sent online users list to: " + entry.getKey());
            } catch (IOException e) {
                removeEmitter(entry.getKey());
                System.err.println("Failed to send online users list to " + entry.getKey() + ": " + e.getMessage());
            }
        }
    }

    public List<String> getActiveUsers() {
        return new ArrayList<>(emitters.keySet());
    }

    public void removeEmitter(String userId) {
        emitters.remove(userId);
        System.out.println("Removed emitter for user: " + userId);
    }

    @Scheduled(fixedRate = 60000)
    public void cleanupEmitters() {
        emitters.entrySet().removeIf(entry -> {
            try {
                entry.getValue().send(SseEmitter.event().name("heartbeat").data("ping"));
                return false;
            } catch (IOException e) {
                System.err.println("Removing stale emitter for user: " + entry.getKey());
                return true;
            }
        });
    }
}
