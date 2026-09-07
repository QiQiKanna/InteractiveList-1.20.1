# Interactive List 交互列表

一款针对 **Minecraft 1.20.1 / Fabric** 的客户端模组。

它会在屏幕右侧渲染一个**可交互物体列表**，把玩家周围一定范围内的方块和实体全部列出来。你**无需用准星精确瞄准**，只要通过鼠标滚轮或侧键在列表里选择目标，按下交互键即可完成交互——开箱子、拉门、点按钮、跟村民对话、骑上马，统统变得更顺滑。

> 原版 Minecraft 交互必须把准星对准物体，遇到成排的箱子、细小的按钮、贴在墙上的拉杆时常常需要反复调整视角。这个模组的思路是：**把"瞄准"替换为"从列表中选择"**。

---

## ✨ 功能特性

- **自动收集附近可交互物体**：默认以玩家为中心 `5` 格半径，通过 BFS 扩散 + 多点射线检测，找出玩家能实际看到、可交互的方块与实体。
- **就近排序展示**：列表按与玩家的距离从近到远排列，最多同时显示 `4` 项。
- **方块与实体都支持**：
  - 方块：箱子、桶、末影箱、工作台、熔炉、漏斗、发射器、砧、床、门、活板门、按钮、拉杆、告示牌、悬挂告示牌、潜影盒等。
  - 实体：村民；以及玩家当前骑乘的马、驴、骡、船、矿车等。
- **双击格方块自动合并**：双箱、床、门这样的多格方块会合并为一个条目，不会重复出现。
- **纯客户端**、**开箱即用**，无需服务端安装。
- 内置 **中文（zh_cn）** 与 **英文（en_us）** 语言文件。

---

## 🎮 按键绑定

| 按键 | 功能 |
| --- | --- |
| <kbd>鼠标侧键 4</kbd> | 选择下一项 |
| <kbd>鼠标侧键 5</kbd> | 选择上一项 |
| <kbd>Ctrl</kbd> + <kbd>鼠标滚轮</kbd> | 切换选择（按住 Ctrl 时滚轮用于选列表，而非切快捷栏） |
| <kbd>F</kbd> | 与当前选中的物体交互 |

> 按键绑定均可在「选项 → 控制 → 键位绑定」中自定义。

---

## 🧠 工作原理

模组的处理循环大致如下（见 `InteractiveListClient` 的 `ClientTickEvents.END_CLIENT_TICK`）：

1. **收集**——每一游戏刻刷新候选列表：
   - `BlockCollector`：从玩家坐标做 **BFS 扩散**，筛选出可交互方块；再对每个候选方块做**多个偏移点的射线检测**，保证玩家真的能"点"到它。
   - `EntityCollector`：遍历周围世界实体，对交互范围内的实体做碰撞盒射线检测。
2. **排序**——把方块和实体合并，按距离从近到远排序。
3. **渲染**——`InteractiveListHud` 在屏幕右侧（约 60% 宽度处）绘制列表，选中项以 `>` 标记。
4. **交互**——按下交互键后，对选中的方块调用 `interactBlock`、对选中的实体调用 `interactEntity`，并附带 **4 刻交互冷却**防止误触。

### 可交互对象如何定义？

由数据生成（Data Generation）产出的**标签**决定，而非硬编码。修改标签即可增减可交互范围：

- `interactive_unconditional_blocks`（方块标签）：无条件可交互的方块。
- `interactive_unconditional_entities`（实体标签）：无条件可交互的实体（如村民）。
- `rideable_entities`（实体标签）：仅当玩家骑乘时才可交互（马、船、矿车等）。

---

## 🔧 技术栈 / 环境

| 项 | 版本 |
| --- | --- |
| Minecraft | 1.20.1 |
| Fabric Loader | 0.19.3 |
| Fabric API | 0.92.11+1.20.1 |
| 映射（Mappings） | Yarn 1.20.1+build.10 |
| Java | 17 |
| 构建 | Gradle（Fabric Loom 1.17-SNAPSHOT） |
| Mod ID / 分组 | `interactive-list` / `com.qiqikanna.interactivelist` |

---

## 🛠️ 构建与安装

### 构建

项目自带 Gradle Wrapper，无需预先安装 Gradle：

```bash
./gradlew build
```

打包产物位于 `build/libs/`。

### language

在语言文件中可以更改交互列表的文本,格式是
```
"<id>.interactive_list_content":"文本"

例如：
"block.minecraft.chest..interactive_list_content": "Open Chest"
```


### 游玩 / 安装

1. 安装 [Fabric Loader](https://fabricmc.net/use/)（Minecraft 1.20.1）与 [Fabric API](https://modrinth.com/mod/fabric-api)。
2. 将 `build/libs/` 里的 `.jar` 放入 `.minecraft/mods/`。
3. 启动游戏，通过「键位绑定」查看/修改按键，按 <kbd>F</kbd> 或滚轮即可体验。

> 在开发环境直接运行：`./gradlew runClient`。

---

## 📂 项目结构

```
src/main/java/com/qiqikanna/interactivelist
├── InteractiveList.java            # 模组主入口（ModInitializer）
├── InteractiveListClient.java      # 客户端入口：收集/排序/渲染循环与交互逻辑
├── InteractiveListDataGenerator.java # 数据生成入口
├── hud/
│   ├── InteractiveListHud.java     # HUD 渲染、选择与范围/数量配置
│   └── HudEntry.java               # 单个列表条目（内容/距离/命中结果）
├── util/
│   ├── BlockCollector.java         # 方块收集：BFS 扩散 + 射线检测 + 双格方块合并
│   └── EntityCollector.java        # 实体收集：范围 + 碰撞盒射线检测
├── option/ModKeyBindings.java      # 键位绑定注册
├── tag/ModTags.java                # 自定义标签定义
├── mixin/
│   ├── PlayerInventoryMixin.java   # 拦截 Ctrl+滚轮，切换为列表选择
│   └── ExampleMixin.java           # 模板 mixin（示例留档）
└── datagen/                        # 标签与语言文件的数据生成器（含 zh_cn / en_us）
```

---

## 📄 License

本项目基于 [MIT License](LICENSE) 开源。

作者：QiQiKanna（2026）
