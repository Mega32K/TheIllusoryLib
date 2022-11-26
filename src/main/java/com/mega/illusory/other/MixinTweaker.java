package com.mega.illusory.other;

import com.mega.illusory.Main;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.Date;
import java.util.List;

public class MixinTweaker implements ITweaker {
    public MixinTweaker() {
    }
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {

    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        Main.log(new Date());
        Main.log("ILLUSORY MIXIN LOADED");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.mega.json");
    }

    @Override
    public String getLaunchTarget() {
        return null;
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
