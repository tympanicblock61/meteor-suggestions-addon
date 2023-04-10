package com.tympanic.suggestion.mixins;

import com.tympanic.suggestion.modules.AntiSlip;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Block.class, priority = 999, remap = false)
public class BlockMixin {
    @Inject(method = "getSlipperiness", at = @At("RETURN"), cancellable = true)
    public void getSlipperiness(CallbackInfoReturnable<Float> info) {
        if (Modules.get() == null) return;
        AntiSlip antiSlip = Modules.get().get(AntiSlip.class);
        Block block = (Block) (Object) this;
        if (antiSlip.isActive() && antiSlip.blocks.get().contains(block)) {
            info.setReturnValue(0.6f);
        }
    }
}
