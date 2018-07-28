package net.infstudio.goki.common.handlers;

import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.stats.Stats;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

public class TickHandler {
    public static final UUID knockbackResistanceID = UUID.randomUUID();
    public static final UUID stealthSpeedID = UUID.randomUUID();
    public static final UUID swimSpeedID = UUID.randomUUID();

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;

        handleTaskPlayerAPI(player);

        IAttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        AttributeModifier mod = new AttributeModifier(stealthSpeedID, "SneakSpeed", Stats.STEALTH.getBonus(player) / 100.0F, 1);
        if (player.isSneaking()) {
            if (atinst.getModifier(stealthSpeedID) == null) {
                atinst.applyModifier(mod);
            }
        } else if (atinst.getModifier(stealthSpeedID) != null) {
            atinst.removeModifier(mod);
        }

        atinst = player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        mod = new AttributeModifier(knockbackResistanceID, "KnockbackResistance", Stats.STEADY_GUARD.getBonus(player), 0);
        if (player.isActiveItemStackBlocking()) {
            if (atinst.getModifier(knockbackResistanceID) == null) {
                atinst.applyModifier(mod);
            }
        } else if (atinst.getModifier(knockbackResistanceID) != null) {
            atinst.removeModifier(mod);
        }

        handleFurnace(player);
    }

    public static boolean isJumping(EntityLivingBase livingBase) {
        Field field = null;
        try {
            field = EntityLivingBase.class.getDeclaredField("field_70703_bu"); // isJumping
        } catch (NoSuchFieldException e) {
            try {
                field = EntityLivingBase.class.getDeclaredField("isJumping");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }

        if (field != null) {
            field.setAccessible(true);
            try {
                return field.getBoolean(livingBase);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void handleTaskPlayerAPI(EntityPlayer player) {
        if (player.isServerWorld() || player.canPassengerSteer())
            if (player.isInWater() && !player.capabilities.isFlying) {
                float multiplier = Math.max(0.0F,
                        Stats.SWIMMING.getBonus(player));
                if (isJumping(player)) {
                    player.jumpMovementFactor += multiplier;
                } else if (multiplier > 0) {
                    //			player.moveEntity(	player.motionX * multiplier,
                    //								player.motionY * multiplier,
                    //								player.motionZ * multiplier);

                    // Copied from EntityLivingBase
                    double d0 = player.posY;
                    float f1 = 0.8f;
                    float f2 = 0.02F;

                    if (multiplier > 0.0F) {
                        f1 += (0.54600006F - f1) * multiplier;
                        f2 += (player.getAIMoveSpeed() - f2) * multiplier;
                    }

                    player.moveRelative(player.moveStrafing, player.moveVertical, player.moveForward, f2);
                    player.move(MoverType.SELF, player.motionX, player.motionY, player.motionZ);
                    player.motionX *= (double) f1;
                    player.motionY *= 0.800000011920929D;
                    player.motionZ *= (double) f1;

                    if (!player.hasNoGravity()) {
                        player.motionY -= 0.02D;
                    }

                    if (player.collidedHorizontally && player.isOffsetPositionInLiquid(player.motionX, player.motionY + 0.6000000238418579D - player.posY + d0, player.motionZ)) {
                        player.motionY = 0.30000001192092896D;
                    }

                    player.move(MoverType.SELF, player.moveStrafing * multiplier, player.moveVertical * multiplier, 0.02f);
                }
            }

        if (player.isOnLadder() && !player.isSneaking()) {
            float multiplier = Stats.CLIMBING.getBonus(player);
            player.move(MoverType.SELF, player.motionX,
                    player.motionY * multiplier,
                    player.motionZ);
        }
    }

    private void handleFurnace(EntityPlayer player) {
        if (DataHelper.getPlayerStatLevel(player, Stats.FURNACE_FINESSE) > 0) {
            ArrayList<TileEntityFurnace> furnacesAroundPlayer = new ArrayList<>();

            for (TileEntity listEntity : player.world.loadedTileEntityList) {
                if (listEntity instanceof TileEntity) {
                    TileEntity tileEntity = (TileEntity) listEntity;
                    BlockPos pos = tileEntity.getPos();
                    if (tileEntity instanceof TileEntityFurnace && MathHelper.sqrt(player.getDistanceSq(pos)) < 4.0D) {
                        // TODO work out alter way to do tileEntity
                        furnacesAroundPlayer.add((TileEntityFurnace) tileEntity);
                    }
                }
            }

            // FIXME Laggy
            for (TileEntityFurnace furnace : furnacesAroundPlayer)
                if (furnace.isBurning())
                    for (int i = 0; i < Stats.FURNACE_FINESSE.getBonus(player); i++) // Intend to "mount" ticks, same as Torcherino.
                        furnace.update();
        }

    }

}