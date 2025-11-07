package com.xsasakihaise.hellaslibrary;

import com.xsasakihaise.hellascontrol.api.sidemods.HellasAPIHellasLibrary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HellasLibrary.MOD_ID)
public class HellasLibrary {
    public static final String MOD_ID = "hellaslibrary";
    private static final Logger LOGGER = LogManager.getLogger();

    public HellasLibrary() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        if (!ModList.get().isLoaded("hellascontrol")) {
            LOGGER.warn("HellasControl was not found; skipping HellasLibrary initialization.");
            return;
        }

        HellasAPIHellasLibrary.verify();
    }
}
