---
title: 创建自定义音效
description: 了解如何通过注册表添加和使用新音效。
authors:
  - JR1811

search: false
---

## 准备音频文件

你的音频文件需要转化为特定格式。 你的音频文件需要转化为特定格式。 OGG Vorbis 是一种用于音频等多媒体数据的开放式容器格式，Minecraft 的声音文件就使用了这种格式。 为了避免 Minecraft 处理声音传播距离的问题，你的音频必须只有单声道 (Mono)。 为了避免 Minecraft 处理声音传播距离的问题，你的音频必须只有单声道 (Mono)。

整理工作 大部分现代 DAW (数字音频工作站) 软件都可以使用这种格式进行导入和导出。 在下面的例子中，我们将使用免费开源软件"[Audacity](https://www.audacityteam.org/)"将音频文件转换成规定的格式，当然其他的 DAW 也可以做到。

![Audacity 中未准备好的音频文件](/assets/develop/sounds/custom_sounds_0.png)

在本例中，[哨声](https://freesound.org/people/strongbot/sounds/568995/) 被作为例子导入 Audacity。 它目前被保存为`.wav`格式的文件，有两个音频通道 (立体声) 。 按照自己的需求编辑音频，并确保使用"音轨头"顶部的下拉元素删除其中一个音频通道。 它目前被保存为`.wav`格式的文件，有两个音频通道 (立体声) 。 按照自己的需求编辑音频，并确保使用"音轨头"顶部的下拉元素删除其中一个音频通道。

![分割立体声轨](/assets/develop/sounds/custom_sounds_1.png)

![删除一个音频通道](/assets/develop/sounds/custom_sounds_2.png)

导出或渲染音频文件时，请确认选择的是 OGG 文件格式。 有些 DAW (如 REAPER) 可能支持多种 OGG 音频层格式。 在这种情况下，选择 OGG Vorbis 即可。 根据注册表项的数量，入口点类可能很快就会变得十分杂乱。 为了避免这种情况，我们可以使用一个新的辅助类。 在这种情况下，选择 OGG Vorbis 即可。

![导出为 OGG 文件](/assets/develop/sounds/custom_sounds_3.png)

另外，音频文件过大会导致模组文件也更大。 如有必要，在编辑和导出文件时适量压缩音频本身，以尽量减小导出的文件大小。 如有必要，在编辑和导出文件时适量压缩音频本身，以尽量减小导出的文件大小。

## 加载音频文件

要在你的模组中使用音频文件，要添加新的 `resources/assets/mod-id/sounds` 目录，并将导出的音频文件 `metal_whistle.ogg` 放入该目录中。

如果 `resources/assets/mod-id/sounds.json` 文件还未生成，继续创建该文件，并将你的音效添加到音效条目中。

@[code lang=json](@/reference/latest/src/main/resources/assets/fabric-docs-reference/sounds.json)

声音字幕 (subtitle) 条目为玩家提供了更多的关于该声音的信息。 声音字幕 (subtitle) 条目为玩家提供了更多的关于该声音的信息。 声音字幕翻译键会在 `resources/assets/mod-id/lang` 目录下的语言文件中用到，如果游戏内字幕设置已打开且正在播放自定义声音，则会显示该翻译键在语言文件内对应的值，如果找不到，那么会直接显示该声音字幕的翻译键。

## 注册自定义音效

要将自定义音效添加到模组里，请在实现了 `ModInitializer` 入口点的类中注册一个 SoundEvent。

```java
Registry.register(Registries.SOUND_EVENT, new Identifier(MOD_ID, "metal_whistle"),
        SoundEvent.of(new Identifier(MOD_ID, "metal_whistle")));
```

## 规范化

根据注册表项的数量，入口点类可能很快就会变得十分杂乱。 为了避免这种情况，我们可以使用一个新的辅助类。

在新创建的辅助类中添加两个新方法： 一个用于注册所有音效，一个用于初始化该类。 之后就可以根据需要，添加新的自定义 `SoundEvent` 常量了。

@[code lang=java transcludeWith=:::1](@/reference/latest/src/main/java/com/example/docs/sound/CustomSounds.java)

如此，在实现了 `ModInitializer` 的入口点类中，只需调用一行即可注册所有的自定义 SoundEvents。

@[code lang=java transcludeWith=:::2](@/reference/latest/src/main/java/com/example/docs/sound/FabricDocsReferenceSounds.java)

## 使用自定义的 SoundEvent

使用辅助类去访问自定义的 SoundEvent。 查看 [播放声音事件（SoundEvent）](./using-sounds) 页面，了解如何播放声音。
