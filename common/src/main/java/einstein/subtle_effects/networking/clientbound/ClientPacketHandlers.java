package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.configs.ReplacedParticlesDisplayType;
import einstein.subtle_effects.init.ModAnimalFedEffectSettings;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.particle.option.SheepFluffParticleOptions;
import einstein.subtle_effects.particle.option.SplashEmitterParticleOptions;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

import static einstein.subtle_effects.init.ModConfigs.*;
import static einstein.subtle_effects.util.MathUtil.*;

public class ClientPacketHandlers {

    private static final List<Block> MASON_STONECUTTER_USE_BLOCKS = List.of(Blocks.STONE, Blocks.CHISELED_STONE_BRICKS, Blocks.QUARTZ_BLOCK, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE);
    private static final List<DyeColor> COMMON_SHEPHERD_WOOL_COLORS = List.of(DyeColor.WHITE, DyeColor.GRAY, DyeColor.BLACK, DyeColor.BROWN);

    public static void handle(ClientLevel level, ClientBoundEntityFellPayload payload) {
        if (level.getEntity(payload.entityId()) instanceof LivingEntity livingEntity) {
            ParticleSpawnUtil.spawnEntityFellParticles(livingEntity, payload.y(), payload.distance(), payload.fallDamage(), getEntityFellConfig(payload));
        }
    }

    public static void handle(ClientLevel level, ClientBoundEntitySpawnSprintingDustCloudsPayload payload) {
        if (level.getEntity(payload.entityId()) instanceof LivingEntity livingEntity) {
            int ySpeedModifier = 5;
            if (livingEntity instanceof Ravager) {
                ySpeedModifier = 20;
            }

            ParticleSpawnUtil.spawnCreatureMovementDustClouds(livingEntity, level, livingEntity.getRandom(), ySpeedModifier);
        }
    }

    public static void handle(ClientLevel level, ClientBoundSpawnSnoreParticlePayload payload) {
        if (ModConfigs.BLOCKS.beehivesHaveSleepingZs) {
            level.addParticle(ModParticles.SNORING.get(), payload.x(), payload.y(), payload.z(), 0, 0, 0);
        }
    }

