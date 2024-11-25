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

    public static void main(String[] args) throws InterruptedException{
        UserService userService = new UserService();
        ChatService chatService = new ChatService();
        MessageService messageService = new MessageService();
        MembersService membersService = new MembersService();


        User user1 = userService.save(new User("user1", "1234567890"));
        User user2 = userService.save(new User("user2", "0987654321"));


        Optional<User> retrievedUser1 = userService.find(user1.getId());
        Optional<User> retrievedUser2 = userService.find(user2.getId());
        if (retrievedUser1.isPresent() && retrievedUser2.isPresent()) {
            LOGGER.info("Users saved successfully.");
        } else {
            LOGGER.error("Failed to save users.");
            return;
        }


        Chat chat = new Chat("group", user1.getId(), "Test Chat", "This is a test chat", false, LocalDateTime.now());
        chat = chatService.save(chat);


        Optional<Chat> retrievedChat = chatService.find(chat.getId());
        if (retrievedChat.isPresent()) {
            LOGGER.info("Chat saved successfully.");
        } else {
            LOGGER.error("Failed to save chat.");
            return;
        }


        Message message = new Message(chat.getId(), user1.getId(), "Hello, World!", LocalDateTime.now(), false, false, null);
        message = messageService.save(message);


        Optional<Message> retrievedMessage = messageService.find(message.getId());
        if (retrievedMessage.isPresent()) {
            LOGGER.info("Message saved successfully.");
        } else {
            LOGGER.error("Failed to save message.");
            return;
        }


        List<Message> messages = messageService.findAll(new PageRequest(0, 10L, new ArrayList<String>()), message);
        if (!messages.isEmpty()) {
            LOGGER.info("Messages retrieved successfully.");
        } else {
            LOGGER.error("Failed to retrieve messages.");
        }


        message.setContent("Hello, Updated World!");
        message = messageService.update(message);


        Optional<Message> updatedMessage = messageService.find(message.getId());
        if (updatedMessage.isPresent() && updatedMessage.get().getContent().equals("Hello, Updated World!")) {
            LOGGER.info("Message updated successfully.");
        } else {
            LOGGER.error("Failed to update message.");
        }


        messageService.delete(message.getId());


        Optional<Message> deletedMessage = messageService.find(message.getId());
        if (deletedMessage.isEmpty()) {
            LOGGER.info("Message deleted successfully.");
        } else {
            LOGGER.error("Failed to delete message.");
        }


        chatService.delete(chat.getId());


        Optional<Chat> deletedChat = chatService.find(chat.getId());
        if (deletedChat.isEmpty()) {
            LOGGER.info("Chat deleted successfully.");
        } else {
            LOGGER.error("Failed to delete chat.");
        }


        userService.delete(user1.getId());
        userService.delete(user2.getId());


        Optional<User> deletedUser1 = userService.find(user1.getId());
        Optional<User> deletedUser2 = userService.find(user2.getId());
        if (deletedUser1.isEmpty() && deletedUser2.isEmpty()) {
            LOGGER.info("Users deleted successfully.");
        } else {
            LOGGER.error("Failed to delete users.");
        }


        Members member = new Members(chat.getId(), user1.getId(), "admin", true, true, true, null, LocalDateTime.now());
        member = membersService.save(member);


//        Optional<Members> retrievedMember = membersService.find(member.getChatId(), member.getUserId());
//        if (retrievedMember.isPresent()) {
//            LOGGER.info("Member saved successfully.");
//        } else {
//            LOGGER.error("Failed to save member.");
//            return;
//        }
//
//
//        member.setRole("member");
//        member = membersService.update(member);
//
//        Optional<Members> updatedMember = membersService.find(member.getChatId(), member.getUserId());
//        if (updatedMember.isPresent() && updatedMember.get().getRole().equals("member")) {
//            LOGGER.info("Member role updated successfully.");
//        } else {
//            LOGGER.error("Failed to update member role.");
//        }
//
//        membersService.delete(member.getChatId(), member.getUserId());
//
//
//        Optional<Members> deletedMember = membersService.find(member.getChatId(), member.getUserId());
//        if (deletedMember.isEmpty()) {
//            LOGGER.info("Member deleted successfully.");
//        } else {
//            LOGGER.error("Failed to delete member.");
//        }
    }
}
