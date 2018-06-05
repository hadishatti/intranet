package qa.tecnositafgulf.model.administration;

import qa.tecnositafgulf.common.PhonePrefix;
import qa.tecnositafgulf.model.administration.inventory.InventoryLog;
import qa.tecnositafgulf.model.administration.inventory.Item;
import qa.tecnositafgulf.model.administration.inventory.Transfer;
import qa.tecnositafgulf.model.meetings.Meeting;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by hadi on 12/17/17.
 */
@Entity
@Table(name = "employee", uniqueConstraints = {@UniqueConstraint(columnNames={"employeeNumber"})})
@NamedQueries({
        @NamedQuery(name = "Employee.listAll", query = "SELECT employee FROM Employee employee ORDER BY employee.employeeNumber"),
        @NamedQuery(name = "Employee.listAllOrderByChartId", query = "SELECT employee FROM Employee employee ORDER BY employee.chartId"),
        @NamedQuery(name = "Employee.countAll", query = "SELECT count(employee) FROM Employee employee"),
        @NamedQuery(name = "Employee.listByLeaveManager", query = "SELECT employee FROM Employee employee WHERE employee.leaveManagerEmployee = :manager"),
        @NamedQuery(name = "Employee.listByHRLeaveManager", query = "SELECT employee FROM Employee employee WHERE employee.HRLeaveManagerEmployee = :manager"),
        @NamedQuery(name = "Employee.listByFinanceLeaveManager", query = "SELECT employee FROM Employee employee WHERE employee.financeLeaveManagerEmployee = :manager"),
        @NamedQuery(name = "Employee.listByTicketLeaveManager", query = "SELECT employee FROM Employee employee WHERE employee.ticketLeaveManagerEmployee = :manager"),

        @NamedQuery(name = "Employee.listLeaveManagers", query = "SELECT employee FROM Employee employee " +
                "JOIN employee.profiles p " +
                "WHERE p.name = 'LM'"),
        @NamedQuery(name = "Employee.listHRLeaveManagers", query = "SELECT employee FROM Employee employee " +
                "JOIN employee.profiles p " +
                "WHERE p.name = 'HRLM'"),
        @NamedQuery(name = "Employee.listFinanceLeaveManagers", query = "SELECT employee FROM Employee employee " +
                "JOIN employee.profiles p " +
                "WHERE p.name = 'FLM'"),
        @NamedQuery(name = "Employee.listTicketLeaveManagers", query = "SELECT employee FROM Employee employee " +
                "JOIN employee.profiles p " +
                "WHERE p.name = 'TLM'"),

        @NamedQuery(name = "Employee.findByUsername", query = "SELECT employee FROM Employee employee WHERE employee.username = :username"),
        @NamedQuery(name = "Employee.findByEmployeeID", query = "SELECT employee FROM Employee employee WHERE employee.employeeNumber = :employeeNumber")
})
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String employeeNumber;

    private String name;

    private String familyName;

    private String address;

    private String officePhoneNumber;

    private String personalPhoneNumberPrefix1;

    private String personalPhoneNumber1;

    private String phoneNumber1;

    private String personalPhoneNumberPrefix2;

    private String personalPhoneNumber2;

    private String phoneNumber2;

    private String mail1;

    private String mail2;

    private String mail3;

    private String image;

    private Date birthDate;

    private String nationality;

    private String qatarID;

    private String passport;

    private String healthCardID;
    /********************************/

    private boolean isLeaveManager;

    private boolean isHRLeaveManager;

    private boolean isFinanceLeaveManager;

    private boolean isTicketLeaveManager;

    /*****************************************/

    private Long parentId;

    private Long secondParentId;

    private boolean isParentEmployee;

    private boolean isSecondParentEmployee;

    private String color;

    private Long chartId;


    /**********************************************************************/

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_profile",
            joinColumns = {@JoinColumn(name = "employeeID", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "profileID", referencedColumnName = "id")}
    )
    private Set<Profile> profiles;

    @ManyToMany(mappedBy = "partecipants")
    private List<Meeting> meetings;

    @OneToOne
    @JoinColumn(name = "leaveManagerID")
    private Employee leaveManagerEmployee;

    @OneToOne
    @JoinColumn(name = "HRleaveManagerID")
    private Employee HRLeaveManagerEmployee;

    @OneToOne
    @JoinColumn(name = "financeLeaveManagerID")
    private Employee financeLeaveManagerEmployee;

    @OneToOne
    @JoinColumn(name = "ticketLeaveManagerID")
    private Employee ticketLeaveManagerEmployee;

    @OneToOne
    @JoinColumn(name = "roleID")
    private Role role;

    @OneToOne
    @JoinColumn(name = "departmentID")
    private Department department;

    @OneToMany(mappedBy = "employee")
    List<Item> items;

    @OneToMany(mappedBy = "employee")
    List<Transfer> transfers;

    @OneToMany(mappedBy = "employee")
    List<InventoryLog> inventoryLogs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOfficePhoneNumber() {
        return officePhoneNumber;
    }

    public void setOfficePhoneNumber(String officePhoneNumber) {
        this.officePhoneNumber = officePhoneNumber;
    }

    public String getPersonalPhoneNumber1() {
        return personalPhoneNumber1;
    }

    public void setPersonalPhoneNumber1(String personalPhoneNumber1) {
        this.personalPhoneNumber1 = personalPhoneNumber1;
    }

    public String getPersonalPhoneNumber2() {
        return personalPhoneNumber2;
    }

    public void setPersonalPhoneNumber2(String personalPhoneNumber2) {
        this.personalPhoneNumber2 = personalPhoneNumber2;
    }

    public String getPersonalPhoneNumberPrefix1() {
        return personalPhoneNumberPrefix1;
    }

    public void setPersonalPhoneNumberPrefix1(String personalPhoneNumberPrefix1) {
        this.personalPhoneNumberPrefix1 = personalPhoneNumberPrefix1;
    }

    public String getPersonalPhoneNumberPrefix2() {
        return personalPhoneNumberPrefix2;
    }

    public void setPersonalPhoneNumberPrefix2(String personalPhoneNumberPrefix2) {
        this.personalPhoneNumberPrefix2 = personalPhoneNumberPrefix2;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isLeaveManager() {
        return isLeaveManager;
    }

    public void setLeaveManager(boolean leaveManager) {
        isLeaveManager = leaveManager;
    }

    public boolean isHRLeaveManager() {
        return isHRLeaveManager;
    }

    public void setHRLeaveManager(boolean HRLeaveManager) {
        isHRLeaveManager = HRLeaveManager;
    }

    public boolean isFinanceLeaveManager() {
        return isFinanceLeaveManager;
    }

    public void setFinanceLeaveManager(boolean financeLeaveManager) {
        isFinanceLeaveManager = financeLeaveManager;
    }

    public boolean isTicketLeaveManager() {
        return isTicketLeaveManager;
    }

    public void setTicketLeaveManager(boolean ticketLeaveManager) {
        isTicketLeaveManager = ticketLeaveManager;
    }

    public boolean isParentEmployee() {
        return isParentEmployee;
    }

    public void setParentEmployee(boolean parentEmployee) {
        isParentEmployee = parentEmployee;
    }

    public boolean isSecondParentEmployee() {
        return isSecondParentEmployee;
    }

    public void setSecondParentEmployee(boolean secondParentEmployee) {
        isSecondParentEmployee = secondParentEmployee;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public Employee getLeaveManagerEmployee() {
        return leaveManagerEmployee;
    }

    public void setLeaveManagerEmployee(Employee leaveManagerEmployee) {
        this.leaveManagerEmployee = leaveManagerEmployee;
    }

    public Employee getHRLeaveManagerEmployee() {
        return HRLeaveManagerEmployee;
    }

    public void setHRLeaveManagerEmployee(Employee HRLeaveManagerEmployee) {
        this.HRLeaveManagerEmployee = HRLeaveManagerEmployee;
    }

    public Employee getFinanceLeaveManagerEmployee() {
        return financeLeaveManagerEmployee;
    }

    public void setFinanceLeaveManagerEmployee(Employee financeLeaveManagerEmployee) {
        this.financeLeaveManagerEmployee = financeLeaveManagerEmployee;
    }

    public Employee getTicketLeaveManagerEmployee() {
        return ticketLeaveManagerEmployee;
    }

    public void setTicketLeaveManagerEmployee(Employee ticketLeaveManagerEmployee) {
        this.ticketLeaveManagerEmployee = ticketLeaveManagerEmployee;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getSecondParentId() {
        return secondParentId;
    }

    public void setSecondParentId(Long secondParentId) {
        this.secondParentId = secondParentId;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    public void setMail1(String mail1) {
        this.mail1 = mail1;
    }

    public String getMail2() {
        return mail2;
    }

    public void setMail2(String mail2) {
        this.mail2 = mail2;
    }

    public String getMail3() {
        return mail3;
    }

    public void setMail3(String mail3) {
        this.mail3 = mail3;
    }

    public String getMail1() {
        return mail1;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    public String getQatarID() {
        return qatarID;
    }

    public void setQatarID(String qatarID) {
        this.qatarID = qatarID;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getHealthCardID() {
        return healthCardID;
    }

    public void setHealthCardID(String healthCardID) {
        this.healthCardID = healthCardID;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getInitials(){
        String[] nameInitials = name.split(" ");
        String[] familyInitials = familyName.split(" ");
        String initials = "";
        for(String initial : nameInitials){
            initials += initial.charAt(0);
        }
        for(String initial : familyInitials){
            initials += initial.charAt(0);
        }
        return initials;
    }

    @Override
    public String toString(){
        return employeeNumber +" - "+name+" "+familyName;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Employee.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Employee other = (Employee) obj;
        if ((this.employeeNumber == null) ? (other.employeeNumber != null) : !this.employeeNumber.equals(other.employeeNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.employeeNumber != null ? this.employeeNumber.hashCode() : 0);
        return hash;
    }
}
