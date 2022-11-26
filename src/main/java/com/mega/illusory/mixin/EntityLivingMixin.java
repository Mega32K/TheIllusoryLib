package com.mega.illusory.mixin;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public class EntityLivingMixin {
    @Inject(method = "func_110143_aJ", at = @At("HEAD"), cancellable = true)
    public void getHealth(CallbackInfoReturnable<Float> cir) {
        if (get)
    }
}
