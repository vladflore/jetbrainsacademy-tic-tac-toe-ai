package tictactoe;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Field {

    public static final char X_CELL = 'X';
    public static final char O_CELL = 'O';
    public static final char EMPTY_CELL = ' ';
    public static final int MATRIX_SIZE = 3;

    private final char[][] field;
    private final Map<Character, Cell> winInOneMove = new HashMap<>();
    private final Map<Character, List<Cell>> fieldStateMap = new HashMap<>();
    private final Map<Integer, List<Cell>> rows = new HashMap<>();
    private final Map<Integer, List<Cell>> columns = new HashMap<>();
    private final List<Cell> diagonalOne = new ArrayList<>();
    private final List<Cell> diagonalTwo = new ArrayList<>();

    public Field() {
        field = new char[MATRIX_SIZE][MATRIX_SIZE];
        init();
    }

    private void init() {
        for (char[] chars : field) {
            Arrays.fill(chars, EMPTY_CELL);
        }
    }

    public void reset() {
        init();
    }

    public void print() {
        System.out.println("---------");
        for (char[] chars : field) {
            System.out.print("|");
            for (char aChar : chars) {
                System.out.print(" " + aChar);
            }
            System.out.println(" |");
        }
        System.out.println("---------");
    }

    public FieldState analyze() {
        FieldState fieldState = null;

        winInOneMove.clear();
        fieldStateMap.clear();

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                List<Cell> cells;
                if (!fieldStateMap.containsKey(field[i][j])) {
                    cells = new ArrayList<>();
                } else {
                    cells = fieldStateMap.get(field[i][j]);
                }
                cells.add(new Cell(i, j));
                fieldStateMap.put(field[i][j], cells);
            }
        }

        boolean hasEmptyCell = false;
        if (fieldStateMap.containsKey(EMPTY_CELL)) {
            hasEmptyCell = true;
        }

        winInOneMove.put(X_CELL, twoInARow(X_CELL));
        winInOneMove.put(O_CELL, twoInARow(O_CELL));

        boolean xWon = hasThreeInARow(X_CELL);
        boolean oWon = hasThreeInARow(O_CELL);

        if (xWon) {
//            System.out.println("X wins");
            fieldState = FieldState.X_WINS;
        }
        if (oWon) {
//            System.out.println("O wins");
            fieldState = FieldState.O_WINS;
        }
        if (!xWon && !oWon && !hasEmptyCell) {
//            System.out.println("Draw");
            fieldState = FieldState.DRAW;
        }
        if (!xWon && !oWon && hasEmptyCell) {
            fieldState = FieldState.GAME_NOT_FINISHED;
        }

        return fieldState;
    }

    private Cell twoInARow(char cellValue) {
        categorizeFieldCells();
        Cell winningCell = null;
        List<Cell> cells = fieldStateMap.get(cellValue);
        if (cells != null) {
            for (Cell cell : cells) {
                diagonalOne.remove(cell);
                if (diagonalOne.size() == 1) {
                    winningCell = diagonalOne.get(0);
                }
                diagonalTwo.remove(cell);
                if (diagonalTwo.size() == 1) {
                    winningCell = diagonalTwo.get(0);
                }
                AtomicReference<Cell> winningCellInRow = new AtomicReference<>();
                rows.forEach((rowIndex, rowCells) -> {
                    rowCells.remove(cell);
                    if (rowCells.size() == 1) {
                        winningCellInRow.set(rowCells.get(0));
                    }
                });
                if (winningCellInRow.get() != null) {
                    winningCell = winningCellInRow.get();
                }

                AtomicReference<Cell> winningCellInColumn = new AtomicReference<>();
                columns.forEach((columnIndex, columnCells) -> {
                    columnCells.remove(cell);
                    if (columnCells.size() == 1) {
                        winningCellInColumn.set(columnCells.get(0));
                    }
                });
                if (winningCellInColumn.get() != null) {
                    winningCell = winningCellInColumn.get();
                }
            }
        }

        // winning cell might be already occupied
        if (winningCell != null && field[winningCell.getI()][winningCell.getJ()] != EMPTY_CELL) {
            return null;
        }

        return winningCell;
    }

    private boolean hasThreeInARow(char cellValue) {
        categorizeFieldCells();
        List<Cell> cells = fieldStateMap.get(cellValue);
        if (cells != null) {
            for (Cell cell : cells) {
                diagonalOne.remove(cell);
                if (diagonalOne.size() == 0) {
                    return true;
                }
                diagonalTwo.remove(cell);
                if (diagonalTwo.size() == 0) {
                    return true;
                }
                AtomicBoolean emptyRow = new AtomicBoolean(false);
                rows.forEach((rowIndex, rowCells) -> {
                    rowCells.remove(cell);
                    if (rowCells.size() == 0) {
                        emptyRow.set(true);
                    }
                });
                if (emptyRow.get()) {
                    return true;
                }
                AtomicBoolean emptyColumn = new AtomicBoolean(false);
                columns.forEach((columnIndex, columnCells) -> {
                    columnCells.remove(cell);
                    if (columnCells.size() == 0) {
                        emptyColumn.set(true);
                    }
                });
                if (emptyColumn.get()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void categorizeFieldCells() {
        rows.clear();
        columns.clear();
        diagonalOne.clear();
        diagonalTwo.clear();

        for (int i = 0; i < field.length; i++) {
            rows.put(i, new ArrayList<>());
            columns.put(i, new ArrayList<>());
            for (int j = 0; j < field.length; j++) {

                //rows
                List<Cell> rowCells = rows.get(i);
                rowCells.add(new Cell(i, j));
                rows.put(i, rowCells);

                //columns
                List<Cell> columnCells = columns.get(i);
                columnCells.add(new Cell(j, i));
                columns.put(i, columnCells);

                //diagonal 1
                if (i == j) {
                    diagonalOne.add(new Cell(i, j));
                }

                //diagonal 2
                if (i == MATRIX_SIZE - j - 1) {
                    diagonalTwo.add(new Cell(i, j));
                }
            }
        }
    }

    public void occupyCell(int coord1, int coord2, char value) {
        field[coord1][coord2] = value;
    }

    public char cell(int coord1, int coord2) {
        return field[coord1][coord2];
    }

    public Cell winInOneMove(char playAs) {
        return winInOneMove.get(playAs);
    }

    public List<Cell> availableCells() {
        List<Cell> emptyCells = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (field[i][j] == EMPTY_CELL) {
                    emptyCells.add(new Cell(i, j));
                }
            }
        }
        return emptyCells;
    }

    public boolean checkWin(char player) {
        FieldState fieldState = analyze();
        if (fieldState == FieldState.X_WINS) {
            return player == X_CELL;
        } else if (fieldState == FieldState.O_WINS) {
            return player == O_CELL;
        }
        return false;
    }

    static class Cell {
        private int i;
        private int j;
        private int score;

        public Cell(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Cell() {

        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getJ() {
            return j;
        }

        public void setJ(int j) {
            this.j = j;
        }

        @Override
        public String toString() {
            return String.format("(%s,%s)", i, j);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return i == cell.i &&
                    j == cell.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
