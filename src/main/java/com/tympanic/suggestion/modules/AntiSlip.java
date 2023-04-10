//https://github.com/MeteorDevelopment/meteor-client/issues/3035
package com.tympanic.suggestion.modules;

import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import java.util.List;

public class AntiSlip extends Module {
    public AntiSlip() {
        super(Categories.Player, "anti-slip", "makes slippery blocks not slippery.");
    }
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("what blocks to remove slipperiness from")
        .defaultValue(Blocks.ICE)
        .filter(this::blockFilter)
        .build()
    );

    private boolean blockFilter(Block block) {
        return block == Blocks.ICE ||
            block == Blocks.PACKED_ICE ||
            block == Blocks.BLUE_ICE ||
            block == Blocks.HONEY_BLOCK ||
            block == Blocks.SLIME_BLOCK;
    }
}
