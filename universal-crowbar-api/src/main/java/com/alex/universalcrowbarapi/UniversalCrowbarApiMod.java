package com.alex.universalcrowbarapi;
import com.alex.universalcrowbarapi.item.UniversalCrowbarItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public final class UniversalCrowbarApiMod implements ModInitializer {
    public static final String MOD_ID = "universal-crowbar-api";
    public static final Map<String, Item> CROWBARS = new LinkedHashMap<>();
    private static final List<String> CROWBAR_IDS = List.of(
        "amethyst_crowbar_tool","certus_quartz_crowbar_tool","copper_crowbar_tool",
        "diamond_crowbar_tool","gold_crowbar_tool","iron_crowbar_tool","lapis_crowbar_tool",
        "netherite_crowbar_tool","quartz_crowbar_tool","redstone_crowbar_tool",
        "ruby_crowbar_tool","stone_crowbar_tool","wood_crowbar_tool"
    );
    @Override
    public void onInitialize() {
        CROWBAR_IDS.forEach(UniversalCrowbarApiMod::registerCrowbar);
        Identifier id = id("crowbars");
        ResourceKey<CreativeModeTab> key = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), id);
        CreativeModeTab tab = FabricItemGroup.builder()
            .title(Component.translatable("itemGroup." + MOD_ID + ".crowbars"))
            .icon(() -> new ItemStack(CROWBARS.get("iron_crowbar_tool")))
            .displayItems((parameters, output) -> CROWBAR_IDS.forEach(path -> output.accept(CROWBARS.get(path))))
            .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }
    private static Item registerCrowbar(String path) {
        Identifier id = id(path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Item item = new UniversalCrowbarItem(new Item.Properties().stacksTo(1).setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        CROWBARS.put(path, item);
        return item;
    }
    public static Identifier id(String path) { return Identifier.fromNamespaceAndPath(MOD_ID, path); }
}
