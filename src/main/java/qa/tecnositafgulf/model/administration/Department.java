package qa.tecnositafgulf.model.administration;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "department", uniqueConstraints = {@UniqueConstraint(columnNames={"name", "companyID"})})
@NamedQueries({
        @NamedQuery(name = "Department.listAll", query = "SELECT department FROM Department department"),
        @NamedQuery(name = "Department.listAllOrderByChartId", query = "SELECT department FROM Department department ORDER BY department.chartId"),
        @NamedQuery(name = "Department.countAll", query = "SELECT count(department) FROM Department department")
})
public class Department implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "companyID")
    private Company company;

    private Long parentId;

    private Long secondParentId;

    private String color;

    private boolean isParentEmployee;

    private boolean isSecondParentEmployee;

    private Long chartId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public Long getChartId() {
        return chartId;
    }

    public void setChartId(Long chartId) {
        this.chartId = chartId;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        return company.equals(that.company);

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 53 * hash +(this.company != null ? this.company.hashCode() : 0);
        return hash;
    }
}
