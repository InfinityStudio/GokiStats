package net.infstudio.goki;

import net.infstudio.goki.config.ConfigManager;
import net.infstudio.goki.config.ConfigurableV2;
import net.infstudio.goki.lib.Reference;
import net.infstudio.goki.lib.StatHelper;
import net.infstudio.goki.stats.StatBase;
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
        Reference.configuration.load();
//        DataHelper.loadOptions(Reference.configuration);
        StatHelper.loadAllStatsFromConfiguration(Reference.configuration);
        StatBase.stats.forEach(ConfigurableV2::reloadConfig);
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