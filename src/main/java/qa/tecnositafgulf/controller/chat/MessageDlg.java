package qa.tecnositafgulf.controller.chat;

import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;
import qa.tecnositafgulf.model.chat.ChatRoom;
import qa.tecnositafgulf.model.chat.Message;

/**
 * Created by hadi on 1/24/18.
 */
public class MessageDlg extends Window {
    private Message _msg;
    private Rows rows;
    private String _nickname;
    private ChatRoom _chatRoom;

    public MessageDlg(Message msg, String nickname, ChatRoom chatRoom)
            throws InterruptedException {
        _msg = msg;
        _nickname = nickname;
        _chatRoom = chatRoom;
        this.setClosable(true);
        this.setPosition("top,center");
        this.setMode("overlapped");
        this.setWidth("30%");
        this.setStyle(".z-window-header{color:#1a4280;}");
        Grid grid = new Grid();
        grid.setWidth("100%");
        grid.setParent(this);
        // define columns
        final Columns columns = new Columns();
        columns.setSizable(false);
        columns.setParent(grid);
        Column clm = new Column();
        clm.setWidth("100%");
        clm.setParent(columns);
        // set rows
        rows = new Rows();
        rows.setParent(grid);
    }

    /**
     * Task: Configure the dialog to recieve a message.
     */
    public void setRecieve() {
        this.setTitle("Instant message from " + _msg.getSender());
        this.setStyle(".z-window-header{font-weight:bold;color:#1a4280;}");
        Row row = new Row();
        row.setParent(rows);
        Hbox hb = new Hbox();
        hb.setParent(row);
        Image img = new Image("/images/chat.png");
        img.setParent(hb);
        img.setWidth("80px");
        Div contentDiv = new Div();
        contentDiv.setParent(hb);
        contentDiv.setStyle("margin:15px 10px 10px 0;color:1a4280;");
        Html content = new Html();
        contentDiv.appendChild(content);
        content.setContent(_msg.getContent());

        row = new Row();
        row.setParent(rows);
        final Button replyBtn = new Button("Reply");
        replyBtn.setParent(row);
        replyBtn.addEventListener("onClick", new EventListener() {
            public void onEvent(Event e) throws Exception {
                Window win = (Window) Path.getComponent("/win");
                MessageDlg.this.detach();
                MessageDlg msgDlg = new MessageDlg(_msg, _nickname, _chatRoom);
                msgDlg.setSend();
                msgDlg.setParent(win);
            }
        });
    }

    /**
     * Task: Configure the dialog to send a message.
     */
    public void setSend() {
        this.setTitle("Send an instant message to " + _msg.getSender());
        this.setStyle("color:#1a4280;");
        Row row = new Row();
        final CKeditor ed = new CKeditor();
        ed.setParent(row);
        ed.setCustomConfigurationsPath("/js/ckconfig.js");
        ed.setWidth("100%");
        ed.setHeight("160px");
        row.setParent(rows);
        row = new Row();
        final Button btn = new Button("Send");
        btn.setParent(row);
        row.setParent(rows);
        btn.addEventListener("onClick", new EventListener() {
            public void onEvent(Event e) throws Exception {
                Message im = new Message(ed.getValue(), _nickname, _msg.getSender());
                _chatRoom.broadcast(im);
                MessageDlg.this.detach();
            }
        });

    }
}