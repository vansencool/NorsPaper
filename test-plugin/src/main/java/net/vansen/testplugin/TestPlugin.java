package net.vansen.testplugin;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
    }
}
