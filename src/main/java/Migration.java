import com.messranger.config.DataBaseConfig;
import com.messranger.entity.*;
import com.messranger.managers.controllers.UserController;
import com.messranger.managers.model.PageRequest;
import com.messranger.managers.repositories.ChatRepository;
import com.messranger.managers.repositories.MessageRepository;
import com.messranger.managers.repositories.UserRepository;
import com.messranger.managers.services.UserService;

import java.util.List;
import java.util.Optional;

public class Migration {
    public static void main(String[] args) throws InterruptedException {

        DataBaseConfig dbConfig = new DataBaseConfig();

        UserRepository userRepository = new UserRepository(dbConfig.getDataSource());
        UserService userService = new UserService(userRepository);

        UserController userController = new UserController(userService);

        User newUser = userController.createUser("JohnDoe", "+123456789");
        System.out.println("User created: " + newUser.getNickname());

        newUser = userController.updateUser(newUser.getId(), "JohnnyDoe", "+987654321");
        System.out.println("User updated: " + newUser.getNickname());

        Optional<User> user = userController.getUserById(newUser.getId());
        user.ifPresent(u -> System.out.println("User found: " + u.getNickname()));

        List<User> users = userController.getAllUsers(10, 0);
        users.forEach(u -> System.out.println("User: " + u.getNickname()));

        userController.deleteUser(newUser.getId());
        System.out.println("User deleted");
    }
}
