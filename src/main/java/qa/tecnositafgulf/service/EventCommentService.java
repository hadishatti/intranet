package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.events.CompanyEvent;
import qa.tecnositafgulf.model.events.EventComment;

import java.util.List;

/**
 * Created by hadi on 12/24/17.
 */
public interface EventCommentService {
    void saveComment(EventComment eventComment);

    void removeComment(EventComment eventComment);

    List<EventComment> listCommentsByEvent(CompanyEvent companyEvent);
}
