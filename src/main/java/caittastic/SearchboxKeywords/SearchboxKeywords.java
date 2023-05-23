package caittastic.SearchboxKeywords;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

@Mod(SearchboxKeywords.MODID)
public class SearchboxKeywords{
  public static final String MODID = "searchbox_keywords";

  public SearchboxKeywords(){
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
  public class CoreClientEvents{
    public static final Style INVISIBLE_STYLE = Style.EMPTY.withFont(new ResourceLocation(MODID, "invisible"));

    private CoreClientEvents(){}

    @SubscribeEvent
    public static void handleItemTooltipEvent(ItemTooltipEvent event){
      List<Component> tooltip = event.getToolTip();
      ItemStack stack = event.getItemStack();
      String keywordKey = stack.getDescriptionId() + ".keyword";
      if(canLocalize(keywordKey) && tooltip.get(0) instanceof MutableComponent mutable){
        mutable.append(GetComponentAsInvisible(keywordKey));
      }
    }

    @SubscribeEvent
    public static void handleRenderTooltipEvent(RenderTooltipEvent.GatherComponents event){
      if(event.getTooltipElements().isEmpty())
        return;
      event.getTooltipElements().get(0).left().ifPresent((text) -> {
        if(text instanceof MutableComponent mutable)
          mutable.getSiblings().removeIf(string -> string.getStyle().equals(INVISIBLE_STYLE));
      });
    }

    public static MutableComponent GetComponentAsInvisible(String key){
      return (canLocalize(key) ? Component.translatable(key) : Component.literal(key)).withStyle(INVISIBLE_STYLE);
    }

    public static boolean canLocalize(String key){
      return I18n.exists(key);
    }
  }

}
