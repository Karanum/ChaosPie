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
 * Represents the "Nascar" chaos effect, which when activated causes all online
 * players to lose the ability to jump, but have ludicrous speed.
 * 
 * @author TheMightyDozen
 */
public class NascarEffect extends ChaosPotionBase {

	private final Collection<PotionEffect> effects = Arrays.asList(
			PotionEffect.builder().amplifier(60).duration(safeLengthInSeconds()).potionType(PotionEffectTypes.SPEED)
					.build(),
			PotionEffect.builder().amplifier(250).duration(safeLengthInSeconds()).potionType(PotionEffectTypes.JUMP_BOOST)
					.build());

	@Override
	public int lengthInSeconds() {
		return 60;
	}

	@Override
	public int getWeight() {
		return 20;
	}

	@Override
	protected Text getStartText() {
		return Text.of(TextColors.GOLD, "Let's go for a race!");
	}

	@Override
	protected Text getStopText() {
		return Text.of(TextColors.GREEN, "Race's over. Nobody wins!");
	}

	@Override
	@SuppressWarnings ("unchecked")
	protected Collection<Player> getRelevantEntities() {
		return Sponge.getServer().getOnlinePlayers();
	}

	@Override
	protected Collection<PotionEffect> getPotionEffects() {
		return effects;
	}

	@Override
	public String getName() {
		return "nAAAAAscAAAr";
	}
}