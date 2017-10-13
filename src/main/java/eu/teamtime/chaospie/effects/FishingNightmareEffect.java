package eu.teamtime.chaospie.effects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.explosion.Explosion;

import com.google.common.collect.Lists;

import eu.teamtime.chaospie.ChaosEffectBase;
import eu.teamtime.chaospie.ChaosPie;

public class FishingNightmareEffect extends ChaosEffectBase {

	private List<UUID> fishedItems;
	private Explosion.Builder explosionTemplate;
	
	public FishingNightmareEffect() {
		explosionTemplate = Explosion.builder()
				.radius(3)
				.shouldBreakBlocks(false)
				.shouldDamageEntities(true)
				.canCauseFire(false);
	}
	
	@Override
	public void start() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.RED, "Fishing sure blows."));
		fishedItems = Lists.newArrayList();
	}

	@Override
	public void stop() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GREEN, "I suppose fishing isn't THAT bad."));
		fishedItems.clear();
		fishedItems = null;
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
		return "Fisherman's Nightmare";
	}
	
	@Listener
	public void onEntitySpawn(SpawnEntityEvent e, @Getter("getContext") EventContext context) {
		if (!context.containsKey(EventContextKeys.USED_ITEM))
			return;
		
		Optional<ItemStackSnapshot> usedItem = context.get(EventContextKeys.USED_ITEM);
		if (!usedItem.isPresent() || usedItem.get().getType() != ItemTypes.FISHING_ROD)
			return;
		
		for (Entity ent : e.getEntities()) {
			if (ent instanceof Item)
				fishedItems.add(ent.getUniqueId());
		}
	}
	
	@Listener
	public void onItemPickup(ChangeInventoryEvent.Pickup e, @Getter("getTargetEntity") Item item, @First Player p) {
		if (!fishedItems.contains(item.getUniqueId()))
			return;
		
		Explosion explosion = explosionTemplate
				.location(item.getLocation())
				.build();
		
		e.setCancelled(true);
		fishedItems.remove(item.getUniqueId());
		item.remove();
		
		Sponge.getCauseStackManager().pushCause(ChaosPie.instance());
		p.getWorld().triggerExplosion(explosion);
		Sponge.getCauseStackManager().popCause();
	}

}
