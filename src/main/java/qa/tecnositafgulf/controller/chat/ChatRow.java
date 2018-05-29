package qa.tecnositafgulf.controller.chat;

import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;
import qa.tecnositafgulf.model.chat.ChatRoom;
import qa.tecnositafgulf.model.chat.ChatUser;
import qa.tecnositafgulf.model.chat.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hadi on 1/24/18.
 */
public class ChatRow extends Row {

    public ChatRow(final Message msg, final ChatRoom chatRoom, ChatUser chatUser) {
        final String nickname = chatUser.getNickname();
        if (!msg.isNotify()) {
            if (msg.getSender().compareTo(nickname) == 0)
                this.setStyle("background:#9acdf9b3");// light blue
            Hbox msgHb = new Hbox();
            //msgHb.setWidth("218px");
            this.appendChild(msgHb);
            Image img = new Image("/images/chat.png");
            img.setParent(msgHb);
            img.setWidth("40px");
            Vbox vb = new Vbox();
            vb.setParent(msgHb);
            vb.setStyle("padding-top:10px;");
            Label contentLbl = new Label(msg.getContent());
            contentLbl.setStyle("font-weight:bold;");
            vb.appendChild(contentLbl);
            Hbox hb = new Hbox();
            hb.setParent(vb);
            Div div = new Div();
            div.setParent(hb);
            div.setStyle("text-align:right;");
            //if the sender is IM enabled and the sender isn't this chatUser
            if (chatUser.isIMEnabled() && msg.getSender().compareTo(nickname)!=0) {
                Image pmImg = new Image("/images/personal-message.png");
                pmImg.setTooltiptext("Click here to send a personal message");
                pmImg.setParent(div);
                pmImg.setHeight("30px");
                pmImg.addEventListener("onClick", new EventListener() {
                    public void onEvent(Event e) throws Exception {
                        MessageDlg msgDlg = new MessageDlg(msg, nickname,
                                chatRoom);
                        msgDlg.setSend();
                        Window win = (Window) Path.getComponent("/win");
                        msgDlg.setParent(win);
                    }
                });
                Space space = new Space();
                div.appendChild(space);
            }

            Date now = new java.util.Date();
            Long ts = now.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            String time = msg.getSender() + " @ " + sdf.format(ts).toString();
            Label sendertimeLbl = new Label(time);
            div.appendChild(sendertimeLbl);
        } else {
            this.setStyle("background:#1a4280;text-align:center;color:#DCDCDC;");
            Label l = new Label(msg.getContent());
            l.setStyle("color:#ffffff");
            this.appendChild(l);
        }

    }
}