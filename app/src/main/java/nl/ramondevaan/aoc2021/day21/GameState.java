package nl.ramondevaan.aoc2021.day21;

public record GameState(int playerOnePosition, int playerTwoPosition, int playerOneScore, int playerTwoScore,
        boolean playerOneTurn) {

    public GameState roll(int value) {
//        playerOneTurn ? new GameState()
        return null;
    }

    @Override
    public int hashCode() {
        return ((playerOnePosition) << 27) |
                ((playerTwoPosition & 0xF) << 23) |
                ((playerOneScore & 0x7FF) << 12) |
                ((playerTwoScore & 0x7FF) << 1) |
                (playerOneTurn ? 1 : 0);
    }
}
