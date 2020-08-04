package tictactoe;

public enum DifficultyLevel {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    private final String level;

    DifficultyLevel(String level) {
        this.level = level;
    }

    public static DifficultyLevel fromString(String level) {
        if (level.equalsIgnoreCase(EASY.getLevel()) || level.equalsIgnoreCase(MEDIUM.getLevel()) || level.equalsIgnoreCase(HARD.getLevel())) {
            return DifficultyLevel.valueOf(level.toUpperCase());
        }
        return null;
    }

    public String getLevel() {
        return level;
    }
}
