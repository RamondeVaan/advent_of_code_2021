package nl.ramondevaan.aoc2021.day02;

public class PositionalCommandHandler implements CommandHandler {
    @Override
    public Position handle(Position position, Command command) {
        if (command instanceof ForwardCommand forward) {
            return position.withHorizontal(position.getHorizontal() + forward.getAmount());
        }
        if (command instanceof UpCommand up) {
            return position.withDepth(position.getDepth() - up.getAmount());
        }
        if (command instanceof DownCommand down) {
            return position.withDepth(position.getDepth() + down.getAmount());
        }

        throw new UnsupportedOperationException();
    }
}
