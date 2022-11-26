package com.mega.illusory;

import com.mega.illusory.other.RunnerBus;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author mega_darkness
  * I used some BadCoreMode codes
 */
@IFMLLoadingPlugin.Name("Illusory")
@IFMLLoadingPlugin.SortingIndex(Integer.MIN_VALUE)
public class Main implements IFMLLoadingPlugin {
    public static long nanos;
    public static long LOADED_TIME = 16000000;
    private static final ScheduledExecutorService transformers_killer = Executors.newSingleThreadScheduledExecutor();
    public static final List<IClassTransformer> RESTRICTED_TRANSFORMER = new ArrayList<>();
    public static final String PATH = "com.mega.illusory.Main";
    private static boolean ifRunsafe = false;  
    public Main() {
        String s = "\u6211\u6ca1\u6709\u5b9e\u7528\u0042\u0061\u0064\u0043\u006f\u0072\u0065\u006d\u006f\u0064\uff0c\u90a3\u8fc7\u4e8e\u66b4\u529b" +
                "codes ->" +
                "defenseCoremod(); fuckTransformers(); StartSafe();";
        log(s);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(RunnerBus::onUpdate, 1, 1, TimeUnit.NANOSECONDS);
    }

    public static void log(Object... o) {
        System.out.println("Illusory:" + Arrays.toString(o));
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    /*
    public static void StartSafe() {
        if (!ifRunsafe) {
            transformers_killer.schedule(() -> {
                try {
                    defenseCoremod();
                    fuckTransformers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },1L, TimeUnit.MICROSECONDS);
            ifRunsafe = true;
        }
    }

    public static void fuckTransformers() {
        try {
            Field f = Launch.classLoader.getClass().getDeclaredFields()[3];
            f.setAccessible(true);
            List<IClassTransformer> oldtransformers = (List<IClassTransformer>)f.get(Launch.classLoader);
            List<IClassTransformer> transformers = new ArrayList<IClassTransformer>(oldtransformers) {
                public boolean add(IClassTransformer e) {
                    String TransformerName = e.getClass().getName();
                    if (TransformerName.startsWith("$wrapper."))
                        TransformerName = TransformerName.replace("$wrapper.", "");
                    if ((!TransformerName.startsWith("net.minecraft") ||
                            !TransformerName.startsWith("net.minecraftforge")) &&
                            !TransformerName.startsWith(PATH)) {
                        if (TransformerName.startsWith("org.spongepowered.asm")) {
                            log("<Transformers monitoring>:Fuck the mixin:{}", TransformerName);
                            return false;
                        }
                        log("<Transformers monitoring>:Intercepted non minecraft transformer, restricted transformer:{}", TransformerName);
                        RESTRICTED_TRANSFORMER.add(e);
                        return false;
                    }
                    for (IClassTransformer Transformer : this) {
                        String ListTransformerName = Transformer.getClass().getName();
                        if (ListTransformerName.startsWith("$wrapper."))
                            ListTransformerName = ListTransformerName.replace("$wrapper.", "");
                        if (ListTransformerName.equals(TransformerName))
                            return false;
                    }
                    log("<Transformers monitoring>:Transformer Add:{}", TransformerName);
                    return super.add(e);
                }
            };
            f.set(Launch.classLoader, transformers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void defenseCoremod() {
        try {
            Field f = CoreModManager.class.getDeclaredFields()[5];
            f.setAccessible(true);
            if (f.get(CoreModManager.class).getClass().getName().startsWith("java.util") &&
                    !f.get(CoreModManager.class).getClass().getName().startsWith(PATH))
                f.set(CoreModManager.class, new ArrayList<Object>((List<?>)f.get(CoreModManager.class)) {
                    public boolean add(Object e) {
                        try {
                            Field f = e.getClass().getDeclaredFields()[1];
                            f.setAccessible(true);
                            IFMLLoadingPlugin coremod = (IFMLLoadingPlugin)f.get(e);
                            log("The " + coremod.getClass().getSimpleName() + " loaded,path:" + coremod.getClass().getCanonicalName().replace("." + coremod.getClass().getSimpleName(), ""));
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        return super.add(e);
                    }
                });
            if (!f.get(CoreModManager.class).getClass().getName().startsWith("java.util") && !f.get(CoreModManager.class).getClass().getName().startsWith(PATH)) {
                log("<Safe Coremod>:A malicious coremod attempts to prevent other coremod from running. The safecoremod of badcoremod has been blocked");
                f.set(CoreModManager.class, new ArrayList<Object>((List<?>)f.get(CoreModManager.class)) {
                    public boolean add(Object e) {
                        try {
                            Field f = e.getClass().getDeclaredFields()[1];
                            f.setAccessible(true);
                            IFMLLoadingPlugin coremod = (IFMLLoadingPlugin)f.get(e);
                            log("The " + coremod.getClass().getSimpleName() + " loaded,path:" + coremod.getClass().getCanonicalName().replace("." + coremod.getClass().getSimpleName(), ""));
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        return super.add(e);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */
}