//toggle prone module: https://github.com/MeteorDevelopment/meteor-client/issues/3532
package com.tympanic.suggestion.mixins.minecraft;

import com.tympanic.suggestion.modules.Prone;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(value = Entity.class, remap = false, priority = 999)
public class EntityMixin {
    Prone module = Modules.get().get(Prone.class);

    @Inject(method = "isInPose", at = @At(value = "HEAD"), cancellable = true)
    public void injectIsInPose(EntityPose pose, CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player;
        try {
            player =  ((ClientPlayerEntity) (Object)this);
        } catch(Exception e) {player = null;}
        if (module.isActive() && mc.player != null && player != null && player.isPlayer()) {
            mc.player.setPose(EntityPose.SWIMMING);
            if (pose == EntityPose.SWIMMING) cir.setReturnValue(true);
        }
    }

    @Inject(method = "isTouchingWater", at = @At(value = "HEAD"), cancellable = true)
    public void injectIsTouchingWater(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player;
        try {
            player =  ((ClientPlayerEntity) (Object)this);
        } catch(Exception e) {player = null;}
        if (module.isActive() && player != null && player.isPlayer()) cir.setReturnValue(false);
    }
}
