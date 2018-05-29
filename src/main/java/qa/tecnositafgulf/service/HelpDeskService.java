package qa.tecnositafgulf.service;

import qa.tecnositafgulf.model.helpDesk.HelpDesk;
import qa.tecnositafgulf.searchcriteria.helpDesk.HelpDeskSearchCriteria;

import java.util.List;

/**
 * Created by klajdi on 20/05/18.
 */
public interface HelpDeskService {

    void saveHelpDesk(HelpDesk helpDesk);

    void removeHelpDesk(HelpDesk helpDesk);

    List<HelpDesk> listHelpDesks();

    List<HelpDesk> listHelpDesks(HelpDeskSearchCriteria helpDeskSearchCriteria);

    Integer countHelpDesk(HelpDeskSearchCriteria helpDeskSearchCriteria);

}
