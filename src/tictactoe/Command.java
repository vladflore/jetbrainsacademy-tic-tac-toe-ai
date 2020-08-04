package tictactoe;

public enum Command {
    START("start"),
    EXIT("exit");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public static Command fromString(String command) {
        if (command.equals(START.getCommand()) || command.equals(EXIT.getCommand())) {
            return Command.valueOf(command.toUpperCase());
        }
        return null;
    }

    public String getCommand() {
        return command;
    }
}