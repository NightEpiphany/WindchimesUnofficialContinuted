package com.moigferdsrte.windchimes.refine.config;

import com.moigferdsrte.windchimes.refine.Refine;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Refine.MOD_ID)
public class WindchimeConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean enableEXPTotemBonus = false;
    @ConfigEntry.Gui.Tooltip
    public boolean enableDreamcatcherHeal = true;
}
