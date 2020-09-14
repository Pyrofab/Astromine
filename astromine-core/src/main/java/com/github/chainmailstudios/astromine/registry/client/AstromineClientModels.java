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

package com.github.chainmailstudios.astromine.registry.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.resources.IResource;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import com.github.chainmailstudios.astromine.AstromineCommon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@OnlyIn(Dist.CLIENT)
public class AstromineClientModels {
	public static final LazyValue<ItemCameraTransforms> ITEM_HANDHELD = new LazyValue<>(() -> {
		try {
			IResource resource = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("minecraft:models/item/handheld.json"));
			InputStream stream = resource.getInputStream();
			ItemCameraTransforms model = BlockModel.fromStream(new BufferedReader(new InputStreamReader(stream))).getTransforms();
			stream.close();
			return model;
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	});

	public static void initialize() {
		ModelLoadingRegistry.INSTANCE.registerAppender((resourceManager, consumer) -> {
			consumer.accept(new ModelResourceLocation(new ResourceLocation(AstromineCommon.MOD_ID, "conveyor_supports"), ""));
		});
	}
}
