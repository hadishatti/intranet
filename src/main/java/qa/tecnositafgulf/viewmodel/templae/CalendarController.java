package qa.tecnositafgulf.viewmodel.templae;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.api.CalendarEvent;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.ui.select.annotation.Subscribe;
import org.zkoss.zul.Textbox;
import qa.tecnositafgulf.config.MyProperties;
import qa.tecnositafgulf.model.calendar.IntranetCalendarEvent;
import qa.tecnositafgulf.model.calendar.IntranetCalendarModel;
import qa.tecnositafgulf.model.calendar.QueueMessage;
import qa.tecnositafgulf.model.calendar.QueueUtils;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;

import java.util.*;

/**
 * Created by hadi on 1/3/18.
 */
public class CalendarController extends SelectorComposer<Component>{

    private static final long serialVersionUID = 1L;
    private String imagePath;

    @Wire
    private Calendars calendars;
    @Wire
    private Textbox filter;

    private IntranetCalendarModel calendarModel;
    private LeaveRequestService service;

    //the in editing calendar ui companyEvent
    private CalendarsEvent calendarsEvent = null;

    private String getRandomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return "rgb("+r+","+g+","+b+")";
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(LeaveRequestService.class);
        List<LeaveRequest> leaveRequests = service.listApprovedLeaveRequests();
        List<CalendarEvent> events = new ArrayList<CalendarEvent>();
        for(LeaveRequest request : leaveRequests)
        {
            String color = getRandomColor();
            CalendarEvent ce = new IntranetCalendarEvent(request.getLeaveFrom(), request.getLeaveTo(), color, color, request.getApplicant().toString());
            events.add(ce);
        }
        calendarModel = new IntranetCalendarModel(events);
        calendars.setModel(this.calendarModel);
    }

    //control the calendar position
    @Listen("onClick = #today")
    public void gotoToday(){
        TimeZone timeZone = calendars.getDefaultTimeZone();
        calendars.setCurrentDate(Calendar.getInstance(timeZone).getTime());
    }
    @Listen("onClick = #next")
    public void gotoNext(){
        calendars.nextPage();
    }
    @Listen("onClick = #prev")
    public void gotoPrev(){
        calendars.previousPage();
    }

    //control page display
    @Listen("onClick = #pageDay")
    public void changeToDay(){
        calendars.setMold("default");
        calendars.setDays(1);
    }
    @Listen("onClick = #pageWeek")
    public void changeToWeek(){
        calendars.setMold("default");
        calendars.setDays(7);
    }
    @Listen("onClick = #pageMonth")
    public void changeToYear(){
        calendars.setMold("month");
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

    public String getImagePath() {
        return MyProperties.getInstance().getImagePath();
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    //listen to the calendar-create and edit of a companyEvent data
    @Listen("onEventCreate = #calendars; onEventEdit = #calendars")
    public void createEvent(CalendarsEvent event) {
        calendarsEvent = event;

        //to display a shadow when editing
        calendarsEvent.stopClearGhost();

        IntranetCalendarEvent data = (IntranetCalendarEvent)event.getCalendarEvent();

        if(data == null) {
            data = new IntranetCalendarEvent();
            data.setHeaderColor("#3366ff");
            data.setContentColor("#6699ff");
            data.setBeginDate(event.getBeginDate());
            data.setEndDate(event.getEndDate());
        } else {
            data = (IntranetCalendarEvent) event.getCalendarEvent();
        }
        //notify the editor
        QueueUtils.lookupQueue().publish(
                new QueueMessage(QueueMessage.Type.EDIT,data));
    }

    //listen to the calendar-update of companyEvent data, usually send when user drag the companyEvent data
    @Listen("onEventUpdate = #calendars")
    public void updateEvent(CalendarsEvent event) {
        IntranetCalendarEvent data = (IntranetCalendarEvent) event.getCalendarEvent();
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
                calendarModel.remove((IntranetCalendarEvent)message.getData());
                //clear the shadow of the companyEvent after editing
                calendarsEvent.clearGhost();
                calendarsEvent = null;
                break;
            case OK:
                if (calendarModel.indexOf((IntranetCalendarEvent)message.getData()) >= 0) {
                    calendarModel.update((IntranetCalendarEvent)message.getData());
                } else {
                    calendarModel.add((IntranetCalendarEvent)message.getData());
                }
            case CANCEL:
                //clear the shadow of the companyEvent after editing
                calendarsEvent.clearGhost();
                calendarsEvent = null;
                break;
        }
    }
}
