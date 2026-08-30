package fr.raff.smpkill;

import fr.raff.smpkill.data.DataManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

public class SmpKill implements ModInitializer {
    public static final String MOD_ID = "smpkill";

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            DataManager.load(server);
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            DataManager.save(server);
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayer victim) {
                if (damageSource.getEntity() instanceof ServerPlayer killer) {
                    if (killer.getUUID().equals(victim.getUUID())) return;

                    DataManager.addKill(killer.getUUID(), killer.getName().getString());
                    DataManager.save(killer.getServer());

                    var data = DataManager.get(killer.getUUID(), killer.getName().getString());
                    var rank = data.getRank();

                    killer.getServer().getPlayerList().broadcastSystemMessage(
                        Component.literal("§7[§cKill§7] " + rank.color + killer.getName().getString() + " §7a tué " + victim.getName().getString() + " §7(" + data.kills + " kills) [" + rank.displayName + "]"), false
                    );
                }
            }
        });

        System.out.println("[SmpKill] Mod chargé!");
    }
}
