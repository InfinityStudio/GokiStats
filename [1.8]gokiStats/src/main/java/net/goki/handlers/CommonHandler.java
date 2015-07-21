package net.goki.handlers;

import net.goki.GokiStats;
import net.goki.handlers.packet.PacketStatAlter;
import net.goki.handlers.packet.PacketSyncStatConfig;
import net.goki.lib.DataHelper;
import net.goki.lib.IDMDTuple;
import net.goki.stats.IStatSpecial;
import net.goki.stats.Stat;
import net.goki.stats.StatMiningMagician;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

public class CommonHandler
{
	@SubscribeEvent
	public void harvestBlock(BlockEvent.HarvestDropsEvent event)
	{
		EntityPlayer player = event.harvester;
		Block block = event.state.getBlock();
		if (player != null)
		{
			if (DataHelper.getPlayerStatLevel(player, Stat.STAT_TREASURE_FINDER) > 0)
			{
				boolean treasureFound = false;
				Random random = player.getRNG();
				List<ItemStack> items = Stat.STAT_TREASURE_FINDER.getApplicableItemStackList(	block,
																								block.getMetaFromState(event.state),
																								DataHelper.getPlayerStatLevel(	player,
																																Stat.STAT_TREASURE_FINDER));
				List<Integer> chances = Stat.STAT_TREASURE_FINDER.getApplicableChanceList(	block,
																							block.getMetaFromState(event.state),
																							DataHelper.getPlayerStatLevel(	player,
																															Stat.STAT_TREASURE_FINDER));
				for (int i = 0; i < items.size(); i++)
				{
					Integer roll = Integer.valueOf(random.nextInt(10000));
					if (roll.intValue() <= ((Integer) chances.get(i)).intValue())
					{
						if (items.get(i) != null)
						{
							event.drops.add(items.get(i));
							treasureFound = true;
						}
						else
						{
							System.out.println("Tried to add an item from Treasure Finder, but it failed!");
						}
					}
				}
				if (treasureFound)
				{
					player.worldObj.playSoundAtEntity(	player,
														"gokiStats:treasure",
														1.0F,
														1.0F);
				}
			}

			if (DataHelper.getPlayerStatLevel(player, Stat.STAT_MINING_MAGICIAN) > 0)
			{
				boolean magicHappened = false;
				IDMDTuple mme = new IDMDTuple(block, block.getMetaFromState(event.state));
				if (Stat.STAT_MINING_MAGICIAN.needAffectedByStat(mme))
				{
					for (int i = 0; i < event.drops.size(); i++)
					{
						if (player.getRNG().nextDouble() * 100.0D <= Stat.STAT_MINING_MAGICIAN.getBonus(player))
						{
							ItemStack item = (ItemStack) event.drops.get(i);
							if (((item.getItem() instanceof ItemBlock)) && (ItemBlock.getIdFromItem((ItemBlock) item.getItem()) == Block.getIdFromBlock(block)))
							{
								if (item.getItemDamage() == block.getMetaFromState(event.state))
								{
									int randomEntry = player.getRNG().nextInt(StatMiningMagician.blockEntries.size());
									IDMDTuple entry = (IDMDTuple) StatMiningMagician.blockEntries.get(randomEntry);
									ItemStack stack = new ItemStack(Item.getItemById(entry.id), 1, entry.md);
									stack.stackSize = ((ItemStack) event.drops.get(i)).stackSize;
									event.drops.set(i, stack);
									magicHappened = true;
								}
							}
							else
							{
								for (int j = 0; j < StatMiningMagician.itemEntries.size(); j++)
								{
									IDMDTuple entry = (IDMDTuple) StatMiningMagician.itemEntries.get(j);
									if ((Item.getIdFromItem(item.getItem()) == entry.id) && (item.getItemDamage() == entry.md))
									{
										int randomEntry = player.getRNG().nextInt(StatMiningMagician.itemEntries.size());
										IDMDTuple chosenEntry = (IDMDTuple) StatMiningMagician.itemEntries.get(randomEntry);
										ItemStack stack = new ItemStack(Item.getItemById(chosenEntry.id), 1, chosenEntry.md);
										stack.stackSize = ((ItemStack) event.drops.get(i)).stackSize;
										event.drops.set(i, stack);
										magicHappened = true;
										break;
									}
								}
							}
						}
					}
					if (magicHappened)
					{
						player.worldObj.playSoundAtEntity(	player,
															"gokiStats:magician",
															0.3F,
															1.0F);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void playerJoinWorld(EntityJoinWorldEvent event)
	{
		if ((event.entity instanceof EntityPlayer))
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			if (!player.worldObj.isRemote)
			{
				GokiStats.packetPipeline.sendTo(new PacketSyncStatConfig(Stat.loseStatsOnDeath, Stat.globalBonusMultiplier, Stat.globalCostMultiplier, Stat.globalLimitMultiplier),
												(EntityPlayerMP) player);
			}
			else
			{
				GokiStats.packetPipeline.sendToServer(new PacketStatAlter(0, 0));
			}
		}
	}

	@SubscribeEvent
	public void playerFall(LivingFallEvent event)
	{
		if ((event.entity instanceof EntityPlayer))
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			int featherFallLevel = DataHelper.getPlayerStatLevel(	player,
																	Stat.STAT_FEATHER_FALL);
			if (event.distance < 3.0D + featherFallLevel * 0.1D)
			{
				event.distance = 0.0F;
			}
		}
	}

	@SubscribeEvent
	public void playerDead(LivingDeathEvent event)
	{
		if ((event.entityLiving instanceof EntityPlayer))
		{
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (Stat.loseStatsOnDeath)
			{
				for (int stat = 0; stat < Stat.totalStats; stat++)
				{
					DataHelper.setPlayerStatLevel(	player,
													(Stat) Stat.stats.get(stat),
													0);
				}
			}
		}
	}

	@SubscribeEvent
	public void playerBreakSpeed(PlayerEvent.BreakSpeed event)
	{
		ItemStack heldItem = event.entityPlayer.getHeldItem();
		EntityPlayer player = event.entityPlayer;

		float i = 0, j = 0, k = 0, l = 0;

		if (Stat.STAT_MINING.needAffectedByStat(heldItem,
												event.pos,
												player.worldObj))
		{
			i = Stat.STAT_MINING.getBonus(player);
		}
		if (Stat.STAT_DIGGING.needAffectedByStat(	heldItem,
													event.pos,
													player.worldObj))
		{
			j = Stat.STAT_DIGGING.getBonus(player);
		}
		if (Stat.STAT_CHOPPING.needAffectedByStat(	heldItem,
													event.pos,
													player.worldObj))
		{
			k = Stat.STAT_CHOPPING.getBonus(player);
		}
		if (Stat.STAT_TRIMMING.needAffectedByStat(	heldItem,
													event.pos,
													player.worldObj))
		{
			l = Stat.STAT_TRIMMING.getBonus(player);
		}

		float multiplier = 1.0F + i + j + k + l;

		event.newSpeed = (event.originalSpeed * multiplier);
	}

	@SubscribeEvent
	public void playerJump(LivingEvent.LivingJumpEvent event)
	{
		if ((event.entity instanceof EntityPlayer))
		{
			EntityPlayer player = (EntityPlayer) event.entity;
			if (player.isSprinting())
			{
				player.motionY *= 1.0F + Stat.STAT_LEAPERV.getBonus(player);
				player.motionX *= 1.0F + Stat.STAT_LEAPERH.getBonus(player);
				player.motionZ *= 1.0F + Stat.STAT_LEAPERH.getBonus(player);
			}
		}
	}

	@SubscribeEvent
	public void entityHurt(LivingHurtEvent event)
	{
		DamageSource source = event.source;

		Entity victim = event.entity;

		if ((victim instanceof EntityPlayer))
		{
			EntityPlayer player = (EntityPlayer) victim;
			float damageMultiplier = 1.0F - (Stat.STAT_PROTECTION.getAppliedBonus(	player,
																					source) + Stat.STAT_TOUGH_SKIN.getAppliedBonus(	player,
																																	source) + Stat.STAT_FEATHER_FALL.getAppliedBonus(	player,
																																														source) + Stat.STAT_TEMPERING.getAppliedBonus(	player,
																																																										source));
			event.ammount *= damageMultiplier;
		}

		Entity src = source.getEntity();

		if (src instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) src;
			ItemStack heldItem = player.getHeldItem();
			Entity target = victim;
			float damage = event.ammount;
			float bonus = 0.0F;

			if (heldItem != null)
			{
				bonus = Math.round(damage * (Stat.STAT_SWORDSMANSHIP.getAppliedBonus(	player,
																						heldItem) + Stat.STAT_BOWMANSHIP.getAppliedBonus(	player,
																																			heldItem)));
			}
			else
			{
//				bonus = Math.round(damage + (Stat.STAT_PUGILISM.getBonus(DataHelper.getPlayerStatLevel(	player, Stat.STAT_PUGILISM))));
				bonus = Math.round(damage+Stat.STAT_PUGILISM.getBonus(player));
			}
			event.ammount = (bonus + damage);
			
			if (Stat.STAT_REAPER.needAffectedByStat(target))
			{
				float reap = Stat.STAT_REAPER.getBonus(player);
				float reapBonus = 0;
				if (Stat.STAT_STEALTH.needAffectedByStat(player))
					reapBonus = reap * ((IStatSpecial)Stat.STAT_STEALTH).getSecondaryBonus(player) / 100.0F;
				float reapChance = reap + reapBonus;
				if (player.getRNG().nextFloat() <= reapChance)
				{
					player.onEnchantmentCritical(target);
					player.worldObj.playSoundAtEntity(	player,
														"gokiStats:reaper",
														1.0F,
														1.0F);
					event.ammount = 100000.0F;
				}
			}
		}
	}
}