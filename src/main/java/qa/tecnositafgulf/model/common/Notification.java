package qa.tecnositafgulf.model.common;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hadi on 1/2/18.
 */
@Entity
@Table(name = "notification")
@NamedQueries({
        @NamedQuery(name = "Notification.listForEmployeeByRecipient", query = "SELECT n FROM Notification n WHERE n.recipient = :recipient AND n.readOn is null AND n.type = 0 ORDER BY n.receivedOn DESC"),
        @NamedQuery(name = "Notification.listForManagerByRecipient", query = "SELECT n FROM Notification n WHERE n.recipient = :recipient AND n.readOn is null AND n.type > 0 ORDER BY n.receivedOn DESC"),
        @NamedQuery(name = "Notification.deleteByAttachment", query = "DELETE FROM Notification n WHERE n.attachment = :attachment"),
        @NamedQuery(name = "Notification.deleteByAttachmentAndRecipientForEmployee", query = "DELETE FROM Notification n WHERE n.attachment = :attachment AND n.recipient = :recipient AND n.forEmployee=true"),
        @NamedQuery(name = "Notification.deleteByAttachmentAndRecipientForManager", query = "DELETE FROM Notification n WHERE n.attachment = :attachment AND n.recipient = :recipient AND n.forEmployee=false")
})
public class Notification implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int type;

    private String object;

    @OneToOne
    @JoinColumn(name = "recipientID")
    private Employee recipient;

    @OneToOne
    @JoinColumn(name = "senderID", nullable = true)
    private Employee sender;

    private Date receivedOn;

    private Date readOn;

    private String attachment;

    private boolean forEmployee;

    private boolean requestCancelled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Employee getRecipient() {
        return recipient;
    }

    public void setRecipient(Employee recipient) {
        this.recipient = recipient;
    }

    public Employee getSender() {
        return sender;
    }

    public void setSender(Employee sender) {
        this.sender = sender;
    }

    public Date getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    public Date getReadOn() {
        return readOn;
    }

    public void setReadOn(Date readOn) {
        this.readOn = readOn;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public boolean isForEmployee() {
        return forEmployee;
    }

    public void setForEmployee(boolean forEmployee) {
        this.forEmployee = forEmployee;
    }

    public boolean isRequestCancelled() {
        return requestCancelled;
    }

    public void setRequestCancelled(boolean requestCancelled) {
        this.requestCancelled = requestCancelled;
    }
}
