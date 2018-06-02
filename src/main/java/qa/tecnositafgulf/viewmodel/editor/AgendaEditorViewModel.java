package qa.tecnositafgulf.viewmodel.editor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zul.Messagebox;
import qa.tecnositafgulf.model.administration.Employee;
import qa.tecnositafgulf.model.calendar.IntranetCalendarEvent;
import qa.tecnositafgulf.model.calendar.QueueMessage;
import qa.tecnositafgulf.model.calendar.QueueUtils;
import qa.tecnositafgulf.model.meetings.Meeting;
import qa.tecnositafgulf.service.MeetingService;
import qa.tecnositafgulf.spring.config.AppConfig;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hadi on 1/3/18.
 */
public class AgendaEditorViewModel  extends IntranetVM {
    private MeetingService service;
    private IntranetCalendarEvent calendarEventData;
    private Meeting meeting;
    private String location;
    private boolean modify;

    private boolean visible = false;

    public IntranetCalendarEvent getCalendarEvent() {
        return calendarEventData;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Init
    public void init() {
        calendarEventData = new IntranetCalendarEvent();
        //subscribe a queue, listen to other controller
        QueueUtils.lookupQueue().subscribe(new QueueListener());
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        service = context.getBean(MeetingService.class);
    }

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view){
        addCommonTags((PageCtrl) view.getPage());
    }

    private void startEditing(IntranetCalendarEvent calendarEventData, Meeting meeting) {
        this.calendarEventData = calendarEventData;
        if(meeting==null) {
            this.meeting = new Meeting();
            modify=false;
        }
        else {
            this.meeting = meeting;
            location = meeting.getLocation();
            modify=true;
        }
        visible = true;

        //reload entire view-model data when going to edit
        BindUtils.postNotifyChange(null, null, this, "*");
    }


    public boolean isAllDay(Date beginDate, Date endDate) {
        int M_IN_DAY = 1000 * 60 * 60 * 24;
        boolean allDay = false;

        if(beginDate != null && endDate != null) {
            long between = endDate.getTime() - beginDate.getTime();
            allDay = between > M_IN_DAY;
        }
        return allDay;
    }

    public Validator getDateValidator() {
        return new AbstractValidator() {

            public void validate(ValidationContext ctx) {
                Map<String, Property> formData = ctx.getProperties(ctx.getProperty().getValue());
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                Calendar cal = Calendar.getInstance();
                Date beginDate = (Date) formData.get("beginDate").getValue();
                Date endDate = (Date) formData.get("endDate").getValue();
                if (beginDate == null) {
                    addInvalidMessage(ctx, "beginDate", "Begin date is empty");
                }
                if (endDate == null) {
                    addInvalidMessage(ctx, "endDate", "End date is empty");
                }
                if (beginDate != null && endDate != null && beginDate.compareTo(endDate) >= 0) {
                    addInvalidMessage(ctx, "endDate", "End date is before begin date");
                }
                String strBeginDate = simpleDateFormat.format(beginDate);
                String currentDate = simpleDateFormat.format(new Timestamp(new Date().getTime()));
                try {
                if(simpleDateFormat.parse(strBeginDate).compareTo(simpleDateFormat.parse(currentDate)) < 0){
                    Messagebox.show("Selected Begin Date is Previous Date", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
                        @Override
                        public void onEvent(Event event) throws Exception {
                            if (event.getName().equals("onOK")) {

                            }
                        }
                    });
                }
                } catch (ParseException parseException) {

                }
            }


        };
    }

    @Command
    @NotifyChange("visible")
    public void cancel() {
        QueueMessage message = new QueueMessage(QueueMessage.Type.CANCEL);
        QueueUtils.lookupQueue().publish(message);
        this.visible = false;
    }

    @Command
    @NotifyChange("visible")
    public void delete() {

        Messagebox.show("Do you want to delete this Meeting?", "Warning", Messagebox.OK | Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener<Event>() {
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onOK")) {
                    QueueMessage message = new QueueMessage(QueueMessage.Type.DELETE, calendarEventData, meeting);
                    QueueUtils.lookupQueue().publish(message);
                    visible = false;
                    service.removeMeeting(meeting);
                    Executions.sendRedirect("/pages/meeting/agenda.zul");
                }
            }
        });

    }

    @Command
    @NotifyChange("visible")
    public void ok() {
        Employee employee = ((Employee) Sessions.getCurrent().getAttribute("employee"));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
            List<Employee> partecipants = new ArrayList<Employee>();
            partecipants.add(employee);
            meeting.setFromTime(calendarEventData.getBeginDate());
            meeting.setToTime(calendarEventData.getEndDate());
            meeting.setTitle(calendarEventData.getContent());
            meeting.setPartecipants(partecipants);
            meeting.setContentColor(calendarEventData.getContentColor());
            meeting.setHeaderColor(calendarEventData.getHeaderColor());
            meeting.setLocation(location);
            service.saveMeeting(meeting);
            QueueMessage message = new QueueMessage(QueueMessage.Type.OK, calendarEventData, meeting);
            QueueUtils.lookupQueue().publish(message);
            this.visible = false;
            Executions.sendRedirect("/pages/meeting/agenda.zul");
        }catch (Exception e){
            Messagebox.show("There is already a meeting in the same time with the same name! Check again","Error", Messagebox.OK, Messagebox.EXCLAMATION);
        }
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    private class QueueListener implements EventListener<QueueMessage> {

        public void onEvent(QueueMessage message)
                throws Exception {
            if (QueueMessage.Type.EDIT.equals(message.getType())){
                AgendaEditorViewModel.this.startEditing((IntranetCalendarEvent)message.getData(), message.getMeeting());
            }
        }
    }
}
