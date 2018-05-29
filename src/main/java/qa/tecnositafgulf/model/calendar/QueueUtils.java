package qa.tecnositafgulf.model.calendar;

import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;

/**
 * Created by hadi on 1/3/18.
 */
public class QueueUtils {
    public static final String QUEUE_NAME = "calendarEvent";

    //look up the desktop queue to communicate with another ui controller
    public static EventQueue<QueueMessage> lookupQueue(){
        EventQueue<QueueMessage> queue = EventQueues.lookup(QUEUE_NAME, EventQueues.DESKTOP, true);
        return queue;
    }
}
