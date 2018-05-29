package qa.tecnositafgulf.model.calendar;

import org.zkoss.calendar.api.CalendarEvent;
import org.zkoss.calendar.api.RenderContext;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import qa.tecnositafgulf.model.meetings.Meeting;

import java.util.*;

/**
 * Created by hadi on 1/3/18.
 */
public class IntranetCalendarModel extends SimpleCalendarModel {
    private static final long serialVersionUID = 1L;
    List<Meeting> meetings;

    private String filterText = "";

    public IntranetCalendarModel(List<CalendarEvent> calendarEvents) {
        super(calendarEvents);
        meetings = new ArrayList<Meeting>();
    }

    public IntranetCalendarModel(List<CalendarEvent> calendarEvents, List<Meeting> meetings){
        super(calendarEvents);
        this.meetings = meetings;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    @Override
    public List<CalendarEvent> get(Date beginDate, Date endDate, RenderContext rc) {
        List<CalendarEvent> list = new LinkedList<CalendarEvent>();
        long begin = beginDate.getTime();
        long end = endDate.getTime();

        for (Iterator<?> itr = _list.iterator(); itr.hasNext();) {
            Object obj = itr.next();
            CalendarEvent ce = obj instanceof CalendarEvent ? (CalendarEvent)obj : null;

            if(ce == null) break;

            long b = ce.getBeginDate().getTime();
            long e = ce.getEndDate().getTime();
            if (e >= begin && b < end && ce.getContent().toLowerCase().contains(filterText.toLowerCase()))
                list.add(ce);
        }
        return list;
    }

    public boolean add(CalendarEvent e, Meeting meeting) {
        boolean ok = false;
        if(meeting == null) {
            throw new NullPointerException("Meeting cannot be null!");
        } else {
            ok = meetings.add(meeting);
        }
        return super.add(e) && ok;
    }

    public boolean update(CalendarEvent e, Meeting meeting) {
        boolean ok = false;
        if(e == null) {
            throw new NullPointerException("CalendarEvent cannot be null!");
        } else {
            ok = meetings.remove(meeting);
            if(!ok) {
                return false;
            } else {
                ok = meetings.add(meeting);
            }
        }
        return super.update(e) && ok;
    }

    public boolean remove(CalendarEvent e, Meeting meeting) {
        return super.remove(e) && meetings.remove(meeting);
    }

}
