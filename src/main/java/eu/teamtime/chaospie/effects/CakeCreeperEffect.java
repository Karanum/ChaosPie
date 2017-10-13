package eu.teamtime.chaospie.effects;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.monster.Creeper;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.explosive.DetonateExplosiveEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;

import eu.teamtime.chaospie.ChaosEffectBase;
import eu.teamtime.chaospie.ChaosPie;

public class CakeCreeperEffect extends ChaosEffectBase {

	private Player winner;
	private Creeper creeper;
	
	private ItemStack cake;
	
	public CakeCreeperEffect() {
		cake = ItemStack.builder()
				.itemType(ItemTypes.CAKE)
				.quantity(1)
				.build();
	}
	
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
			MutableMessageChannel mutChannel = channel.asMutable();
			mutChannel.removeMember(winner);
			channel = mutChannel;
				
			winner.sendMessage(Text.of(TextColors.GREEN, "You have satiated your creeper craving! Congrats!"));
			channel.send(Text.of(TextColors.GREEN, winner.getName() + " was the first to satiate their creeper craving..."));
		}
	}

	@Override
	public int lengthInSeconds() {
		return 0;
	}

	@Override
	public int getWeight() {
		return 10;
	}

	@Override
	public String getName() {
		return "Cake Creeper";
	}
	
	@Listener
	public void onDropItemOnDestruct(DropItemEvent.Destruct e, @First ChangeBlockEvent.Break cause) {	
		Optional<Creeper> explosive = cause.getCause().first(Creeper.class);
		if (!explosive.isPresent())
			return;
		
		if (creeper.getUniqueId().equals(explosive.get().getUniqueId())) {
			for (Entity entItem : e.getEntities()) {
				if (!(entItem instanceof Item))
					continue;
				entItem.offer(Keys.REPRESENTED_ITEM, cake.createSnapshot());
			}
		}
	}
	
	@Listener
	public void onDetonateExplosive(DetonateExplosiveEvent e, @Getter("getTargetEntity") Creeper c) {
		if (c.getTarget().isPresent() && c.getTarget().get() instanceof Player) {
			creeper = c;
			winner = (Player) c.getTarget().get();
			Sponge.getScheduler().createTaskBuilder()
					.execute(() -> {
						stopSelf();
					})
					.submit(ChaosPie.instance());
		}
	}

}
