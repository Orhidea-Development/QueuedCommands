package dev.orhidea.commandqueue;

import com.massivecraft.massivecore.MassivePlugin;
import dev.orhidea.commandqueue.coll.ConfColl;
import dev.orhidea.commandqueue.commands.CommandQueueCommand;
import dev.orhidea.commandqueue.database.QueuedCommandDatabase;
import dev.orhidea.commandqueue.engine.JoinEngine;

public final class QueuedCommand extends MassivePlugin {

    private static QueuedCommand i;
    public static QueuedCommand get() { return i; }

    public QueuedCommand() {
        i = this;
    }

    @Override
    public void onEnableInner() {
        this.activate(
                ConfColl.class,
                JoinEngine.class,
                CommandQueueCommand.class,
                QueuedCommandDatabase.class
        );
    }
}
