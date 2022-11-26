package com.mega.illusory.manager;

import com.google.common.collect.Multimap;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ModManager {
    public static final Loader loader = Loader.instance();

    public static LoadController controller() throws Throwable {
        Field controller = ReflectionHelper.findField(Loader.class, "modController");
        Object loadController = controller.get(loader);
        return loadController instanceof LoadController ? (LoadController) loadController : null;

    }

    public static Map<String, ModContainer> namedMods() throws Throwable {
        Field o = ReflectionHelper.findField(Loader.class, "namedMods");
        Map<String, ModContainer> nameMods = (Map<String, ModContainer>) o.get(loader);
        return nameMods;

    }

    public static Multimap<String, LoaderState.ModState> modStates() throws Throwable {
        Field modStates = ReflectionHelper.findField(LoadController.class, "modStates");
        return (Multimap<String, LoaderState.ModState>) modStates.get(loader);
    }

    public static List<ModContainer> getMods() {
        return loader.getModList();
    }

    public static List<ModContainer> getActiveMods() {
        return loader.getActiveModList();
    }

    public static LoaderState state() {
        return loader.getLoaderState();
    }

    public static void removeMod(String modName) {
        if (!Loader.isModLoaded(modName))
            return;
        try {
            if (modStates() != null && modStates().containsKey(modName))
                modStates().put(modName, LoaderState.ModState.DISABLED);
            namedMods().remove(modName);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void clearMods() {
        try {
            getMods().clear();
            getActiveMods().clear();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}
