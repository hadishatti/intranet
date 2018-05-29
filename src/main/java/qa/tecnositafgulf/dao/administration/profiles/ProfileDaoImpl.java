package qa.tecnositafgulf.dao.administration.profiles;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import qa.tecnositafgulf.model.administration.Profile;
import qa.tecnositafgulf.searchcriteria.ProfileSearchCriteria;

import javax.persistence.*;
import java.util.List;

/**
 * Created by hadi on 12/17/17.
 */
@Repository
public class ProfileDaoImpl implements ProfileDao{
    @PersistenceContext
    private EntityManager em;

    public void save(Profile profile) {
        em.merge(profile);
    }

    public void remove(Profile profile) throws ConstraintViolationException {
        em.remove(em.contains(profile) ? profile : em.merge(profile));
    }

    public List<Profile> listAll() {
        TypedQuery<Profile> query = em.createNamedQuery("Profile.listAll", Profile.class);
        return query.getResultList();
    }

    @Override
    public Integer getProfileCount(ProfileSearchCriteria profileSearchCriteria) {
        Query query = profileSearchCriteria.getCountQuery(em);
        Number cResults = (Number) query.getSingleResult();
        return cResults.intValue();
    }

    @Override
    public List<Profile> getProfileList(ProfileSearchCriteria profileSearchCriteria) {
        Query query = profileSearchCriteria.toQuery(em);
        return query.getResultList();
    }

    @Override
    public Profile getDefaultProfile(){
        TypedQuery<Profile> query = em.createNamedQuery("Profile.getDefaultProfile", Profile.class);
        Profile profile;
        try {
            profile = query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
        return profile;
    }

    @Override
    public Profile findByName(String name) {
        TypedQuery<Profile> query = em.createNamedQuery("Profile.findByName", Profile.class);
        query.setParameter("name", name);
        return  query.getSingleResult();
    }

    public int countAll() {
        Query query = em.createNamedQuery("Profile.countAll");
        return ((Long)query.getSingleResult()).intValue();
    }
}
