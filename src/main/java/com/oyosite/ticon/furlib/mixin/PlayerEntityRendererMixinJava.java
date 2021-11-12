package com.oyosite.ticon.furlib.mixin;

import com.oyosite.ticon.furlib.client.SpecieFeature;
import com.oyosite.ticon.furlib.power.SpeciePower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@SuppressWarnings("rawtypes")
//@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixinJava extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    /*@Shadow protected abstract void setModelPose(AbstractClientPlayerEntity p);
    SpecieFeature specieFeature;*/

    public PlayerEntityRendererMixinJava(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    /*
    @SuppressWarnings("unchecked")
    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Z)V")
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        specieFeature = new SpecieFeature((FeatureRendererContext<PlayerEntity, BipedEntityModel<PlayerEntity>>) (Object) this);
        this.addFeature(specieFeature);
    }

    @Inject(at = @At("HEAD"), method = "renderArm(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/model/ModelPart;)V", cancellable = true)
    private void iRenderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci){
        if (PowerHolderComponent.KEY.get(player).getPowers(SpeciePower.class).size() <= 0) return;
        PlayerEntityModel pem = getModel();
        setModelPose(player);
        pem.handSwingProgress = 0.0f;
        pem.sneaking = false;
        pem.leaningPitch = 0.0f;
        pem.setAngles(player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        specieFeature.render(matrices, vertexConsumers, light, player, 0f, 0f, 0f, 0f, 0f, 0f, arm == pem.leftArm ? s -> s == "leftArm" : s -> s == "rightArm");
        ci.cancel();
    }

    @Inject(method = "setModelPose(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)V", at = @At("TAIL"), cancellable = true)
    public void setModelPose(AbstractClientPlayerEntity p, CallbackInfo info) {
        if(PowerHolderComponent.KEY.get(p).getPowers(SpeciePower.class).size()>0)model.setVisible(false);
    }*/


}
