package ru.shipitteam.targets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class NeuroAuraScreen extends Screen {
    private final NeuroAuraManager manager;

    public NeuroAuraScreen(NeuroAuraManager manager) {
        super(Text.literal("Neuro Aura"));
        this.manager = manager;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2 - 125;
        int startY = 40;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Start Recording"), button -> {
            manager.startRecording();
        }).dimensions(centerX, startY, 250, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Stop Recording"), button -> {
            manager.stopRecording();
        }).dimensions(centerX, startY + 28, 250, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Playback"), button -> {
            manager.playbackLatest();
        }).dimensions(centerX, startY + 56, 250, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Toggle Hotkey"), button -> {
            manager.toggleRecording();
        }).dimensions(centerX, startY + 84, 250, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> {
            MinecraftClient.getInstance().setScreen(null);
        }).dimensions(centerX, startY + 112, 250, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Neuro Aura"), this.width / 2, 16, 0xFFB0E0FF);

        String status = manager.isRecording() ? "Recording active" : "Recording inactive";
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(status), this.width / 2, 25, 0xFFFFFFFF);

        List<String> savedSessions = manager.getSessionNames();
        if (!savedSessions.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Saved sessions: " + savedSessions.size()), this.width / 2, this.height - 44, 0xFF99FF99);
        }
    }
}
