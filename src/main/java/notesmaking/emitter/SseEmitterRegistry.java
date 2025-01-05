package notesmaking.emitter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRegistry {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void addEmitter(String key, SseEmitter emitter) {
        emitters.put(key, emitter);
    }

    public SseEmitter getEmitter(String key) {
        return emitters.get(key);
    }

    public void removeEmitter(String key) {
        emitters.remove(key);
    }

    public Map<String, SseEmitter> getAllEmitters() {
        return emitters;
    }
}
