package einstein.subtle_effects.configs.entities;

import einstein.subtle_effects.init.ModConfigs;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.ArrayList;
import java.util.List;

@Translation(prefix = ModConfigs.BASE_KEY + "entities.damageTaken")
public class DamageTakenConfigs extends ConfigSection {

    private static final List<ResourceLocation> DEFAULT_DAMAGE_TYPES = Util.make(new ArrayList<>(), list -> {
        add(list, DamageTypes.GENERIC);
        add(list, DamageTypes.STALAGMITE);
        add(list, DamageTypes.TRIDENT);
        add(list, DamageTypes.FALL);
        add(list, DamageTypes.FALLING_STALACTITE);
        add(list, DamageTypes.MOB_PROJECTILE);
        add(list, DamageTypes.WIND_CHARGE);
        add(list, DamageTypes.ARROW);
        add(list, DamageTypes.BAD_RESPAWN_POINT);
        add(list, DamageTypes.EXPLOSION);
        add(list, DamageTypes.FALLING_ANVIL);
        add(list, DamageTypes.FALLING_BLOCK);
        add(list, DamageTypes.FIREBALL);
        add(list, DamageTypes.FLY_INTO_WALL);
        add(list, DamageTypes.LIGHTNING_BOLT);
        add(list, DamageTypes.MOB_ATTACK);
        add(list, DamageTypes.MOB_ATTACK_NO_AGGRO);
        add(list, DamageTypes.PLAYER_ATTACK);
        add(list, DamageTypes.PLAYER_EXPLOSION);
        add(list, DamageTypes.SONIC_BOOM);
        add(list, DamageTypes.STING);
        add(list, DamageTypes.THROWN);
        add(list, DamageTypes.UNATTRIBUTED_FIREBALL);
        add(list, DamageTypes.WITHER_SKULL);
    });

    public ValidatedList<ResourceLocation> damageTypes = ModConfigs.registryList(Registries.DAMAGE_TYPE, DEFAULT_DAMAGE_TYPES);
    public boolean damagedChickenFeathers = true;
    public boolean damagedParrotFeathers = true;
    public boolean damagedSnowGolemSnowflakes = true;
    public boolean damagedSheepFluff = true;
    public boolean damagedSlimeSlime = true;
    public boolean damagedSkeletonBones = true;
    public boolean damagedSkeletonHorseBones = true;

    private static void add(List<ResourceLocation> list, ResourceKey<DamageType> key) {
        list.add(key.location());
    }
}
