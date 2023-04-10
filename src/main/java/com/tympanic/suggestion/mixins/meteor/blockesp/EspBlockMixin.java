//BlockEsp exposed setting: https://github.com/MeteorDevelopment/meteor-client/issues/3541
package com.tympanic.suggestion.mixins.meteor.blockesp;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.blockesp.BlockESP;
import meteordevelopment.meteorclient.systems.modules.render.blockesp.ESPBlock;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(value = ESPBlock.class, remap = false)
public class EspBlockMixin {
    @Shadow
    @Final
    private static BlockPos.Mutable blockPos;
    private final Settings settings = Modules.get().get(BlockESP.class).settings;

    public boolean isBlocked(Block block, BlockPos blockPos) {
        Setting<List<Block>> blocks = (Setting<List<Block>>)settings.get("blocks");
        Setting<Boolean> exposedOnly = (Setting<Boolean>)settings.get("exposed-only");
        return !(blocks.get().contains(block) && (!exposedOnly.get() || (blockPos == null || BlockUtils.isExposed(blockPos))));
    }
    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    public void injectRender(Render3DEvent event, CallbackInfo ci) {
        assert mc.world != null;
        if (isBlocked(mc.world.getBlockState(blockPos).getBlock(), blockPos)) ci.cancel();
    }
}
