package com.moigferdsrte.windchimes.register;

import com.moigferdsrte.windchimes.WindChimes;
import com.moigferdsrte.windchimes.item.BambooChimeItem;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemRegistry {
    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(WindChimes.MOD_ID, "chime_tab"), FabricCreativeModeTab.builder()
                .title(Component.translatable("itemGroup.windchimes.item_tab"))
                .icon(Items.BELL::getDefaultInstance)
                .displayItems((par, output) -> {
                    output.accept(IRON_CHIME);
                    output.accept(COPPER_CHIME);
                    output.accept(BAMBOO_CHIME);
                }).build());
    }

    public static final Item IRON_CHIME = registerItemViaBlock(BlocksRegistry.Blocks.IRON_CHIME);
    public static final Item COPPER_CHIME = registerItemViaBlock(BlocksRegistry.Blocks.COPPER_CHIME);
    public static final Item BAMBOO_CHIME = registerItemViaBlock(BlocksRegistry.Blocks.BAMBOO_CHIME, (b ,p) -> new BambooChimeItem(b, BlocksRegistry.Blocks.WALL_BAMBOO_CHIME, Direction.DOWN, p));
    public static Item registerItemViaBlock(Block block, BiFunction<Block, Item.Properties, Item> biFunction, Item.Properties properties) {
        return registerItem(
                blockIdToItemId(block.builtInRegistryHolder().key()), properties2 -> biFunction.apply(block, properties2), properties.useBlockDescriptionPrefix()
        );
    }

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> resourceKey) {
        return ResourceKey.create(Registries.ITEM, resourceKey.identifier());
    }

    public static Item registerItem(ResourceKey<Item> resourceKey, Function<Item.Properties, Item> function, Item.Properties properties) {
        Item item = function.apply(properties.setId(resourceKey));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        return Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
    }

    public static Item registerItemViaBlock(Block block, BiFunction<Block, Item.Properties, Item> biFunction) {
        return registerItemViaBlock(block, biFunction, new Item.Properties());
    }

    public static Item registerItemViaBlock(Block block) {
        return registerItemViaBlock(block, BlockItem::new);
    }
}
