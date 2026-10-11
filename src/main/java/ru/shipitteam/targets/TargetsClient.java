package ru.shipitteam.targets;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class TargetsClient implements ClientModInitializer {
    public static NeuroAuraManager manager;

    @Override
    public void onInitializeClient() {
        manager = new NeuroAuraManager();
        KeyBindings.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }

            manager.onClientTick(client.player);

            while (KeyBindings.OPEN_GUI.wasPressed()) {
                client.setScreen(new NeuroAuraScreen(manager));
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.player != null) {
                manager.onJoinWorld(client.player);
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            manager.onDisconnect();
        });

        System.out.println("[Targets] Neuro Aura initialized.");
    }

    public static void openGui(MinecraftClient client) {
        if (client != null) {
            client.setScreen(new NeuroAuraScreen(manager));
        }
    }

    public static void sendMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), false);
        }
    }
}
