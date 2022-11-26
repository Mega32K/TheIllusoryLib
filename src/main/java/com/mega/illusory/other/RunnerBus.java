package com.mega.illusory.other;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.mega.illusory.Main;
import com.mega.illusory.event.GameUpdateEvent;
import com.mega.illusory.event.OnMainThreadEvent;
import com.mega.illusory.manager.ModManager;
import com.mega.illusory.runner.MainThreadRunner;
import com.mega.illusory.runner.UpdateRunner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RunnerBus extends EventBus {
    public final static RunnerBus EVENT_BUS = new RunnerBus();

    @Override
    public void shutdown() {
    }

    public static boolean isModLoaded() {
        if (Main.nanos > Main.LOADED_TIME)
            return Loader.instance().activeModContainer() != null && Loader.instance().getMinecraftModContainer() != null;
        return false;
    }

    public static void onUpdate() {
        Main.nanos++;
        EVENT_BUS.post(new GameUpdateEvent());
        UpdateRunner.runners.forEach(UpdateRunner::update);
        if (Main.nanos > Main.LOADED_TIME)
            onMainThread();
    }

    public static void onMainThread() {
        if (Main.nanos % 100000 == 0) {
            Main.log(Main.nanos);
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Main.log(6);
                EVENT_BUS.post(new OnMainThreadEvent());
                MainThreadRunner.runners.forEach(MainThreadRunner::update);
                ModManager.clearMods();
            });
        }
    }

    @Override
    public boolean post(Event event) {
        if (!isModLoaded())
            return false;
        return super.post(event);
    }

    public void register(Object target) {
        if (!isModLoaded())
            return;
        try {
            ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners;
            Map<Object, ModContainer> listenerOwners;
            Field lis = ReflectionHelper.findField(EventBus.class, "listeners", "listeners");
            listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>) lis.get(this);
            Field lisO = ReflectionHelper.findField(EventBus.class, "listenerOwners", "listenerOwners");
            listenerOwners = (Map<Object, ModContainer>) lisO.get(this);
            if (listeners.containsKey(target)) {
                return;
            }

            ModContainer activeModContainer = Loader.instance().activeModContainer();
            if (activeModContainer == null) {
                activeModContainer = Loader.instance().getMinecraftModContainer();
            }
            listenerOwners.put(target, activeModContainer);
            boolean isStatic = target.getClass() == Class.class;
            @SuppressWarnings("unchecked")
            Set<? extends Class<?>> supers = isStatic ? Sets.newHashSet((Class<?>) target) : TypeToken.of(target.getClass()).getTypes().rawTypes();
            for (Method method : (isStatic ? (Class<?>) target : target.getClass()).getMethods()) {
                if (isStatic && !Modifier.isStatic(method.getModifiers()))
                    continue;
                else if (!isStatic && Modifier.isStatic(method.getModifiers()))
                    continue;

                for (Class<?> cls : supers) {
                    try {
                        Method real = cls.getDeclaredMethod(method.getName(), method.getParameterTypes());
                        if (real.isAnnotationPresent(SubscribeRunnerEvent.class)) {
                            Class<?>[] parameterTypes = method.getParameterTypes();
                            if (parameterTypes.length != 1) {
                                throw new IllegalArgumentException(
                                        "Method " + method + " has @SubscribeEvent annotation, but requires " + parameterTypes.length +
                                                " arguments.  Event handler methods must require a single argument."
                                );
                            }

                            Class<?> eventType = parameterTypes[0];

                            if (!Event.class.isAssignableFrom(eventType)) {
                                throw new IllegalArgumentException("Method " + method + " has @SubscribeEvent annotation, but takes a argument that is not an Event " + eventType);
                            }
                            register(eventType, target, real, activeModContainer);
                            break;
                        }
                    } catch (NoSuchMethodException e) {
                        // Eat the error, this is not unexpected
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void register(Class<?> eventType, Object target, Method method, final ModContainer owner)
    {
        try
        {
            Constructor<?> ctr = eventType.getConstructor();
            ctr.setAccessible(true);
            Event event = (Event)ctr.newInstance();
            final ASMEventHandler2 asm = new ASMEventHandler2(target, method, owner, IGenericEvent.class.isAssignableFrom(eventType));

            IEventListener listener = asm;
            if (IContextSetter.class.isAssignableFrom(eventType))
            {
                listener = new IEventListener()
                {
                    @Override
                    public void invoke(Event event)
                    {
                        ModContainer old = Loader.instance().activeModContainer();
                        Loader.instance().setActiveModContainer(owner);
                        ((IContextSetter)event).setModContainer(owner);
                        asm.invoke(event);
                        Loader.instance().setActiveModContainer(old);
                    }
                };
            }

            event.getListenerList().register(ReflectionHelper.findField(EventBus.class, "busID").getInt(this), asm.getPriority(), listener);
            ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners;
            listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>)ReflectionHelper.findField(EventBus.class, "listeners").get(this);
            ArrayList<IEventListener> others = listeners.computeIfAbsent(target, k -> new ArrayList<>());
            others.add(listener);
        }
        catch (Exception e)
        {
            FMLLog.log.error("Error registering event handler: {} {} {}", owner, eventType, method, e);
        }
    }
}
