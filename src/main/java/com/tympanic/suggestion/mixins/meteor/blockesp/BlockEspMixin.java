//BlockEsp exposed setting: https://github.com/MeteorDevelopment/meteor-client/issues/3541
package com.tympanic.suggestion.mixins.meteor.blockesp;

import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.render.blockesp.BlockESP;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockESP.class, remap = false)
public class BlockEspMixin {
    @Shadow
    @Final
    private SettingGroup sgGeneral;
    private Setting<Boolean> exposedOnly;
    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInit(CallbackInfo ci) {
        exposedOnly = sgGeneral.add(new BoolSetting.Builder()
            .name("exposed-only")
            .description("only show exposed blocks")
            .defaultValue(false)
            .build()
        );
    }
}
