package einstein.ambient_sleep.mixin.client.entity;

import einstein.ambient_sleep.particle.option.SheepFluffParticleOptions;
import einstein.ambient_sleep.init.ModConfigs;
import einstein.ambient_sleep.util.MathUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal {

    @Shadow
    public abstract boolean readyForShearing();

    @Shadow
    public abstract DyeColor getColor();

    protected SheepMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"))
    private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (level().isClientSide() && ModConfigs.INSTANCE.sheepShearFluff.get()) {
            if (player.getItemInHand(hand).is(Items.SHEARS) && readyForShearing()) {
                for (int i = 0; i < 7; i++) {
                    SheepFluffParticleOptions particle = new SheepFluffParticleOptions(new Vector3f(Sheep.getColorArray(getColor())));
                    level().addParticle(particle, getRandomX(0.5), getY(0.5), getRandomZ(0.5), MathUtil.nextNonAbsDouble(), random.nextDouble(), MathUtil.nextNonAbsDouble());
                }
            }
        }
    }
}
