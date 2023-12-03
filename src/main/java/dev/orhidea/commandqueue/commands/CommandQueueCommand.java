package dev.orhidea.commandqueue.commands;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import dev.orhidea.commandqueue.commands.sub.AddCommand;
import dev.orhidea.commandqueue.commands.sub.LookupCommand;

public class CommandQueueCommand extends MassiveCommand {

    private static final CommandQueueCommand i = new CommandQueueCommand();
    public static CommandQueueCommand get() { return i; }

    private CommandQueueCommand() {
        this.setAliases("cq", "cq", "queuecommand", "commandqueue");
        this.setVisibility(Visibility.SECRET);
        this.setDesc("Queue command to execute for player even if offline");

        this.addChild(new AddCommand());
        this.addChild(new LookupCommand());

        this.addRequirements(RequirementHasPerm.get("cq.admin"));
    }

}
