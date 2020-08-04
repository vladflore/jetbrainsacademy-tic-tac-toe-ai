package tictactoe;

import java.util.List;
import java.util.Scanner;

public class TicTacToe {

    public static final char EMPTY_CELL = ' ';
    public static final InputCommand inputCommand = new InputCommand(new Scanner(System.in));

    private final Field field;

    public TicTacToe() {
        field = new Field();
    }

    public void start() {
        inputCommand.parse();
        while (inputCommand.getCommand() != Command.EXIT) {
            if (inputCommand.isValid()) {
                List<Player> players = inputCommand.getPlayers();
                players.get(0).addField(field);
                players.get(1).addField(field);
                play(players);
                field.reset();
            }
            inputCommand.parse();
        }
    }

    private void play(List<Player> players) {
        field.print();
        FieldState fieldState;
        do {
            players.get(0).makeMove();
            field.print();
            fieldState = field.analyze();
            printState(fieldState);
            if (continueGame(fieldState)) {
                players.get(1).makeMove();
                field.print();
                fieldState = field.analyze();
                printState(fieldState);
            }
        } while (continueGame(fieldState));
    }

    private void printState(FieldState fieldState) {
        switch (fieldState) {
            case X_WINS:
                System.out.println("X wins");
                break;
            case O_WINS:
                System.out.println("O wins");
                break;
            case DRAW:
                System.out.println("Draw");
                break;
        }
    }

    private boolean continueGame(FieldState fieldState) {
        return fieldState != FieldState.DRAW && fieldState != FieldState.X_WINS && fieldState != FieldState.O_WINS;
    }
}
