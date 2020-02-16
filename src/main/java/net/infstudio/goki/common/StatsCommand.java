package net.infstudio.goki.common;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.infstudio.goki.common.config.ConfigManager;
import net.infstudio.goki.common.config.Configurable;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.S2COpenGui;
import net.minecraft.command.*;
import net.infstudio.goki.api.stat.StatBase;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StatsCommand extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "gokistats";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/gokistats";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1)
            throw new WrongUsageException("Invalid usage! Valid commands: /gokistats reload, /gokistats gui");

        if (args[0].equals("reload")) {
            StatBase.stats.forEach(Configurable::reloadConfig);
            ConfigManager.INSTANCE.reloadConfig();
            EntityPlayer player;
            if ((sender instanceof EntityPlayer)) {
                player = (EntityPlayer) sender;
                player.sendMessage(new TextComponentString("Reloaded gokistats configuration file."));
            } else {
                server.logInfo("Reloaded gokistats configuration file.");
            }
        } else if (args[0].equals("gui")) {
            EntityPlayerMP player = null;
            if (args.length == 1) {
                if ((sender instanceof EntityPlayerMP)) {
                    player = (EntityPlayerMP) sender;
                } else {
                    throw new WrongUsageException("This command should be only used by player");
                }
            } else if (args.length == 2) player = server.getPlayerList().getPlayerByUsername(args[1]);
            if (player == null)
                throw new PlayerNotFoundException(args[1]);
            GokiPacketHandler.CHANNEL.sendTo(new S2COpenGui(0), player);
        } else {
            throw new CommandNotFoundException("No sub-command " + args[0]);
        }
    }

    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            String s = args[args.length - 1];
            if (args[0].isEmpty()) return Arrays.asList("reload", "gui");
            if (doesStringStartWith(s, "reload"))
                return Collections.singletonList("reload");
            else if (doesStringStartWith(s, "gui"))
                return Collections.singletonList("gui");
        } else if (args.length == 2) {
            if (args[0].equals("gui")) {
                List<String> list = Lists.newArrayList();

                for (GameProfile gameprofile : server.getOnlinePlayerProfiles())
                    if (!server.getPlayerList().canSendCommands(gameprofile) && doesStringStartWith(args[1], gameprofile.getName()))
                        list.add(gameprofile.getName());

                return list;
            }
        }
        return Arrays.asList("reload", "gui");
    }

    public String getCommandUsage(ICommandSender sender) {
        return null;
    }
}
