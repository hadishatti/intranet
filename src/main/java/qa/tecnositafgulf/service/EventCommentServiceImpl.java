package qa.tecnositafgulf.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.events.EventCommentDao;
import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.model.events.EventComment;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
@Service
public class EventCommentServiceImpl implements EventCommentService {

    @Autowired
    private EventCommentDao eventCommentDao;

    @Transactional
    public void saveComment(EventComment eventComment) {
        eventCommentDao.save(eventComment);
    }

    @Transactional
    public void removeComment(EventComment eventComment) {
        eventCommentDao.remove(eventComment);
    }

    @Transactional
    public List<EventComment> listCommentsByEvent(CompanyEvent companyEvent) {
        return eventCommentDao.listByEvent(companyEvent);
    }
}
