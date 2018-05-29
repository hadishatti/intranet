package qa.tecnositafgulf.dao.companyInfo.projects;

import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.config.ProjectsStatusEnum;
import qa.tecnositafgulf.model.companyInfo.Project;
import qa.tecnositafgulf.searchcriteria.ProjectSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by ameljo on 5/16/18.
 */
@Repository
public class ProjectDaoImpl implements ProjectDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void saveProject(Project project) {
        em.merge(project);
    }

    @Override
    public void removeProject(Project project) {
        em.remove(em.contains(project) ? project : em.merge(project));
    }

    @Override
    public List<Project> listAll() {
        TypedQuery<Project> query = em.createNamedQuery("Project.listAll", Project.class);
        return query.getResultList();
    }

    @Override
    public List<Project> getProjectList(ProjectSearchCriteria searchCriteria) {
        Query query = searchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public int getProjectCount(ProjectSearchCriteria searchCriteria) {
        Query query = searchCriteria.getCountQuery(em);
        Number count = (Number) query.getSingleResult();
        return count.intValue();
    }

    @Override
    public List<Project> projectListByStatus(ProjectsStatusEnum statusEnum) {
        Query query = em.createQuery("SELECT project FROM Project project WHERE project.status = :status");
        query.setParameter("status", statusEnum);
        return query.getResultList();
    }

    @Override
    public List<Project> projectListByStatusOrderDate(ProjectsStatusEnum statusEnum, int limit) {
        Query query = em.createQuery("SELECT project FROM Project project WHERE project.status = :status ORDER BY project.startingDate DESC");
        query.setParameter("status", statusEnum);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
