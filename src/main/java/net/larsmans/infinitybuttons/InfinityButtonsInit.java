package net.larsmans.infinitybuttons;

import net.fabricmc.api.ModInitializer;
import net.larsmans.infinitybuttons.block.InfinityButtonsBlocks;
import net.larsmans.infinitybuttons.item.InfinityButtonsItemGroups;
import net.larsmans.infinitybuttons.item.InfinityButtonsItems;
import net.larsmans.infinitybuttons.sounds.InfinityButtonsSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinityButtonsInit implements ModInitializer {
	public static final String MOD_ID = "infinitybuttons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final net.larsmans.infinitybuttons.config.InfinityButtonsConfig CONFIG = net.larsmans.infinitybuttons.config.InfinityButtonsConfig.createAndLoad();

	@Override
	public void onInitialize() {
		InfinityButtonsItems.registerModItems();
		InfinityButtonsBlocks.registerModBlocks();
		InfinityButtonsSounds.registerSounds();
		InfinityButtonsItemGroups.loadItemGroup();
	}
}