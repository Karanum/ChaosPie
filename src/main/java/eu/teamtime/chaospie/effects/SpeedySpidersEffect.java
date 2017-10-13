package eu.teamtime.chaospie.effects;

import java.util.Arrays;
import java.util.Collection;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.monster.Spider;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.common.collect.Lists;

import eu.teamtime.chaospie.ChaosPotionBase;

/**
 * Represents the "Speedy Spiders" chaos effect, which when activated gives all
 * spiders in the server ludicrous speed.
 * 
 * @author TheMightyDozen
 */
public class SpeedySpidersEffect extends ChaosPotionBase {

	private final Collection<PotionEffect> effects = Arrays.asList(PotionEffect.builder().amplifier(40)
			.duration(lengthInSeconds()).potionType(PotionEffectTypes.SPEED).build());

	@Override
	public int lengthInSeconds() {
		return 0;
	}

	@Override
	public int getWeight() {
		return 20;
	}

	@Override
	protected Text getStartText() {
		return Text.of(TextColors.YELLOW, "Hey who wants flying spiders? I'd like some!");
	}

	@Override
	protected Text getStopText() {
		return Text.of(TextColors.GREEN, "Aww, the spiders aren't flying anymore...");
	}

	@Override
	protected Collection<Entity> getRelevantEntities() {
		Collection<Entity> spiders = Lists.newArrayList();
		Sponge.getServer().getWorlds().forEach(world -> spiders.addAll(world.getEntities(e -> e instanceof Spider)));
		return spiders;
	}

	@Override
	protected Collection<PotionEffect> getPotionEffects() {
		return effects;
	}

	@Override
	public String getName() {
		return "Speedy Spiders";
	}
}