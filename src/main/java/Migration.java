import com.messranger.entity.Chat;
import com.messranger.entity.Message;
import com.messranger.entity.User;
import com.messranger.entity.Members;
import com.messranger.model.PageRequest;
import com.messranger.services.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Migration {
    private static final Logger LOGGER = LoggerFactory.getLogger(Migration.class);

    public static void main(String[] args) throws InterruptedException {
        ChatService chatService = new ChatService();

        User user1 = chatService.getUserService().save(new User("user1", "1234567890"));
        User user2 = chatService.getUserService().save(new User("user2", "0987654321"));

        Optional<User> retrievedUser1 = chatService.getUserService().find(user1.getId());
        Optional<User> retrievedUser2 = chatService.getUserService().find(user2.getId());
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
        chatService.getMembersService().save(member1);
        chatService.getMembersService().save(member2);

        Message message = new Message(chat.getId(), user1.getId(), "Hello, World!", LocalDateTime.now(), false, false, null);
        message = chatService.getMessageService().save(message);

        Optional<Message> retrievedMessage = chatService.getMessageService().find(message.getId());
        if (retrievedMessage.isPresent()) {
            LOGGER.info("Сообщение успешно сохранено.");
        } else {
            LOGGER.error("Не удалось сохранить сообщение.");
            return;
        }

        List<Message> unreadMessages = chatService.getMessageService().getUnreadMessages(chat.getId(), LocalDateTime.now().minusDays(1));
        if (!unreadMessages.isEmpty()) {
            LOGGER.info("Непрочитанные сообщения успешно получены.");
            unreadMessages.forEach(msg -> chatService.getMessageService().markAsRead(msg.getId()));
        } else {
            LOGGER.error("Не удалось получить непрочитанные сообщения.");
        }

        chatService.getMembersService().promoteToAdmin(chat.getId(), user1.getId());
        chatService.getMembersService().demoteToMember(chat.getId(), user2.getId());

        chatService.getMembersService().delete(chat.getId(), user2.getId());

        chatService.delete(chat.getId());

        chatService.getUserService().delete(user1.getId());
        chatService.getUserService().delete(user2.getId());
    }
}
