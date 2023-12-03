package dev.orhidea.commandqueue.coll;

import com.massivecraft.massivecore.store.ConfigurationColl;
import dev.orhidea.commandqueue.entity.Conf;

public class ConfColl extends ConfigurationColl<Conf> {

    private static final ConfColl i = new ConfColl();
    public static ConfColl get() { return i; }

    private ConfColl() {
        super("commandqueue_conf", Conf.class);
    }
}
