package ru.meproject.distributify.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import ru.meproject.distributify.api.BuildConstants;

import java.nio.file.Path;

@Plugin(
        id = "distributify",
        name = "Distributify",
        version = BuildConstants.DISTRIBUTIFY_VERSION,
        description = "API for mocking Java structures with underlying storages",
        url = "",
        authors = {
                "",
        }
)
public class Distributify {
    private Logger logger;
    private ProxyServer proxyServer;

    @Inject
    public Distributify(Logger logger, ProxyServer proxyServer, @DataDirectory Path dataDirectory) {
        this.logger = logger;
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // TODO: what to do anyway? load libraries through classpath perhaps?
    }
}
