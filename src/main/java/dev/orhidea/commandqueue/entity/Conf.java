package dev.orhidea.commandqueue.entity;

import com.massivecraft.massivecore.store.Entity;

public class Conf extends Entity<Conf> {

    private static Conf i;
    public static Conf get() { return i; }
    public static void set(Conf conf) { i = conf; }

    //--------------------------//
    // MESSAGES
    //--------------------------//
    public String msgCommandSent = "&6&lQC &8» &fQueued command for &e{player} &8(&7{command}&8)";
}
