//toggle prone module: https://github.com/MeteorDevelopment/meteor-client/issues/3532
package com.tympanic.suggestion.modules;

import meteordevelopment.meteorclient.settings.KeybindSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;

public class Prone extends Module {
    public Prone() {super(Categories.Player, "prone", "allows you to be prone anywhere");}
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Keybind> keybind = sgGeneral.add(new KeybindSetting.Builder()
        .name("keybind")
        .description("key to press to toggle")
        .defaultValue(Keybind.none())
        .action(this::toggle)
        .build()
    );
}
