package einstein.subtle_effects.mixin.client.item.component;

import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.ColorAndIntegerParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static einstein.subtle_effects.init.ModConfigs.ENTITIES;

@Mixin(Consumable.class)
public class ConsumableMixin {

    @Inject(method = "onConsume", at = @At(value = "HEAD"))
    private void spawnPotionParticles(Level level, LivingEntity entity, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (level.isClientSide) {
            Minecraft minecraft = Minecraft.getInstance();
            if (entity instanceof Player player) {
                if (player.equals(minecraft.player) && !ENTITIES.humanoids.potionRingsDisplayType.test(minecraft)) {
                    return;
                }
            }

            ItemStack useItem = entity.getUseItem();
            if (!stack.isEmpty() && useItem.has(DataComponents.POTION_CONTENTS)) {
                PotionContents contents = useItem.get(DataComponents.POTION_CONTENTS);

                // noinspection all
                if (contents.hasEffects()) {
                    int color = contents.getColor();

                    level.addParticle(new ColorAndIntegerParticleOptions(ModParticles.POTION_EMITTER.get(), color, entity.getId()),
                            entity.getX(),
                            entity.getY(),
                            entity.getZ(),
                            0, 0, 0
                    );
                }
            }
        }
    }
}
