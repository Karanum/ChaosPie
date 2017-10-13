package eu.teamtime.chaospie.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import eu.teamtime.chaospie.ChaosEffectBase;
import eu.teamtime.chaospie.ChaosPie;
import eu.teamtime.chaospie.EffectMetrics;

public class CommandReport implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Text result = Text.of(TextColors.BLUE, "ChaosPie Effect Metrics:");
		
		EffectMetrics metrics = ChaosPie.instance().getEffectMetrics();
		List<ChaosEffectBase> effects = ChaosPie.instance().getAllEffects();
		
		int totalWeight = 0;
		for (ChaosEffectBase effect : effects) {
			totalWeight += effect.getWeight();
		}
		
		for (ChaosEffectBase effect : effects) {
			result = Text.of(result, Text.NEW_LINE,
					TextColors.YELLOW, effect.getName(), "  ",
					TextColors.GREEN, String.format("%.2f", (effect.getWeight() / (double) totalWeight) * 100), "%  ",
					TextColors.GOLD, metrics.getEffectCount(effect));
		}
		
		src.sendMessage(result);
		return CommandResult.success();
	}

}
