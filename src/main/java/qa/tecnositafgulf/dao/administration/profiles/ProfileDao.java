package qa.tecnositafgulf.dao.administration.profiles;

import org.hibernate.exception.ConstraintViolationException;
import qa.tecnositafgulf.model.administration.Profile;
import qa.tecnositafgulf.searchcriteria.ProfileSearchCriteria;

import java.util.List;

/**
 * Created by hadi on 12/17/17.
 */
public interface ProfileDao {

    void save(Profile profile);

    void remove(Profile profile) throws ConstraintViolationException;

    List<Profile> listAll();

    Integer getProfileCount(ProfileSearchCriteria profileSearchCriteria);

    List<Profile> getProfileList(ProfileSearchCriteria profileSearchCriteria);

    Profile getDefaultProfile();

    Profile findByName(String name);

    int countAll();
}
