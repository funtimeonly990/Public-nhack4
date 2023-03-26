package ryo.mrbubblegum.nhack4.world;

import net.minecraft.util.MovementInput;

public class PlayerUpdateMoveEvent
        extends EventStage {
    public MovementInput movementInput;

    public PlayerUpdateMoveEvent(MovementInput movementInput) {
        this.movementInput = movementInput;
    }
}