package net.infstudio.goki.common.handlers;

import net.infstudio.goki.api.capability.CapabilityStat;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StatCapabilityHandler {
    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayer)) return;
        event.addCapability(new ResourceLocation(Reference.MODID, "stat_container"), new CapabilityStat.Provider());
    }
}
