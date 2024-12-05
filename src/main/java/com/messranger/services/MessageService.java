    package com.messranger.services;

    import com.messranger.entity.Message;
    import com.messranger.model.PageRequest;
    import com.messranger.repositories.MessageRepository;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    public class MessageService extends BaseService<Message> {
        private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

        public MessageService() {
            repository = new MessageRepository(dbConfig.getDataSource());
        }

        @Override
        public Message save(Message instance) {
            LOGGER.info("Sending message to chat with ID: {}", instance.getChatId());
            if (instance.getChatId() == null || instance.getChatId().isEmpty()) {
                throw new IllegalArgumentException("Chat ID cannot be null or empty");
            }
            if (instance.getSenderId() == null || instance.getSenderId().isEmpty()) {
                throw new IllegalArgumentException("Sender ID cannot be null or empty");
            }
            if (instance.getContent() == null || instance.getContent().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be null or empty");
            }

            instance.setCreatedAt(LocalDateTime.now());
            instance.setRead(false);
            instance.setDeleted(false);
            return repository.save(instance);
        }

        @Override
        public Message update(Message instance) {
            Message message = repository.find(instance.getId()).orElseThrow();

            if (instance.getChatId() == null || instance.getChatId().isEmpty()) {
                throw new IllegalArgumentException("Chat ID cannot be null or empty");
            }
            if (instance.getSenderId() == null || instance.getSenderId().isEmpty()) {
                throw new IllegalArgumentException("Sender ID cannot be null or empty");
            }
            if (instance.getContent() == null || instance.getContent().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be null or empty");
            }
            if (instance.getCreatedAt() == null) {
                instance.setCreatedAt(LocalDateTime.now());
            }

            message.setId(instance.getId());
            message.setContent(instance.getContent());
            message.setDeleted(instance.isDeleted());
            message.setRead(instance.isRead());
            message.setEditedAt(LocalDateTime.now());
            return repository.update(message);
        }

        public List<Message> getUnreadMessages(String chatId, LocalDateTime lastReadTime) {
            LOGGER.info("Getting unread messages for chat with ID: {}", chatId);
            if (chatId == null || chatId.isEmpty()) {
                throw new IllegalArgumentException("Chat ID cannot be null or empty");
            }
            if (lastReadTime == null) {
                throw new IllegalArgumentException("Last read time cannot be null");
            }
            return repository.findAll(new PageRequest(0, Long.MAX_VALUE, new ArrayList<>()), new Message(chatId, null, null, lastReadTime, false, false, null));
        }

        public void markAsRead(String messageId) {
            LOGGER.info("Marking message with ID: {} as read", messageId);
            if (messageId == null || messageId.isEmpty()) {
                throw new IllegalArgumentException("Message ID cannot be null or empty");
            }
            Message message = repository.find(messageId).orElseThrow(() -> new IllegalArgumentException("Message not found"));
            message.setRead(true);
            repository.update(message);
        }

        public List<Message> findAll(PageRequest pageRequest, Message filter) {
            return repository.findAll(pageRequest, filter);
        }

        public void delete(String id) {
            repository.delete(id);
        }
    }