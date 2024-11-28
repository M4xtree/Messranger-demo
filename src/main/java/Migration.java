import com.messranger.entity.Chat;
import com.messranger.entity.Message;
import com.messranger.entity.User;
import com.messranger.entity.Members;
import com.messranger.model.PageRequest;
import com.messranger.services.ChatService;
import com.messranger.services.MessageService;
import com.messranger.services.UserService;
import com.messranger.services.MembersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Migration {
    private static final Logger LOGGER = LoggerFactory.getLogger(Migration.class);

    public static void main(String[] args) throws InterruptedException {
        UserService userService = new UserService();
        ChatService chatService = new ChatService();
        MessageService messageService = new MessageService();
        MembersService membersService = new MembersService();

        User user1 = userService.save(new User("user1", "1234567890"));
        User user2 = userService.save(new User("user2", "0987654321"));

        Optional<User> retrievedUser1 = userService.find(user1.getId());
        Optional<User> retrievedUser2 = userService.find(user2.getId());
        if (retrievedUser1.isPresent() && retrievedUser2.isPresent()) {
            LOGGER.info("Пользователи успешно сохранены.");
        } else {
            LOGGER.error("Не удалось сохранить пользователей.");
            return;
        }

        Chat chat = new Chat("group", user1.getId(), "Test Chat", "This is a test chat", false, LocalDateTime.now());
        chat = chatService.save(chat);

        Optional<Chat> retrievedChat = chatService.find(chat.getId());
        if (retrievedChat.isPresent()) {
            LOGGER.info("Чат успешно сохранен.");
        } else {
            LOGGER.error("Не удалось сохранить чат.");
            return;
        }

        Members member1 = new Members(chat.getId(), user1.getId(), "member", false, false, false, null, LocalDateTime.now());
        Members member2 = new Members(chat.getId(), user2.getId(), "member", false, false, false, null, LocalDateTime.now());
        membersService.save(member1);
        membersService.save(member2);

        Message message = new Message(chat.getId(), user1.getId(), "Hello, World!", LocalDateTime.now(), false, false, null);
        message = messageService.save(message);

        Optional<Message> retrievedMessage = messageService.find(message.getId());
        if (retrievedMessage.isPresent()) {
            LOGGER.info("Сообщение успешно сохранено.");
        } else {
            LOGGER.error("Не удалось сохранить сообщение.");
            return;
        }

        List<Message> unreadMessages = messageService.getUnreadMessages(chat.getId(), LocalDateTime.now().minusDays(1));
        if (!unreadMessages.isEmpty()) {
            LOGGER.info("Непрочитанные сообщения успешно получены.");
            unreadMessages.forEach(msg -> messageService.markAsRead(msg.getId()));
        } else {
            LOGGER.error("Не удалось получить непрочитанные сообщения.");
        }

        membersService.promoteToAdmin(chat.getId(), user1.getId());
        membersService.demoteToMember(chat.getId(), user2.getId());

        membersService.delete(chat.getId(), user2.getId());

        chatService.delete(chat.getId());

        userService.delete(user1.getId());
        userService.delete(user2.getId());
    }
}
