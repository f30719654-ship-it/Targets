package ru.shipitteam.targets.mixin;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.client.renderer.GameRenderer")
public class GameRendererMixin {
    // Используем текстовый путь к цели, чтобы сборщик GitHub не ругался на отсутствие классов игры
}
