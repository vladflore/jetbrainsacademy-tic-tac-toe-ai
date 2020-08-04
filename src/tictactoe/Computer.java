package tictactoe;

import java.util.Random;

import static tictactoe.Field.O_CELL;
import static tictactoe.Field.X_CELL;
import static tictactoe.TicTacToe.EMPTY_CELL;

public class Computer extends Player {

    private final DifficultyLevel difficultyLevel;
    private final char playAs;
    private final Random random = new Random();
    private Minimax minimax = null;

    public Computer(DifficultyLevel difficultyLevel, char playAs) {
        this.difficultyLevel = difficultyLevel;
        this.playAs = playAs;
    }

    @Override
    protected void makeMove() {
        System.out.println("Making move level \"" + difficultyLevel.getLevel() + "\"");
        int[] moveCoordinates = move();
        field.occupyCell(moveCoordinates[0], moveCoordinates[1], playAs);
    }

    private int[] move() {
        switch (difficultyLevel) {
            case EASY:
                return makeRandomMove();
            case MEDIUM:
                return makeMediumMove();
            case HARD:
                minimax = new Minimax(playAs);
                return makeHardMove();
            default:
                throw new IllegalArgumentException("Move level \"" + difficultyLevel.getLevel() + "\" not supported!");
        }
    }

    private int[] makeRandomMove() {
        int c1 = random.nextInt(3);
        int c2 = random.nextInt(3);
        while (field.cell(c1, c2) != EMPTY_CELL) {
            c1 = random.nextInt(3);
            c2 = random.nextInt(3);
        }
        return new int[]{c1, c2};
    }

    private int[] makeMediumMove() {
        char opponent = playAs == X_CELL ? O_CELL : X_CELL;
        Field.Cell canWinInOneMove = field.winInOneMove(playAs);
        if (canWinInOneMove != null) {
            return new int[]{canWinInOneMove.getI(), canWinInOneMove.getJ()};
        }
        Field.Cell opponentCanWinInOneMove = field.winInOneMove(opponent);
        if (opponentCanWinInOneMove != null) {
            return new int[]{opponentCanWinInOneMove.getI(), opponentCanWinInOneMove.getJ()};
        }
        return makeRandomMove();
    }

    private int[] makeHardMove() {
        return minimax.calculateMove(field);
    }
}
