package com.alex.datadrivenitemattributesapi.api;
import net.minecraft.core.registries.BuiltInRegistries;import net.minecraft.world.item.*;import java.util.*;import java.util.concurrent.CopyOnWriteArrayList;
public final class DataDrivenItemAttributesApi{
 private static final Map<String,ItemAttributeDefinition> DEFINITIONS=new LinkedHashMap<>();
 private static final Map<String,Map<String,Double>> VALUES=new LinkedHashMap<>();
 private static final List<ItemAttributeChangeListener> LISTENERS=new CopyOnWriteArrayList<>();
 private DataDrivenItemAttributesApi(){}
 public static void registerAttribute(ItemAttributeDefinition d){DEFINITIONS.put(d.id(),Objects.requireNonNull(d));}
 public static Map<String,ItemAttributeDefinition> definitions(){return Collections.unmodifiableMap(DEFINITIONS);}
 public static ItemAttributeDefinition definition(String id){return DEFINITIONS.get(id);}
 public static void addListener(ItemAttributeChangeListener l){LISTENERS.add(Objects.requireNonNull(l));}
 public static double get(ItemStack s,String id){ItemAttributeDefinition d=req(id);String item=BuiltInRegistries.ITEM.getKey(s.getItem()).toString();return VALUES.getOrDefault(item,Map.of()).getOrDefault(id,d.defaultValue());}
 public static double set(ItemStack s,String id,double v){ItemAttributeDefinition d=req(id);double old=get(s,id),nv=d.clamp(v);String item=BuiltInRegistries.ITEM.getKey(s.getItem()).toString();VALUES.computeIfAbsent(item,x->new LinkedHashMap<>()).put(id,nv);for(var l:LISTENERS)l.onChanged(s.getItem(),d,old,nv);return nv;}
 public static double reset(ItemStack s,String id){ItemAttributeDefinition d=req(id);double old=get(s,id);String item=BuiltInRegistries.ITEM.getKey(s.getItem()).toString();Map<String,Double> m=VALUES.get(item);if(m!=null){m.remove(id);if(m.isEmpty())VALUES.remove(item);}for(var l:LISTENERS)l.onChanged(s.getItem(),d,old,d.defaultValue());return d.defaultValue();}
 public static Map<String,Map<String,Double>> valueSnapshot(){Map<String,Map<String,Double>> c=new LinkedHashMap<>();VALUES.forEach((k,v)->c.put(k,Map.copyOf(v)));return Collections.unmodifiableMap(c);}
 public static void loadValue(String item,String id,double v){ItemAttributeDefinition d=DEFINITIONS.get(id);if(d!=null)VALUES.computeIfAbsent(item,x->new LinkedHashMap<>()).put(id,d.clamp(v));}
 private static ItemAttributeDefinition req(String id){ItemAttributeDefinition d=DEFINITIONS.get(id);if(d==null)throw new IllegalArgumentException("Unknown item attribute: "+id);return d;}
}