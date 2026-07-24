# TopIsHere Anticheat System

A comprehensive anticheat system for Minecraft networks running Paper servers with Velocity proxy.

## Features

### Detection Capabilities

#### Combat Cheats
- **AutoClicker** - Detects rapid clicking patterns (18+ CPS detection)
- **KillAura** - Detects impossible angles, perfect tracking, and multi-target attacks
- **Reach** - Detects extended hit distances beyond normal 3-4 block range
- **Velocity** - Detects knockback resistance and no-hit exploitation

#### Movement Cheats
- **Speed** - Detects abnormal movement speeds
- **Fly** - Detects extended air time and impossible ascension
- **NoClip** - Detects players inside solid blocks
- **Blink** - Detects teleportation hacks (preparation)

#### Block Interaction Cheats
- **FastPlace** - Detects rapid block placement
- **FastBreak** - Detects rapid block destruction
- **Scaffold** - Detects automatic tower building (expansion)

#### Packet/Timer Cheats
- **Timer** - Detects increased packet rates indicating timer modifications
- **Phase** - Detects phasing through blocks

### Client Detection
Detects known cheat clients on join:
- Meteor Client
- Wurst Client
- Impact Client
- Future Client
- Baritone
- RusherHack
- LiquidBounce
- And 8+ more...

## Installation

### Requirements
- Java 21+
- Paper 1.21.1 or higher
- Velocity 3.3.0 or higher
- Maven (for building)

### Build Instructions

1. Clone the repository:
```bash
git clone https://github.com/TopIsHere/anticheat.git
cd anticheat
```

2. Build with Maven:
```bash
mvn clean package
```

3. The compiled plugins will be in:
- Paper Plugin: `paper-plugin/target/anticheat-paper-1.0.0.jar`
- Velocity Plugin: `velocity-plugin/target/anticheat-velocity-1.0.0.jar`

4. Place the jars in their respective plugin folders:
   - Paper: `plugins/anticheat-paper-1.0.0.jar`
   - Velocity: `plugins/anticheat-velocity-1.0.0.jar`

5. Restart servers and the plugin will generate configuration files.

## Commands

### Admin Commands

#### /anticheat
- `/anticheat reload` - Reload configuration
- `/anticheat ban <player>` - Ban a player for cheating
- `/anticheat unban <player>` - Unban a player
- `/anticheat reset <player>` - Reset violation data
- `/anticheat stats` - Show system statistics

#### /acheck
- `/acheck <player>` - Check a player's violation history and suspicion level

## Configuration

### Paper Plugin

The plugin automatically handles:
- **Movement tracking** - Monitors player movement patterns
- **Combat detection** - Analyzes attack patterns and angles
- **Block interactions** - Tracks placement and breaking speed
- **Automatic punishments** - Issues warnings and kicks based on violation level

### Violation Levels
- **0-20**: Monitored (may indicate lag or false positives)
- **21-50**: Warning (player receives warning message)
- **51-80**: High Risk (potential cheat detected)
- **80+**: Automatic Kick

### Velocity Plugin

The proxy plugin:
- Detects client brand on connection
- Identifies known cheat clients
- Logs suspicious connections
- Provides real-time alerts to admins

## Checks Overview

### AutoClicker Detection
- CPS (Clicks Per Second) monitoring
- Threshold: 18+ CPS (high), 25+ CPS (extreme)
- Detects unrealistic click patterns

### KillAura Detection
- Angle tracking (deviation from player's view)
- Multi-target attack frequency
- Impossible angles (< 2°)

### Reach Detection
- Distance-based verification
- Thresholds: 4 blocks (high), 6 blocks (extreme)
- Event-based tracking

### Speed Detection
- Blocks per second calculation
- On-ground speed verification
- Potion effect compensation

### Fly Detection
- Air time tracking
- Vertical velocity monitoring
- Ascension detection

### Timer Detection
- Packet rate analysis
- Expected: 20 packets/sec
- Threshold: 22+ packets/sec

## Expansion

The system is designed for easy expansion. To add new checks:

1. Create a new class extending `Check`
2. Implement the `check()` method
3. Register it in `CheckManager`

Example:
```java
public class NewCheatCheck extends Check {
    public NewCheatCheck() {
        super("NewCheat");
    }
    
    @Override
    public ViolationRecord check(Player player, PlayerCheckData data) {
        // Your detection logic
        return new ViolationRecord(...);
    }
}
```

## Reporting Issues

Found a false positive or bug? Please open an issue on GitHub with:
- Minecraft version
- Player action when flag occurred
- Violation type and level
- Server logs (if available)

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

All rights reserved. Created by TopIsHere.

## Support

For support:
- GitHub Issues: https://github.com/TopIsHere/anticheat/issues
- Discord: [Your Discord if applicable]

---

**Note**: This anticheat is designed for network security. False positives may occur - always review logs before taking action against players.
