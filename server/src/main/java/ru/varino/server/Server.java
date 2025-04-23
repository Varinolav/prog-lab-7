package ru.varino.server;

import ru.varino.common.utility.NetworkManager;
import ru.varino.server.managers.*;


public class Server implements Runnable {
    private CommandListener commandListener;
    private NetworkManager networkManager;

    public Server(CommandListener commandListener, NetworkManager networkManager) {
        this.commandListener = commandListener;
        this.networkManager = networkManager;
    }

    public void run() {
        Thread listener = commandListener.getListener();
        listener.start();
        networkManager.connect();
        networkManager.startHandling();
    }

}
