package com.wdiscute.echoes.perks;

import com.wdiscute.echoes.entity.totem.TotemEntity;
import com.wdiscute.echoes.upgrades.Perk;

import java.util.List;

public abstract class TotemPerk extends Perk
{
    public abstract void onTick(TotemEntity entity, List<Float> amplifiers);
}
