package ru.varino.server.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.models.User;
import ru.varino.server.Server;
import ru.varino.server.commands.Command;
import ru.varino.server.db.service.MovieService;
import ru.varino.server.db.service.UserService;


public class RequestManager {
    private CommandManager commandManager;
    private UserService userService;
    private MovieService movieService;


    public RequestManager(CommandManager commandManager, UserService userService, MovieService movieService) {
        this.commandManager = commandManager;
        this.userService = userService;
        this.movieService = movieService;
    }

    public ResponseEntity process(RequestEntity req) {
        String commandReq = req.getCommand();
        if ("auth".equals(commandReq)) {
            User user = (User) req.getPayload();
            if (userService.findByUsernamePassword(user.getUsername(), user.getPassword()).isPresent()) {
                return ResponseEntity.ok().body("Вы успешно авторизованы");
            } else {
                userService.save(user);
                return ResponseEntity.unauthorized().body("Вы - новый пользователь. Авторизация прошла успешно");
            }
        }
        if (commandReq.isEmpty()) return ResponseEntity.badRequest().body("Введено 0 аргументов");
        Command command = commandManager.getCommand(commandReq);
        if (command == null) {
            return ResponseEntity.badRequest().body("Команда '" + commandReq + "' не найдена, воспользуйтесь help");
        }


        return command.execute(req);
    }
}
