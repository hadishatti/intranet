package qa.tecnositafgulf.dao.chat;

import qa.tecnositafgulf.model.chat.ChatRoom;
import qa.tecnositafgulf.model.chat.ChatUser;
import qa.tecnositafgulf.model.chat.Message;

import java.util.List;

public interface ChatRoomDao {

    public void saveChatRoom(ChatRoom chatRoom);
    public void saveChatUSer(ChatUser chatUser);
    public void saveMessages(Message message);
    public ChatRoom getActiveChat(Long chatId);
    public List<ChatUser> getListOfActiveChatUser(Long chatId);
    public void deActivateChatWindow(Long chatId, boolean isActiveFlag);
}
