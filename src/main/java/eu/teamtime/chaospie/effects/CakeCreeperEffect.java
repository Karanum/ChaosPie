package eu.teamtime.chaospie.effects;

import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;

import eu.teamtime.chaospie.ChaosEffectBase;

public class CakeCreeperEffect extends ChaosEffectBase {

	private User winner;
	
	@Override
	public void start() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.RED, "You suddenly crave creeper explosions...?"));
		winner = null;
	}

	@Override
	public void stop() {
		if (winner == null) {
			MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GREEN, "The creeper craving has vanished."));
		} else {
			MessageChannel channel = MessageChannel.TO_PLAYERS;
			Optional<Player> p = winner.getPlayer();
			if (p.isPresent()) {
				MutableMessageChannel mutChannel = channel.asMutable();
				mutChannel.removeMember(p.get());
				channel = mutChannel;
				
				p.get().sendMessage(Text.of(TextColors.GREEN, "You have satiated your creeper craving! Congrats!"));
			}
			channel.send(Text.of(TextColors.GREEN, winner.getName() + " was the first to satiate their creeper craving..."));
		}
	}

	@Override
	public int lengthInSeconds() {
		return 0;
	}

	@Override
	public int getWeight() {
		return 30;
	}

	@Override
	public String getName() {
		return "Cake Creeper";
	}
	
	@Listener
	public void onDropItemOnDestruct(DropItemEvent.Destruct e) {
		System.out.println("Source: " + e.getSource());
		System.out.println("Context: " + e.getContext());
		System.out.println("Cause: " + e.getCause());
		System.out.println("Entities: " + e.getEntities().size());
	}

}
