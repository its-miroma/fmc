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

// #region 1
public class CounterBlockEntityRenderer implements BlockEntityRenderer<CounterBlockEntity, CounterBlockEntityRenderState> {
	// #endregion 1

	private final Font textRenderer;

	// #region 1
	public CounterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		// #endregion 1
		textRenderer = context.font();
		// #region 1
	}

	@Override
	public CounterBlockEntityRenderState createRenderState() {
		return new CounterBlockEntityRenderState();
	}

	@Override
	public void extractRenderState(CounterBlockEntity blockEntity, CounterBlockEntityRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
		// #endregion 1
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
		state.setClicks(blockEntity.getClicks());
		// #region 1
	}

	@Override
	public void submit(CounterBlockEntityRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
		// #endregion 1

		// #region 2
		matrices.pushPose();
		matrices.translate(0.5, 1, 0.5);
		matrices.mulPose(Axis.XP.rotationDegrees(90));
		matrices.scale(1/18f, 1/18f, 1/18f);
		// #endregion 2

		// #region 3
		String text = state.getClicks() + "";
		float width = textRenderer.width(text);

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
		// #endregion 3

		matrices.popPose();

		// #region 1
	}
}
// #endregion 1
