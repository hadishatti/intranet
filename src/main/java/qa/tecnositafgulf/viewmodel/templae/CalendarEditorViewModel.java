package qa.tecnositafgulf.viewmodel.templae;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.event.EventListener;
import qa.tecnositafgulf.model.calendar.IntranetCalendarEvent;
import qa.tecnositafgulf.model.calendar.QueueMessage;
import qa.tecnositafgulf.model.calendar.QueueUtils;
import qa.tecnositafgulf.viewmodel.IntranetVM;

import java.util.Date;
import java.util.Map;

/**
 * Created by hadi on 1/3/18.
 */
public class CalendarEditorViewModel  extends IntranetVM {
    private IntranetCalendarEvent calendarEventData = new IntranetCalendarEvent();

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

    @Init
    public void init() {
        //subscribe a queue, listen to other controller
        QueueUtils.lookupQueue().subscribe(new QueueListener());
    }

    private void startEditing(IntranetCalendarEvent calendarEventData) {
        this.calendarEventData = calendarEventData;
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

    public Validator getDateValidator(){
        return new AbstractValidator(){

            public void validate(ValidationContext ctx) {
                Map<String,Property> formData = ctx.getProperties(ctx.getProperty().getValue());
                Date beginDate = (Date)formData.get("beginDate").getValue();
                Date endDate = (Date)formData.get("endDate").getValue();
                if(beginDate==null){
                    addInvalidMessage(ctx, "beginDate","Begin date is empty");
                }
                if(endDate==null){
                    addInvalidMessage(ctx, "endDate","End date is empty");
                }
                if(beginDate!=null && endDate!=null && beginDate.compareTo(endDate) >= 0){
                    addInvalidMessage(ctx, "endDate","End date is before begin date");
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
        QueueMessage message = new QueueMessage(QueueMessage.Type.DELETE, calendarEventData);
        QueueUtils.lookupQueue().publish(message);
        this.visible = false;
    }

    @Command
    @NotifyChange("visible")
    public void ok() {
        QueueMessage message = new QueueMessage(QueueMessage.Type.OK, calendarEventData);
        QueueUtils.lookupQueue().publish(message);
        this.visible = false;
    }

    private class QueueListener implements EventListener<QueueMessage> {

        public void onEvent(QueueMessage message)
                throws Exception {
            if (QueueMessage.Type.EDIT.equals(message.getType())){
                CalendarEditorViewModel.this.startEditing((IntranetCalendarEvent)message.getData());
            }
        }
    }
}
