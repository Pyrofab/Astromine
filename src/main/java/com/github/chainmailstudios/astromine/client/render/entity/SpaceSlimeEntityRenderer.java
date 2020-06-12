package com.github.chainmailstudios.astromine.client.render.entity;

import com.github.chainmailstudios.astromine.client.model.SpaceSlimeEntityModel;
import com.github.chainmailstudios.astromine.common.entity.SpaceSlimeEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SpaceSlimeEntityRenderer extends MobEntityRenderer<SpaceSlimeEntity, SpaceSlimeEntityModel> {

    private static final Identifier TEXTURE = new Identifier("astromine", "textures/entity/space_slime/space_slime.png");

    public SpaceSlimeEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new SpaceSlimeEntityModel(16), 0.25F);
        this.addFeature(new SlimeOverlayFeatureRenderer(this));
    }

    @Override
    public void render(SpaceSlimeEntity slimeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25F * (float) slimeEntity.getSize();
        super.render(slimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public void scale(SpaceSlimeEntity slimeEntity, MatrixStack matrixStack, float f) {
        float scale = 0.999F;
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0.0D, -0.125D, 0.0D);

        float slimeSize = (float) slimeEntity.getSize();
        float i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (slimeSize * 0.5F + 1.0F);
        float j = 1.0F / (i + 1.0F);

        matrixStack.scale(j * slimeSize, 1.0F / j * slimeSize, j * slimeSize);
    }

    @Override
    public Identifier getTexture(SpaceSlimeEntity slimeEntity) {
        return TEXTURE;
    }
}
