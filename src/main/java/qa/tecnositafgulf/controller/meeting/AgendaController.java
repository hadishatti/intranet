package qa.tecnositafgulf.controller.meeting;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.api.CalendarEvent;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.calendar.*;
import qa.tecnositafgulf.model.meetings.Meeting;
import qa.tecnositafgulf.service.MeetingService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hadi on 1/7/18.
 */
public class AgendaController extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;
    static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs

    @Wire
    private Calendars calendars;
    @Wire
    private Textbox filter;
    @Wire
    private Datebox focus;
    @Wire
    private Label current;
    @Wire
    private Button prev;
    @Wire
    private Button next;


    private IntranetCalendarModel calendarModel;
    private MeetingService service;
    private String imagePath;

    //the in editing calendar ui companyEvent
    private CalendarsEvent calendarsEvent = null;

    private String getRandomColor(){
        String[] colors = {"#0000CD", "#6B8E23", "#800000", "#7B68EE", "#FF00FF", "#B8860B", "#2F4F4F", "#006400", "#D2691E", "#009CFF"};

        Random rand = new Random();
        int i = rand.nextInt(colors.length);
        return colors[i];
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(MeetingService.class);
        Employee employee = ((Employee) Sessions.getCurrent().getAttribute("employee"));
        List<Meeting> meetings = service.listAllEmployeeMeetings(employee);
        List<CalendarEvent> events = new ArrayList<CalendarEvent>();
        imagePath = MyProperties.getInstance().getImagePath();
        prev.setImage(imagePath.concat("/arrow-180.png"));
        next.setImage(imagePath.concat("/arrow.png"));
        for(Meeting meeting : meetings)
        {
            String color = getRandomColor();
            CalendarEvent ce = new IntranetCalendarEvent(meeting.getFromTime(), meeting.getToTime(), meeting.getHeaderColor(), meeting.getContentColor(), meeting.getTitle());
            events.add(ce);
        }
        calendarModel = new IntranetCalendarModel(events, meetings);
        calendars.setDateFormatter(new IntranetCalendarDateFormatter());
        calendars.setModel(this.calendarModel);
        SimpleDateFormat sdf;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current.setValue(sdf.format(start) + " - " + sdf.format(end));
    }

    @Listen("onChange = #focus")
    public void gotoDate(){
        Date date = focus.getValue();
        calendars.setCurrentDate(date);
        SimpleDateFormat sdf;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current.setValue(sdf.format(start) + " - " + sdf.format(end));
    }

    //control the calendar position
    @Listen("onClick = #today")
    public void gotoToday(){
        TimeZone timeZone = calendars.getDefaultTimeZone();
        calendars.setCurrentDate(Calendar.getInstance(timeZone).getTime());
        focus.setValue(null);
        SimpleDateFormat sdf;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current.setValue(sdf.format(start) + " - " + sdf.format(end));
    }
    @Listen("onClick = #next")
    public void gotoNext(){
        calendars.nextPage();
        focus.setValue(null);
        SimpleDateFormat sdf;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current.setValue(sdf.format(start) + " - " + sdf.format(end));
    }
    @Listen("onClick = #prev")
    public void gotoPrev(){
        calendars.previousPage();
        focus.setValue(null);
        SimpleDateFormat sdf;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current.setValue(sdf.format(start) + " - " + sdf.format(end));
    }

    //control page display
    @Listen("onClick = #pageDay")
    public void changeToDay(){
        calendars.setMold("default");
        calendars.setDays(1);
        focus.setValue(null);
        SimpleDateFormat sdf;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current.setValue(sdf.format(start) + " - " + sdf.format(end));
    }
    @Listen("onClick = #pageWeek")
    public void changeToWeek(){
        calendars.setMold("default");
        calendars.setDays(7);
        focus.setValue(null);
        SimpleDateFormat sdf;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current.setValue(sdf.format(start) + " - " + sdf.format(end));
    }
    @Listen("onClick = #pageMonth")
    public void changeToYear(){
        calendars.setMold("month");
        focus.setValue(null);
        SimpleDateFormat sdf;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current.setValue(sdf.format(start) + " - " + sdf.format(end));
    }

    //control the filter
    @Listen("onClick = #applyFilter")
    public void applyFilter(){
        calendarModel.setFilterText(filter.getValue());
        calendars.setModel(calendarModel);
    }
    @Listen("onClick = #resetFilter")
    public void resetFilter(){
        filter.setText("");
        calendarModel.setFilterText("");
        calendars.setModel(calendarModel);
    }

    //listen to the calendar-create and edit of a companyEvent data
    @Listen("onEventCreate = #calendars; onEventEdit = #calendars")
    public void createEvent(CalendarsEvent event) {
        calendarsEvent = event;

        //to display a shadow when editing
        calendarsEvent.stopClearGhost();

        IntranetCalendarEvent data = (IntranetCalendarEvent) event.getCalendarEvent();

        if(data == null) {
            data = new IntranetCalendarEvent();
            data.setHeaderColor(getRandomColor());
            data.setContentColor(getRandomColor());
            //put condition if begin date same as current date then show the current timeStamp
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String beginDate = simpleDateFormat.format(event.getBeginDate());
            String currentDate = simpleDateFormat.format(new Timestamp(new Date().getTime()));
            if(beginDate.equals(currentDate)){
                data.setBeginDate(new Timestamp(new Date().getTime()));
                data.setEndDate(new Timestamp(Calendar.getInstance().getTimeInMillis() + (30 * ONE_MINUTE_IN_MILLIS)));
            }else{
                data.setBeginDate(event.getBeginDate());
                data.setEndDate(event.getEndDate());
            }


        } else {
            data = (IntranetCalendarEvent) event.getCalendarEvent();
        }
        Meeting meeting = service.findMeetingByTitleAndFrom(data.getContent(), data.getBeginDate(), ((Employee) Sessions.getCurrent().getAttribute("employee")));
        //notify the editor
        QueueUtils.lookupQueue().publish(
                new QueueMessage(QueueMessage.Type.EDIT,data, meeting));
    }

    //listen to the calendar-update of companyEvent data, usually send when user drag the companyEvent data
    @Listen("onEventUpdate = #calendars")
    public void updateEvent(CalendarsEvent event) {

        IntranetCalendarEvent data = (IntranetCalendarEvent) event.getCalendarEvent();
        Meeting meeting = service.findMeetingByTitleAndFrom(data.getContent(), data.getBeginDate(), ((Employee) Sessions.getCurrent().getAttribute("employee")));
        service.updateMeeting(meeting, event.getBeginDate(), event.getEndDate(),
                ((Employee) Sessions.getCurrent().getAttribute("employee")));
        data.setBeginDate(event.getBeginDate());
        data.setEndDate(event.getEndDate());
        calendarModel.update(data);

    }

    //listen to queue message from other controller
    @Subscribe(value = QueueUtils.QUEUE_NAME)
    public void handleQueueMessage(Event event) {
        if(!(event instanceof QueueMessage)) {
            return;
        }
        QueueMessage message = (QueueMessage)event;
        switch(message.getType()){
            case DELETE:
                calendarModel.remove((IntranetCalendarEvent)message.getData(), message.getMeeting());
                //clear the shadow of the companyEvent after editing
                calendarsEvent.clearGhost();
                calendarsEvent = null;
                break;
            case OK:
                if (calendarModel.indexOf((IntranetCalendarEvent)message.getData()) >= 0) {
                    calendarModel.update((IntranetCalendarEvent)message.getData(), message.getMeeting());
                    calendarsEvent.clearGhost();
                } else {
                    calendarModel.add((IntranetCalendarEvent)message.getData(), message.getMeeting());
                }
            case EDIT:
                calendarModel.update((IntranetCalendarEvent)message.getData(),message.getMeeting());
                calendarsEvent.clearGhost();
            case CANCEL:
                //clear the shadow of the companyEvent after editing
                //calendarsEvent.clearGhost();
                calendarsEvent = null;
                break;
        }
    }

}
