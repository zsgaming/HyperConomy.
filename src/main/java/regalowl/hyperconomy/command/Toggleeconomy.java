package regalowl.hyperconomy.command;


import org.bukkit.command.CommandSender;

import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.util.LanguageFile;

public class Toggleeconomy {
	Toggleeconomy(CommandSender sender) {
		HyperConomy hc = HyperConomy.hc;
		LanguageFile L = hc.getLanguageFile();
		try {
			if (hc.getConf().getBoolean("economy-plugin.use-external")) {
				hc.getConf().set("economy-plugin.use-external", false);
				sender.sendMessage(L.get("TOGGLEECONOMY_DISABLED"));
			} else {
				hc.getConf().set("economy-plugin.use-external", true);
				sender.sendMessage(L.get("TOGGLEECONOMY_ENABLED"));
			}
		} catch (Exception e) {
			sender.sendMessage(L.get("TOGGLEECONOMY_INVALID"));
			return;
		}
	}
}
