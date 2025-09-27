package einstein.subtle_effects.mixin.client.entity;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal {

    @Shadow
    public abstract boolean readyForShearing();

    @Shadow
    public abstract DyeColor getColor();

    @Unique
    private final Sheep subtleEffects$sheep = (Sheep) (Object) this;

    protected SheepMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (level().isClientSide() && ModConfigs.ENTITIES.sheepShearFluff) {
            if (player.getItemInHand(hand).is(Items.SHEARS) && readyForShearing()) {
                ParticleSpawnUtil.sheep(subtleEffects$sheep);
            }
        }
    }
}
