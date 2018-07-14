package net.infstudio.goki.core;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class ModContainer extends DummyModContainer {
    public ModContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "gokistat-core";
        meta.name = "GokiStat Core";
        meta.description = "GokiStat-coremode";
        meta.version = "1.0";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
