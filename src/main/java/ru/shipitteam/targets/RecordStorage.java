package ru.shipitteam.targets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RecordStorage {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Path getRecordsDirectory() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path recordsDir = configDir.resolve("neuroaura").resolve("records");
        try {
            Files.createDirectories(recordsDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recordsDir;
    }

    public static void saveSession(RecordingSession session) {
        try {
            Path file = getRecordsDirectory().resolve(session.getId() + ".json");
            Files.writeString(file, GSON.toJson(session.toSerializableMap()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getSavedSessionNames() {
        try {
            Path dir = getRecordsDirectory();
            if (!Files.exists(dir)) {
                return Collections.emptyList();
            }

            List<String> names = new ArrayList<>();
            try (var stream = Files.list(dir)) {
                stream.filter(path -> path.getFileName().toString().endsWith(".json"))
                        .map(path -> path.getFileName().toString().replace(".json", ""))
                        .forEach(names::add);
            }
            return names;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static RecordingSession loadSession(String sessionId) {
        Path file = getRecordsDirectory().resolve(sessionId + ".json");
        if (!Files.exists(file)) {
            return null;
        }

        try {
            String json = Files.readString(file, StandardCharsets.UTF_8);
            Map<?, ?> data = GSON.fromJson(json, Map.class);
            if (data == null) {
                return null;
            }

            String id = String.valueOf(data.getOrDefault("id", sessionId));
            String name = String.valueOf(data.getOrDefault("name", id));
            String worldName = String.valueOf(data.getOrDefault("worldName", "unknown"));
            RecordingSession session = new RecordingSession(id, name, worldName);

            Object eventsObj = data.get("events");
            if (eventsObj instanceof java.util.List<?> eventsList) {
                for (Object eventObj : eventsList) {
                    if (eventObj instanceof Map<?, ?> map) {
                        long tick = Long.parseLong(String.valueOf(map.getOrDefault("tick", 0L)));
                        String type = String.valueOf(map.getOrDefault("type", "unknown"));
                        Map<String, Object> payload = new java.util.LinkedHashMap<>();
                        Object dataValue = map.get("data");
                        if (dataValue instanceof Map<?, ?> nested) {
                            for (Map.Entry<?, ?> entry : nested.entrySet()) {
                                payload.put(String.valueOf(entry.getKey()), entry.getValue());
                            }
                        }
                        session.addEvent(new RecordedEvent(tick, type, payload));
                    }
                }
            }

            return session;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
