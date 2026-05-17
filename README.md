# ✦ PickupHighlight

**Never lose track of newly picked-up items again.** Every new item in your hotbar or inventory gets an animated golden star — plus a count badge showing how many you picked up.

![PickupHighlight Preview](screenshots/preview.gif)

## Features

- ⭐ **Animated star** pulses on every newly picked-up item
- 🔢 **Count badge** shows exactly how many items you picked up (`+1`, `+5`, …)
- 🎯 Works in **hotbar** and **inventory**
- 🔄 Highlights **auto-clear** on hover, inventory close, or hotbar selection
- ⏱️ **Optional timeout** — auto-clear after X seconds
- 🎨 **Configurable color** — gold by default, any hex color works
- ⚙️ **Full config GUI** when used with Cloth Config + Mod Menu

## Configuration

Config file: `.minecraft/config/pickuphighlight.json`

| Option | Default | Description |
|--------|---------|-------------|
| `clearOnHover` | `true` | Remove highlight when hovering over the item |
| `clearOnClose` | `true` | Remove all highlights when closing inventory |
| `clearOnSelect` | `true` | Remove highlight when selecting on hotbar |
| `highlightColor` | `0xFFD700` | Star color (gold by default) |
| `timeoutSeconds` | `0` | Auto-clear after X seconds (`0` = never) |
| `showCount` | `true` | Show count badge next to the star |

If [Cloth Config](https://modrinth.com/mod/cloth-config) and [Mod Menu](https://modrinth.com/mod/modmenu) are installed, you can configure the mod in-game from the Mods screen — no manual file editing needed.

## Building from source

```bash
./gradlew build
```

Built JAR will be in `build/libs/`.

## License

MIT — see [LICENSE](LICENSE)
