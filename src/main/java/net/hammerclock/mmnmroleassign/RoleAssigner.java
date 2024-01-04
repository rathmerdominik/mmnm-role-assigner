package net.hammerclock.mmnmroleassign;

import net.hammerclock.mmnmroleassign.config.CommonConfig;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.network.FMLNetworkConstants;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RoleAssigner.PROJECT_ID)
public class RoleAssigner {
	private static final Logger LOGGER = LogManager.getLogger(RoleAssigner.PROJECT_ID);

	public static final String PROJECT_ID = "mmnmroleassign";
	public static final String CONFIG_NAME = "mmnm-roleassign-common.toml";

	public RoleAssigner() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
				() -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		ModLoadingContext context = ModLoadingContext.get();

		context.registerConfig(Type.COMMON, CommonConfig.CONFIG, CONFIG_NAME);

		MinecraftForge.EVENT_BUS.addListener(RoleAssigner::onServerStarted);
	}

	private static void onServerStarted(FMLServerStartedEvent event) {
		LOGGER.info("Role Assigner Started!");
	}
}
