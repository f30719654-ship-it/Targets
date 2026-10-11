package ru.shipitteam.targets;

import net.minecraft.client.network.ClientPlayerEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PatternAnalyzer {
    private PatternAnalyzer() {
    }

    public static Map<String, Object> analyze(RecordingSession session) {
        Map<String, Object> summary = new LinkedHashMap<>();
        List<RecordedEvent> events = session.getEvents();

        long attackCount = events.stream().filter(event -> "attack".equals(event.getType())).count();
        long jumpCount = events.stream().filter(event -> "jump".equals(event.getType())).count();
        long snapshotCount = events.stream().filter(event -> "snapshot".equals(event.getType())).count();

        double averageSpeed = 0.0;
        int speedSamples = 0;
        for (RecordedEvent event : events) {
            if (!"snapshot".equals(event.getType())) {
                continue;
            }
            Object speed = event.getData().get("speed");
            if (speed instanceof Number number) {
                averageSpeed += number.doubleValue();
                speedSamples++;
            }
        }
        if (speedSamples > 0) {
            averageSpeed /= speedSamples;
        }

        List<String> patterns = new ArrayList<>();
        if (attackCount > 0) {
            patterns.add("attack_timing");
        }
        if (jumpCount > 0) {
            patterns.add("jump_pressure");
        }
        if (snapshotCount > 25) {
            patterns.add("strafe_cycle");
        }
        if (patterns.isEmpty()) {
            patterns.add("low_data");
        }

        summary.put("attack_count", attackCount);
        summary.put("jump_count", jumpCount);
        summary.put("snapshot_count", snapshotCount);
        summary.put("average_speed", averageSpeed);
        summary.put("patterns", patterns);
        return summary;
    }
}
