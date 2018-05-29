package qa.tecnositafgulf.dao.events;

import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.model.events.EventComment;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
public interface EventCommentDao {
    void save(EventComment eventComment);

    void remove(EventComment eventComment);

    List<EventComment> listByEvent(CompanyEvent companyEvent);
}
