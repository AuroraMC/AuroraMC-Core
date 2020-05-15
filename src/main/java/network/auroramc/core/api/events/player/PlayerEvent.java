package network.auroramc.core.api.events.player;

import network.auroramc.core.api.players.AuroraMCPlayer;
import org.bukkit.event.Event;

public abstract class PlayerEvent extends Event {

    private final AuroraMCPlayer player;

    public PlayerEvent(AuroraMCPlayer player) {
        super(false);
        this.player = player;
    }

    public PlayerEvent(AuroraMCPlayer player, boolean isAsync) {
        super(isAsync);
        this.player = player;
    }


    public AuroraMCPlayer getPlayer() {
        return player;
    }
}
