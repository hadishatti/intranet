package qa.tecnositafgulf.model.chat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hadi on 1/24/18.
 */
@Entity
@Table(name = "chatMessage", uniqueConstraints = {@UniqueConstraint(columnNames={"messageId"})})
public class Message implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    @Column(name="chatId", nullable = false)
    private Long chatId;
    @Column(name="created_at", nullable = false)
    private Date createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatUserId", nullable = false, updatable = false)
    private ChatUser chatUser;
    private String _content;
    private String _sender;
    private Boolean _notify;
    private String _recipient;


    public Message(String content, String sender){
        _content = content;
        _sender = sender;
        _notify = false;
        _recipient = "";
    }

    public Message(String content, String sender, String recipient){
        _content = content;
        _sender = sender;
        _notify = false;
        _recipient = recipient;
    }

    public Message(String content, String sender, Boolean notify){
        _content = content;
        _sender = sender;
        _notify = notify;
        _recipient = "";
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public String get_sender() {
        return _sender;
    }

    public void set_sender(String _sender) {
        this._sender = _sender;
    }

    public Boolean get_notify() {
        return _notify;
    }

    public void set_notify(Boolean _notify) {
        this._notify = _notify;
    }

    public String get_recipient() {
        return _recipient;
    }

    public void set_recipient(String _recipient) {
        this._recipient = _recipient;
    }

    public Boolean isNotify() {
        return _notify;
    }

    public String getContent() {
        return _content;
    }


    public String getSender() {
        return _sender;
    }

    public String getRecipient() {
        return _recipient;
    }


}
