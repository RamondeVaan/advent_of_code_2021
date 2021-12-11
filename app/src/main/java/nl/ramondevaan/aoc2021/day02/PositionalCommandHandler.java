package nl.ramondevaan.aoc2021.day02;

public class PositionalCommandHandler implements CommandHandler {
    @Override
    public Position handle(Position position, Command command) {
        if (command instanceof ForwardCommand forward) {
            return position.withHorizontal(position.getHorizontal() + forward.amount());
        }
        if (command instanceof UpCommand up) {
            return position.withDepth(position.getDepth() - up.amount());
        }
        if (command instanceof DownCommand down) {
            return position.withDepth(position.getDepth() + down.amount());
        }

        throw new UnsupportedOperationException();
    }
}
