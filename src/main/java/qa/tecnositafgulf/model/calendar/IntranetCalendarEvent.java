package qa.tecnositafgulf.model.calendar;

import org.zkoss.bind.annotation.Immutable;
import org.zkoss.calendar.impl.SimpleCalendarEvent;

import java.util.Date;

/**
 * Created by hadi on 1/3/18.
 */
public class IntranetCalendarEvent extends SimpleCalendarEvent {
    private static final long serialVersionUID = 1L;

    public IntranetCalendarEvent(Date beginDate, Date endDate, String headerColor, String contentColor, String content) {
        setHeaderColor(headerColor);
        setContentColor(contentColor);
        setContent(content);
        setBeginDate(beginDate);
        getBeginDate().setTime(getBeginDate().getTime());
        setEndDate(endDate);
        getEndDate().setTime(getEndDate().getTime());

    }

    public IntranetCalendarEvent(Date beginDate, Date endDate, String headerColor, String contentColor, String content,
                             String title) {
        setHeaderColor(headerColor);
        setContentColor(contentColor);
        setContent(content);
        setTitle(title);
        setBeginDate(beginDate);
        getBeginDate().setTime(getBeginDate().getTime());
        setEndDate(endDate);
        getEndDate().setTime(getEndDate().getTime());
    }

    public IntranetCalendarEvent(Date beginDate, Date endDate, String headerColor, String contentColor, String content,
                             String title, boolean locked) {
        setHeaderColor(headerColor);
        setContentColor(contentColor);
        setContent(content);
        setTitle(title);
        setBeginDate(beginDate);
        getBeginDate().setTime(getBeginDate().getTime());
        setEndDate(endDate);
        getEndDate().setTime(getEndDate().getTime());
        setLocked(locked);
    }
    public IntranetCalendarEvent(Date beginDate, Date endDate) {
        setHeaderColor("#FFFFFF");
        setContentColor("#000000");
        setBeginDate(beginDate);
        setEndDate(endDate);
    }
    public IntranetCalendarEvent() {
        setHeaderColor("#FFFFFF");
        setContentColor("#000000");
    }

    @Override
    @Immutable
    public Date getBeginDate() {
        return super.getBeginDate();
    }

    @Override
    @Immutable
    public Date getEndDate() {
        return super.getEndDate();
    }

}