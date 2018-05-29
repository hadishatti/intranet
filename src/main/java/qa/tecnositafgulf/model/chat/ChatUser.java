package qa.tecnositafgulf.model.chat;

import org.zkoss.lang.Threads;
import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by hadi on 1/24/18.
 */
@Entity
@Table(name = "chatUser", uniqueConstraints = {@UniqueConstraint(columnNames={"chatUserId"})})
public class ChatUser extends Thread implements Serializable{
    private static final Log log = Log.lookup(ChatUser.class);
    @Id
    @Column(name="chatUserId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatUserId;
    @Column(name = "isActiveUser")
    private boolean isUserActive;
    @Column(name = "isOwnChat")
    private boolean isOwnChat;

    @Column(name = "createdAt")
    private Date createdAt;
    private boolean _ceased;

    @ManyToOne
    @JoinColumn(name = "chatId",
            nullable = false, updatable = false)
    private ChatRoom chatRoom;

    @Transient
    private final Desktop _desktop;
    @Transient
    private ChatRoom _chatRoom;
    private String _nickname;
    private Message _msg;
    private Boolean _IMEnabled;


    public ChatUser(ChatRoom chatRoom, String nickname, Desktop desktop, Boolean IMEnabled) {
        _chatRoom = chatRoom;
        this.chatRoom = chatRoom;
        _nickname = nickname;
        _desktop = desktop;
        _msg = null;
        _IMEnabled = IMEnabled;
        _chatRoom.add(this);

    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
    
    public static Log getLog() {
        return log;
    }

    public Long getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(Long chatUserId) {
        this.chatUserId = chatUserId;
    }

    public boolean isUserActive() {
        return isUserActive;
    }

    public void setUserActive(boolean userActive) {
        isUserActive = userActive;
    }

    public boolean isOwnChat() {
        return isOwnChat;
    }

    public void setOwnChat(boolean ownChat) {
        isOwnChat = ownChat;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean is_ceased() {
        return _ceased;
    }

    public void set_ceased(boolean _ceased) {
        this._ceased = _ceased;
    }

    public ChatRoom get_chatRoom() {
        return _chatRoom;
    }

    public void set_chatRoom(ChatRoom _chatRoom) {
        this._chatRoom = _chatRoom;
    }

    public Desktop get_desktop() {
        return _desktop;
    }

    public String get_nickname() {
        return _nickname;
    }

    public void set_nickname(String _nickname) {
        this._nickname = _nickname;
    }

    public Message get_msg() {
        return _msg;
    }

    public void set_msg(Message _msg) {
        this._msg = _msg;
    }

    public Boolean get_IMEnabled() {
        return _IMEnabled;
    }

    public void set_IMEnabled(Boolean _IMEnabled) {
        this._IMEnabled = _IMEnabled;
    }

    /**
     * Send new messages to UI if necessary.
     */
    public void run() {
        if (!_desktop.isServerPushEnabled())
            _desktop.enableServerPush(true);
        log.info("Active chatUser thread: " + getName());
        try {
            while (!_ceased) {
                try {
                    if (_msg == null) {
                        Threads.sleep(500);// Update each 0.5 seconds
                    } else {
                        Executions.activate(_desktop);
                        try {
                            process();
                        } finally {
                            Executions.deactivate(_desktop);
                        }
                    }
                } catch (DesktopUnavailableException ex) {
                    log.info("Browser exited.");
                    cleanUp();
                } catch (Throwable ex) {
                    log.error(ex);
                    throw UiException.Aide.wrap(ex);
                }
            }
        } finally {
            cleanUp();
        }
        log.info("chatUser thread ceased: " + getName() );
    }

    /**
     * Task: If there is a new message for the chat user, post a new "onBroadcast" event with
     * the message passed in.
     * @throws Exception
     */
    private void process() throws Exception {
        if (_msg != null) {
            log.info("processing message: "+_msg.getContent());
            HashMap<String, Message> msgs = new HashMap<String, Message>();
            msgs.put("msg",_msg);
            Events.postEvent(new Event("onBroadcast", null, msgs));
            _msg = null;//reset message
        }
    }


    /**
     * Task: Clean up before stopping thread.
     */
    public void cleanUp(){
        log.info(getNickname() + " has logged out of the chatroom!");
        _chatRoom.remove(this);
        setDone();
    }

    /**
     * Task: Stop this thread.
     */
    public void setDone() {
        _ceased = true;
    }

    /**
     * Task: Set message for this chatUser.
     * @param message
     */
    public void addMessage(Message message) {
        _msg = message;
    }

    /**
     * return sender's name
     *
     * @return
     */
    public String getNickname() {
        return _nickname;
    }

    public Boolean isIMEnabled() {
        return _IMEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUser chatUser = (ChatUser) o;
        return Objects.equals(getChatUserId(), chatUser.getChatUserId()) &&
                Objects.equals(get_chatRoom(), chatUser.get_chatRoom());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getChatUserId(), get_chatRoom());
    }
}