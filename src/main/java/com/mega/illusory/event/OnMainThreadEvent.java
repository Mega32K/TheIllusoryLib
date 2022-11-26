package com.mega.illusory.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OnMainThreadEvent extends Event {
    public final Minecraft mc = Minecraft.getMinecraft();
    public final EntityRenderer entityRenderer = mc.entityRenderer;

    public void setIsGamePaused(boolean paused) {
        mc.isGamePaused = paused;
    }

    public void setTimer(Timer timer) {
        mc.timer = timer;
    }
}
