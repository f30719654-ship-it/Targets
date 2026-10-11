package ru.shipitteam.targets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecordingSession {
    private final String id;
    private final String name;
    private final long startedAt;
    private final String worldName;
    private final List<RecordedEvent> events = new ArrayList<>();
    private long lastUpdatedAt;

    public RecordingSession(String id, String name, String worldName) {
        this.id = id;
        this.name = name;
        this.worldName = worldName;
        this.startedAt = System.currentTimeMillis();
        this.lastUpdatedAt = startedAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public long getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public String getWorldName() {
        return worldName;
    }

    public List<RecordedEvent> getEvents() {
        return new ArrayList<>(events);
    }

    public void addEvent(RecordedEvent event) {
        if (event == null) {
            return;
        }
        events.add(event);
        this.lastUpdatedAt = System.currentTimeMillis();
    }

    public long getDurationMs() {
        return Math.max(0L, lastUpdatedAt - startedAt);
    }

    public Map<String, Object> toSerializableMap() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", id);
        payload.put("name", name);
        payload.put("worldName", worldName);
        payload.put("startedAt", startedAt);
        payload.put("lastUpdatedAt", lastUpdatedAt);

        List<Map<String, Object>> serializedEvents = new ArrayList<>();
        for (RecordedEvent event : events) {
            serializedEvents.add(event.toSerializableMap());
        }
        payload.put("events", serializedEvents);
        return payload;
    }
}
