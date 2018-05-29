package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.chat.ChatRoomDao;
import qa.tecnositafgulf.model.chat.ChatRoom;
import qa.tecnositafgulf.model.chat.ChatUser;
import qa.tecnositafgulf.model.chat.Message;

import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomDao chatRoomDao;

    @Override
    @Transactional
    public void saveChatRoom(ChatRoom chatRoom) {
        chatRoomDao.saveChatRoom(chatRoom);
    }

    @Override
    @Transactional
    public void saveChatUser(ChatUser chatUser) {
        chatRoomDao.saveChatUSer(chatUser);
    }

    @Override
    @Transactional
    public void saveChatMessage(Message message) {
        chatRoomDao.saveMessages(message);
    }

    @Override
    @Transactional
    public ChatRoom getActiveChat(Long chatId) {
        return chatRoomDao.getActiveChat(chatId);
    }

    @Override
    @Transactional
    public List<ChatUser> getListOfActiveChatUser(Long chatId) {
        return chatRoomDao.getListOfActiveChatUser(chatId);
    }

    public ChatRoomDao getChatRoomDao() {
        return chatRoomDao;
    }

    public void setChatRoomDao(ChatRoomDao chatRoomDao) {
        this.chatRoomDao = chatRoomDao;
    }

    @Override
    @Transactional
    public void deActivateChatWindow(Long chatId, boolean isActiveChat) {
        chatRoomDao.deActivateChatWindow(chatId, isActiveChat);
    }
}
