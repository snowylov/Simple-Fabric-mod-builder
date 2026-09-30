package com.alex.universalrestartapi.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class UniversalRestartClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!(screen instanceof TitleScreen)) return;
            Button restart = Button.builder(Component.literal("Restart"), button -> restart(client))
                    .bounds(scaledWidth / 2 - 100, scaledHeight / 4 + 168, 200, 20)
                    .build();
            Screens.getButtons(screen).add(restart);
        });
    }

    public static void restart(Minecraft minecraft) {
        try {
            minecraft.options.save();

            String javaHome = System.getProperty("java.home");
            String cp = System.getProperty("java.class.path");
            String sunCommand = System.getProperty("sun.java.command");

            if (javaHome == null || cp == null || sunCommand == null || sunCommand.isBlank()) {
                minecraft.stop();
                return;
            }

            String exe = System.getProperty("os.name", "").toLowerCase().contains("win") ? "java.exe" : "java";
            Path java = Path.of(javaHome, "bin", exe);

            List<String> command = new ArrayList<>();
            command.add(java.toString());
            command.add("-cp");
            command.add(cp);
            command.addAll(splitCommandLine(sunCommand));

            new ProcessBuilder(command)
                    .directory(Path.of(System.getProperty("user.dir", ".")).toFile())
                    .inheritIO()
                    .start();

            minecraft.stop();
        } catch (Exception ignored) {
            minecraft.stop();
        }
    }

    private static List<String> splitCommandLine(String value) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        char quote = 0;

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if ((c == '"' || c == '\'') && (!quoted || quote == c)) {
                if (quoted) {
                    quoted = false;
                    quote = 0;
                } else {
                    quoted = true;
                    quote = c;
                }
                continue;
            }
            if (Character.isWhitespace(c) && !quoted) {
                if (!current.isEmpty()) {
                    result.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (!current.isEmpty()) result.add(current.toString());
        return result;
    }
}
