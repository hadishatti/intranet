package qa.tecnositafgulf.controller.meeting;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.api.CalendarEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.calendar.IntranetCalendarDateFormatter;
import qa.tecnositafgulf.model.calendar.IntranetCalendarEvent;
import qa.tecnositafgulf.model.calendar.IntranetCalendarModel;
import qa.tecnositafgulf.model.meetings.Meeting;
import qa.tecnositafgulf.service.MeetingService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hadi on 1/7/18.
 */
public class AgendaTodayController extends SelectorComposer<Component> {
    private static final long serialVersionUID = 1L;

    @Wire
    private Calendars calendars;
    @Wire
    private Label appointments;
    private MeetingService service;
    private IntranetCalendarModel calendarModel;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(MeetingService.class);
        Employee employee = ((Employee) Sessions.getCurrent().getAttribute("employee"));
        List<Meeting> meetings = service.listAllEmployeeMeetings(employee);
        List<CalendarEvent> events = new ArrayList<CalendarEvent>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        Date start = c.getTime();
        c.add(Calendar.DAY_OF_MONTH,1);
        Date end = c.getTime();
        int index = 0;
        for(Meeting meeting : meetings)
        {
            Calendar app = Calendar.getInstance();
            app.setTime(meeting.getFromTime());
            if(app.getTime().after(start) && app.getTime().before(end)) {
                CalendarEvent ce = new IntranetCalendarEvent(meeting.getFromTime(), meeting.getToTime(), meeting.getHeaderColor(), meeting.getContentColor(), meeting.getTitle()+((meeting.getLocation()!=null&&!meeting.getLocation().isEmpty())?" at "+meeting.getLocation():""));
                events.add(ce);
                index++;
            }
        }
        calendarModel = new IntranetCalendarModel(events, meetings);
        calendars.setModel(this.calendarModel);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");
        c = Calendar.getInstance();
        appointments.setValue("Today "+sdf.format(c.getTime())+" you have "+index+" registered appointments.");
        calendars.setCurrentDate(c.getTime());
        calendars.setDateFormatter(new IntranetCalendarDateFormatter());
    }
}
