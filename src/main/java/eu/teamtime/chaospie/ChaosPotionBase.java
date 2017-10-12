package eu.teamtime.chaospie;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.world.World;

import com.google.common.collect.Lists;

/**
 * Defines methods and logic that potion-based chaos effects utilize.
 * <p>
 * If need be, extend the logic of this class so it allows for more complex
 * signalling than just plain text, probably using {@link Runnable}
 * 
 * @author TheMightyDozen
 */
public abstract class ChaosPotionBase extends ChaosEffectBase {

	private final List<UUID> entities = Lists.newArrayList();

	@Override
	public final void start() {
		MessageChannel.TO_PLAYERS.send(getStartText());

		getRelevantEntities()
				.forEach(e -> e.offer(e.getOrCreate(PotionEffectData.class).get().addElements(getPotionEffects())));
	}

	@Override
	public final void stop() {
		MessageChannel.TO_PLAYERS.send(getStopText());

		for (World world : Sponge.getServer().getWorlds()) {
			for (UUID id : entities) {
				// TODO check if this works
				world.getEntity(id)
						.ifPresent(e -> e.getOrCreate(PotionEffectData.class).get().removeAll(getPotionEffects()));
			}
		}
	}

	protected abstract Text getStartText();

	protected abstract Text getStopText();

	protected abstract <T extends Entity> Collection<T> getRelevantEntities();

	protected abstract Collection<PotionEffect> getPotionEffects();

}