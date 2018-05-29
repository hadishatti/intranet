package qa.tecnositafgulf.model.companyInfo;

import qa.tecnositafgulf.config.ProjectsStatusEnum;
import qa.tecnositafgulf.model.administration.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ameljo on 5/16/18.
 */
@Entity
@Table(name = "projects")
@NamedQueries({
        @NamedQuery(name = "Project.listAll", query = "SELECT project FROM Project project")
})
public class Project implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String projectCode;

    @OneToOne
    @JoinColumn(name = "managerID")
    public Employee manager;

    @Lob
    private String content;

    private String link;

    private String name;

    @Lob
    private String image;

    private Date startingDate;

    private Date actualStartingDate;

    private Date endingDate;

    private Date actualEndingDate;

    private ProjectsStatusEnum status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ProjectsStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ProjectsStatusEnum status) {
        this.status = status;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public Date getActualStartingDate() {
        return actualStartingDate;
    }

    public void setActualStartingDate(Date actualStartingDate) {
        this.actualStartingDate = actualStartingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public Date getActualEndingDate() {
        return actualEndingDate;
    }

    public void setActualEndingDate(Date actualEndingDate) {
        this.actualEndingDate = actualEndingDate;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }
}
