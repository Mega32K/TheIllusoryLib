package com.mega.illusory;

import com.mega.illusory.event.GameUpdateEvent;
import com.mega.illusory.event.OnMainThreadEvent;
import com.mega.illusory.other.RunnerBus;
import com.mega.illusory.other.SubscribeRunnerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "mod")
public class MyMod {
    @Mod.EventHandler
    public static void init(FMLPreInitializationEvent event) {
        RunnerBus.EVENT_BUS.register(MyMod.class);
    }

    @SubscribeRunnerEvent
    public static void onClientUpdate(OnMainThreadEvent event) {
        if (event.mc.player != null)
            event.mc.player.world.updateEntity(event.mc.player);
    }
}
