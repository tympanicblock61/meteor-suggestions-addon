//profiles keybinds: https://github.com/MeteorDevelopment/meteor-client/issues/3536

package com.tympanic.suggestion.mixins.meteor;

import meteordevelopment.meteorclient.settings.KeybindSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.profiles.Profile;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(value = Profile.class, remap = false)
public abstract class ProfileMixin {
    @Shadow
    @Final
    private SettingGroup sgGeneral;

    @Shadow
    public abstract void load();

    @Shadow
    protected abstract File getFile();

    public Setting<Keybind> keybind;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInit(CallbackInfo ci) {
        keybind = sgGeneral.add(new KeybindSetting.Builder()
            .name("keybind")
            .description("key to press to load")
            .defaultValue(Keybind.none())
            .action(() -> {if (this.getFile().exists()) this.load();})
            .build()
        );
    }
}
