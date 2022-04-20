# GokiStats

[![Build Status](https://travis-ci.org/InfinityStudio/GokiStats.svg?branch=1.15.2)](https://travis-ci.org/InfinityStudio/GokiStats)
![](https://img.shields.io/github/downloads/InfinityStudio/GokiStats/total.svg)
[![CurseForge](http://cf.way2muchnoise.eu/298141.svg)](https://minecraft.curseforge.com/projects/gokistats)
[![Gitter](https://badges.gitter.im/InfinityStudio/GokiStats.svg)](https://gitter.im/InfinityStudio/GokiStats?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

[![JProfiler](https://www.ej-technologies.com/images/product_banners/jprofiler_large.png)](https://www.ej-technologies.com/products/jprofiler/overview.html)

GokiStats is a skill improvement mod for Minecraft, it has been well-known before 1.7.10 but it has stopped developing for years.

Feel free to solve any issue with crash-report in GitHub Issues.

Forum thread on [Mcbbs](http://www.mcbbs.net/thread-805910-1-1.html)

## Configuration

Since GokiStats 1.4.0, GokiStats is highly configurable through Minecraft datapack system.

Stats for tool speed and attack damage depends on specific tag. They are:

Bowsmanship(Bows): `gokistats:bow`

Chopping(Axes): `gokistats:chopping`

Digging(Shovels): `gokistats:digging`

Mining(Pickaxes): `gokistats:mining`

Swordsmanship(Swords): `gokistats:sword`

Trimming(Shears): `minecraft:shears`

Mining Magician Ores can be added to `gokistats:magician_ore` block tag and its output can be modified through `gokistats:magician_item` item tag.

Treasure Finder are totally rewritten to loot tables. Now you can modify or create loot tables with a name pattern `<mod>:treasure_finder/<block>`, like `minecraft:treasure_finder/dirt`, the mod will detect it and consider it as a treasure finder loot table.

Other stat configuration can be easily altered per-world in `serverconfig`.

## Why Furnace Finesse is disabled?

Because it drops TPS heavily.

## Screenshots

![2018-07-14_19.43.42.png](https://i.loli.net/2018/07/14/5b49e762d2305.png)

## License

Source code are under GPL-3.0 License, and resources are under CC-BY-NC-SA 4.0 License.

## Donation

[![Become a Patreon](https://c5.patreon.com/external/logo/become_a_patron_button.png)](https://www.patreon.com/bePatron?u=10845019)
