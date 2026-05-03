# PickupHighlight

**Never lose track of newly picked-up items again — a golden star marks every new item in your hotbar and inventory.**

![PickupHighlight Preview](https://raw.githubusercontent.com/Cukkoo12/pickuphighlight/master/screenshot.png)

## What it does

PickupHighlight adds an animated ✦ star to item slots whenever you pick up something new. Glance at your hotbar and instantly know what changed.

- ✦ **Animated star** on newly picked-up items
- 🎯 **Hotbar + inventory** both supported
- 🔄 **Auto-clears** on hover, inventory close, or hotbar selection
- ⏱️ **Optional timeout** — auto-clear after X seconds
- 🎨 **Configurable color** — gold by default
- ⚙️ **Simple config** — JSON file or in-game Cloth Config screen

## Preview

![Hotbar highlight](https://raw.githubusercontent.com/Cukkoo12/pickuphighlight/master/screenshot.png)

## Configuration

Config file: `.minecraft/config/pickuphighlight.json`

| Option | Default | Description |
|--------|---------|-------------|
| `clearOnHover` | `true` | Remove highlight when hovering over the item |
| `clearOnClose` | `true` | Remove all highlights when closing inventory |
| `clearOnSelect` | `true` | Remove highlight when selecting on hotbar |
| `highlightColor` | `0xFFD700` | Star color (gold by default) |
| `timeoutSeconds` | `0` | Auto-clear after X seconds (0 = never) |

If [Cloth Config](https://modrinth.com/mod/cloth-config) and [Mod Menu](https://modrinth.com/mod/modmenu) are installed, you can configure the mod in-game from the Mods screen.

## Requirements

- Minecraft 26.1.x
- [Fabric Loader](https://fabricmc.net/) ≥ 0.18.5
- [Fabric API](https://modrinth.com/mod/fabric-api)

## Optional

- [Cloth Config](https://modrinth.com/mod/cloth-config) — in-game config screen
- [Mod Menu](https://modrinth.com/mod/modmenu) — access config from the Mods list

## License

MIT — see [LICENSE](LICENSE)
