package com.wdiscute.echoes.registry;

import com.wdiscute.echoes.Echoes;
import com.wdiscute.echoes.upgrades.Perk;
import com.wdiscute.echoes.upgrades.perks.EmptyPerk;
import com.wdiscute.echoes.upgrades.perks.ExtraSculkDamagePerk;
import com.wdiscute.utils.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ECPerks
{
    Map<Identifier, Perk> MAP = new HashMap<>();

    Perk EMPTY = new EmptyPerk();

    Utils.Duo<Identifier, Perk> EXTRA_SCULK_DAMAGE = registerPerk(Echoes.rl("extra_sculk_damage"), new ExtraSculkDamagePerk());
    Utils.Duo<Identifier, Perk> EXTRA_TICKS_PER_KILL = registerPerk(Echoes.rl("extra_tickks_per_kill"), new ExtraSculkDamagePerk());


    static Perk get(Identifier identifier)
    {
        return MAP.getOrDefault(identifier, EMPTY);
    }

    static void add(ServerPlayer player, Identifier identifier, float value)
    {
        List<Utils.Duo<Identifier, Float>> data = new ArrayList<>(player.getData(ECDataAttachments.PERKS));
        data.add(new Utils.Duo<>(identifier, value));
        player.setData(ECDataAttachments.PERKS, data);
    }

    static Utils.Duo<Identifier, Perk> registerPerk(Identifier identifier, Perk perkInstance)
    {
        MAP.put(identifier, perkInstance);
        return new Utils.Duo<>(identifier, perkInstance);
    }

    static void register(IEventBus modEventBus)
    {
    }
}
