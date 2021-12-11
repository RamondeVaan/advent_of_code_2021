package nl.ramondevaan.aoc2021.day02;

public class AimCommandHandler implements CommandHandler {
    @Override
    public Position handle(Position position, Command command) {
        if (command instanceof ForwardCommand forward) {
            return new Position(
                    position.getHorizontal() + forward.amount(),
                    position.getDepth() + position.getAim() * forward.amount(),
                    position.getAim()
            );
        }
        if (command instanceof UpCommand up) {
            return position.withAim(position.getAim() - up.amount());
        }
        if (command instanceof DownCommand down) {
            return position.withAim(position.getAim() + down.amount());
        }

        throw new UnsupportedOperationException();
    }
}
