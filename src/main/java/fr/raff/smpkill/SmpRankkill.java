package fr.raff.smpkill;

import fr.raff.smpkill.command.RankCommand;
import fr.raff.smpkill.data.BossBarManager;
import fr.raff.smpkill.data.RankManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class SmpRankkill implements ModInitializer {
    public static final String MOD_ID = "smp-rankkill";

    @Override
    public void onInitialize() {
        System.out.println("[SMP-RankKill] Mod chargé!");
        RankManager.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) -> {
            RankCommand.register(dispatcher);
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            RankManager.onPlayerJoin(player);
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            BossBarManager.remove(handler.getPlayer());
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof ServerPlayerEntity victim) {
                if (damageSource.getAttacker() instanceof ServerPlayerEntity killer) {
                    if (killer.getUuid().equals(victim.getUuid())) return;
                    RankManager.addKill(killer, victim);
                }
            }
        });
    }
}
