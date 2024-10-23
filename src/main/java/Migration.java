import com.messranger.config.DataBaseConfig;
import com.messranger.entity.*;
import com.messranger.model.PageRequest;
import com.messranger.repositories.ChatRepository;
import com.messranger.repositories.MessageRepository;
import com.messranger.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class Migration {
    public static void main(String[] args) throws InterruptedException{
        DataBaseConfig dbConfig = new DataBaseConfig();

        UserRepository repository = new UserRepository(dbConfig.getDataSource());
        ChatRepository chatRepository = new ChatRepository(dbConfig.getDataSource());
        MessageRepository messageRepository = new MessageRepository(dbConfig.getDataSource());

        try {
            User user = new User("Maxtree", "+375291194560");
            repository.save(user);
            user.setNickname("M4xtree");
            repository.update(user);
            Optional<User> foundUser = repository.find(user.getId());
            foundUser.isPresent();
            PageRequest pageRequest = new PageRequest(10, 0L, List.of("nickname ASC"));
            List<User> allUsers = repository.findAll(pageRequest);
            List<User> selectedUsers = repository.findAll(pageRequest, user);
            allUsers.forEach(id -> dbConfig.getLogger().info("User: "+ id.getNickname()));
            selectedUsers.forEach(id -> dbConfig.getLogger().info("User: " + id.getPhoneNumber() + " " + id.getNickname()));


            //messageRepository.delete(message.getId());
            //Thread.sleep(100000);
        } finally {
            dbConfig.close();
        }
    }
}
