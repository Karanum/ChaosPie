package eu.teamtime.chaospie.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import eu.teamtime.chaospie.ChaosEffectBase;
import eu.teamtime.chaospie.ChaosPie;

/**
 * Represents the "Mini Cook" chaos effect, which when activated causes dropped
 * cooked meat to turn into its respective mob.
 * 
 * @author TheMightyDozen
 */
public class MiniCookEffect extends ChaosEffectBase {
	
	private Task scanner;

	private static Map<ItemType, EntityType> MEAT_TO_ANIMAL = new HashMap<ItemType, EntityType>(5);
	static {
		MEAT_TO_ANIMAL.put(ItemTypes.COOKED_BEEF, EntityTypes.COW); // TODO allow cooked beef to spawn mooshrooms?
		MEAT_TO_ANIMAL.put(ItemTypes.COOKED_CHICKEN, EntityTypes.CHICKEN);
		MEAT_TO_ANIMAL.put(ItemTypes.COOKED_MUTTON, EntityTypes.SHEEP); // TODO allow cooked mutton to spawn sheep of randomized colors?
		MEAT_TO_ANIMAL.put(ItemTypes.COOKED_PORKCHOP, EntityTypes.PIG);
		MEAT_TO_ANIMAL.put(ItemTypes.COOKED_RABBIT, EntityTypes.RABBIT);
	}

	@Override
	public void start() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.LIGHT_PURPLE, "Uhh... Why does your steak look alive all of a sudden?"));

		Task.builder()
			.execute(() -> MessageChannel.TO_PLAYERS.send(Text.of(TextColors.LIGHT_PURPLE, "Oh geez, get rid of that! Throw it away!")))
			.delay(3, TimeUnit.SECONDS)
			.name("ChaosPie-MiniCookEffect-Chatbot")
			.submit(ChaosPie.instance());

		scanner = Task.builder()
				.execute(this::scanItems)
				.intervalTicks(30)
				.name("ChaosPie-MiniCookEffect-ItemScanner")
				.submit(ChaosPie.instance());
	}

	private void scanItems() {
		// TODO is this how you use the cause stack manager?
		Sponge.getCauseStackManager().addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN);
		for (World world : Sponge.getServer().getWorlds()) {
			for (Entity entity : world.getEntities(e -> e instanceof Item)) {
				Item item = (Item)entity;
				if (MEAT_TO_ANIMAL.containsKey((item).getItemType())
					&& item.getVelocity().length() == 0) {
					world.spawnEntity(world.createEntity(MEAT_TO_ANIMAL.get((item).getItemType()),
														 item.getLocation().getPosition()));
					item.remove();
				}
			}
		}
		Sponge.getCauseStackManager().removeContext(EventContextKeys.SPAWN_TYPE);
	}

	@Override
	public void stop() {
		MessageChannel.TO_PLAYERS.send(Text.of(TextColors.GREEN, "OK, no more zombie cows now. Phew."));
		scanner.cancel();
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
		return "Mini Cook";
	}
}