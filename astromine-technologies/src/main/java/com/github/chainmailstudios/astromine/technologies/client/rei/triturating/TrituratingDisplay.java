/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.technologies.client.rei.triturating;

import com.github.chainmailstudios.astromine.technologies.client.rei.AstromineTechnologiesRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.technologies.common.recipe.TrituratingRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.util.ResourceLocation;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class TrituratingDisplay implements RecipeDisplay {
	private final List<List<EntryStack>> inputs;
	private final List<EntryStack> outputs;
	private final int timeRequired;
	private final double energyRequired;
	private final ResourceLocation recipeId;

	public TrituratingDisplay(TrituratingRecipe recipe) {
		this(EntryStack.ofIngredients(recipe.getPreviewInputs()), Collections.singletonList(EntryStack.create(recipe.getOutput())), recipe.getTime(), recipe.getEnergyConsumed(), recipe.getId());
	}

	public TrituratingDisplay(List<List<EntryStack>> inputs, List<EntryStack> outputs, int timeRequired, double energyRequired, ResourceLocation recipeId) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.timeRequired = timeRequired;
		this.energyRequired = energyRequired;
		this.recipeId = recipeId;
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return inputs;
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return outputs;
	}

	public int getTimeRequired() {
		return timeRequired;
	}

	public double getEnergyRequired() {
		return energyRequired;
	}

	@Override
	public ResourceLocation getRecipeCategory() {
		return AstromineTechnologiesRoughlyEnoughItemsPlugin.TRITURATING;
	}

	@Override
	public Optional<ResourceLocation> getRecipeLocation() {
		return Optional.ofNullable(this.recipeId);
	}
}
