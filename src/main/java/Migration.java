import com.messranger.config.DataBaseConfig;
import com.messranger.entity.*;
import com.messranger.managers.*;
import com.messranger.managers.model.PageRequest;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

            Chat chat = new Chat(user.getId(),"asas", "asdasd", "asdasdasd","test", "P");

            chatRepository.save(chat);
            Optional<Chat> foundChat = chatRepository.find(chat.getId());
            foundChat.isPresent();
            PageRequest pageRequest1 = new PageRequest(10, 0L, List.of("name ASC"));
            List<Chat> allChats = chatRepository.findAll(pageRequest1);
            List<Chat> selectedChats = chatRepository.findAll(pageRequest1, chat);
            allChats.forEach(id -> dbConfig.getLogger().info("chat: " + id.getName()));
            selectedChats.forEach(id-> dbConfig.getLogger().info("chat: " + id.getId() + " with name: " + id.getName()));

            Message message = new Message(chat.getId(), "HELLO JORA", LocalDate.now(), LocalTime.now(), true);
            messageRepository.save(message);
            //messageRepository.delete(message.getId());
            //Thread.sleep(100000);
        } finally {
            dbConfig.close();
        }
    }
}
