package qa.tecnositafgulf.model.chat;

import java.util.ArrayList;

/**
 * Created by hadi on 1/24/18.
 */
public class MessageBoard {
    private ChatUser _chatUser;
    private ChatRoom _chatRoom;
    private ArrayList<Message> _msgs;
    public static final int MAX_NO_OF_MSGS = 50;

    public MessageBoard(ChatUser chatUser, ChatRoom chatRoom){
        this._chatUser = chatUser;
        this._chatRoom = chatRoom;
        this._msgs = new ArrayList<Message>();
    }
    public ChatUser getChatUser() {
        return _chatUser;
    }
    public ChatRoom getChatRoom() {
        return _chatRoom;
    }
    public ArrayList<Message> getMessages() {
        return _msgs;
    }
    /**
     * Task: Set a message and remove the first if the number of messages
     * exceeds the limit.
     * @param msg
     */
    public void setMessage(Message msg) {
        if(_msgs.size() >= MAX_NO_OF_MSGS)
            _msgs.remove(0);
        _msgs.add(msg);
    }
}