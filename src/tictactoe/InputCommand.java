package tictactoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static tictactoe.Field.O_CELL;
import static tictactoe.Field.X_CELL;

public class InputCommand {
    private static final String USER = "user";
    private static final String EASY = "easy";
    private static final String MEDIUM = "medium";
    private static final String HARD = "hard";
    private static final int CORRECT_NUMBER_OF_PARAMS = 3;
    private static final String CMD_LN_PARAMS_SEP = " ";
    private static final int CMD_IDX = 0;
    private static final int PLAYER_ONE_IDX = 1;
    private static final int PLAYER_TWO_IDX = 2;
    private final Scanner scanner;

    private Command command;
    private List<Player> players;
    private boolean valid;

    public InputCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    public void parse() {
        System.out.print("Input command: ");
        String[] commandLineParams = scanner.nextLine().split(CMD_LN_PARAMS_SEP);
        players = new ArrayList<>();
        valid = true;
        if (commandLineParams.length > 0) {
            command = Command.fromString(commandLineParams[CMD_IDX]);
            if (command == null) {
                errorOut();
                return;
            }
            if (command == Command.START) {
                if (commandLineParams.length == CORRECT_NUMBER_OF_PARAMS) {
                    Player playerOne = createPlayer(commandLineParams[PLAYER_ONE_IDX], X_CELL);
                    Player playerTwo = createPlayer(commandLineParams[PLAYER_TWO_IDX], O_CELL);
                    if (playerOne == null || playerTwo == null) {
                        errorOut();
                    } else {
                        players.add(playerOne);
                        players.add(playerTwo);
                    }
                } else {
                    errorOut();
                }
            }
        } else {
            errorOut();
        }
    }

    private void errorOut() {
        System.out.println("Bad parameters!");
        valid = false;
    }

    private Player createPlayer(String type, char playAs) {
        Player player = null;
        switch (type) {
            case USER:
                player = new User(playAs);
                break;
            case EASY:
                player = new Computer(DifficultyLevel.fromString(EASY), playAs);
                break;
            case MEDIUM:
                player = new Computer(DifficultyLevel.fromString(MEDIUM), playAs);
                break;
            case HARD:
                player = new Computer(DifficultyLevel.fromString(HARD), playAs);
                break;
        }
        return player;
    }

    public Command getCommand() {
        return command;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public boolean isValid() {
        return valid;
    }

    public String readUserMove() {
        return scanner.nextLine();
    }
}
