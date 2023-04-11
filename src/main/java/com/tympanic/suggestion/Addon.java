package com.tympanic.suggestion;

import com.tympanic.suggestion.commands.CommandExample;
import com.tympanic.suggestion.hud.HudExample;
import com.tympanic.suggestion.modules.Prone;
import com.tympanic.suggestion.modules.AntiSlip;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Example");
    public static final HudGroup HUD_GROUP = new HudGroup("Example");

    @Override
    public void onInitialize() {
        Modules m = Modules.get();
        m.add(new AntiSlip());
        m.add(new Prone());
        Commands.get().add(new CommandExample());
        Hud.get().register(HudExample.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.tympanic.suggestion";
    }
}
