package einstein.subtle_effects.mixin.client.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @WrapOperation(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation replaceParrotTexture(LivingEntityRenderer<T, M> renderer, Entity entity, Operation<ResourceLocation> original) {
        if (entity.getType() == EntityType.PARROT && ((Parrot) entity).isPartyParrot()) {
            String name = entity.getName().getString();
            String nameLowercase = name.toLowerCase();

            if (entity.hasCustomName() && ("jeb_".equals(name) || "sirocco".equals(nameLowercase) || "party parrot".contains(nameLowercase))) {
                int speed = 2;
                int tickCount = entity.tickCount;
                int i = tickCount / speed + entity.getId();
                int length = Parrot.Variant.values().length;
                int index = i % length;
                int nextIndex = (i + 1) % length;
                float delta = ((float) (tickCount % speed) + Util.getPartialTicks()) / speed;

                return ParrotRenderer.getVariantTexture(Parrot.Variant.values()[Mth.lerpInt(delta, index, nextIndex)]);
            }
        }
        return original.call(renderer, entity);
    }
}
