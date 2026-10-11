package ru.shipitteam.targets;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.Map;

public final class PlayerSnapshot {
    private PlayerSnapshot() {
    }

    public static Map<String, Object> snapshot(ClientPlayerEntity player, double targetDistance, boolean hasTarget) {
        Vec3d velocity = player.getVelocity();
        double speed = velocity.length();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("x", player.getX());
        data.put("y", player.getY());
        data.put("z", player.getZ());
        data.put("yaw", player.getYaw());
        data.put("pitch", player.getPitch());
        data.put("speed", speed);
        data.put("velocityX", velocity.x);
        data.put("velocityY", velocity.y);
        data.put("velocityZ", velocity.z);
        data.put("onGround", player.isOnGround());
        data.put("sprinting", player.isSprinting());
        data.put("sneaking", player.isSneaking());
        data.put("jumping", player.jumpLoadingCount > 0 || player.isInLava() || player.isClimbing());
        data.put("health", player.getHealth());
        data.put("food", player.getHungerManager().getFoodLevel());
        data.put("targetDistance", targetDistance);
        data.put("hasTarget", hasTarget);
        data.put("attackCooldown", player.getAttackCooldownProgress(0.0F));
        return data;
    }
}
