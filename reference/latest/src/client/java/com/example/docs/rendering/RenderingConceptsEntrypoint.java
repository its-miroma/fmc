package com.example.docs.rendering;

import org.joml.Matrix4f;

import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;

import com.example.docs.FabricDocsReference;

public class RenderingConceptsEntrypoint implements ClientModInitializer {
	public float totalTickProgess = 0F;

	@Override
	public void onInitializeClient() {
		// "A Practical Example: Rendering a Triangle Strip"
		// :::1
		HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
			layeredDrawer.addLayer(IdentifiedLayer.of(Identifier.of(FabricDocsReference.MOD_ID, "layer"), hudLayer()));
		});
		// :::1
	}

	private LayeredDrawer.Layer hudLayer() {
		return (drawContext, tickCounter) -> {
			// :::1
			if (false) {
				return;
			}

			// :::2
			MatrixStack matrices = drawContext.getMatrices();

			// Store the total tick delta in a field, so we can use it later.
			totalTickProgess += tickCounter.getTickProgress(true);

			// Push a new matrix onto the stack.
			matrices.push();
			// :::2

			// :::2
			// Scale the matrix by 0.5 to make the triangle smaller and larger over time.
			float scaleAmount = MathHelper.sin(totalTickProgess / 10F) / 2F + 1.5F;

			// Apply the scaling amount to the matrix.
			// We don't need to scale the Z axis since it's on the HUD and 2D.
			matrices.scale(scaleAmount, scaleAmount, 1F);
			// :::2
			matrices.scale(1 / scaleAmount, 1 / scaleAmount, 1F);
			matrices.translate(60f, 60f, 0f);
			// :::3
			// Lerp between 0 and 360 degrees over time.
			float rotationAmount = (float) (totalTickProgess / 50F % 360);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotation(rotationAmount));
			// Shift entire diamond so that it rotates in its center.
			matrices.translate(-20f, -40f, 0f);
			// :::3

			// :::1
			drawContext.draw(vertexConsumerProvider -> {
				VertexConsumer buffer = vertexConsumerProvider.getBuffer(RenderLayer.getGui());
				buffer.vertex(20, 20, 5).color(0xFF414141);
				buffer.vertex(5, 40, 5).color(0xFF000000);
				buffer.vertex(35, 40, 5).color(0xFF000000);
				buffer.vertex(20, 60, 5).color(0xFF414141);
			});
			// :::1
			// :::2

			// ... write to the buffer.

			// Pop our matrix from the stack.
			matrices.pop();
			// :::2
			// :::1
		};
	}
}
