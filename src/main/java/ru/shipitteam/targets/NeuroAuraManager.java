package ru.shipitteam.targets;

import net.minecraft.client.network.ClientPlayerEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NeuroAuraManager {
    private RecordingSession activeSession;
    private boolean recording;
    private float lastAttackCooldown = 0.0F;
    private String lastWorldName = "unknown";
    private final List<String> savedSessionNames = new ArrayList<>();

    public void startRecording() {
        if (recording) {
            return;
        }

        String sessionId = "session_" + System.currentTimeMillis();
        activeSession = new RecordingSession(sessionId, "PvP Session " + sessionId, lastWorldName);
        recording = true;
        TargetsClient.sendMessage("[Neuro Aura] Recording started");
    }

    public void stopRecording() {
        if (!recording || activeSession == null) {
            return;
        }

        recording = false;
        RecordStorage.saveSession(activeSession);
        savedSessionNames.add(activeSession.getId());
        TargetsClient.sendMessage("[Neuro Aura] Recording saved: " + activeSession.getId());
        activeSession = null;
    }

    public void toggleRecording() {
        if (recording) {
            stopRecording();
        } else {
            startRecording();
        }
    }

    public void onJoinWorld(ClientPlayerEntity player) {
        this.lastWorldName = player.getWorld().getRegistryKey().getValue().toString();
    }

    public void onDisconnect() {
        if (recording) {
            stopRecording();
        }
    }

    public void onClientTick(ClientPlayerEntity player) {
        if (!recording || player == null || activeSession == null) {
            return;
        }

        double targetDistance = 0.0D;
        boolean hasTarget = false;

        if (player.getTargetingEntity() != null) {
            hasTarget = true;
            targetDistance = player.squaredDistanceTo(player.getTargetingEntity());
            targetDistance = Math.sqrt(targetDistance);
        }

        Map<String, Object> snapshot = PlayerSnapshot.snapshot(player, targetDistance, hasTarget);
        activeSession.addEvent(new RecordedEvent(activeSession.getEvents().size(), "snapshot", snapshot));

        float attackCooldown = player.getAttackCooldownProgress(0.0F);
        if (lastAttackCooldown <= 0.0F && attackCooldown > 0.0F) {
            Map<String, Object> attackData = new LinkedHashMap<>();
            attackData.put("x", player.getX());
            attackData.put("y", player.getY());
            attackData.put("z", player.getZ());
            attackData.put("yaw", player.getYaw());
            attackData.put("pitch", player.getPitch());
            attackData.put("targetDistance", targetDistance);
            activeSession.addEvent(new RecordedEvent(activeSession.getEvents().size(), "attack", attackData));
        }
        lastAttackCooldown = attackCooldown;

        if (player.input.jumping) {
            Map<String, Object> jumpData = new LinkedHashMap<>();
            jumpData.put("x", player.getX());
            jumpData.put("y", player.getY());
            jumpData.put("z", player.getZ());
            activeSession.addEvent(new RecordedEvent(activeSession.getEvents().size(), "jump", jumpData));
        }
    }

    public boolean isRecording() {
        return recording;
    }

    public RecordingSession getActiveSession() {
        return activeSession;
    }

    public List<String> getSessionNames() {
        List<String> names = new ArrayList<>(savedSessionNames);
        names.addAll(RecordStorage.getSavedSessionNames());
        return names;
    }

    public void playbackLatest() {
        List<String> saved = RecordStorage.getSavedSessionNames();
        if (saved.isEmpty()) {
            TargetsClient.sendMessage("[Neuro Aura] No recorded sessions found.");
            return;
        }

        String latest = saved.get(saved.size() - 1);
        RecordingSession session = RecordStorage.loadSession(latest);
        if (session == null) {
            TargetsClient.sendMessage("[Neuro Aura] Failed to load session: " + latest);
            return;
        }

        Map<String, Object> analysis = PatternAnalyzer.analyze(session);
        TargetsClient.sendMessage("[Neuro Aura] Playback loaded: " + latest + " | " + analysis.get("patterns"));
    }
}
