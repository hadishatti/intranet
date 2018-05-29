package qa.tecnositafgulf.dao.events;

import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.searchcriteria.companyevents.CompanyEventsSearchCriteria;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
public interface EventDao {
    void save(CompanyEvent companyEvent);

    void remove(CompanyEvent companyEvent);

    List<CompanyEvent> listAll();

    CompanyEvent find(CompanyEvent companyEvent);

    List<CompanyEvent> listEvents(CompanyEventsSearchCriteria companyEventsSearchCriteria);

    Integer getCountCompanyEvents(CompanyEventsSearchCriteria companyEventsSearchCriteria);
}
