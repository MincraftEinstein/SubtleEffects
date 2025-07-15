package einstein.subtle_effects.networking.clientbound;

import einstein.subtle_effects.configs.ReplacedParticlesDisplayType;
import einstein.subtle_effects.init.ModConfigs;
import einstein.subtle_effects.init.ModParticles;
import einstein.subtle_effects.particle.option.FloatParticleOptions;
import einstein.subtle_effects.particle.option.SheepFluffParticleOptions;
import einstein.subtle_effects.ticking.tickers.TickerManager;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

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
                    PotionContents.getColor(Potions.WATER)
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
                RandomSource random = level.getRandom();
                ParticleOptions options = getParticleForFallingBlock(level, pos, state);

                for (int i = 0; i < 25; i++) {
                    boolean b = random.nextBoolean();
                    int xSign = nextSign(random);
                    int zSign = nextSign(random);

                    level.addParticle(options,
                            pos.getX() + 0.5 + (b ? 0.55 * xSign : nextNonAbsDouble(random, 0.55)),
                            pos.getY() + nextDouble(random, 0.3),
                            pos.getZ() + 0.5 + (!b ? 0.55 * zSign : nextNonAbsDouble(random, 0.55)),
                            b ? 50 * xSign : 0, // 50 because dust velocity gets multiplied by 0.1
                            0.3,
                            !b ? 50 * zSign : 0
                    );
                }
            }
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
            VillagerProfession profession = villagerData.getProfession();
            int professionLevel = villagerData.getLevel();
            RandomSource random = level.getRandom();
            BlockPos pos = payload.pos();
            BlockState state = level.getBlockState(pos);

            if (profession == VillagerProfession.LEATHERWORKER) {
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
            else if (profession == VillagerProfession.WEAPONSMITH) {
                ParticleSpawnUtil.spawnGrindstoneUsedParticles(level, pos, state, random);
            }
            else if (profession == VillagerProfession.TOOLSMITH) {
                if (BLOCKS.smithingTableUseParticles) {
                    ParticleSpawnUtil.spawnHammeringWorkstationParticles(pos, random, level);
                }
            }
            else if (profession == VillagerProfession.MASON) {
                ParticleSpawnUtil.spawnStonecutterParticles(level, new ItemStack(MASON_STONECUTTER_USE_BLOCKS.get(random.nextInt(MASON_STONECUTTER_USE_BLOCKS.size()))), pos, state);
            }
            else if (profession == VillagerProfession.SHEPHERD) {
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
            else if (profession == VillagerProfession.FLETCHER) {
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
            else if (profession == VillagerProfession.BUTCHER || profession == VillagerProfession.ARMORER) {
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

    private static DyeColor getColorForShepherdWoolFluff(int professionLevel, RandomSource random) {
        if (professionLevel >= 2 && random.nextDouble() < 0.5) {
            return DyeColor.values()[random.nextInt(DyeColor.values().length)];
        }
        return COMMON_SHEPHERD_WOOL_COLORS.get(random.nextInt(COMMON_SHEPHERD_WOOL_COLORS.size()));
    }

    private static ParticleOptions getParticleForFallingBlock(ClientLevel level, BlockPos pos, BlockState state) {
        if (Block.canSupportRigidBlock(level, pos.below())) {
            int color = getFallingBlockDustColor(level, state.getBlock(), state, pos);
            return new DustParticleOptions(
                    new Vector3f(
                            ((color >> 16) & 255) / 255F,
                            ((color >> 8) & 255) / 255F,
                            (color & 255) / 255F
                    ), 1
            );
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
