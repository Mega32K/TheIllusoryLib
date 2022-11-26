package com.mega.illusory.other;

import net.minecraftforge.fml.common.eventhandler.EventPriority;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;
import static java.lang.annotation.ElementType.*;

@Retention(value = RUNTIME)
@Target(value = METHOD)
public @interface SubscribeRunnerEvent
{
    EventPriority priority() default EventPriority.NORMAL;
    boolean receiveCanceled() default false;
}