    public static void handle(ClientLevel level, ClientBoundBlockDestroyEffectsPayload payload) {
        if (getBlockDestroyEffectConfig(payload)) {
            BlockPos pos = payload.pos();
            BlockState state = Block.stateById(payload.stateId());
            SoundType soundType = state.getSoundType();

            level.addDestroyBlockEffect(pos, state);
            level.playLocalSound(pos, soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1F) / 2F, soundType.getPitch() * 0.8F, false);
        }
    }

    public static void handle(ClientLevel level, ClientBoundXPBottleEffectsPayload payload) {
        BlockPos pos = payload.pos();
        Vec3 vec3 = Vec3.atBottomCenterOf(pos);
        RandomSource random = level.getRandom();
        if (ITEMS.projectiles.xpBottleParticlesDisplayType == ReplacedParticlesDisplayType.BOTH || ITEMS.projectiles.xpBottleParticlesDisplayType == ReplacedParticlesDisplayType.VANILLA) {
            level.levelEvent(
                    LevelEvent.PARTICLES_SPELL_POTION_SPLASH,
                    pos,
                    PotionContents.BASE_POTION_COLOR
            );
        }

        if (ITEMS.projectiles.xpBottleParticlesDisplayType == ReplacedParticlesDisplayType.BOTH || ITEMS.projectiles.xpBottleParticlesDisplayType == ReplacedParticlesDisplayType.DEFAULT) {
            for (int i = 0; i < ITEMS.projectiles.xpBottleParticlesDensity.get(); ++i) {
                double d = random.nextDouble() * 4;
                double d1 = random.nextDouble() * Math.PI * 2;
                double xPower = Math.cos(d1) * d;
                double zPower = Math.sin(d1) * d;

                level.addParticle(
                        new FloatParticleOptions(ModParticles.EXPERIENCE.get(), (float) d),
                        vec3.x + xPower * 0.1,
                        vec3.y + 0.3,
                        vec3.z + zPower * 0.1,
                        xPower,
                        0.01 + random.nextDouble() * 0.5,
                        zPower
                );
            }
        }

        if (ITEMS.projectiles.xpBottleParticlesDisplayType == ReplacedParticlesDisplayType.DEFAULT) {
            for (int i = 0; i < 8; ++i) {
                level.addParticle(
                        new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SPLASH_POTION)),
                        vec3.x, vec3.y, vec3.z,
                        random.nextGaussian() * 0.15,
                        random.nextDouble() * 0.2,
                        random.nextGaussian() * 0.15
                );
            }

            level.playLocalSound(pos,
                    SoundEvents.SPLASH_POTION_BREAK,
                    SoundSource.NEUTRAL,
                    1,
                    random.nextFloat() * 0.1F + 0.9F,
                    false
            );
        }
    }

    public static void handle(ClientLevel level, ClientBoundFallingBlockLandPayload payload) {
        BlockPos pos = payload.pos();
        BlockState state = Block.stateById(payload.stateId());
        Block block = state.getBlock();

        if (BLOCKS.fallingBlocks.onLandSound && !(block instanceof AnvilBlock)) {
            SoundType soundType = state.getSoundType();

            Util.playClientSound(pos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                    (soundType.getVolume() + 1.0F) / 2F,
                    soundType.getPitch() * 0.8F
            );
        }

        if (BLOCKS.fallingBlocks.onLandDust) {
            if (BLOCKS.fallingBlocks.dustyBlocks.contains(block)) {
                boolean isInWater = payload.isInWater() && BLOCKS.fallingBlocks.replaceDustWithBubblesUnderwater;
                RandomSource random = level.getRandom();
                ParticleOptions options = isInWater ? ParticleTypes.BUBBLE : getParticleForFallingBlock(level, pos, state);

                for (int i = 0; i < 25; i++) {
                    boolean bool = random.nextBoolean();
                    int xSign = nextSign(random);
                    int zSign = nextSign(random);

                    level.addParticle(options,
                            pos.getX() + 0.5 + (bool ? 0.55 * xSign : nextNonAbsDouble(random, 0.55)),
                            pos.getY() + nextDouble(random, 0.3),
                            pos.getZ() + 0.5 + (!bool ? 0.55 * zSign : nextNonAbsDouble(random, 0.55)),
                            bool ? (isInWater ? nextDouble(random, 0.5) : 50) * xSign : 0, // 50 because dust velocity gets multiplied by 0.1
                            0.3,
                            !bool ? (isInWater ? nextDouble(random, 0.5) : 50) * zSign : 0
                    );
                }
            }
        }
    }

    public static void handle(ClientLevel level, ClientBoundFallingBlockTickPayload payload) {
        Entity entity = level.getEntity(payload.entityId());
        if (entity instanceof FallingBlockEntity) {
            entity.fallDistance = payload.fallDistance();
        }
    }

    public static void handle(ClientLevel level, ClientBoundCompostItemPayload payload) {
        if (BLOCKS.compostingItemParticles && (!payload.wasFarmer() || ENTITIES.villagerWorkAtWorkstationParticles)) {
            RandomSource random = level.getRandom();
            ParticleSpawnUtil.spawnCompostParticles(level, payload.pos(),
                    new ItemParticleOption(ParticleTypes.ITEM, payload.stack()),
                    nextNonAbsDouble(random, 0.15),
                    nextNonAbsDouble(random, 0.15),
                    nextNonAbsDouble(random, 0.15)
            );
        }
    }

    public static void handle(ClientLevel level, ClientBoundStonecutterUsedPayload payload) {
        BlockPos pos = payload.pos();
        ParticleSpawnUtil.spawnStonecutterParticles(level, payload.stack(), pos, level.getBlockState(pos));
    }

    public static void handle(ClientLevel level, ClientBoundVillagerWorkPayload payload) {
        if (!ENTITIES.villagerWorkAtWorkstationParticles) {
            return;
        }

        Entity entity = level.getEntity(payload.villagerId());
        if (entity instanceof Villager villager) {
            VillagerData villagerData = villager.getVillagerData();
            Holder<VillagerProfession> profession = villagerData.profession();
            int professionLevel = villagerData.level();
            RandomSource random = level.getRandom();
            BlockPos pos = payload.pos();
            BlockState state = level.getBlockState(pos);

            if (profession.is(VillagerProfession.LEATHERWORKER)) {
                if (!BLOCKS.cauldronUseParticles) {
                    return;
                }

                double fluidHeight = Util.getCauldronFillHeight(state);
                ParticleOptions particle = Util.getCauldronParticle(state);

                if (fluidHeight > 0 && particle != null) {
                    for (int i = 0; i < 16; i++) {
                        level.addParticle(
                                particle,
                                pos.getX() + random.nextDouble(),
                                pos.getY() + fluidHeight,
                                pos.getZ() + random.nextDouble(),
                                0, 0, 0
                        );
                    }
                }
            }
            else if (profession.is(VillagerProfession.WEAPONSMITH)) {
                ParticleSpawnUtil.spawnGrindstoneUsedParticles(level, pos, state, random);
            }
            else if (profession.is(VillagerProfession.TOOLSMITH)) {
                if (BLOCKS.smithingTableUseParticles) {
                    ParticleSpawnUtil.spawnHammeringWorkstationParticles(pos, random, level);
                }
            }
            else if (profession.is(VillagerProfession.MASON)) {
                ParticleSpawnUtil.spawnStonecutterParticles(level, new ItemStack(MASON_STONECUTTER_USE_BLOCKS.get(random.nextInt(MASON_STONECUTTER_USE_BLOCKS.size()))), pos, state);
            }
            else if (profession.is(VillagerProfession.SHEPHERD)) {
                SheepFluffParticleOptions particle = new SheepFluffParticleOptions(
                        getColorForShepherdWoolFluff(professionLevel, random), -1, false
                );

                for (int i = 0; i < 10; i++) {
                    level.addParticle(particle,
                            pos.getX() + random.nextDouble(),
                            pos.getY() + 1,
                            pos.getZ() + random.nextDouble(),
                            MathUtil.nextNonAbsDouble(random),
                            random.nextDouble(),
                            MathUtil.nextNonAbsDouble(random)
                    );
                }
            }
            else if (profession.is(VillagerProfession.FLETCHER)) {
                for (int i = 0; i < 8; i++) {
                    level.addParticle(ModParticles.CHICKEN_FEATHER.get(),
                            pos.getX() + random.nextDouble(),
                            pos.getY() + 1,
                            pos.getZ() + random.nextDouble(),
                            nextNonAbsDouble(random, 0.1),
                            nextDouble(random, 0.1),
                            nextNonAbsDouble(random, 0.1)
                    );

                    level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.FLINT)),
                            pos.getX() + random.nextDouble(),
                            pos.getY() + 1,
                            pos.getZ() + random.nextDouble(),
                            nextNonAbsDouble(random, 0.25),
                            nextDouble(random, 0.25),
                            nextNonAbsDouble(random, 0.25)
                    );
                }
            }
            else if (profession.is(VillagerProfession.BUTCHER) || profession.is(VillagerProfession.ARMORER)) {
                if (state.hasProperty(BlockStateProperties.LIT) && !state.getValue(BlockStateProperties.LIT)) {
                    Block block = state.getBlock();
                    BlockState litState = state.setValue(BlockStateProperties.LIT, true);
                    TickerManager.schedule(10, () -> block.animateTick(litState, level, pos, random));

                    for (int i = 0; i < 2; i++) {
                        block.animateTick(litState, level, pos, random);
                    }
                }
            }
        }
    }

    public static void handle(ClientLevel level, ClientBoundMooshroomShearedPayload payload) {
        Entity entity = level.getEntity(payload.entityId());
        if (entity instanceof MushroomCow mooshroom) {
            if (!ENTITIES.improvedMooshroomShearingEffects) {
                level.addParticle(ParticleTypes.EXPLOSION, mooshroom.getX(), mooshroom.getY(0.5F), mooshroom.getZ(), 0, 0, 0);
                return;
            }

            RandomSource random = mooshroom.getRandom();
            ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(
                    mooshroom.getVariant() == MushroomCow.Variant.BROWN ? Blocks.BROWN_MUSHROOM_BLOCK : Blocks.RED_MUSHROOM_BLOCK
            ));

            for (int i = 0; i < 20; i++) {
                level.addParticle(
                        particle,
                        mooshroom.getRandomX(1),
                        mooshroom.getRandomY(),
                        mooshroom.getRandomZ(1),
                        nextNonAbsDouble(random, 0.15),
                        nextDouble(random, 0.15),
                        nextNonAbsDouble(random, 0.15)
                );
            }

            mooshroom.spawnAnim();
        }
    }

    public static void handle(ClientLevel level, ClientBoundAnimalFedPayload payload) {
        Entity entity = level.getEntity(payload.animalId());
        ItemStack stack = payload.stack();

        if (entity instanceof Animal || entity instanceof Dolphin || entity instanceof Tadpole) {
            LivingEntity livingEntity = (LivingEntity) entity;
            RandomSource random = entity.getRandom();
            ModAnimalFedEffectSettings.Settings settings = ModAnimalFedEffectSettings.getSetting(entity.getType());

            if (ENTITIES.animalFeedingParticles) {
                for (int i = 0; i < 16; i++) {
                    ParticleSpawnUtil.spawnEntityFaceParticle(
                            new ItemParticleOption(ParticleTypes.ITEM, settings.stackReplacer().apply(stack)),
                            livingEntity, level, random, settings.offset(), Util.getPartialTicks()
                    );
                }
            }

            float volume = ENTITIES.animalFeedingSoundVolume.get();
            if (volume > 0 && !entity.isSilent()) {
                Util.playClientSound(entity, getEatSound(livingEntity, stack, settings), entity.getSoundSource(), volume, livingEntity.getVoicePitch());
            }
        }
    }

    public static void handle(ClientLevel level, ClientBoundDrankPotionPayload payload) {
        Entity entity = level.getEntity(payload.entityId());

        if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
            ParticleSpawnUtil.spawnPotionRings(livingEntity);
        }
    }

    public static void handle(ClientLevel level, ClientBoundDispenseBucketPayload payload) {
        BlockPos pos = payload.pos();
        BlockState state = level.getBlockState(pos);

        if (state.hasProperty(BlockStateProperties.FACING)) {
            Direction direction = state.getValue(BlockStateProperties.FACING);
            pos = pos.relative(direction);
        }

        ParticleSpawnUtil.spawnBucketParticles(level, pos, payload.stack());
    }

    public static void handle(ClientLevel level, ClientBoundSheepShearPayload payload) {
        Entity entity = level.getEntity(payload.entityId());
        if (entity instanceof Sheep sheep && ENTITIES.sheepShearFluff) {
            ParticleSpawnUtil.sheep(sheep);
        }
    }

    public static void handle(ClientLevel level, ClientBoundEntityLandInFluidPayload payload) {
        Entity entity = level.getEntity(payload.entityId());

        if (entity != null) {
            ParticleType<SplashEmitterParticleOptions> particleType = payload.isLava() ? ModParticles.LAVA_SPLASH_EMITTER.get() : ModParticles.WATER_SPLASH_EMITTER.get();
            ParticleSpawnUtil.spawnSplashEffects(entity, level, particleType, payload.y(), payload.yVelocity());
        }
    }

    public static void handle(ClientLevel level, ClientBoundExplosionPayload payload) {
        if (ModConfigs.ENTITIES.splashes.explosionsCauseSplashes) {
            float radius = payload.radius();
            Vec3 position = payload.position();
            BlockPos pos = BlockPos.containing(position);
            FluidState fluidState = level.getFluidState(pos);

            if (!fluidState.isEmpty()) {
                int blockY = pos.getY();

                for (int y = blockY; y < blockY + (radius * 1.5) + 1; y++) {
                    BlockPos currentPos = pos.atY(y);
                    FluidState currentFluidState = level.getFluidState(currentPos);

                    if (fluidState.getType().isSame(currentFluidState.getType())) {
                        continue;
                    }

                    if (level.getBlockState(currentPos).isSolidRender()) {
                        return;
                    }

                    ParticleType<SplashEmitterParticleOptions> type = fluidState.is(FluidTags.WATER) ? ModParticles.WATER_SPLASH_EMITTER.get() : fluidState.is(FluidTags.LAVA) ? ModParticles.LAVA_SPLASH_EMITTER.get() : null;
                    if (type != null) {
                        BlockPos surfacePos = currentPos.below();
                        FluidState surfaceFluidState = level.getFluidState(surfacePos);
                        float scale = radius - ((y - blockY) / radius);

                        level.addAlwaysVisibleParticle(new SplashEmitterParticleOptions(type, scale, scale * (scale * 0.1F), -1, -1),
                                true, position.x(), surfacePos.getY() + surfaceFluidState.getHeight(level, surfacePos) + 0.01, position.z(),
                                0, 0, 0
                        );
                    }
                    return;
                }
            }
        }
    }

    private static SoundEvent getEatSound(LivingEntity entity, ItemStack stack, ModAnimalFedEffectSettings.Settings settings) {
        Supplier<SoundEvent> overrideSound = settings.sound();
        if (overrideSound != null) {
            return overrideSound.get();
        }
        return Util.getEntityEatSound(entity, stack);
    }

    private static DyeColor getColorForShepherdWoolFluff(int professionLevel, RandomSource random) {
        if (professionLevel >= 2 && random.nextDouble() < 0.5) {
            return DyeColor.values()[random.nextInt(DyeColor.values().length)];
        }
        return COMMON_SHEPHERD_WOOL_COLORS.get(random.nextInt(COMMON_SHEPHERD_WOOL_COLORS.size()));
    }

    private static ParticleOptions getParticleForFallingBlock(ClientLevel level, BlockPos pos, BlockState state) {
        if (Block.canSupportRigidBlock(level, pos.below())) {
            int color = getFallingBlockDustColor(level, state.getBlock(), state, pos);
            return new DustParticleOptions(color, 1);
        }
        return new BlockParticleOption(ParticleTypes.FALLING_DUST, state);
    }

    private static int getFallingBlockDustColor(ClientLevel level, Block block, BlockState state, BlockPos pos) {
        if (block instanceof FallingBlock fallingBlock) {
            return fallingBlock.getDustColor(state, level, pos);
        }

        if (block instanceof BrushableBlock brushableBlock) {
            Block turnsIntoBlock = brushableBlock.getTurnsInto();

            if (turnsIntoBlock != block) {
                return getFallingBlockDustColor(level, turnsIntoBlock, state, pos);
            }
        }

        return Minecraft.getInstance().getBlockColors().getColor(state, level, pos);
    }

    // Don't convert to enum parameters, because the server will crash trying to access the client configs
    private static boolean getBlockDestroyEffectConfig(ClientBoundBlockDestroyEffectsPayload packet) {
        return switch (packet.config()) {
            case LEAVES_DECAY -> ModConfigs.BLOCKS.leavesDecayEffects;
            case FARMLAND_DESTROY -> ModConfigs.BLOCKS.farmlandDestroyEffects;
        };
    }

    private static boolean getEntityFellConfig(ClientBoundEntityFellPayload packet) {
        return switch (packet.config()) {
            case ENTITY -> ENTITIES.dustClouds.mobFell;
            case PLAYER -> ENTITIES.dustClouds.playerFell;
            case MACE -> ENTITIES.dustClouds.landMaceAttack;
            case ELYTRA -> ENTITIES.dustClouds.flyIntoWall;
        };
    }
}
