package com.moigferdsrte.windchimes.refine;

import com.moigferdsrte.windchimes.refine.config.WindchimeConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public class ModmenuEntryPoint implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return s -> AutoConfig.getConfigScreen(WindchimeConfig.class, s).get();
    }
}
