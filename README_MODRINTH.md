# Important

In order for the addon to work several changes to the main Mine Mine no Mi mod had to be made.  
Those changes have already been committed by me to the main mod and will be available with the next Mine Mine no Mi update.  
Specifics can be found [here](https://github.com/rathmerdominik/mine-mine-no-mi-modded).  

It is imperative that for now this custom version is used. Changes in there are purely server sided. So only you need to install this version on your server.  
Players can still use the normal Mine Mine no Mi Version.

I HAVE EXPLICIT PERMISSION BY THE CREATOR OF THE MINE MINE NO MI MOD TO REDISTRIBUTE THIS.  
YOU DO NOT! THEREFORE EVERY FURTHER REDISTRIBUTION HAS TO HAPPEN BY REFERENCING TO THIS PAGE!

!!! A bit of competency is required to use this mod !!!  
Do not be illiterate.  
Read the instructions!

---


<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/GB2NqdvRk1c" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

# Discord Role Assigner
This [Mine Mine no Mi](https://modrinth.com/mod/mine-mine-no-mi) Addon uses [Discord Integration](https://modrinth.com/plugin/dcintegration) to synchronize the in-game stats with Discord as a Role.


It synchronizes:
- Faction
- Race
- Fighting Style
- Marine Rank
- Revolutionary Rank


# Config Options

```toml
#To disable a specific Role from syncing just do not change the ID on the config option
[Races]
    #Range: 0 ~ 9223372036854775807
    "Human Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Mink Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Cyborg Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Fishman Role Id" = 0

[Factions]
    #Range: 0 ~ 9223372036854775807
    "Bounty Hunter Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Revolution Army Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Pirate Role Id" = 0

["Marine Ranks"]
    #Range: 0 ~ 9223372036854775807
    "Marine Lieutenant Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Commander Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Sea Man Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Commodore Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Chore Boy Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Petty Officer Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Vice Admiral Man Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Admiral Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Captain Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Marine Fleet Admiral Role Id" = 0

["Revolutionary Ranks"]
    #Range: 0 ~ 9223372036854775807
    "Revolutionary Army Officer Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Revolutionary Army Supreme Commander Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Revolutionary Army Member Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Revolutionary Army Commander Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Revolutionary Army Chief of Staff Role Id" = 0

["Fighting Styles"]
    #Range: 0 ~ 9223372036854775807
    "Art of Weather Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Brawler Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Sniper Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Black Leg Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Doctor Role Id" = 0
    #Range: 0 ~ 9223372036854775807
    "Swords Man Role Id" = 0
```

## Compile it yourself

```
git clone https://github.com/rathmerdominik/mmnm-role-assigner
cd mmnm-role-assigner
cd libs
curl -L -O https://github.com/rathmerdominik/mine-mine-no-mi-modded/raw/main/release/mine-mine-no-mi-1.16.5-0.9.5-HAMMER.jar
cd ..
./gradlew build
cd build/libs
```

You can now just take the jar file out of this directory and place it under the `mods/` folder on your server.

This mod requires Discord Integration 3.0.5 to be installed and setup on your server!
