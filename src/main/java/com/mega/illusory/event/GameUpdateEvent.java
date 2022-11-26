package com.mega.illusory.event;

import com.mega.illusory.Main;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GameUpdateEvent extends Event {
    public long nanos;
    public GameUpdateEvent() {
        this.nanos = Main.nanos;
    }
}
