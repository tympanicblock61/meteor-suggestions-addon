//https://github.com/MeteorDevelopment/meteor-client/issues/3562
package com.tympanic.suggestion.mixins;

import meteordevelopment.meteorclient.mixininterface.IRaycastContext;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.combat.CrystalAura;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrystalAura.class)
public class CrystalAuraMixin extends Module {
    public CrystalAuraMixin(Category category, String name, String description) {
        super(category, name, description);
    }

    private final SettingGroup sgRayCast = settings.createGroup("RayCast-settings");
    private Setting<RaycastContext.ShapeType> rayCastShape;
    private Setting<RaycastContext.FluidHandling> rayCastFluid;
    @Shadow
    private RaycastContext raycastContext;
    @Shadow
    @Final
    private Vec3d vec3d;
    @Shadow
    @Final
    private Vec3d vec3dRayTraceEnd;
    @Shadow
    @Final
    private Vec3d playerEyePos;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInit(CallbackInfo ci) {
        rayCastShape = sgRayCast.add(new EnumSetting.Builder<RaycastContext.ShapeType>()
            .name("shape-type")
            .description("raycast shape type for raycasting")
            .defaultValue(RaycastContext.ShapeType.COLLIDER)
            .build()
        );
        rayCastFluid = sgRayCast.add(new EnumSetting.Builder<RaycastContext.FluidHandling>()
            .name("fluid-handling-type")
            .description("raycast fluid handling type for raycasting")
            .defaultValue(RaycastContext.FluidHandling.NONE)
            .build()
        );
    }

    @Inject(method = "onActivate", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/RaycastContext;<init>(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/RaycastContext$ShapeType;Lnet/minecraft/world/RaycastContext$FluidHandling;Lnet/minecraft/entity/Entity;)V"))
    private void onActivate(CallbackInfo info) {
        assert mc.player != null;
        raycastContext = new RaycastContext(new Vec3d(0, 0, 0), new Vec3d(0, 0, 0), rayCastShape.get(), rayCastFluid.get(), mc.player);
    }

    @Inject(method = "getPlaceInfo", at = @At(value = "INVOKE_ASSIGN", target = "Lmeteordevelopment/meteorclient/mixininterface/IRaycastContext;set(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/RaycastContext$ShapeType;Lnet/minecraft/world/RaycastContext$FluidHandling;Lnet/minecraft/entity/Entity;)V"))
    private void getPlaceInfo(BlockPos blockPos, CallbackInfoReturnable<BlockHitResult> cir) {
        ((IRaycastContext) raycastContext).set(vec3d, vec3dRayTraceEnd, rayCastShape.get(), rayCastFluid.get(), mc.player);
    }

    @Inject(method = "isOutOfRange", at = @At(value = "INVOKE_ASSIGN", target = "Lmeteordevelopment/meteorclient/mixininterface/IRaycastContext;set(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/RaycastContext$ShapeType;Lnet/minecraft/world/RaycastContext$FluidHandling;Lnet/minecraft/entity/Entity;)V"))
    private void isOutOfRange(Vec3d vec3d, BlockPos blockPos, boolean place, CallbackInfoReturnable<Boolean> cir) {
        ((IRaycastContext) raycastContext).set(playerEyePos, vec3d, rayCastShape.get(), rayCastFluid.get(), mc.player);
    }
}
