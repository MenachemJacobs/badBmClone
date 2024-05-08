package edu.touro.mco152.bm.Command;

public class Invoker {
    Command myCommand;

    public Invoker(Command passedCommand) {
        myCommand = passedCommand;
    }

    public void invoke(){
        myCommand.execute();
    }
}
