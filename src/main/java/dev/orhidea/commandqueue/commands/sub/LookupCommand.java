package dev.orhidea.commandqueue.commands.sub;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.pager.Msonifier;
import com.massivecraft.massivecore.pager.Pager;
import dev.orhidea.commandqueue.database.QueuedCommandDatabase;
import dev.orhidea.commandqueue.entity.wrapper.CommandWrapper;
import dev.orhidea.commandqueue.utils.TxtUtil;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LookupCommand extends MassiveCommand {

    public LookupCommand() {
        this.setAliases("lookup");
        this.setVisibility(Visibility.SECRET);
        this.setDesc("View queued commands");

        this.addParameter(TypeString.get(), "player");
        this.addParameter(Parameter.getPage());

        this.addRequirements(RequirementHasPerm.get("cq.admin"));
    }

    @Override
    public void perform() throws MassiveException {
        String player = this.readArg();
        int page = this.readArg();

        QueuedCommandDatabase.get().getStoredCommands(player, logs -> {
            Collections.reverse(logs);

            long currentMs = System.currentTimeMillis();
            Pager<CommandWrapper> pager = new Pager<>(this, "Queued Commands", page, logs, (Msonifier<CommandWrapper>) (logEntry, index) -> {
                List<String> toolTip = new ArrayList<>();

                toolTip.add(ChatColor.GREEN + DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(logEntry.getTimeReceivedMs()));
                toolTip.add(DurationFormatUtils.formatDurationWords(currentMs - logEntry.getTimeReceivedMs(), true, true) + " ago");

                String line = player + " &8- &7" + TxtUtil.parseAndReplace(logEntry.getCommand() + "&f");

                return Mson.parse(line).tooltip(toolTip);
            });

            pager.message();
        }, false);
    }
}
