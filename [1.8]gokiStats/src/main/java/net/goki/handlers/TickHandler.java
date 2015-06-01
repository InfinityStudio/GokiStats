package net.goki.handlers;

import net.goki.lib.DataHelper;
import net.goki.stats.Stat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;

public class TickHandler
{
	public static final UUID knockbackResistanceID = UUID.randomUUID();
	public static final UUID stealthSpeedID = UUID.randomUUID();
	public static final UUID swimSpeedID = UUID.randomUUID();

	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event)
	{
	    EntityPlayer player = event.player;

	    handleTaskPlayerAPI(player);

	    IAttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
	    AttributeModifier mod = new AttributeModifier(stealthSpeedID, "SneakSpeed", Stat.STAT_STEALTH.getBonus(player) / 100.0F, 1);
	    if (player.isSneaking())
	    {
	     	if (atinst.getModifier(stealthSpeedID) == null)
	     	{
	     		atinst.applyModifier(mod);
	     	}
	    }
	    else if (atinst.getModifier(stealthSpeedID) != null)
	    {
	    	atinst.removeModifier(mod);
	    }
	
	    atinst = player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
	    mod = new AttributeModifier(knockbackResistanceID, "KnockbackResistance", Stat.STAT_STEADY_GUARD.getBonus(player), 0);
	    if (player.isBlocking())
	    {
	      if (atinst.getModifier(knockbackResistanceID) == null)
	      {
	    	  atinst.applyModifier(mod);
	      }
	    }
	    else if (atinst.getModifier(knockbackResistanceID) != null)
	    {
	    	atinst.removeModifier(mod);
	    }
	    
	    handleFurnace(player);
	}

	private void handleTaskPlayerAPI(EntityPlayer player)
	{
		 if ((player.isInWater()) )
    	 {
    	      float multiplier = Math.max(0.0F, Stat.STAT_SWIMMING.getBonus(player));
    	      player.moveEntity(player.motionX * multiplier, player.motionY * multiplier, player.motionZ * multiplier);
    	 }

    	 if ((player.isOnLadder()) &&  (!player.isSneaking()) && (player.isCollidedHorizontally))
    	 {
    		 float multiplier = Stat.STAT_CLIMBING.getBonus(player);
    		 player.moveEntity(player.motionX, player.motionY * multiplier, player.motionZ);
    	 }
	}

	private void handleFurnace(EntityPlayer player) 
	{
		int tickBonus;
	    float timeBonus;
	    if (DataHelper.getPlayerStatLevel(player, Stat.STAT_FURNACE_FINESSE) > 0)
	    {
	    	tickBonus = (int)Stat.STAT_FURNACE_FINESSE.getBonus(player);
	    	timeBonus = (int)Stat.STAT_FURNACE_FINESSE.getSecondaryBonus(player);
	
	    	ArrayList<TileEntityFurnace> furnacesAroundPlayer = new ArrayList<TileEntityFurnace>();
	
	    	for (@SuppressWarnings("rawtypes")Iterator i$ = player.worldObj.loadedTileEntityList.iterator(); i$.hasNext();) 
	    	{ 
	    		Object listEntity = i$.next();
	
		        if ((listEntity instanceof TileEntity))
		        {
		        	TileEntity tileEntity = (TileEntity)listEntity;
		        	BlockPos pos = tileEntity.getPos();
		        	if (((tileEntity instanceof TileEntityFurnace)) && (MathHelper.sqrt_double(player.getDistanceSq(pos)) < 4.0D))
		        	{
		        		//TODO work out alter way to do tileEntity
		        		furnacesAroundPlayer.add((TileEntityFurnace)tileEntity);
		        	}
		        }
	    	}
	
	    	for (TileEntityFurnace furnace : furnacesAroundPlayer)
	    	{
	    		//TODO furnace is overwritten, find altner way to do it
	    		BlockPos pos = furnace.getPos();
	    		if (player.getRNG().nextFloat() < 0.3F)
	    		{
	    			player.worldObj.spawnParticle(EnumParticleTypes.REDSTONE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1, (double)pos.getZ() + 0.5D, 1.0D, 1.0D, 0);
	    		}
	    		if ((furnace != null) && (furnace.isBurning()))
	    		{
	    			if (furnace.getField(1) < 200)
	    			{
	    				if (player.getRNG().nextInt(100) < timeBonus)
	    				{
	    					furnace.setField(tickBonus,1);
	    				}
	    			}
	    			else
	    				furnace.setField(199,1);
	    		}
	    	}
	    }
		
	}
	
}