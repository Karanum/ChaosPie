package eu.teamtime.chaospie.effects;

import java.util.Arrays;
import java.util.Collection;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import eu.teamtime.chaospie.ChaosPotionBase;

/**
 * Represents the "Floating" chaos effect, which when activated gives all
 * players levitation -1, which causes them to be unable to change their Y
 * value.
 * 
 * @author TheMightyDozen
 */
public class FloatingEffect extends ChaosPotionBase {

	private final Collection<PotionEffect> effects = Arrays.asList(PotionEffect.builder().amplifier(255)
			.duration(lengthInSeconds()).potionType(PotionEffectTypes.LEVITATION).build());

	@Override
	public int lengthInSeconds() {
		return 120;
	}

	@Override
	public int getWeight() {
		return 30;
	}

	@Override
	protected Text getStartText() {
		return Text.of(TextColors.AQUA, "Gravity? Who gives a crap about gravity?");
	}

	@Override
	protected Text getStopText() {
		return Text.of(TextColors.GREEN, "Hey, I found the gravity switch!");
	}

	@Override
	protected Collection<Player> getRelevantEntities() {
		return Sponge.getServer().getOnlinePlayers();
	}

	@Override
	protected Collection<PotionEffect> getPotionEffects() {
		return effects;
	}

	@Override
	public String getName() {
		return "Floating";
	}
}