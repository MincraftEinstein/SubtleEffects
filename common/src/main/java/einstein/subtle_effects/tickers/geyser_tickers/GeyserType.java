package einstein.subtle_effects.tickers.geyser_tickers;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;

import static einstein.subtle_effects.init.ModConfigs.ENVIRONMENT;
import static einstein.subtle_effects.tickers.geyser_tickers.GeyserManager.VALID_BLOCKS;

public enum GeyserType implements StringRepresentable {
    FLAME("flame", true, FlameGeyserTicker::new, VALID_BLOCKS, ENVIRONMENT.flameGeyserSpawnChance, ENVIRONMENT.flameGeyserActiveTime, ENVIRONMENT.flameGeyserInactiveTime),
    ;

    public static final Codec<GeyserType> CODEC = StringRepresentable.fromEnum(GeyserType::values);
    public static final StreamCodec<ByteBuf, GeyserType> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    private final String name;
    public final boolean isNetherOnly;
    public final TriFunction<Level, BlockPos, RandomSource, GeyserTicker> geyserTickerProvider;
    public final List<Block> spawnableBlocks;
    public final ValidatedInt spawnChance;
    public final ValidatedInt activeTime;
    public final ValidatedInt inactiveTime;

    GeyserType(String name, boolean isNetherOnly, TriFunction<Level, BlockPos, RandomSource, GeyserTicker> geyserTickerProvider, List<Block> spawnableBlocks, ValidatedInt spawnChance, ValidatedInt activeTime, ValidatedInt inactiveTime) {
        this.name = name;
        this.isNetherOnly = isNetherOnly;
        this.geyserTickerProvider = geyserTickerProvider;
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
