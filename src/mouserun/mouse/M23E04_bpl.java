package mouserun.mouse;

import mouserun.game.Cheese;
import mouserun.game.Grid;
import mouserun.game.Mouse;

public class M23E04_bpl extends Mouse {
    /**
     * Creates a new instance of Mouse.
     *
     * @param name The name of the Mouse to appear in the game interface.
     */
    public M23E04_bpl(String name) {
        super("EL NANO");
    }

    @Override
    public int move(Grid currentGrid, Cheese cheese) {
        return 0;
    }

    @Override
    public void newCheese() {

    }

    @Override
    public void respawned() {

    }
}
