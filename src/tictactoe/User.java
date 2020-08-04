package tictactoe;

import static tictactoe.TicTacToe.EMPTY_CELL;

public class User extends Player {

    private final char playAs;

    public User(char playAs) {
        this.playAs = playAs;
    }

    @Override
    protected void makeMove() {
        int[] coordinates;
        do {
            System.out.print("Enter the coordinates: ");
            coordinates = userMove(TicTacToe.inputCommand.readUserMove());
        } while (coordinates.length == 0);
        field.occupyCell(coordinates[0], coordinates[1], playAs);
    }

    private int[] userMove(String coordinates) {
        if (!coordinates.matches("\\d\\s\\d")) {
            System.out.println("You should enter numbers!");
            return new int[0];
        }

        int c1 = Integer.parseInt(coordinates.split(" ")[0]);
        int c2 = Integer.parseInt(coordinates.split(" ")[1]);
        if (c1 < 1 || c1 > 3 || c2 < 1 || c2 > 3) {
            System.out.println("Coordinates should be from 1 to 3!");
            return new int[0];
        }

        int realC1 = c2 - 1;
        if (c2 == 1) {
            realC1 += 2;
        }
        if (c2 == 3) {
            realC1 -= 2;
        }
        int realC2 = c1 - 1;

        if (field.cell(realC1, realC2) != EMPTY_CELL) {
            System.out.println("This cell is occupied! Choose another one!");
            return new int[0];
        }

        return new int[]{realC1, realC2};
    }
}
