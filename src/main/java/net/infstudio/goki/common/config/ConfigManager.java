package net.infstudio.goki.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ConfigManager {
    public static Gson GSON = new GsonBuilder().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).setPrettyPrinting().create();
    public static ConfigManager INSTANCE;

    private final Path configPath;
    private final Map<String, Object> configMap = new HashMap<>();
    private final Map<String, Type> typeMap = new HashMap<>();

    public ConfigManager(Path configPath) {
        this.configPath = configPath;
        INSTANCE = this;

        if (!Files.isDirectory(configPath) || Files.notExists(configPath)) {
            try {
                Files.createDirectories(configPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    @Nullable
    public <T> T getConfig(String key) {
        return (T) configMap.get(key);
    }

    public <T> T getOrCreateConfig(String key, T instance) {
        if (!typeMap.containsKey(key))
            typeMap.put(key, instance.getClass());

        if (configMap.containsKey(key))
            return getConfig(key);
        else {
            createConfig(key, instance);
            return instance;
        }
    }

    public void createConfig(String key, Object instance) {
        configMap.put(key, instance);
        typeMap.put(key, instance.getClass());
        saveConfig(key);
        System.out.println("Created config for " + key);
    }

    public void saveConfig(String key) {
        try {
            Files.write(configPath.resolve(key + ".json"), GSON.toJson(this.<Object>getConfig(key)).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(String key) {
        try {
            byte[] bytes = Files.readAllBytes(configPath.resolve(key + ".json"));
            configMap.put(key, GSON.fromJson(new String(bytes, 0, bytes.length, StandardCharsets.UTF_8), typeMap.get(key)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerConfig(String key, Type type) {
        typeMap.put(key, type);
    }

    public void reloadConfig(String key) {
        if (Files.notExists(configPath.resolve(key + ".json"))) {
            saveConfig(key);
            return;
        }
        loadConfig(key);
        saveConfig(key);
    }

    public void reloadConfig() {
        try {
            Files.list(configPath).forEach(path -> {
                String key = FilenameUtils.getBaseName(path.getFileName().toString());
                if (configMap.containsKey(key)) reloadConfig(key);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasConfig(String key) {
        return Files.exists(configPath.resolve(key + ".json"));
    }
}
