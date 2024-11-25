import com.messranger.entity.Chat;
import com.messranger.entity.Message;
import com.messranger.entity.User;
import com.messranger.model.PageRequest;
import com.messranger.services.ChatService;
import com.messranger.services.MessageService;
import com.messranger.services.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Migration {
    public static void main(String[] args) throws InterruptedException{
        UserService userService = new UserService();
        ChatService chatService = new ChatService();
        MessageService messageService = new MessageService();

        User user1 = new User("user1", "1234567890");
        userService.save(user1);
        User user2 = new User("user2", "0987654321");

        Optional<User> retrievedUser1 = userService.find(user1.getId());
        Optional<User> retrievedUser2 = userService.find(user2.getId());
        if (retrievedUser1.isPresent() && retrievedUser2.isPresent()) {
            System.out.println("Users saved successfully.");
        } else {
            System.out.println("Failed to save users.");
            return;
        }

        Chat chat = new Chat("group", user1.getId(), "Test Chat", "This is a test chat", false, LocalDateTime.now());
        chat = chatService.save(chat);


        Optional<Chat> retrievedChat = chatService.find(chat.getId());
        if (retrievedChat.isPresent()) {
            System.out.println("Chat saved successfully.");
        } else {
            System.out.println("Failed to save chat.");
            return;
        }

        Message message = new Message(chat.getId(), user1.getId(), "Hello, World!", LocalDateTime.now(), false, false, null);
        message = messageService.save(message);

        Optional<Message> retrievedMessage = messageService.find(message.getId());
        if (retrievedMessage.isPresent()) {
            System.out.println("Message saved successfully.");
        } else {
            System.out.println("Failed to save message.");
            return;
        }

        List<String> filter = new ArrayList<>();
        filter.add("name");
        List<Message> messages = messageService.findAll(new PageRequest(0, 10L, filter), message);

        if (!messages.isEmpty()) {
            System.out.println("Messages retrieved successfully.");
        } else {
            System.out.println("Failed to retrieve messages.");
        }

        message.setContent("Hello, Updated World!");
        message = messageService.update(message);

        Optional<Message> updatedMessage = messageService.find(message.getId());
        if (updatedMessage.isPresent() && updatedMessage.get().getContent().equals("Hello, Updated World!")) {
            System.out.println("Message updated successfully.");
        } else {
            System.out.println("Failed to update message.");
        }


        messageService.delete(message.getId());


        Optional<Message> deletedMessage = messageService.find(message.getId());
        if (deletedMessage.isEmpty()) {
            System.out.println("Message deleted successfully.");
        } else {
            System.out.println("Failed to delete message.");
        }


        chatService.delete(chat.getId());


        Optional<Chat> deletedChat = chatService.find(chat.getId());
        if (deletedChat.isEmpty()) {
            System.out.println("Chat deleted successfully.");
        } else {
            System.out.println("Failed to delete chat.");
        }


        userService.delete(user1.getId());
        userService.delete(user2.getId());


        Optional<User> deletedUser1 = userService.find(user1.getId());
        Optional<User> deletedUser2 = userService.find(user2.getId());
        if (deletedUser1.isEmpty() && deletedUser2.isEmpty()) {
            System.out.println("Users deleted successfully.");
        } else {
            System.out.println("Failed to delete users.");
        }
    }
}
