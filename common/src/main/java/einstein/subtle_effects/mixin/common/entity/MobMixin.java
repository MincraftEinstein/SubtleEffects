package einstein.subtle_effects.mixin.common.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.networking.clientbound.ClientBoundAnimalFedPayload;
import einstein.subtle_effects.platform.Services;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Mob.class)
public class MobMixin {

    @Unique
    private final Mob subtleEffects$mob = (Mob) (Object) this;

    @Inject(method = "checkAndHandleImportantInteractions", at = @At("HEAD"))
    private void mobSpawnedFromMobEffects(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = subtleEffects$mob.level();
        if (!level.isClientSide()) {
            return;
        }

        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof SpawnEggItem spawnEggItem) {
            if (spawnEggItem.spawnsEntity(stack, subtleEffects$mob.getType())) {
                TickerManager.schedule(3, () -> {
                    List<Entity> entities = new ArrayList<>();
                    Vec3 pos = subtleEffects$mob.position();
                    level.getEntities(spawnEggItem.getType(stack), new AABB(pos, pos.add(1, 1, 1)),
                            entity -> entity instanceof AgeableMob ageableMob && ageableMob.isBaby(),
                            entities, 1
                    );

                    if (!entities.isEmpty()) {
                        Entity entity = entities.getFirst();
                        float volume = ModConfigs.ITEMS.spawnEggUseSoundVolume.get();

                        if (volume > 0) {
                            level.playSound(player, pos.x, pos.y, pos.z, ModSounds.EGG_BREAK.get(), SoundSource.PLAYERS,
                                    volume, Mth.nextFloat(entity.getRandom(), 0.7F, 1.5F));
                        }

                        if (ModConfigs.ITEMS.spawnEggUseParticles) {
                            if (entity instanceof Mob mob) {
                                mob.spawnAnim();
                                return;
                            }

                            entity.handleEntityEvent(EntityEvent.POOF);
                        }
                    }
                });
            }
        }
    }

    @WrapOperation(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;"))
    private InteractionResult onMobInteract(Mob mob, Player player, InteractionHand hand, Operation<InteractionResult> original) {
        ItemStack stack = player.getItemInHand(hand).copy();
        InteractionResult result = original.call(mob, player, hand);
        Level level = mob.level();

        if (level instanceof ServerLevel serverLevel && result.consumesAction()) {
            if (subtleEffects$isFood(mob, stack)) {
                ItemStack stackCopy = stack.copy();
                ItemStack handStack = player.getMainHandItem().copy();
                stack.shrink(1);

                if (!stackCopy.isEmpty()) {
                    if ((ItemStack.isSameItemSameComponents(stack, handStack) && stack.getCount() == handStack.getCount())
                            || player.isCreative()) {
                        Services.NETWORK.sendToClientsTracking(serverLevel, mob.blockPosition(), new ClientBoundAnimalFedPayload(mob.getId(), stack.isEmpty() ? stackCopy : stack));
                    }
                }
            }
        }
        return result;
    }

    @Unique
    private static boolean subtleEffects$isFood(Mob mob, ItemStack stack) {
        if (mob instanceof Animal animal) {
            return animal.isFood(stack)
                    || (animal instanceof Wolf && stack.is(Items.BONE))
                    || (animal instanceof Parrot && (stack.is(ItemTags.PARROT_FOOD) || stack.is(ItemTags.PARROT_POISONOUS_FOOD)));
        }
        else if (mob instanceof Dolphin) {
            return stack.is(ItemTags.FISHES);
        }
        else if (mob instanceof Tadpole) {
            return stack.is(ItemTags.FROG_FOOD);
        }
        return false;
    }
}