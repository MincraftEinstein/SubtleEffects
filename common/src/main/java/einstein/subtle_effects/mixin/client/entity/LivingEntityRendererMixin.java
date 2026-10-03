package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModRenderStateAttachmentKeys;
import einstein.subtle_effects.util.RenderStateAttachmentAccessor;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.parrot.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void extractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (entity.getType() == EntityType.PARROT && ((Parrot) entity).isPartyParrot()) {
            String name = entity.getName().getString();
            String nameLowercase = name.toLowerCase();

            if (entity.hasCustomName() && ("jeb_".equals(name) || "sirocco".equals(nameLowercase) || "party parrot".contains(nameLowercase))) {
                RenderStateAttachmentAccessor accessor = (RenderStateAttachmentAccessor) state;
                accessor.subtleEffects$set(ModRenderStateAttachmentKeys.IS_PARTY_PARROT, true);
                accessor.subtleEffects$set(ModRenderStateAttachmentKeys.TICK_COUNT, entity.tickCount);
                accessor.subtleEffects$set(ModRenderStateAttachmentKeys.ENTITY_ID, entity.getId());
            }
        }
    }

    @WrapOperation(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Lnet/minecraft/resources/Identifier;"))
    private Identifier replaceParrotTexture(LivingEntityRenderer<T, S, M> renderer, S s, Operation<Identifier> original) {
        RenderStateAttachmentAccessor accessor = (RenderStateAttachmentAccessor) s;

        if (accessor.subtleEffects$get(ModRenderStateAttachmentKeys.IS_PARTY_PARROT, false)) {
            int speed = 2;
            int tickCount = accessor.subtleEffects$get(ModRenderStateAttachmentKeys.TICK_COUNT, 0);
            int i = tickCount / speed + accessor.subtleEffects$get(ModRenderStateAttachmentKeys.ENTITY_ID, 0);
            int length = Parrot.Variant.values().length;
            int index = i % length;
            int nextIndex = (i + 1) % length;
            float delta = ((float) (tickCount % speed) + Util.getPartialTicks()) / speed;

            return ParrotRenderer.getVariantTexture(Parrot.Variant.values()[Mth.lerpInt(delta, index, nextIndex)]);
        }
        return original.call(renderer, s);
    }
}
