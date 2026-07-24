package me.topishere.anticheat.velocity.detection;

import me.topishere.anticheat.common.ClientType;
import com.velocitypowered.api.proxy.Player;

import java.util.*;

public class ClientDetector {
    private final Map<UUID, ClientType> detectedClients = new HashMap<>();

    public ClientType detectClient(Player player, String userAgent, String resourcePackHash) {
        ClientType detected = ClientType.VANILLA;

        if (userAgent != null && !userAgent.isEmpty()) {
            detected = detectFromBrand(userAgent);
        }

        if (detected == ClientType.VANILLA && resourcePackHash != null && !resourcePackHash.isEmpty()) {
            detected = ClientType.fromResourcePack(resourcePackHash);
        }

        detected = checkModSignatures(player, detected);

        detectedClients.put(player.getUniqueId(), detected);
        return detected;
    }

    private ClientType detectFromBrand(String brand) {
        String lowerBrand = brand.toLowerCase(Locale.ROOT);

        if (lowerBrand.contains("meteor")) return ClientType.METEOR;
        if (lowerBrand.contains("wurst")) return ClientType.WURST;
        if (lowerBrand.contains("impact")) return ClientType.IMPACT;
        if (lowerBrand.contains("future")) return ClientType.FUTURE;
        if (lowerBrand.contains("baritone")) return ClientType.BARITONE;
        if (lowerBrand.contains("aristois")) return ClientType.ARISTOIS;
        if (lowerBrand.contains("minementat")) return ClientType.MINEMENTAT;
        if (lowerBrand.contains("rusherhack")) return ClientType.RUSHERHACK;
        if (lowerBrand.contains("liquidbounce")) return ClientType.LIQUIDBOUNCE;
        if (lowerBrand.contains("novoline")) return ClientType.NOVOLINE;
        if (lowerBrand.contains("skidder")) return ClientType.SKIDDERMOD;
        if (lowerBrand.contains("ares")) return ClientType.ARES;
        if (lowerBrand.contains("bleach")) return ClientType.BLEACHHACK;
        if (lowerBrand.contains("cryptic")) return ClientType.CRYPTIC;
        if (lowerBrand.contains("sigma")) return ClientType.SIGMA;

        if (!lowerBrand.equals("vanilla") && !lowerBrand.contains("forge") && !lowerBrand.contains("fabric")) {
            return ClientType.UNKNOWNMOD;
        }

        return ClientType.VANILLA;
    }

    private ClientType checkModSignatures(Player player, ClientType current) {
        return current;
    }

    public ClientType getDetectedClient(UUID uuid) {
        return detectedClients.getOrDefault(uuid, ClientType.VANILLA);
    }

    public void removePlayer(UUID uuid) {
        detectedClients.remove(uuid);
    }

    public boolean isSuspiciousClient(UUID uuid) {
        ClientType client = getDetectedClient(uuid);
        return client.isSuspicious();
    }
}