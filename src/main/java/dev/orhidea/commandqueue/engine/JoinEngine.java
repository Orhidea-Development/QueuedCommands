package dev.orhidea.commandqueue.engine;

import com.massivecraft.massivecore.Engine;
import dev.orhidea.commandqueue.api.CommandQueueApi;
import dev.orhidea.commandqueue.database.QueuedCommandDatabase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEngine extends Engine {

    private static final JoinEngine i = new JoinEngine();
    public static JoinEngine get() { return i; }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        QueuedCommandDatabase.get().getStoredCommands(player.getName(), commandWrappers -> commandWrappers.forEach(commandWrapper -> CommandQueueApi.processCommand(commandWrapper, player, true)), true);
    }

}