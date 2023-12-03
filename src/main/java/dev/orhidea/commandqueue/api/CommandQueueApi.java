package dev.orhidea.commandqueue.api;

import dev.orhidea.commandqueue.QueuedCommand;
import lombok.experimental.UtilityClass;
import dev.orhidea.commandqueue.database.QueuedCommandDatabase;
import dev.orhidea.commandqueue.entity.wrapper.CommandWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@UtilityClass
public class CommandQueueApi {
    public void addCommandToQeueu(String player, String command) {
        Bukkit.getScheduler().runTaskAsynchronously(QueuedCommand.get(), () -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
            long receivedMs = System.currentTimeMillis();

            CommandWrapper commandWrapper = new CommandWrapper(receivedMs, player, command);

            processCommand(commandWrapper, offlinePlayer.getPlayer(), offlinePlayer.isOnline());
        });
    }
    public void processCommand(CommandWrapper commandWrapper, Player player,boolean isOnline){
        if(isOnline) {
            QueuedCommand.get().log("Executing queued command for " + player.getName() + " - " + commandWrapper.getCommand());
            Bukkit.getScheduler().runTask(QueuedCommand.get(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandWrapper.getCommand().replace("{player}", player.getName())));
        } else
            QueuedCommandDatabase.get().addStoredCommand(commandWrapper);

    }
}
