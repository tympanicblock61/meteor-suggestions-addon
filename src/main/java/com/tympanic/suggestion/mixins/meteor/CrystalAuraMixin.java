//raycast settings: https://github.com/MeteorDevelopment/meteor-client/issues/3562
package com.tympanic.suggestion.mixins.meteor;

import meteordevelopment.meteorclient.mixininterface.IRaycastContext;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.combat.CrystalAura;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrystalAura.class, remap = false)
public class CrystalAuraMixin extends Module {
    @Shadow
    private RaycastContext raycastContext;
    private final SettingGroup sgRayCast = settings.createGroup("RayCast-settings");
    private Setting<RaycastContext.ShapeType> rayCastShape;
    private Setting<RaycastContext.FluidHandling> rayCastFluid;

    public CrystalAuraMixin(Category category, String name, String description) {
        super(category, name, description);
    }

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

    @Redirect(method = "getPlaceInfo", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/mixininterface/IRaycastContext;set(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/RaycastContext$ShapeType;Lnet/minecraft/world/RaycastContext$FluidHandling;Lnet/minecraft/entity/Entity;)V"))
    private void injectGetPlaceInfo(IRaycastContext instance, Vec3d vec3d, Vec3d vec3dRayTraceEnd, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling, Entity entity) {
        ((IRaycastContext) raycastContext).set(vec3d, vec3dRayTraceEnd, rayCastShape.get(), rayCastFluid.get(), mc.player);
    }
    @Redirect(method = "isOutOfRange", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/mixininterface/IRaycastContext;set(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/RaycastContext$ShapeType;Lnet/minecraft/world/RaycastContext$FluidHandling;Lnet/minecraft/entity/Entity;)V"))
    private void injectIsOutOfRange(IRaycastContext instance, Vec3d playerEyePos, Vec3d vec3d, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling, Entity entity) {
        ((IRaycastContext) raycastContext).set(playerEyePos, vec3d, rayCastShape.get(), rayCastFluid.get(), mc.player);
    }
}
