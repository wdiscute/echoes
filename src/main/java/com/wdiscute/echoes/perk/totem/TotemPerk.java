package com.wdiscute.echoes.perk.totem;

import com.wdiscute.echoes.entity.totem.TotemEntity;
import com.wdiscute.echoes.perk.PerkInstance;

import java.util.List;

public interface TotemPerk
{
    void onTick(TotemEntity entity, PerkInstance amplifiers);
}
