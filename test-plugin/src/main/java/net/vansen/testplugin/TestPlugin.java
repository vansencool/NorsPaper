package net.vansen.testplugin;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValueFactory;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

public class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getComponentLogger().info("TestPlugin enabled");
        this.getComponentLogger().info("Trying to register a new command");
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(Commands.literal("test").executes(context -> {
                this.getComponentLogger().info("Command executed");
                return 1;
            }).build(), "Test Command", Collections.singleton("yetanothertest"), "testcommand");
        });
        this.getComponentLogger().info("Trying to read config");
        Config config = ConfigFactory.parseFile(this.getDataFolder().toPath().resolve("config.conf").toFile());
        this.getComponentLogger().info("Config has path: {}", config.hasPath("test"));
        if (config.hasPath("test")) {
            this.getComponentLogger().info(config.getString("test"));
        } else {
            Config newConfig = config.withValue("test", ConfigValueFactory.fromAnyRef("Tests are working!"));
            this.getComponentLogger().info(newConfig.getString("test"));
            String configString = newConfig.root().render(ConfigRenderOptions.defaults());
            try {
                this.getDataFolder().toPath().resolve("config.conf").toFile().getParentFile().mkdirs();
                this.getDataFolder().toPath().resolve("config.conf").toFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (Writer writer = new FileWriter(this.getDataFolder().toPath().resolve("config.conf").toFile())) {
                writer.write(configString);
                writer.flush();
                this.getComponentLogger().info("Config saved!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
