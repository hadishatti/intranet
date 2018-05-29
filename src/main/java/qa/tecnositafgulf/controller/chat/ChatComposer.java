package qa.tecnositafgulf.controller.chat;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.chat.ChatRoom;
import qa.tecnositafgulf.model.chat.ChatUser;
import qa.tecnositafgulf.model.chat.Message;
import qa.tecnositafgulf.model.chat.MessageBoard;
import qa.tecnositafgulf.service.ChatRoomService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by hadi on 1/24/18.
 */
public class ChatComposer extends GenericForwardComposer {
    // auto-wired components
    private Window win;
    private Textbox nameTb;
    private Textbox msgTb;
    private Grid loginGrid;
    private Hbox inputHb;
    private Grid chatGrid;
    private Checkbox IMCb;
    private Div infoDiv;
    private Rows rows;
    private Image newpmImg;
    private Hbox userInfoHb;
    private Label IMLbl;

    // other instance variables
    private ChatRoom _chatRoom;
    private MessageBoard _msgBoard;
    private ChatUser _chatUser;
    private ChatRoomService chatRoomService;
    private String _nickname;
    private boolean _IMEnabled;
    private MessageDlg _msgDlg;
    private int _noOfUsers;
    private int _noOfIMs;
    private Session session;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        session = Sessions.getCurrent();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        chatRoomService = context.getBean(ChatRoomService.class);
        init();
        login();
    }
    
    public void init() {
        _noOfIMs = 0;
        _noOfUsers = 0;
        //if MessageBoard is already in the session
        if (session.getAttribute("msgBoard") != null) {
            if(!desktop.isServerPushEnabled())
                desktop.enableServerPush(true);
            //get MessageBoard and initialize a new chat user, replacing the old one
            _msgBoard = (MessageBoard) session.getAttribute(
                    "msgBoard");
            ArrayList<Message> msgs = _msgBoard.getMessages();
            _chatUser = _msgBoard.getChatUser();
            _IMEnabled = _chatUser.isIMEnabled();
            _chatRoom = _msgBoard.getChatRoom();
            _nickname = _chatUser.getNickname();
            _chatRoom.remove(_nickname);
            _chatUser = new ChatUser(_chatRoom, _nickname, desktop, _IMEnabled);
            _chatUser.setCreatedAt(new Date());
            _chatUser.setOwnChat(Boolean.FALSE);
            _chatUser.setUserActive(Boolean.TRUE);
            _chatUser.start();
            chatRoomService.saveChatUser(_chatUser);

            //refresh UI
            displayChatGrid();
            refreshChatGrid(msgs);
        }


    }

    /********************** Handlers for custom events ********************************/

    /**
     * Handles the event fired when a message is broadcasted from another chat user.
     * @param event
     * @throws InterruptedException
     */
    public void onBroadcast(Event event) throws InterruptedException {
        HashMap<String, Message> hm = (HashMap<String, Message>) event.getData();
        final Message msg = hm.get("msg");
        //if a user is entering or leaving chatroom
        if(msg.isNotify()){
            displayUserInfo(_chatRoom.getChatUsers());
        }
        // if is private message
        if (msg.getRecipient().compareTo(_nickname) == 0) {
            _noOfIMs++;
            String s = "";
            if(_noOfIMs == 1)
                s= " new instant message";
            else
                s= " new instant messages";
            IMLbl.setValue("You have " +_noOfIMs + s);
            infoDiv.setVisible(true);
            newpmImg.setVisible(true);
            newpmImg.addEventListener("onClick", new EventListener() {
                public void onEvent(Event e) throws Exception {
                    msg.setChatId(_chatRoom.getChatId());
                    msg.setCreatedAt(new Date());
                    msg.setChatUser(_chatUser);
                    _msgDlg = new MessageDlg(msg, _nickname, _chatRoom);
                    _msgDlg.setParent(win);
                    _msgDlg.setRecieve();
                    chatRoomService.saveChatMessage(msg);
                    newpmImg.setVisible(false);
                    infoDiv.setVisible(false);
                    newpmImg.removeEventListener("onClick", this);
                    _noOfIMs = 0;
                }
            });
        } else {
            _msgBoard.setMessage(msg);
            session.setAttribute("msgBoard", _msgBoard);
            appendMessage(msg);
        }
    }

    /**
     * Handles the event fired when an instant message is sent to this chat user.
     * @param event
     * @throws InterruptedException
     */
    public void onIMSend(Event event) throws InterruptedException {
        IMLbl.setValue("Instant message sent successfully");
        infoDiv.setVisible(true);
        Timer timer = (Timer) Path.getComponent("/win/timer");
        timer.start();
    }

    /********************** Component event handlers ********************************/

    public void onOK$win() {
        if (session.getAttribute("msgBoard") != null) {
            sendMsg();
        } else
            login();
    }

    public void onClick$loginBtn() {
        login();
    }

    public void onClose$win(){
        onClick$exitBtn();
    }

    public void onClick$sendBtn() {
        sendMsg();
    }

    public void onClick$exitBtn() {
        // clean up
        _chatUser.setDone();
        session.setAttribute("msgBoard", null);
        _chatRoom.broadcast(new Message(_nickname + " has left the chat room",
                _nickname, true));

        // refresh the UI
        chatGrid.setVisible(false);
        inputHb.setVisible(false);
        closeChatRoom();

    }

    private void closeChatRoom(){
        if(_chatRoom.getChatUsers().size() == 1) {
            chatRoomService.deActivateChatWindow(_chatRoom.getChatId(), Boolean.FALSE);
            _chatRoom = null;
            desktop.getWebApp().setAttribute("chatroom", _chatRoom);
        }

    }

    /********************** Methods ********************************/

    /**
     * Task: Send the message from this chat user. This involves broadcasting the message to other users,
     * updating the MessageBoard in the session, and refreshing this chat user's UI.
     */
    public void sendMsg() {
        // broadcast message to others
        Message msg = new Message(msgTb.getValue(), _nickname);
        msg.setChatId(_chatRoom.getChatId());
        msg.setChatUser(_chatUser);
        msg.setCreatedAt(new Date());

        _chatRoom.broadcast(msg);
        // set msg to MessageBoard
        _msgBoard.setMessage(msg);
        session.setAttribute("msgBoard", _msgBoard);
        chatRoomService.saveChatMessage(msg);
        // refresh UI
        appendMessage(msg);
        displayUserInfo(_chatRoom.getChatUsers());
        msgTb.setRawValue("");
        msgTb.setFocus(true);
        //insert into the chat_user_message table to keep records who send to whom msg, chat id, and user who to and from

    }
    /**
     * Task: Log the chat user in. This involves simple validation, initialization
     */
    public void login() {
        Employee e = ((Employee)session.getAttribute("employee"));
        _nickname = e.getName()+" "+e.getFamilyName();
        _chatRoom = (ChatRoom) desktop.getWebApp().getAttribute("chatroom");
        if (_chatRoom == null) {
            _chatRoom = new ChatRoom();
            _chatRoom.get_chatUsers().clear();
            _chatRoom.getChatUserList().clear();
            desktop.getWebApp().setAttribute("chatroom", _chatRoom);
            //here for the first time chat room is opened means initiator open the chat window
            //As there is no chat room mean no active chat user except initiator
            _chatRoom.setChatActive(Boolean.TRUE);
            _chatRoom.setChatDesc("Chat initiated by user "+_nickname);
            _chatRoom.setCreatedAt(new Date());
            chatRoomService.saveChatRoom(_chatRoom);//save chat room

        }
        //if username is not already being used
        if (_chatRoom.getChatUser(_nickname) == null) {
            //initialize
            if(!desktop.isServerPushEnabled())
                desktop.enableServerPush(true);
            //set IM
            _IMEnabled = true;
            _chatUser = new ChatUser(_chatRoom, _nickname, desktop, _IMEnabled);
            //broadcast
            _chatRoom.broadcast(new Message(_nickname
                    + " has joined this chatroom", _nickname, true));
            _chatUser.start();
            //set the MessageBoard to the session
            _msgBoard = new MessageBoard(_chatUser, _chatRoom);
            session.setAttribute("msgBoard", _msgBoard);
            //New user has joined the chat window need to insert the records in the chat_userTable
            //welcome message
            _chatUser.setChatRoom(_chatRoom);
            _chatUser.set_chatRoom(_chatRoom);
            _chatUser.setCreatedAt(new Date());
            _chatUser.setOwnChat(Boolean.TRUE);
            _chatUser.setUserActive(Boolean.TRUE);
            chatRoomService.saveChatUser(_chatUser);
            Message msg = new Message("Welcome " + _nickname+" in the GBDG Intranet Chat Room", _nickname, true);
            _msgBoard.setMessage(msg);
            //refresh UI
            displayChatGrid();
            appendMessage(msg);
        } /*else {
            alert("This username is already in use. Please choose another.");
        }*/

    }

    /**
     * Task: Display the the main grid used for chatting and hide the login grid.
     */
    private void displayChatGrid() {
        //nameTb.setRawValue("");
        msgTb.setFocus(true);
        loginGrid.setVisible(false);
        chatGrid.setVisible(true);
        inputHb.setVisible(true);
        displayUserInfo(_chatRoom.getChatUsers());
    }

    /**
     * Task: Display the current user information, including number of chat users
     * and their usernames.
     * @param chatUsers
     */
    private void displayUserInfo(List<ChatUser> chatUsers){
        int noOfUsers = chatUsers.size();
        //if the number of users has been modified
        if (noOfUsers != _noOfUsers) {
            _noOfUsers = noOfUsers;
            //refresh the user info div
            if(win.hasFellow("userInfoDiv"))
                win.getFellow("userInfoDiv").detach();
            Div userInfoDiv = new Div();
            userInfoDiv.setParent(userInfoHb);
            userInfoDiv.setId("userInfoDiv");
            String s = "";
            if(noOfUsers == 1)
                s = " person is chatting: ";
            else
                s = " people are chatting: ";
            Label userInfoLbl = new Label(noOfUsers + s);
            userInfoLbl.setParent(userInfoDiv);
            //append each chat user's nickname to the div
            A userA = null;
            Label userL;
            for (int i=0; i < chatUsers.size(); i++){
                final ChatUser cu = (ChatUser)chatUsers.get(i);
                if(!cu.getNickname().equals(_nickname)) {
                    userA = new A();
                    userA.setLabel(cu.getNickname() + " ");
                    userA.addEventListener("onClick", new EventListener<Event>() {
                        public void onEvent(Event event) throws Exception {
                            MessageDlg msgDlg = new MessageDlg(new Message("", cu.getNickname()), _nickname,
                                    _chatRoom);
                            msgDlg.setSend();
                            Window win = (Window) Path.getComponent("/win");
                            msgDlg.setParent(win);
                        }
                    });
                    userA.setStyle("color: #1a4280;");
                    userA.setTooltiptext("Click here to send a personal message");
                    userA.setParent(userInfoDiv);
                }else{
                    userL = new Label(cu.getNickname() + " ");
                    userL.setStyle("color: #1a4280;");
                    userL.setParent(userInfoDiv);
                }
            }
        }
    }

    public ChatRoomService getChatRoomService() {
        return chatRoomService;
    }

    public void setChatRoomService(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    /**
     * Task: Populate the chat grid with messages.
     * @param msgs
     */
    private void refreshChatGrid(ArrayList<Message> msgs) {
        for (int i = 0; i < msgs.size(); i++)
            appendMessage(msgs.get(i));
    }

    /**
     * Append a ChatRow to the chat grid.
     * @param msg
     */
    private void appendMessage(Message msg) {
        ChatRow row = new ChatRow(msg, _chatRoom, _chatUser);
        rows.appendChild(row);
        Clients.scrollIntoView(row);
    }

}