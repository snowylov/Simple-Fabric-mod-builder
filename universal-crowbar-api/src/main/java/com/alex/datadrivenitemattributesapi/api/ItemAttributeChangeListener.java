package com.alex.datadrivenitemattributesapi.api;
import net.minecraft.world.item.Item;
@FunctionalInterface public interface ItemAttributeChangeListener{void onChanged(Item item,ItemAttributeDefinition definition,double oldValue,double newValue);}