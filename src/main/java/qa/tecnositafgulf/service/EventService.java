package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.searchcriteria.companyevents.CompanyEventsSearchCriteria;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
public interface EventService {
    void saveEvent(CompanyEvent companyEvent);

    void removeEvent(CompanyEvent companyEvent);

    List<CompanyEvent> listEvents();

    List<CompanyEvent> listEvents(CompanyEventsSearchCriteria companyEventsSearchCriteria);

    Integer getCountCompanyEvents(CompanyEventsSearchCriteria companyEventsSearchCriteria);


    CompanyEvent findEvent(CompanyEvent companyEvent);
}
