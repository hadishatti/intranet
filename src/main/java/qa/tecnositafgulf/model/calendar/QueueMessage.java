package qa.tecnositafgulf.model.calendar;

import org.zkoss.zk.ui.event.Event;
import qa.tecnositafgulf.model.meetings.Meeting;

public class QueueMessage extends Event {
    private static final long serialVersionUID = 1L;
    private Meeting meeting;

    static public enum Type {
        EDIT, OK, DELETE, CANCEL;
    }

    private Type type;
    private Object data;

    public QueueMessage(Type type) {
        super("onCalendarMessage");
        this.type = type;
    }
    public QueueMessage(Type type, Object data) {
        this(type);
        this.data = data;
    }

    public QueueMessage(Type type, Object data, Meeting meeting) {
        this(type);
        this.data = data;
        this.meeting = meeting;
    }

    public Type getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public Meeting getMeeting() {
        return meeting;
    }
}
