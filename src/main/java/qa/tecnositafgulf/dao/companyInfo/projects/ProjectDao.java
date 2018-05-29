package qa.tecnositafgulf.dao.companyInfo.projects;

import qa.tecnositafgulf.config.ProjectsStatusEnum;
import qa.tecnositafgulf.model.companyInfo.Project;
import qa.tecnositafgulf.searchcriteria.ProjectSearchCriteria;

import java.util.List;

/**
 * Created by ameljo on 5/16/18.
 */
public interface ProjectDao {

    void saveProject(Project project);

    void removeProject(Project project);

    List<Project> listAll();

    List<Project> getProjectList(ProjectSearchCriteria searchCriteria);

    int getProjectCount(ProjectSearchCriteria searchCriteria);

    List<Project> projectListByStatus(ProjectsStatusEnum statusEnum);

    List<Project> projectListByStatusOrderDate(ProjectsStatusEnum statusEnum, int limit);
}
