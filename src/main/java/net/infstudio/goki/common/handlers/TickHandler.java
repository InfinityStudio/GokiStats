package net.infstudio.goki.common.handlers;

import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static net.infstudio.goki.common.utils.Reference.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class TickHandler {
    public static final UUID knockbackResistanceID = UUID.randomUUID();
    public static final UUID stealthSpeedID = UUID.randomUUID();
    public static final UUID swimSpeedID = UUID.randomUUID();

    public static AtomicInteger tickTimer = new AtomicInteger();

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) { // Due to issue #32
            PlayerEntity player = event.player;

            handleTaskPlayerAPI(player);

            ModifiableAttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attributeInstance == null) return;
            AttributeModifier modifier = new AttributeModifier(stealthSpeedID, "SneakSpeed", Stats.STEALTH.getBonus(player) / 100.0F, AttributeModifier.Operation.fromValue(1));
            if (player.isCrouching()) {
                if (attributeInstance.getModifier(stealthSpeedID) == null) {
                    attributeInstance.addTransientModifier(modifier);
                }
            } else if (attributeInstance.getModifier(stealthSpeedID) != null) {
                attributeInstance.removeModifier(modifier);
            }

            attributeInstance = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            if (attributeInstance == null) return;
            modifier = new AttributeModifier(knockbackResistanceID, "KnockbackResistance", Stats.STEADY_GUARD.getBonus(player), AttributeModifier.Operation.fromValue(0));
            if (player.isBlocking()) {
                if (attributeInstance.getModifier(knockbackResistanceID) == null) {
                    attributeInstance.addTransientModifier(modifier);
                }
            } else if (attributeInstance.getModifier(knockbackResistanceID) != null) {
                attributeInstance.removeModifier(modifier);
            }

            handleFurnace(player);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && GokiConfig.SERVER.syncTicks.get() > 0) {
            if (tickTimer.get() == GokiConfig.SERVER.syncTicks.get()) {
                tickTimer.lazySet(0);
                for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                    SyncEventHandler.syncPlayerData(player);
                }
            } else {
                tickTimer.getAndIncrement();
            }
        }
    }

    public static boolean isJumping(LivingEntity livingBase) {
        return ObfuscationReflectionHelper.getPrivateValue(LivingEntity.class, livingBase, "field_70703_bu");
    }

    private static void handleTaskPlayerAPI(PlayerEntity player) {
        if (!player.isLocalPlayer() || player.canRiderInteract())
            if (player.isSwimming()) {
                float multiplier = Math.max(0.0F,
                        Stats.SWIMMING.getBonus(player));
                if (isJumping(player)) {
//                    player += multiplier;
                } else if (multiplier > 0) {
                    //			player.moveEntity(	player.motionX * multiplier,
                    //								player.motionY * multiplier,
                    //								player.motionZ * multiplier);

                    // Copied from LivingEntity
                    double d0 = player.getY();
                    float f1 = 0.8f;
                    float f2 = 0.02F;

                    if (multiplier > 0.0F) {
                        f1 += (0.54600006F - f1) * multiplier;
                        f2 += (player.getSpeed() - f2) * multiplier;
                    }

                    player.moveRelative(f2, player.getDeltaMovement());
                    player.move(MoverType.SELF, player.getDeltaMovement());
                    player.setDeltaMovement(player.getDeltaMovement().multiply(f1, 0.800000011920929D, f1));

                    if (!player.isNoGravity()) {
                        player.setDeltaMovement(player.getDeltaMovement().add(0, 0.02, 0));
                    }

                    Vector3d offset = player.getDeltaMovement().add(0, 0.6000000238418579D - player.getY() + d0, 0);
//                    if (player.horizontalCollision && player.isOffsetPositionInLiquid(offset.x, offset.y, offset.z)) {
//                        player.setMotion(new Vector3d(player.getMotion().x, 0.30000001192092896D, player.getMotion().z));
//                    }


//                    player.move(MoverType.SELF, new Vector3d(player.moveStrafing * multiplier, player.moveVertical * multiplier, 0.02f));
                }
            }

        if (player.onClimbable() && !player.isShiftKeyDown()) {
            float multiplier = Stats.CLIMBING.getBonus(player);
            player.move(MoverType.SELF, player.getDeltaMovement().multiply(1, multiplier, 1));
        }
    }

    private static void handleFurnace(PlayerEntity player) {
        int level = DataHelper.getPlayerStatLevel(player, Stats.FURNACE_FINESSE);
        if (level > 0 && !player.level.isClientSide()) {

            ArrayList<FurnaceTileEntity> furnacesAroundPlayer = new ArrayList<>();
            for (TileEntity listEntity : player.level.tickableBlockEntities) {
                if (listEntity != null) {
                    BlockPos pos = listEntity.getBlockPos();
                    if (listEntity instanceof FurnaceTileEntity && player.distanceToSqr(Vector3d.atCenterOf(pos)) < 4.0D) {
                        furnacesAroundPlayer.add((FurnaceTileEntity) listEntity);
                    }
                }
            }

            for (FurnaceTileEntity furnace : furnacesAroundPlayer)
                for (int i = 0; i < Stats.FURNACE_FINESSE.getBonus(player); i++) // Intend to "mount" ticks, same as Torcherino.
                    furnace.tick();

            furnacesAroundPlayer.clear();
        }

    }

}
