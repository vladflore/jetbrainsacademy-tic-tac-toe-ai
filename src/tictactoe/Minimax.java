package tictactoe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static tictactoe.Field.*;

public class Minimax {
    private final char playAsAi;

    private static final int WIN_SCORE = 10;
    public static final int LOSE_SCORE = -10;
    public static final int TIE_SCORE = 0;

    public Minimax(char playAs) {
        this.playAsAi = playAs;
    }

    public int[] calculateMove(Field board) {
        Field.Cell minimax = minimax(board, playAsAi);
        return new int[]{minimax.getI(), minimax.getJ()};
    }

    private Field.Cell minimax(Field board, char player) {
        List<Field.Cell> availableCells = board.availableCells();

        if (board.checkWin(getHumanPlayer())) {
            Field.Cell cell = new Field.Cell();
            cell.setScore(LOSE_SCORE);
            return cell;
        } else if (board.checkWin(playAsAi)) {
            Field.Cell cell = new Field.Cell();
            cell.setScore(WIN_SCORE);
            return cell;
        } else if (availableCells.size() == 0) {
            Field.Cell cell = new Field.Cell();
            cell.setScore(TIE_SCORE);
            return cell;
        }

        List<Field.Cell> cellsPath = new ArrayList<>();

        for (Field.Cell cell : availableCells) {
            board.occupyCell(cell.getI(), cell.getJ(), player);
            if (player == getHumanPlayer()) {
                cell.setScore(minimax(board, playAsAi).getScore());
            } else {
                cell.setScore(minimax(board, getHumanPlayer()).getScore());
            }
            board.occupyCell(cell.getI(), cell.getJ(), EMPTY_CELL);
            cellsPath.add(cell);
        }

        if (player == playAsAi) {
            return cellsPath.stream().max(Comparator.comparingInt(Cell::getScore)).get();
        } else {
            return cellsPath.stream().min(Comparator.comparingInt(Cell::getScore)).get();
        }
    }

    private char getHumanPlayer() {
        return playAsAi == X_CELL ? O_CELL : X_CELL;
    }
}
