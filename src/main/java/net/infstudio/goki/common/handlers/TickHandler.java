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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

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

            ModifiableAttributeInstance atinst = player.getAttribute(Attributes.MOVEMENT_SPEED);
            AttributeModifier mod = new AttributeModifier(stealthSpeedID, "SneakSpeed", Stats.STEALTH.getBonus(player) / 100.0F, AttributeModifier.Operation.byId(1));
            if (player.isSneaking()) {
                if (atinst.getModifier(stealthSpeedID) == null) {
                    atinst.applyPersistentModifier(mod);
                }
            } else if (atinst.getModifier(stealthSpeedID) != null) {
                atinst.removeModifier(mod);
            }

            atinst = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            mod = new AttributeModifier(knockbackResistanceID, "KnockbackResistance", Stats.STEADY_GUARD.getBonus(player), AttributeModifier.Operation.byId(0));
            if (player.isActiveItemStackBlocking()) {
                if (atinst.getModifier(knockbackResistanceID) == null) {
                    atinst.applyPersistentModifier(mod);
                }
            } else if (atinst.getModifier(knockbackResistanceID) != null) {
                atinst.removeModifier(mod);
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
        if (player.isServerWorld() || player.canPassengerSteer())
            if (player.isSwimming()) {
                float multiplier = Math.max(0.0F,
                        Stats.SWIMMING.getBonus(player));
                if (isJumping(player)) {
                    player.jumpMovementFactor += multiplier;
                } else if (multiplier > 0) {
                    //			player.moveEntity(	player.motionX * multiplier,
                    //								player.motionY * multiplier,
                    //								player.motionZ * multiplier);

                    // Copied from LivingEntity
                    double d0 = player.getPosY();
                    float f1 = 0.8f;
                    float f2 = 0.02F;

                    if (multiplier > 0.0F) {
                        f1 += (0.54600006F - f1) * multiplier;
                        f2 += (player.getAIMoveSpeed() - f2) * multiplier;
                    }

                    player.moveRelative(f2, new Vector3d(player.moveStrafing, player.moveVertical, player.moveForward));
                    player.move(MoverType.SELF, player.getMotion());
                    player.setMotion(player.getMotion().mul(f1, 0.800000011920929D, f1));

                    if (!player.hasNoGravity()) {
                        player.setMotion(player.getMotion().add(0, 0.02, 0));
                    }

                    Vector3d offset = player.getMotion().add(0, 0.6000000238418579D - player.getPosY() + d0, 0);
                    if (player.collidedHorizontally && player.isOffsetPositionInLiquid(offset.x, offset.y, offset.z)) {
                        player.setMotion(new Vector3d(player.getMotion().x, 0.30000001192092896D, player.getMotion().z));
                    }


                    player.move(MoverType.SELF, new Vector3d(player.moveStrafing * multiplier, player.moveVertical * multiplier, 0.02f));
                }
            }

        if (player.isOnLadder() && !player.isSneaking()) {
            float multiplier = Stats.CLIMBING.getBonus(player);
            player.move(MoverType.SELF, player.getMotion().mul(1, multiplier, 1));
        }
    }

    private static void handleFurnace(PlayerEntity player) {
        if (DataHelper.getPlayerStatLevel(player, Stats.FURNACE_FINESSE) > 0) {
            /*
            ArrayList<FurnaceTileEntity> furnacesAroundPlayer = new ArrayList<>();

            for (TileEntity listEntity : player.world.loadedTileEntityList) {
                if (listEntity != null) {
                    TileEntity tileEntity = listEntity;
                    BlockPos pos = tileEntity.getPos();
                    if (tileEntity instanceof FurnaceTileEntity && MathHelper.sqrt(player.getDistanceSq(pos)) < 4.0D) {
                        // TODO work out alter way to do tileEntity
                        furnacesAroundPlayer.add((FurnaceTileEntity) tileEntity);
                    }
                }
            }

            // FIXME Laggy

            for (FurnaceTileEntity furnace : furnacesAroundPlayer)
                if (furnace.isBurning())
                    for (int i = 0; i < Stats.FURNACE_FINESSE.getBonus(player); i++) // Intend to "mount" ticks, same as Torcherino.
                        furnace.update();*/
        }

    }

}
