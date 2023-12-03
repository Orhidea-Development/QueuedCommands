package dev.orhidea.commandqueue.entity.wrapper;

public class CommandWrapper {

    private final long timeReceivedMs;
    private final String command;
    private final String playerName;

    public CommandWrapper(long timeReceivedMs, String playerName, String command) {
        this.timeReceivedMs = timeReceivedMs;
        this.playerName = playerName;
        this.command = command;
    }

    public long getTimeReceivedMs() {
        return this.timeReceivedMs;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getCommand() {
        return this.command;
    }
}
