package ru.varino.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.io.Console;
import ru.varino.common.io.StandartConsole;

import ru.varino.common.utility.NetworkManager;
import ru.varino.server.db.DatabaseManager;
import ru.varino.server.db.service.MovieService;
import ru.varino.server.db.service.UserService;
import ru.varino.server.managers.*;
import ru.varino.server.commands.*;
import ru.varino.common.utility.ServerConfiguration;

import java.net.InetSocketAddress;
import java.sql.SQLException;



public class RunServer {
    private static final Logger logger = LoggerFactory.getLogger(RunServer.class.getSimpleName());

    public static void main(String[] args) {
        logger.info("Initialization...");

        Console console = new StandartConsole();



        DatabaseManager databaseManager = null;
        try {
            databaseManager = DatabaseManager.getInstance();
        } catch (SQLException e) {
            logger.error("Ошибка подключения к базе данных: {}", e.getMessage());
            System.exit(0);
        }
        UserService userService = new UserService(databaseManager.getConnection());
        MovieService movieService = new MovieService(databaseManager.getConnection());


        CollectionManager collectionManager = new CollectionManager(movieService);

        collectionManager.load();
        logger.info("Коллекция загружена");


        CommandManager commandManager = new CommandManager();

        commandManager
                .add("show", new Show(collectionManager))
                .add("help", new Help(commandManager))
                .add("info", new Info(collectionManager))
                .add("insert", new Insert(collectionManager))
                .add("update", new Update(collectionManager))
                .add("remove_key", new RemoveKey(collectionManager))
                .add("clear", new Clear(collectionManager))
                .add("exit", new Exit())
                .add("replace_if_greater", new ReplaceIf("greater", collectionManager))
                .add("replace_if_lower", new ReplaceIf("lower", collectionManager))
                .add("remove_lower_key", new RemoveLowerKey(collectionManager))
                .add("average_of_total_box_office", new AverageTotalBoxOffice(collectionManager))
                .add("min_by_director", new MinByDirector(collectionManager))
                .add("count_less_than_genre", new CountLessGenre(collectionManager))
                .add("save", new Save(collectionManager));
        logger.info("Команды загружены");


        RequestManager requestManager = new RequestManager(commandManager, userService, movieService);
        CommandListener commandListener = new CommandListener(collectionManager, console);
        InetSocketAddress address = new InetSocketAddress(ServerConfiguration.HOST, ServerConfiguration.PORT);
        NetworkManager networkManager = new NetworkManagerImpl(address, requestManager);

        Server server = new Server(commandListener, networkManager);
        logger.info("Сервер запущен");

        server.run();
    }


}
