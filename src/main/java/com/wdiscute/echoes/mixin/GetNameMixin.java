package com.wdiscute.echoes.mixin;

import com.wdiscute.echoes.registry.ECDataComponents;
import com.wdiscute.echoes.upgrades.BlacksmithTrade;
import com.wdiscute.libtooltips.Tooltips;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class GetNameMixin
{
    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
    public void getHoverNameMixin(CallbackInfoReturnable<Component> cir)
    {
        ItemStack stack = (ItemStack) (Object) this;

        stack = stack.copy();

        if (stack.has(ECDataComponents.RARITY))
        {
            Component baseName;
            Component customName = stack.get(DataComponents.CUSTOM_NAME);
            Component itemName = stack.get(DataComponents.ITEM_NAME);

            if (customName != null)
            {
                baseName = customName;
            }
            else if (itemName != null)
            {
                baseName = itemName;
            }
            else baseName = Component.translatable(stack.getItem().getDescriptionId());

            BlacksmithTrade.Rarity rarity = stack.getOrDefault(ECDataComponents.RARITY, BlacksmithTrade.Rarity.COMMON);

            //decode name string and return value
            MutableComponent returnValue = Tooltips.resolveTagsToComponent(rarity.wrapWithRarityMarkdownAsString(baseName.getString()));
            cir.setReturnValue(returnValue);
            cir.cancel();
        }
    }
}
