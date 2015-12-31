package tech.bcozo.game.tools.console;

public enum Controller {
    UP(1), UPRIGHT(2), RIGHT(4), DOWNRIGHT(8), DOWN(16), DOWNLEFT(32), LEFT(64),
    UPLEFT(128);
    private int value;

    private Controller(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
