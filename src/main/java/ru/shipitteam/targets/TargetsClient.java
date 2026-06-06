package ru.shipitteam.targets;

import net.fabricmc.api.ClientModInitializer;

public class TargetsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("[Targets Mod] Мод успешно загружен!");
    }
}
