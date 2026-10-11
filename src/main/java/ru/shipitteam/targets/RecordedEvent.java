package ru.shipitteam.targets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecordedEvent {
    private final long tick;
    private final long timestamp;
    private final String type;
    private final Map<String, Object> data;

    public RecordedEvent(long tick, String type, Map<String, Object> data) {
        this.tick = tick;
        this.timestamp = System.currentTimeMillis();
        this.type = type;
        this.data = data == null ? new LinkedHashMap<>() : new LinkedHashMap<>(data);
    }

    public long getTick() {
        return tick;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public Map<String, Object> getData() {
        return new LinkedHashMap<>(data);
    }

    public Map<String, Object> toSerializableMap() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("tick", tick);
        payload.put("timestamp", timestamp);
        payload.put("type", type);
        payload.put("data", data);
        return payload;
    }
}
