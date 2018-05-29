package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.chat.ChatRoom;
import qa.tecnositafgulf.model.chat.ChatUser;
import qa.tecnositafgulf.model.chat.Message;

import java.util.List;

public interface ChatRoomService {

    public void saveChatRoom(ChatRoom chatRoom);
    public void saveChatUser(ChatUser chatUser);
    public void saveChatMessage(Message message);
    public ChatRoom getActiveChat(Long chatId);
    public List<ChatUser> getListOfActiveChatUser(Long chatId);
    public void deActivateChatWindow(Long chatId, boolean isActiveChat);

}
