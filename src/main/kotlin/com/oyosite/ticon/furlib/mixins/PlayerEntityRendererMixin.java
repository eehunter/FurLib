package com.oyosite.ticon.furlib.mixins;

import com.oyosite.ticon.furlib.client.SpecieFeature;
import com.oyosite.ticon.furlib.power.SpeciePower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@SuppressWarnings("rawtypes")
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }
    @Inject(method = "setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V", at = @At("TAIL"), cancellable = true)
    public void setModelPose(AbstractClientPlayerEntity p, CallbackInfo info) {
        if(PowerHolderComponent.KEY.get(p).getPowers(SpeciePower.class).size()>0)model.setVisible(false);
    }
    @SuppressWarnings("unchecked")
    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Z)V")
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        SpecieFeature specieFeature = new SpecieFeature((FeatureRendererContext<PlayerEntity, BipedEntityModel<PlayerEntity>>) (Object) this);
        this.addFeature(specieFeature);
    }
}
