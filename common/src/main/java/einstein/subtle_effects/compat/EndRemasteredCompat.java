package einstein.subtle_effects.compat;

import com.teamremastered.endrem.block.AncientPortalFrameEntity;
import com.teamremastered.endrem.item.JsonEye;
import com.teamremastered.endrem.registry.CommonBlockRegistry;
import einstein.subtle_effects.configs.ModBlockConfigs;
import einstein.subtle_effects.init.ModBlockTickers;
import einstein.subtle_effects.util.ParticleSpawnUtil;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static einstein.subtle_effects.compat.CompatHelper.endRemLoc;
import static einstein.subtle_effects.init.ModConfigs.BLOCKS;

public class EndRemasteredCompat {

    public static void init() {
        ModBlockTickers.REGISTERED_SPECIAL.put(state -> state.is(CommonBlockRegistry.ANCIENT_PORTAL_FRAME), (state, level, pos, random) -> {
            if (BLOCKS.endPortalFrameParticlesDisplayType.get() == ModBlockConfigs.EndPortalFrameParticlesDisplayType.OFF) {
                return;
            }

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AncientPortalFrameEntity frameBlockEntity) {
                if (frameBlockEntity.isEmpty()) {
                    return;
                }

                ParticleSpawnUtil.spawnEndPortalParticles(level, pos, random, BLOCKS.endPortalFrameParticlesDisplayType.get().particle.apply(level, pos), BLOCKS.endPortalFrameParticlesDisplayType.get().count);
            }
        });
    }

    @Nullable
    public static ValidatedColor.ColorHolder getEyeColor(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AncientPortalFrameEntity frameBlockEntity) {
            return BLOCKS.eyeColors.get().get(frameBlockEntity.getEyeID());
        }
        return null;
    }

    public static List<ResourceLocation> getAllEyes() {
        return JsonEye.getEyes().stream().map(eye -> endRemLoc(getId(eye))).toList();
    }

    // As of writing this, the "getId" method has been changed to a string
    // on NeoForge, however, the Fabric version hasn't been updated yet and
    // still returns a ResourceLocation
    private static String getId(JsonEye eye) {
        Object id = eye.getID();
        if (id instanceof String s) {
            return s;
        }
        else if (id instanceof ResourceLocation loc) {
            return loc.getPath();
        }
        throw new IllegalStateException();
    }
}
