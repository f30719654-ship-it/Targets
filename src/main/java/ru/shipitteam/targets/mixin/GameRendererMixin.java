package ru.shipitteam.targets.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    // Пустой миксин, чтобы Fabric не выдавал ошибку при запуске
}
