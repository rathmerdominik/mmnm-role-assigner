package net.hammerclock.mmnmroleassign.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import de.erdbeerbaerlp.dcintegration.common.storage.linking.LinkManager;
import de.erdbeerbaerlp.dcintegration.common.storage.linking.PlayerLink;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import net.hammerclock.mmnmroleassign.RoleAssigner;
import net.hammerclock.mmnmroleassign.config.CommonConfig;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import xyz.pixelatedw.mineminenomi.api.events.SetPlayerDetailsEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.LoyaltyEvent;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.IEntityStats;
import xyz.pixelatedw.mineminenomi.init.ModValues;

@Mod.EventBusSubscriber(modid = RoleAssigner.PROJECT_ID)
public class RoleAssignerEvent {
	private static final Logger LOGGER = LogManager.getLogger(RoleAssigner.PROJECT_ID);
	public static final CommonConfig CONFIG = CommonConfig.INSTANCE;

	// This acts as a kind of failsafe. Everyone who has worked with Discord bots knows that they can be very unreliable.
	@SubscribeEvent
	public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		Optional<PlayerLink> playerLink = getOrWarnPlayerLink(event.getPlayer().getUUID());
		if (playerLink.isPresent()) {
			LOGGER.debug("Playerjoin detail set for {}", event.getPlayer().getName().getString());
			Member member = DiscordIntegration.INSTANCE.getMemberById(playerLink.get().discordID);

			if(member == null) {
				LOGGER.warn("Member with Discord ID {} could not be found. Unlinking.", playerLink.get().discordID);
				LinkManager.unlinkPlayer(playerLink.get().discordID);
				return;
			}
			IEntityStats entityStats = EntityStatsCapability.get(event.getPlayer());

			List<Role> roles = assembleRoles(entityStats, member);
			LOGGER.debug("Setting roles for {}", member.getEffectiveName());
			member.getGuild().modifyMemberRoles(member, roles).queue();
		}
	}

	@SubscribeEvent
	public static void onPlayerDetailsSet(SetPlayerDetailsEvent event) {
		Optional<PlayerLink> playerLink = getOrWarnPlayerLink(event.getPlayer().getUUID());
		if (playerLink.isPresent()) {
			LOGGER.debug("Details set for player {}", event.getPlayer().getName().getString());
			Member member = DiscordIntegration.INSTANCE.getMemberById(playerLink.get().discordID);

			if(member == null) {
				LOGGER.warn("Member with Discord ID {} could not be found. Unlinking.", playerLink.get().discordID);
				LinkManager.unlinkPlayer(playerLink.get().discordID);
				return;
			}
			IEntityStats entityStats = EntityStatsCapability.get(event.getPlayer());

			List<Role> roles = assembleRoles(entityStats, member);
			LOGGER.debug("Setting roles for {}", member.getEffectiveName());
			member.getGuild().modifyMemberRoles(member, roles).queue();
		}
	}

	@SubscribeEvent
	public static void onLoyaltyChange(LoyaltyEvent.Post event) {
		Optional<PlayerLink> playerLink = getOrWarnPlayerLink(event.getPlayer().getUUID());
		if (playerLink.isPresent()) {
			LOGGER.debug("Loyalty changed for player {} to {}", event.getPlayer().getName().getString(), event.getLoyalty());
			Member member = DiscordIntegration.INSTANCE.getMemberById(playerLink.get().discordID);

			if(member == null) {
				LOGGER.warn("Member with Discord ID {} could not be found. Unlinking.", playerLink.get().discordID);
				LinkManager.unlinkPlayer(playerLink.get().discordID);
				return;
			}
			IEntityStats entityStats = EntityStatsCapability.get(event.getPlayer());

			List<Role> roles = assembleRoles(entityStats, member);
			LOGGER.debug("Setting roles for {}", member.getEffectiveName());
			member.getGuild().modifyMemberRoles(member, roles).queue();
		}
	}

	public static List<Role> assembleRoles(IEntityStats entityStats, Member member) {
		List<Role> roles = new ArrayList<>();
		member.getRoles().forEach((Role role) -> {
			if(!CONFIG.getAllRoleIds().contains(role.getIdLong())) {
				LOGGER.debug("Adding Pre-Existing Role {} to roles", role.getIdLong());
				roles.add(role);
			}
		});

		try {
			Long factionRoleId = getFactionRoleId(entityStats.getFaction());
			if(factionRoleId != 0L) {
				LOGGER.debug("Adding Faction Role {} to roles", factionRoleId);
				roles.add(member.getGuild().getRoleById(factionRoleId));
			}

			Long raceRoleId = getRaceRoleId(entityStats.getRace());
			if(raceRoleId != 0L) {
				LOGGER.debug("Adding Race Role {} to roles", raceRoleId);
				roles.add(member.getGuild().getRoleById(raceRoleId));

			}

			if(entityStats.getRace().equals(ModValues.MINK)){
				Long minkSubRaceRoleId = getMinkSubRaceRoleId(entityStats.getSubRace());
				if(minkSubRaceRoleId != null) {
					LOGGER.debug("Adding Mink Sub Race Role to roles");
					roles.add(member.getGuild().getRoleById(minkSubRaceRoleId));
				}
			}

			Long fightingStyleRoleId = getFightingStyleRoleId(entityStats.getFightingStyle());
			if(fightingStyleRoleId != 0L) {
				LOGGER.debug("Adding Fighting Style Role {} to roles", fightingStyleRoleId);
				roles.add(member.getGuild().getRoleById(fightingStyleRoleId));
			}

			LOGGER.debug("Faction check on {} returned {}", member.getEffectiveName(), entityStats.getFaction());
			if(entityStats.getFaction().equals(ModValues.MARINE)) {
				Long marineRankRoleId = getMarineRankRoleId(entityStats);
				if(marineRankRoleId != 0L) {
					LOGGER.debug("Adding Marine Rank Role {} to roles", marineRankRoleId);
					roles.add(member.getGuild().getRoleById(marineRankRoleId));
				}
			} 
			
			if(entityStats.getFaction().equals(ModValues.REVOLUTIONARY)) {
				Long revoRankRoleId = getRevoRankRoleId(entityStats);
				if(revoRankRoleId != 0L) {
					LOGGER.debug("Adding Revo Rank Role {} to roles", revoRankRoleId);
					roles.add(member.getGuild().getRoleById(revoRankRoleId));
				}
			}

			return roles;
		} catch (ErrorResponseException e) {
			LOGGER.error("Error while assembling roles for {}", member.getEffectiveName());
			LOGGER.error(e.getMessage());
		}
		return roles;
	}

	public static Long getFactionRoleId(String faction) {
		switch (faction) {
			case ModValues.PIRATE:
				LOGGER.debug("Getting RoleID for Pirate");
				return CONFIG.getPirateRoleId();
			case ModValues.MARINE:
				LOGGER.debug("Getting RoleID for Marine");
				return CONFIG.getMarineRoleId();
			case ModValues.BOUNTY_HUNTER:
				LOGGER.debug("Getting RoleID for Bounty Hunter");
				return CONFIG.getBountyHunterRoleId();
			case ModValues.REVOLUTIONARY:
				LOGGER.debug("Getting RoleID for Revolutionary");
				return CONFIG.getRevolutionArmyRoleId();
			default:
				LOGGER.debug("Found no RoleID for Faction {}", faction);
				return 0L;
		}
	}

	public static Long getRaceRoleId(String race) {
		switch (race) {
			case ModValues.HUMAN:
				LOGGER.debug("Getting RoleID for Human");
				return CONFIG.getHumanRoleId();
			case ModValues.FISHMAN:
				LOGGER.debug("Getting RoleID for Fishman");
				return CONFIG.getFishManRoleId();
			case ModValues.CYBORG:
				LOGGER.debug("Getting RoleID for Cyborg");
				return CONFIG.getCyborgRoleId();
			case ModValues.MINK:
				LOGGER.debug("Getting RoleID for Mink");
				return CONFIG.getMinkRoleId();
			default:
				LOGGER.debug("Found no RoleID for Race {}", race);
				return 0L;
		}
	}

	public static Long getMinkSubRaceRoleId(String minkType) {
		switch (minkType) {
			case ModValues.MINK_BUNNY:
				LOGGER.debug("Getting RoleID for Mink Bunny");
				return CONFIG.getBunnyRoleId();
			case ModValues.MINK_DOG:
				LOGGER.debug("Getting RoleID for Mink Bunny");
				return CONFIG.getDogRoleId();
			case ModValues.MINK_LION:
				LOGGER.debug("Getting RoleID for Mink Bunny");
				return CONFIG.getLionRoleId();
			default:
				return 0L;
		}
	}

	public static Long getFightingStyleRoleId(String fightingStyle) {
		switch (fightingStyle) {
			case ModValues.SWORDSMAN:
				LOGGER.debug("Getting RoleID for Swordsman");
				return CONFIG.getSwordsManRoleId();
			case ModValues.SNIPER:
				LOGGER.debug("Getting RoleID for Sniper");
				return CONFIG.getSniperRoleId();
			case ModValues.DOCTOR:
				LOGGER.debug("Getting RoleID for Doctor");
				return CONFIG.getDoctorRoleId();
			case ModValues.ART_OF_WEATHER:
				LOGGER.debug("Getting RoleID for Art of Weather");
				return CONFIG.getArtOfWeatherRoleId();
			case ModValues.BLACK_LEG:
				LOGGER.debug("Getting RoleID for Black Leg");
				return CONFIG.getBlackLegRoleId();
			case ModValues.BRAWLER:
				LOGGER.debug("Getting RoleID for Brawler");
				return CONFIG.getBrawlerRoleId();
			default:
				LOGGER.debug("Found no RoleID for Fighting Style {}", fightingStyle);
				return 0L;
		}
	}

	public static Long getMarineRankRoleId(IEntityStats entityStats) {
		switch (entityStats.getMarineRank()) {
			case CHORE_BOY:
				LOGGER.debug("Getting RoleID for Marine Chore Boy");
				return CONFIG.getMarineChoreBoyRoleId();
			case SEAMAN:
				LOGGER.debug("Getting RoleID for Marine Seaman");
				return CONFIG.getMarineSeaManRoleId();
			case PETTY_OFFICER:
				LOGGER.debug("Getting RoleID for Marine Petty Officer");
				return CONFIG.getMarinePettyOfficerRoleId();
			case LIEUTENANT:
				LOGGER.debug("Getting RoleID for Marine Lieutenant");
				return CONFIG.getMarineLieutenantRoleId();
			case COMMANDER:
				LOGGER.debug("Getting RoleID for Marine Commander");
				return CONFIG.getMarineCommanderRoleId();
			case CAPTAIN:
				LOGGER.debug("Getting RoleID for Marine Captain");
				return CONFIG.getMarineCaptainRoleId();
			case COMMODORE:
				LOGGER.debug("Getting RoleID for Marine Commodore");
				return CONFIG.getMarineCommodoreRoleId();
			case VICE_ADMIRAL:
				LOGGER.debug("Getting RoleID for Marine Vice Admiral");
				return CONFIG.getMarineViceAdmiralRoleId();
			case ADMIRAL:
				LOGGER.debug("Getting RoleID for Marine Admiral");
				return CONFIG.getMarineAdmiralRoleId();
			case FLEET_ADMIRAL:
				LOGGER.debug("Getting RoleID for Marine Fleet Admiral");
				return CONFIG.getMarineFleetAdmiralRoleId();
			default:
				LOGGER.debug("Found no RoleID for Marine Rank {}", entityStats.getMarineRank());
				return 0L;
		}
	}

	public static Long getRevoRankRoleId(IEntityStats entityStats) {
		switch (entityStats.getRevolutionaryRank()) {
			case MEMBER:
				LOGGER.debug("Getting RoleID for Revo Member");
				return CONFIG.getRevoMemberRoleId();
			case OFFICER:
				LOGGER.debug("Getting RoleID for Revo Officer");
				return CONFIG.getRevoOfficerRoleId();
			case COMMANDER:
				LOGGER.debug("Getting RoleID for Revo Commander");
				return CONFIG.getRevoCommanderRoleId();
			case CHIEF_OF_STAFF:
				LOGGER.debug("Getting RoleID for Revo Chief of Staff");
				return CONFIG.getRevoChiefRoleId();
			case SUPREME_COMMANDER:
				LOGGER.debug("Getting RoleID for Revo Supreme Commander");
				return CONFIG.getRevoSupremeCommanderRoleId();
			default:
				LOGGER.debug("Found no RoleID for Revo Rank {}", entityStats.getRevolutionaryRank());
				return 0L;
		}
	}

	public static Optional<PlayerLink> getOrWarnPlayerLink(UUID uuid) {
		if (LinkManager.isPlayerLinked(uuid)) {
			return Optional.of(LinkManager.getLink(null, uuid));
		} else {
			LOGGER.warn(
				"User with the UUID {} is not Linked! Consider enforcing linking in Discord Integration's Config. Cannot set Roles!",
					uuid
				);
		}
		return Optional.empty();
	}
}
