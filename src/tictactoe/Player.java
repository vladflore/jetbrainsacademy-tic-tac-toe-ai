package tictactoe;

public abstract class Player {
    protected Field field;

    protected abstract void makeMove();

    public void addField(Field field) {
        this.field = field;
    }
}
