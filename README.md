# GokiStats

[![Build Status](https://travis-ci.org/InfinityStudio/GokiStats.svg?branch=1.15.2)](https://travis-ci.org/InfinityStudio/GokiStats)
![](https://img.shields.io/github/downloads/InfinityStudio/GokiStats/total.svg)
[![CurseForge](http://cf.way2muchnoise.eu/298141.svg)](https://minecraft.curseforge.com/projects/gokistats)

GokiStats is a skill improvement mod for Minecraft, it has been well-known before 1.7.10 but it has stopped developing for years.

Feel free to solve any issue with crash-report in GitHub Issues.

Forum thread on [Mcbbs](http://www.mcbbs.net/thread-805910-1-1.html)

## Why my Mining Magician and Treasure Finder skill are disabled?

According to https://github.com/MinecraftForge/MinecraftForge/pull/5871 Forge are recommending to use global loot functions, but they are rather complicated and not suitable for the skill.

You have to install [Additional Events](https://www.curseforge.com/minecraft/mc-mods/additional-events) and its dependency [MixinBootstrap](https://www.curseforge.com/minecraft/mc-mods/mixinbootstrap), and your skill will work fine.

## Why Furnace Finesse is disabled?

Because it drops TPS heavily. I will working on a better implementation.

## 1.15.2 Version

**After 1.15.2, we change to GPL License as I discovered illegal copies of this Mod.**

This version of GokiStats is highly experimental.

Configuration for each skill have no plan to implement as in 1.12.2 version it has severe bugs.

## Screenshots

![2018-07-14_19.43.42.png](https://i.loli.net/2018/07/14/5b49e762d2305.png)

## License

Source code are under GPL-3.0 License, and resources are under CC-BY-NC-SA 4.0 License.

## Donation

[![Become a Patreon](https://c5.patreon.com/external/logo/become_a_patron_button.png)](https://www.patreon.com/bePatron?u=10845019)