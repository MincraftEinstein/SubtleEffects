package einstein.subtle_effects.ticking.tickers;

import einstein.subtle_effects.util.ChestAccessor;
import einstein.subtle_effects.util.MathUtil;
import einstein.subtle_effects.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.HashMap;
import java.util.Map;

import static einstein.subtle_effects.init.ModConfigs.BLOCKS;
import static net.minecraft.world.level.block.ChestBlock.TYPE;

public class ChestBlockEntityTicker extends BlockPosTicker {

    private static final int MAX_TICKS_SINCE_LAST_ANIMATION = 100;
    private static final Map<BlockPos, ChestBlockEntityTicker> CHEST_TICKERS = new HashMap<>();
    private int ticksSinceLastAnimation;
    private int animationTicks;
    private float oldOpenness;
    private final ChestLidController lidController;

    public ChestBlockEntityTicker(Level level, BlockPos pos, ChestLidController lidController) {
        super(level, pos);
        this.lidController = lidController;
    }

    public static void trySpawn(Level level, BlockPos pos) {
        if (!BLOCKS.chestsOpenRandomlyUnderwater && !BLOCKS.openingChestsSpawnsBubbles) {
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null || CHEST_TICKERS.containsKey(pos)) {
            return;
        }

        if (blockEntity instanceof ChestAccessor chestAccessor) {
            ChestBlockEntityTicker ticker = new ChestBlockEntityTicker(level, pos, chestAccessor.subtleEffects$getLidController());

            CHEST_TICKERS.put(pos, ticker);
            TickerManager.add(ticker);
        }
    }

    public static void clear() {
        CHEST_TICKERS.values().forEach(ticker -> ticker.lidController.shouldBeOpen(false));
        CHEST_TICKERS.clear();
    }

    @Override
    protected void positionedTick() {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof AbstractChestBlock)) {
            remove();
            return;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ChestBlockEntity)) {
            return;
        }

        ChestType type = state.hasProperty(TYPE) ? state.getValue(TYPE) : ChestType.SINGLE;
        if (type == ChestType.LEFT) {
            return;
        }

        boolean isDoubleChest = type != ChestType.SINGLE;
        Direction connectedDirection = isDoubleChest ? ChestBlock.getConnectedDirection(state) : Direction.NORTH;
        BlockPos oppositePos = pos.relative(connectedDirection);

        if (ChestBlock.isChestBlockedAt(level, pos) || (isDoubleChest && ChestBlock.isChestBlockedAt(level, oppositePos))) {
            animationTicks = 0;
            ticksSinceLastAnimation = 0;
            return;
        }

        if (BLOCKS.randomChestOpeningNeedsSoulSand) {
            if (isNotUpwardsBubbleColumn(level, pos) || (isDoubleChest && isNotUpwardsBubbleColumn(level, oppositePos))) {
                return;
            }
        }

        if (!(isUnderwater(level, pos) && (!isDoubleChest || isUnderwater(level, oppositePos)))) {
            return;
        }

        RandomSource random = level.getRandom();
        float openness = lidController.getOpenness(Util.getPartialTicks());
        if (openness > 0 && BLOCKS.openingChestsSpawnsBubbles) {
            boolean isClosing = openness <= 0.5 && openness < oldOpenness;
            if (isClosing || random.nextInt(isDoubleChest ? 2 : 4) == 0) {
                double xOffset = 0;
                double zOffset = 0;
                double xRandomOffset = 0.3125;
                double zRandomOffset = 0.3125;

                if (isDoubleChest) {
                    xOffset = 0.5 * connectedDirection.getStepX();
                    zOffset = 0.5 * connectedDirection.getStepZ();

                    Direction.Axis axis = connectedDirection.getAxis();
                    if (axis == Direction.Axis.X) {
                        xRandomOffset = 0.8125;
                    }
                    else if (axis == Direction.Axis.Z) {
                        zRandomOffset = 0.8125;
                    }
                }

                double xSpeed = 0;
                double zSpeed = 0;
                if (isClosing) {
                    Direction facing = state.getValue(ChestBlock.FACING);
                    xSpeed = 0.5 * facing.getStepX();
                    zSpeed = 0.5 * facing.getStepZ();
                }

                level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP,
                        pos.getX() + 0.5 + xOffset + MathUtil.nextNonAbsDouble(random, xRandomOffset),
                        pos.getY() + 0.625,
                        pos.getZ() + 0.5 + zOffset + MathUtil.nextNonAbsDouble(random, zRandomOffset),
                        xSpeed, 0, zSpeed
                );
            }
        }
        oldOpenness = openness;

        if (isDownwardsBubbleColumn(level, pos) || (isDoubleChest && isDownwardsBubbleColumn(level, oppositePos))) {
            return;
        }

        if (ticksSinceLastAnimation < MAX_TICKS_SINCE_LAST_ANIMATION) {
            ticksSinceLastAnimation++;
            return;
        }

        boolean isEnderChest = state.is(Blocks.ENDER_CHEST);
        if (BLOCKS.chestsOpenRandomlyUnderwater && random.nextInt(100) == 0 && openness == 0) {
            // Triggering the block entity event rather than calling the lid controller, otherwise Lithium will block the animation
            blockEntity.triggerEvent(1, 1);
            playSound(connectedDirection, type, isEnderChest ? SoundEvents.ENDER_CHEST_OPEN : SoundEvents.CHEST_OPEN);
            animationTicks = Mth.nextInt(random, 50, 200);
            return;
        }

        if (animationTicks > 0) {
            animationTicks--;

            if (animationTicks <= 0) {
                blockEntity.triggerEvent(1, 0);
                playSound(connectedDirection, type, isEnderChest ? SoundEvents.ENDER_CHEST_CLOSE : SoundEvents.CHEST_CLOSE);
                ticksSinceLastAnimation = 0;
            }
        }
    }

    public static boolean isNotUpwardsBubbleColumn(Level level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        return !belowState.is(Blocks.SOUL_SAND) && (!belowState.is(Blocks.BUBBLE_COLUMN) || belowState.getValue(BubbleColumnBlock.DRAG_DOWN));
    }

    public static boolean isDownwardsBubbleColumn(Level level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        return belowState.is(Blocks.MAGMA_BLOCK) || (belowState.is(Blocks.BUBBLE_COLUMN) && belowState.getValue(BubbleColumnBlock.DRAG_DOWN));
    }

    @Override
    public void remove() {
        super.remove();
        CHEST_TICKERS.remove(pos);
    }

    public static boolean isUnderwater(Level level, BlockPos pos) {
        return level.isWaterAt(pos) && level.getBlockState(pos.above()).is(Blocks.WATER);
    }

    private void playSound(Direction direction, ChestType type, SoundEvent sound) {
        double x = pos.getX() + 0.5F;
        double y = pos.getY() + 0.5F;
        double z = pos.getZ() + 0.5F;

        if (type == ChestType.RIGHT) {
            x += direction.getStepX() * 0.5F;
            z += direction.getStepZ() * 0.5F;
        }

        level.playSound(Minecraft.getInstance().player,
                x, y, z, sound, SoundSource.BLOCKS, 0.5F,
                level.getRandom().nextFloat() * 0.1F + 0.9F
        );
    }
}