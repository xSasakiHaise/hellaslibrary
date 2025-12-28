package com.xsasakihaise.hellaslibrary;

import com.xsasakihaise.hellascontrol.api.CoreCheck;
import com.xsasakihaise.hellascontrol.api.sidemods.HellasAPIHellasLibrary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HellasLibrary.MOD_ID)
public class HellasLibrary {
    public static final String MOD_ID = "hellaslibrary";
    private static final Logger LOGGER = LogManager.getLogger("HellasLibrary");
    private static final String ENTITLEMENT_KEY = MOD_ID;
    private static volatile boolean ENABLED = false;
    private static volatile String DISABLE_REASON = "UNINITIALIZED";

    public HellasLibrary() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            initGate();
            if (!ENABLED) {
                return;
            }

            HellasAPIHellasLibrary.verify();
        });
    }

    private void initGate() {
        if (FMLEnvironment.dist != Dist.DEDICATED_SERVER) {
            ENABLED = true;
            DISABLE_REASON = "OK (non-dedicated)";
            return;
        }

        if (!ModList.get().isLoaded("hellascontrol")) {
            ENABLED = false;
            DISABLE_REASON = "HellasControl missing";
            LOGGER.warn("[HellasLibrary] disabled: {}", DISABLE_REASON);
            return;
        }

        try {
            CoreCheck.verifyCoreLoaded();
            CoreCheck.verifyEntitled(ENTITLEMENT_KEY);

            ENABLED = true;
            DISABLE_REASON = "OK";
            LOGGER.info("[HellasLibrary] enabled (license OK) entitlement='{}'", ENTITLEMENT_KEY);
        } catch (Exception e) {
            ENABLED = false;
            DISABLE_REASON = "License invalid";
            LOGGER.warn("[HellasLibrary] disabled: {} entitlement='{}'", DISABLE_REASON, ENTITLEMENT_KEY, e);
        }
    }
}
