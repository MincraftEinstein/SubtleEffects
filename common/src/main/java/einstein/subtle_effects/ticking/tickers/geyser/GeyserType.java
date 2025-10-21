package einstein.subtle_effects.ticking.tickers.geyser;

import com.mojang.serialization.Codec;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;

public enum GeyserType implements StringRepresentable {
    FLAME("flame", true, 5, FlameGeyserTicker::new, null, ENVIRONMENT.geysers.flameGeyserSpawnableBlocks, ENVIRONMENT.geysers.flameGeyserSpawnChance, ENVIRONMENT.geysers.flameGeyserActiveTime, ENVIRONMENT.geysers.flameGeyserInactiveTime),
    SMOKE("smoke", true, 4, SmokeGeyserTicker::new, null, ENVIRONMENT.geysers.smokeGeyserSpawnableBlocks, ENVIRONMENT.geysers.smokeGeyserSpawnChance, ENVIRONMENT.geysers.smokeGeyserActiveTime, ENVIRONMENT.geysers.smokeGeyserInactiveTime),
    BUBBLE("bubble", false, 3, BubbleGeyserTicker::new, Fluids.WATER, ENVIRONMENT.geysers.bubbleGeyserSpawnableBlocks, ENVIRONMENT.geysers.bubbleGeyserSpawnChance, ENVIRONMENT.geysers.bubbleGeyserActiveTime, ENVIRONMENT.geysers.bubbleGeyserInactiveTime);

    public static final Codec<GeyserType> CODEC = StringRepresentable.fromEnum(GeyserType::values);

    private final String name;
    public final boolean isNetherOnly;
    public final int height; // Geyser will be 1 block higher than this number
    public final TriFunction<Level, BlockPos, RandomSource, GeyserTicker> geyserTickerProvider;
    @Nullable
    public final Fluid fluid;
    public final ValidatedList<Block> spawnableBlocks;
    public final ValidatedInt spawnChance;
    public final ValidatedInt activeTime;
    public final ValidatedInt inactiveTime;

    GeyserType(String name, boolean isNetherOnly, int height, TriFunction<Level, BlockPos, RandomSource, GeyserTicker> geyserTickerProvider, @Nullable Fluid fluid, ValidatedList<Block> spawnableBlocks, ValidatedInt spawnChance, ValidatedInt activeTime, ValidatedInt inactiveTime) {
        this.name = name;
        this.isNetherOnly = isNetherOnly;
        this.height = height;
        this.geyserTickerProvider = geyserTickerProvider;
        this.fluid = fluid;
        this.spawnableBlocks = spawnableBlocks;
        this.spawnChance = spawnChance;
        this.activeTime = activeTime;
        this.inactiveTime = inactiveTime;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
