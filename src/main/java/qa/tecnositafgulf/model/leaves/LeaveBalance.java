package qa.tecnositafgulf.model.leaves;

import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by hadi on 5/22/18.
 */
@Entity
@Table(name = "leave_balance", uniqueConstraints = {@UniqueConstraint(columnNames={"employeeID","type"})})
@NamedQueries({
        @NamedQuery(name = "LeaveBalance.getBalance", query = "SELECT balance FROM LeaveBalance balance WHERE balance.employee = :employee AND balance.type = :type")
})
public class LeaveBalance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employeeID")
    private Employee employee;

    private Integer value;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
