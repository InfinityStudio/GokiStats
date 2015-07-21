package net.goki;

import net.goki.lib.DataHelper;
import net.goki.lib.Reference;
import net.goki.lib.StatHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

public class StatsCommand extends CommandBase
{
	@Override
	public String getName()
	{
		return "reloadGokiStats";
	}

	@Override
	public void execute(ICommandSender icommandsender, String[] astring)
	{
		Reference.configuration.load();
		DataHelper.loadOptions(Reference.configuration);
		StatHelper.loadAllStatsFromConfiguration(Reference.configuration);
		EntityPlayer player;
		if ((icommandsender instanceof EntityPlayer))
		{
			player = (EntityPlayer) icommandsender;
			player.addChatComponentMessage(new ChatComponentTranslation("Reloaded gokiStats configuration file.", new Object[0]));
		}
		else
		{
			MinecraftServer.getServer().logInfo("Reloaded gokiStats configuration file.");
		}
	}

	public String getCommandUsage(ICommandSender icommandsender)
	{
		return null;
	}

	public int compareTo(Object o)
	{
		return 0;
	}

}