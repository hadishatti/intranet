package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.events.EventDao;
import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.searchcriteria.companyevents.CompanyEventsSearchCriteria;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
@Service
public class EventServiceImpl implements EventService{

    @Autowired
    private EventDao eventDao;

    @Transactional
    public void saveEvent(CompanyEvent companyEvent) {
        eventDao.save(companyEvent);
    }

    @Transactional
    public void removeEvent(CompanyEvent companyEvent) {
        eventDao.remove(companyEvent);
    }

    @Transactional
    public List<CompanyEvent> listEvents() {
        return eventDao.listAll();
    }

    @Transactional
    public CompanyEvent findEvent(CompanyEvent companyEvent){
        return eventDao.find(companyEvent);
    }

    @Override
    @Transactional
    public List<CompanyEvent> listEvents(CompanyEventsSearchCriteria companyEventsSearchCriteria) {
        return eventDao.listEvents(companyEventsSearchCriteria);
    }

    @Override
    @Transactional
    public Integer getCountCompanyEvents(CompanyEventsSearchCriteria companyEventsSearchCriteria) {
        return eventDao.getCountCompanyEvents(companyEventsSearchCriteria);
    }
}
