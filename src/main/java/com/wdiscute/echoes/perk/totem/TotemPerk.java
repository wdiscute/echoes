package com.wdiscute.echoes.perk.totem;

import com.wdiscute.echoes.entity.misc.totem.TotemEntity;
import com.wdiscute.echoes.perk.PerkInstance;

public interface TotemPerk
{
    void onTick(TotemEntity entity, PerkInstance amplifiers);
}
