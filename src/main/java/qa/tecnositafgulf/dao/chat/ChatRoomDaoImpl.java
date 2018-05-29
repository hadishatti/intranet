package qa.tecnositafgulf.dao.chat;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.chat.ChatRoom;
import qa.tecnositafgulf.model.chat.ChatUser;
import qa.tecnositafgulf.model.chat.Message;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ChatRoomDaoImpl implements ChatRoomDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveChatRoom(ChatRoom chatRoom) {
        em.persist(chatRoom);
    }

    @Override
    public void saveChatUSer(ChatUser chatUser) {
        em.persist(chatUser);
    }

    @Override
    public void saveMessages(Message message) {
        em.persist(message);
    }

    @Override
    public ChatRoom getActiveChat(Long chatId) {
        return null;
    }

    @Override
    public List<ChatUser> getListOfActiveChatUser(Long chatId) {
        return null;
    }

    @Override
    public void deActivateChatWindow(Long chatId, boolean isActiveFlag) {
        Query query = em.createQuery(
                "UPDATE ChatRoom c SET isChatActive = 0 where chatId =:id ");
        query.setParameter("id", chatId).executeUpdate();
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
