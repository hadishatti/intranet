package qa.tecnositafgulf.model.leaves;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hadi on 12/30/17.
 */
@Entity
@Table(name = "leave_request", uniqueConstraints = {@UniqueConstraint(columnNames={"number", "status"})})
@NamedQueries({
        @NamedQuery(name = "LeaveRequest.listApproved", query = "SELECT leaveRequest FROM LeaveRequest leaveRequest WHERE leaveRequest.status = 10 OR leaveRequest.status = 11"),
        @NamedQuery(name = "LeaveRequest.listSickLeaves", query = "SELECT leaveRequest FROM LeaveRequest leaveRequest WHERE leaveRequest.status = 11 ORDER BY leaveRequest.createdOn DESC"),
        @NamedQuery(name = "LeaveRequest.listEmergencyLeaves", query = "SELECT leaveRequest FROM LeaveRequest leaveRequest WHERE leaveRequest.status = 12 ORDER BY leaveRequest.createdOn DESC"),

        @NamedQuery(name = "LeaveRequest.listLastByApplicant", query = "SELECT leaveRequest FROM LeaveRequest leaveRequest " +
                "WHERE leaveRequest.status IN (" +
                "SELECT MAX(lr.status) FROM LeaveRequest lr " +
                "WHERE leaveRequest.number = lr.number GROUP BY lr.number) " +
                "AND leaveRequest.applicant = :applicant " +
                "AND leaveRequest.status <> 11  AND leaveRequest.status <> 12" +
                "ORDER BY leaveRequest.createdOn DESC"),

        @NamedQuery(name = "LeaveRequest.listLastByApplicants", query = "SELECT leaveRequest FROM LeaveRequest leaveRequest " +
                "WHERE leaveRequest.status IN (" +
                "SELECT MAX(lr.status) FROM LeaveRequest lr " +
                "WHERE leaveRequest.number = lr.number GROUP BY lr.number) " +
                "AND leaveRequest.applicant IN (:applicants) " +
                "AND leaveRequest.status <> 11 AND leaveRequest.status <> 12" +
                "ORDER BY leaveRequest.createdOn DESC"),
        @NamedQuery(name = "LeaveRequest.listLastByApplicantAndDates", query = "SELECT leaveRequest FROM LeaveRequest leaveRequest " +
                "WHERE leaveRequest.status IN (" +
                "SELECT MAX(lr.status) FROM LeaveRequest lr " +
                "WHERE leaveRequest.number = lr.number GROUP BY lr.number) " +
                "AND leaveRequest.applicant = :applicant " +
                "AND ((leaveRequest.leaveFrom <= :leaveFrom AND leaveRequest.leaveFrom <= :leaveTo AND leaveRequest.leaveTo >= :leaveFrom AND leaveRequest.leaveTo <= :leaveTo) OR " +
                "(leaveRequest.leaveFrom >= :leaveFrom AND leaveRequest.leaveFrom <= :leaveTo AND leaveRequest.leaveTo >= :leaveFrom AND leaveRequest.leaveTo <= :leaveTo) OR "+
                "(leaveRequest.leaveFrom >= :leaveFrom AND leaveRequest.leaveFrom <= :leaveTo AND leaveRequest.leaveTo >= :leaveFrom AND leaveRequest.leaveTo >= :leaveTo) OR "+
                "(leaveRequest.leaveFrom <= :leaveFrom AND leaveRequest.leaveFrom <= :leaveTo AND leaveRequest.leaveTo >= :leaveFrom AND leaveRequest.leaveTo >= :leaveTo))"+
                " AND leaveRequest.status <> 13  AND leaveRequest.status <> 2  AND leaveRequest.status <> 4  AND leaveRequest.status <> 6AND leaveRequest.status <> 9  " +
                "ORDER BY leaveRequest.createdOn DESC"),

        @NamedQuery(name = "LeaveRequest.listLastByApprover", query = "SELECT leaveRequest FROM LeaveRequest leaveRequest WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) AND leaveRequest.approver = :approver AND leaveRequest.status <> 13 ORDER BY leaveRequest.createdOn DESC"),

        @NamedQuery(name = "LeaveRequest.countPendingByApplicantAndDates", query = "SELECT count(leaveRequest) FROM LeaveRequest leaveRequest WHERE leaveRequest.status IN (SELECT MAX(lr.status) FROM LeaveRequest lr WHERE leaveRequest.number = lr.number GROUP BY lr.number) AND leaveRequest.applicant = :applicant AND " +
                "((leaveRequest.leaveFrom <= :leaveFrom AND leaveRequest.leaveFrom <= :leaveTo AND leaveRequest.leaveTo >= :leaveFrom AND leaveRequest.leaveTo <= :leaveTo) OR " +
                "(leaveRequest.leaveFrom >= :leaveFrom AND leaveRequest.leaveFrom <= :leaveTo AND leaveRequest.leaveTo >= :leaveFrom AND leaveRequest.leaveTo <= :leaveTo) OR "+
                "(leaveRequest.leaveFrom >= :leaveFrom AND leaveRequest.leaveFrom <= :leaveTo AND leaveRequest.leaveTo >= :leaveFrom AND leaveRequest.leaveTo >= :leaveTo) OR "+
                "(leaveRequest.leaveFrom <= :leaveFrom AND leaveRequest.leaveFrom <= :leaveTo AND leaveRequest.leaveTo >= :leaveFrom AND leaveRequest.leaveTo >= :leaveTo)) AND (leaveRequest.status <> 13 AND leaveRequest.status <> 2 AND leaveRequest.status <> 4 AND leaveRequest.status <> 6 AND leaveRequest.status <> 9)"),

        @NamedQuery(name = "LeaveRequest.findByNumber", query ="SELECT leaveRequest FROM LeaveRequest leaveRequest WHERE leaveRequest.number = :number"),
        @NamedQuery(name = "LeaveRequest.findByNumberAndStatus", query ="SELECT leaveRequest FROM LeaveRequest leaveRequest WHERE leaveRequest.number = :number AND leaveRequest.status = :status")

})
public class LeaveRequest  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number; //[A (Annual),C (Casual), S (Sick), E (Emergency)]-<Employee Initials>-from-to

    @OneToOne
    @JoinColumn(name = "applicantID")
    private Employee applicant;

    @OneToOne
    @JoinColumn(name = "approverID")
    private Employee approver;  //Manager

    private int status;
    private Date createdOn;
    private Date approvedOn;
    private Date leaveFrom;
    private Date leaveTo;
    private String type;
    private String addressOnHoliday;
    private String phoneNumber;
    private String casualLeaveReason;
    @OneToOne
    @JoinColumn(name = "employeeOnBehalf")
    private Employee employeeOnBehalf;
    private boolean tickets;
    @OneToOne
    @JoinColumn(name = "managerID")
    private Employee manager; //manager
    private String reason;
    @Lob
    private String message;

    @Lob
    private String certificate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getApplicant() {
        return applicant;
    }

    public void setApplicant(Employee applicant) {
        this.applicant = applicant;
    }

    public Employee getApprover() {
        return approver;
    }

    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(Date approveddOn) {
        this.approvedOn = approveddOn;
    }

    public Date getLeaveFrom() {
        return leaveFrom;
    }

    public void setLeaveFrom(Date leaveFrom) {
        this.leaveFrom = leaveFrom;
    }

    public Date getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(Date leaveTo) {
        this.leaveTo = leaveTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isTickets() {
        return tickets;
    }

    public void setTickets(boolean tickets) {
        this.tickets = tickets;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getAddressOnHoliday() {
        return addressOnHoliday;
    }

    public void setAddressOnHoliday(String addressOnHoliday) {
        this.addressOnHoliday = addressOnHoliday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCasualLeaveReason() {
        return casualLeaveReason;
    }

    public void setCasualLeaveReason(String casualLeaveReason) {
        this.casualLeaveReason = casualLeaveReason;
    }

    public Employee getEmployeeOnBehalf() {
        return employeeOnBehalf;
    }

    public void setEmployeeOnBehalf(Employee employeeOnBehalf) {
        this.employeeOnBehalf = employeeOnBehalf;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
