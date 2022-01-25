package net.infstudio.goki.common.handlers;

import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.config.GokiConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.server.ServerLifecycleHooks;

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
        if (event.phase == TickEvent.Phase.START && event.side.isServer()) { // Due to issue #32
            var player = event.player;

            handleTaskPlayerAPI(player);
            if (!player.isAlive()) return;
            var attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attributeInstance == null) return;
            var modifier = new AttributeModifier(stealthSpeedID, "SneakSpeed", Stats.STEALTH.getBonus(player) / 100.0F, AttributeModifier.Operation.fromValue(1));
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
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && GokiConfig.SERVER.syncTicks.get() > 0) {
            if (tickTimer.get() == GokiConfig.SERVER.syncTicks.get()) {
                tickTimer.lazySet(0);
                for (var player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                    SyncEventHandler.syncPlayerData(player);
                }
            } else {
                tickTimer.getAndIncrement();
            }
        }
    }

    public static boolean isJumping(LivingEntity livingBase) {
        return Boolean.TRUE.equals(ObfuscationReflectionHelper.getPrivateValue(LivingEntity.class, livingBase, "field_70703_bu"));
    }

    private static void handleTaskPlayerAPI(Player player) {
        if (!player.isLocalPlayer() || player.canRiderInteract())
            /*if (player.isSwimming()) {
                var multiplier = Math.max(0.0F,
                        Stats.SWIMMING.getBonus(player));
                if (isJumping(player)) {
//                    player += multiplier;
                } else if (multiplier > 0) {
                    //			player.moveEntity(	player.motionX * multiplier,
                    //								player.motionY * multiplier,
                    //								player.motionZ * multiplier);

                    // Copied from LivingEntity
                    var d0 = player.getY();
                    var f1 = 0.8f;
                    var f2 = 0.02F;

                    f1 += (0.54600006F - f1) * multiplier;
                    f2 += (player.getSpeed() - f2) * multiplier;

                    player.moveRelative(f2, player.getDeltaMovement());
                    player.move(MoverType.SELF, player.getDeltaMovement());
                    player.setDeltaMovement(player.getDeltaMovement().multiply(f1, 0.800000011920929D, f1));

                    if (!player.isNoGravity()) {
                        player.setDeltaMovement(player.getDeltaMovement().add(0, 0.02, 0));
                    }

                    var offset = player.getDeltaMovement().add(0, 0.6000000238418579D - player.getY() + d0, 0);
//                    if (player.horizontalCollision && player.isOffsetPositionInLiquid(offset.x, offset.y, offset.z)) {
//                        player.setMotion(new Vector3d(player.getMotion().x, 0.30000001192092896D, player.getMotion().z));
//                    }


//                    player.move(MoverType.SELF, new Vector3d(player.moveStrafing * multiplier, player.moveVertical * multiplier, 0.02f));
                }
            }*/

        if (player.onClimbable() && !player.isShiftKeyDown()) {
            var multiplier = Stats.CLIMBING.getBonus(player);
            player.move(MoverType.SELF, player.getDeltaMovement().multiply(1, multiplier, 1));
        }
    }

}
