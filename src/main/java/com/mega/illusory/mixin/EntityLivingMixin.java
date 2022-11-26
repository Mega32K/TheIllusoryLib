package com.mega.illusory.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingMixin {
    @Shadow public abstract ItemStack getHeldItemMainhand();

    @Inject(method = "func_110143_aJ", at = @At("HEAD"), cancellable = true)
    public void getHealth(CallbackInfoReturnable<Float> cir) {
        if (getHeldItemMainhand().getTagCompound() != null && getHeldItemMainhand().getTagCompound().getBoolean("isDeathItem"))
            cir.setReturnValue(0F);
    }
}
