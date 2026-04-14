package einstein.subtle_effects.util;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class SuppliedComponent implements Component {

    private final Supplier<Component> component;

    public SuppliedComponent(boolean memoize, Supplier<Component> component) {
        this.component = memoize ? Suppliers.memoize(component) : component;
    }

    @Override
    public Style getStyle() {
        return component.get().getStyle();
    }

    @Override
    public ComponentContents getContents() {
        return component.get().getContents();
    }

    @Override
    public List<Component> getSiblings() {
        return component.get().getSiblings();
    }

    @Override
    public FormattedCharSequence getVisualOrderText() {
        return component.get().getVisualOrderText();
    }
}
