package qa.tecnositafgulf.model.chat;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by hadi on 1/24/18.
 */
@Entity
@Table(name = "chatRoom", uniqueConstraints = {@UniqueConstraint(columnNames={"chatId"})})
public class ChatRoom implements Serializable {

    @Id
    @Column(name="chatId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @Column(name = "chatDesc")
    private String chatDesc;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "isChatActive")
    private boolean isChatActive;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "chatUserId",
            orphanRemoval = true)
    private List<ChatUser> chatUserList;

    @Transient
    private List<ChatUser> _chatUsers;

    public ChatRoom() {
        _chatUsers = new ArrayList<ChatUser>();
        chatUserList = new ArrayList<ChatUser>();
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getChatDesc() {
        return chatDesc;
    }

    public void setChatDesc(String chatDesc) {
        this.chatDesc = chatDesc;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isChatActive() {
        return isChatActive;
    }

    public void setChatActive(boolean chatActive) {
        isChatActive = chatActive;
    }

    public List<ChatUser> get_chatUsers() {
        return _chatUsers;
    }

    public void set_chatUsers(List<ChatUser> _chatUsers) {
        this._chatUsers = _chatUsers;
    }

    /**
     * Task: Send messages to all chatUsers except sender.
     * @param msg
     */
    public void broadcast(Message msg) {
        // if message is not private
        if (msg.getRecipient() == "") {
            synchronized (_chatUsers) {
                for (ChatUser chatUser : _chatUsers)
                    if (chatUser.getNickname().compareTo(msg.getSender()) != 0) {
                        chatUser.addMessage(msg);
                    }
            }
        } else {
            ChatUser chatUser = getChatUser(msg.getRecipient());
            if (chatUser != null) {
                chatUser.addMessage(msg);
                Events.postEvent(new Event("onIMSend", null, null));
            }
        }
    }

    /**
     * Add a chat user.
     * @param chatUser
     */
    public void add(ChatUser chatUser) {
        synchronized (_chatUsers) {
            _chatUsers.add(chatUser);

        }
        synchronized (chatUserList){
            this.chatUserList.add(chatUser);
        }
    }

    /**
     * Task: Remove a chat user.
     * @param chatUser
     */
    public void remove(ChatUser chatUser) {
        synchronized (_chatUsers) {
            _chatUsers.remove(chatUser);
        }
        synchronized (chatUserList){
            chatUserList.remove(chatUser);
        }
    }

    /**
     * Task: Remove a chatUser with a given nickname.
     * @param nickname
     */
    public void remove(String nickname) {
        synchronized (_chatUsers) {
            _chatUsers.remove(getChatUser(nickname));
        }
        synchronized (chatUserList){
            chatUserList.remove(nickname);
        }
    }

    /**
     * Task: Get a chat user with a given nickname.
     * @param nickname
     * @return
     */
    public ChatUser getChatUser(String nickname) {
        ChatUser cu = null;
        synchronized (_chatUsers) {
            for (ChatUser chatUser : _chatUsers)
                if (chatUser.getNickname().compareTo(nickname) == 0) {
                    cu = chatUser;
                    break;
                }
        }
        return cu;
    }

    /**
     * Task: Get a list of chat users.
     * @return _chatUsers
     */
    public List<ChatUser> getChatUsers() {
        return _chatUsers;
    }

    public List<ChatUser> getChatUserList() {
        return chatUserList;
    }

    public void setChatUserList(List<ChatUser> chatUserList) {
        this.chatUserList = chatUserList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(getChatId(), chatRoom.getChatId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getChatId());
    }
}
