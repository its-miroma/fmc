package com.example.docs.rendering.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import com.example.docs.block.entity.custom.CounterBlockEntity;

// #region new-renderer
public class CounterBlockEntityRenderer implements BlockEntityRenderer<CounterBlockEntity, CounterBlockEntityRenderState> {
	// #endregion new-renderer

	private final Font font;

	// #region new-renderer
	public CounterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		// #endregion new-renderer
		font = context.font();
		// #region new-renderer
	}

	// #region create-render-state
	@Override
	public CounterBlockEntityRenderState createRenderState() {
		return new CounterBlockEntityRenderState();
	}
	// #endregion create-render-state

	// #region extract-render-state
	@Override
	public void extractRenderState(CounterBlockEntity blockEntity, CounterBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
		// #endregion new-renderer
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
		state.setClicks(blockEntity.getClicks());
		// #region new-renderer
	}
	// #endregion extract-render-state

	@Override
	public void submit(CounterBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
		// #endregion new-renderer

		// #region transformation
		matrices.pushPose();
		// #region translate
		matrices.translate(0.5, 1, 0.5);
		// #endregion translate
		// #region rotate
		matrices.mulPose(Axis.XP.rotationDegrees(90));
		// #endregion rotate
		// #region scale
		matrices.scale(1/18f, 1/18f, 1/18f);
		// #endregion scale
		// #endregion transformation

		// #region draw-text
		String text = state.getClicks() + "";
		float width = font.width(text);

		// draw the text. params:
		// text, x, y, color, ordered text, shadow, text layer type, light, color, background color, outline color
		queue.submitText(
				matrices,
				-width / 2, -4f,
				Component.literal(text).getVisualOrderText(),
				false,
				Font.DisplayMode.SEE_THROUGH,
				state.lightCoords,
				0xffffffff,
				0,
				0
		);
		// #endregion draw-text

		matrices.popPose();

		// #region new-renderer
	}
}
// #endregion new-renderer
