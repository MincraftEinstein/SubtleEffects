package einstein.subtle_effects.mixin.client.item;

import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModSounds;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.SpawnEggItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin extends Item implements SpawnEggItemAccessor {

    @Shadow
    @Final
    private EntityType<?> defaultType;

    public SpawnEggItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "useOn", at = @At("HEAD"))
    private void mobSpawnedOnBlockEffects(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (!level.isClientSide() || level.getBlockEntity(pos) instanceof SpawnerBlockEntity) {
            return;
        }

        RandomSource random = level.getRandom();
        BlockPos spawnPos = state.getCollisionShape(level, pos).isEmpty() ? pos : pos.relative(context.getClickedFace());
        subtleEffects$preformMobSpawnedEffects(context.getPlayer(), level, spawnPos, random, context.getItemInHand());
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/InteractionResultHolder;success(Ljava/lang/Object;)Lnet/minecraft/world/InteractionResultHolder;"))
    private void mobSpawnedInFluidEffects(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (!level.isClientSide()) {
            return;
        }

        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos resultPos = result.getBlockPos();

        if (result.getType() == BlockHitResult.Type.BLOCK && level.getBlockState(resultPos).getBlock() instanceof LiquidBlock) {
            ItemStack stack = player.getItemInHand(hand);

            if (level.mayInteract(player, resultPos) && player.mayUseItemAt(resultPos, result.getDirection(), stack)) {
                subtleEffects$preformMobSpawnedEffects(player, level, resultPos, level.getRandom(), stack);
            }
        }
    }

    @Unique
    private void subtleEffects$preformMobSpawnedEffects(Player player, Level level, BlockPos pos, RandomSource random, ItemStack stack) {
        float volume = ModConfigs.ITEMS.spawnEggUseSoundVolume.get();
        if (volume > 0) {
            level.playSound(player, pos, ModSounds.SPAWN_EGG_SPAWN_MOB.get(), SoundSource.PLAYERS, volume, Mth.nextFloat(random, 0.7F, 1.5F));
        }

        if (ModConfigs.ITEMS.spawnEggUseParticles) {
            TickerManager.schedule(3, () -> {
                List<Entity> entities = new ArrayList<>();

                EntityType<?> entityType = subtleEffects$getDefaultEntityType();
                if (entityType != null) {
                    // noinspection ConstantConditions
                    level.getEntities(entityType, new AABB(pos),
                            entity -> !entity.hasCustomName() || entity.getCustomName().equals(stack.getHoverName()),
                            entities, 1
                    );
                }

                if (!entities.isEmpty()) {
                    Entity entity = entities.get(0);
                    if (entity instanceof Mob mob) {
                        mob.spawnAnim();
                        return;
                    }

                    entity.handleEntityEvent(EntityEvent.POOF);
                }
            });
        }
    }

    @Override
    public @Nullable EntityType<?> subtleEffects$getDefaultEntityType() {
        return defaultType;
    }
}
