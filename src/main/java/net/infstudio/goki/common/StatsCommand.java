package net.infstudio.goki.common;

import net.infstudio.goki.common.config.ConfigManager;
import net.infstudio.goki.common.config.Configurable;
import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;

public class StatsCommand extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "reloadGokiStats";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/gokistats reload";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender icommandsender, String[] astring) {
        StatBase.stats.forEach(Configurable::reloadConfig);
        ConfigManager.INSTANCE.reloadConfig();
        EntityPlayer player;
        if ((icommandsender instanceof EntityPlayer)) {
            player = (EntityPlayer) icommandsender;
            player.sendMessage(new TextComponentTranslation("Reloaded gokistats configuration file."));
        } else {
            server.logInfo("Reloaded gokistats configuration file.");
        }
    }

    public String getCommandUsage(ICommandSender icommandsender) {
        return null;
    }

}