package com.genomu.starttravel.util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInvoker {
    private List<DBCommand> dbCommands = new ArrayList<>();
    public void addCommand(DBCommand dbCommand){
        dbCommands.add(dbCommand);
    }
    public void assignCommand(){
        for(DBCommand dbCommand : dbCommands){
            dbCommand.work();
        }
        dbCommands.clear();
    }
}
