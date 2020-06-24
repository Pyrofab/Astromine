package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyFluidContainer;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.common.widget.WEnergyVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.client.screen.BaseContainerScreen;
import spinnery.common.container.BaseContainer;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

import java.util.Collection;

public abstract class DefaultedEnergyFluidContainerScreen<T extends BaseContainer> extends BaseContainerScreen<T> {
	public WInterface mainInterface;
	public WPanel mainPanel;
	public Collection<WSlot> playerSlots;
	public WEnergyVolumeFractionalVerticalBar energyBar;
	public WFluidVolumeFractionalVerticalBar fluidBar;

	public DefaultedEnergyFluidContainerScreen(Text name, DefaultedEnergyFluidContainer linkedContainer, PlayerEntity player) {
		super(name, (T) linkedContainer, player);

		mainInterface = getInterface();

		mainPanel = mainInterface.createChild(WPanel::new, Position.ORIGIN, Size.of(176, 160));

		mainPanel.center();
		mainPanel.setOnAlign(WAbstractWidget::center);

		playerSlots = WSlot.addPlayerInventory(Position.of(mainPanel, 7, 77, 0), Size.of(18, 18), mainPanel);

		energyBar = mainPanel.createChild(WEnergyVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 7, 0), Size.of(24, 48));

		fluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(energyBar, energyBar.getWidth() + 4, 0, 0), Size.of(energyBar));

		ComponentProvider componentProvider = linkedContainer.blockEntity;

		energyBar.setEnergyVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).getVolume(0));

		fluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(0));
	}
}