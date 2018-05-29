package qa.tecnositafgulf.viewmodel.hr;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.annotation.*;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.api.CalendarEvent;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import qa.tecnositafgulf.model.calendar.IntranetCalendarDateFormatter;
import qa.tecnositafgulf.model.calendar.IntranetCalendarEvent;
import qa.tecnositafgulf.model.calendar.IntranetCalendarModel;
import qa.tecnositafgulf.model.leaves.LeaveRequest;
import qa.tecnositafgulf.service.LeaveRequestService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hadi on 1/4/18.
 */
public class WhoIsInLeaveVM extends IntranetVM {
    private IntranetCalendarModel calendarModel;
    private LeaveRequestService service;
    private String filter;
    private String current;
    private SimpleDateFormat sdf;
    private Date focusDate;

    @Wire
    private Calendars calendars;

    //the in editing calendar ui companyEvent
    private CalendarsEvent calendarsEvent = null;

    private String getRandomColor(){
        String[] colors = {"#0000CD", "#6B8E23", "#800000", "#7B68EE", "#FF00FF", "#B8860B", "#2F4F4F", "#006400", "#D2691E", "#009CFF"};

        Random rand = new Random();
        int i = rand.nextInt(colors.length);
        return colors[i];
    }

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        init();
        Selectors.wireComponents(view, this, false);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(LeaveRequestService.class);

        List<LeaveRequest> leaveRequests = service.listApprovedLeaveRequests();
        List<CalendarEvent> events = new ArrayList<CalendarEvent>();

        for(LeaveRequest request : leaveRequests)
        {
            String color = getRandomColor();
            CalendarEvent ce = new IntranetCalendarEvent(request.getLeaveFrom(), request.getLeaveTo(), color, color, toString(request));
            events.add(ce);
        }
        calendarModel = new IntranetCalendarModel(events);
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        sdf = new SimpleDateFormat("MMMM,dd yyyy");
        current = sdf.format(start)+" - "+sdf.format(end);
        calendars.setDateFormatter(new IntranetCalendarDateFormatter());
        focusDate = null;
    }

    public String toString(LeaveRequest request){
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return request.getApplicant().getName()+" "+request.getApplicant().getFamilyName()+" - "+request.getType()+" LEAVE";
    }

    public IntranetCalendarModel getCalendarModel() {
        return calendarModel;
    }

    public void setCalendarModel(IntranetCalendarModel calendarModel) {
        this.calendarModel = calendarModel;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Date getFocusDate() {
        return focusDate;
    }

    public void setFocusDate(Date focusDate) {
        this.focusDate = focusDate;
    }

    @Command
    @NotifyChange({"current", "focusDate"})
    public void gotoDate(@BindingParam("date") Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        calendars.setCurrentDate(c.getTime());
        focusDate = date;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current = sdf.format(start) + " - " + sdf.format(end);
    }

    @Command
    @NotifyChange({"current", "focusDate"})
    public void gotoToday(){
        TimeZone timeZone = calendars.getDefaultTimeZone();
        calendars.setCurrentDate(Calendar.getInstance(timeZone).getTime());
        focusDate = calendars.getBeginDate();
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current = sdf.format(start) + " - " + sdf.format(end);
    }

    @Command
    @NotifyChange({"current", "focusDate"})
    public void gotoNext(){
        calendars.nextPage();
        focusDate = null;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current = sdf.format(start) + " - " + sdf.format(end);
    }

    @Command
    @NotifyChange({"current", "focusDate"})
    public void gotoPrev(){
        calendars.previousPage();
        focusDate = null;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current = sdf.format(start) + " - " + sdf.format(end);
    }

    @Command
    @NotifyChange({"current", "focusDate"})
    public void changeToDay(){
        calendars.setMold("default");
        focusDate = null;
        calendars.setDays(1);
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current = sdf.format(start) + " - " + sdf.format(end);
    }

    @Command
    @NotifyChange({"current", "focusDate"})
    public void changeToWeek(){
        calendars.setMold("default");
        focusDate = null;
        calendars.setDays(7);
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current = sdf.format(start) + " - " + sdf.format(end);
    }
    @Command
    @NotifyChange({"current", "focusDate"})
    public void changeToYear(){
        calendars.setMold("month");
        focusDate = null;
        if(calendars.getMold().equals("default")){
            sdf = new SimpleDateFormat("EEEE MMMM,dd yyyy HH:mm");
        }else {
            sdf = new SimpleDateFormat("MMMM,dd yyyy");
        }
        Date start = calendars.getBeginDate();
        Date end = calendars.getEndDate();
        current = sdf.format(start) + " - " + sdf.format(end);
    }

    //control the filter
    @Command
    public void applyFilter(){
        calendarModel.setFilterText(filter);
        calendars.setModel(calendarModel);
    }

    @Command
    @NotifyChange("filter")
    public void resetFilter(){
        filter = "";
        calendarModel.setFilterText("");
        calendars.setModel(calendarModel);
    }
}
