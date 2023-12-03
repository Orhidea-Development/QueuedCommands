package dev.orhidea.commandqueue.commands.sub;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mixin.MixinMessage;
import dev.orhidea.commandqueue.QueuedCommand;
import dev.orhidea.commandqueue.api.CommandQueueApi;
import dev.orhidea.commandqueue.entity.Conf;
import dev.orhidea.commandqueue.utils.TxtUtil;

public class AddCommand extends MassiveCommand {
    public AddCommand() {
        setAliases("add");
        setDesc("Queue command for player.");

        this.addParameter(TypeString.get(), "player");
        this.addParameter(TypeString.get(), "command", true);
        this.addRequirements(RequirementHasPerm.get("cq.admin"));

    }

    @Override
    public void perform() throws MassiveException {
        String target = this.readArg();
        String command = this.readArg();

        CommandQueueApi.addCommandToQeueu(target, command);
        QueuedCommand.get().log("&fQueued command for " + target + " - " + command);
        MixinMessage.get().messageOne(this.me, TxtUtil.parseAndReplace(Conf.get().msgCommandSent, "{player}", target, "{command}", command));
    }
}
