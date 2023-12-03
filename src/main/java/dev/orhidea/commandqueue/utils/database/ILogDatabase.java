package dev.orhidea.commandqueue.utils.database;

import dev.orhidea.commandqueue.utils.Callback;

import java.util.List;

public interface ILogDatabase<Target, Log> {

    void addLog(Target target, Log log);

    void getLog(Target target, long timeMs, Callback<Log> callback);

    void getLogs(Target target, Callback<List<Log>> callback);

    void shrinkLogs(Target target, List<Log> logs);

    void deleteLog(Target target, long timeMs);

    void setupTable(Target target);

}
