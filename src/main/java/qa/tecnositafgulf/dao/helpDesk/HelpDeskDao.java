package qa.tecnositafgulf.dao.helpDesk;

import qa.tecnositafgulf.model.helpDesk.HelpDesk;
import qa.tecnositafgulf.searchcriteria.helpDesk.HelpDeskSearchCriteria;

import java.util.List;

/**
 * Created by klajdi on 20/05/18.
 */
public interface HelpDeskDao {

    void save(HelpDesk helpDesk);

    void remove(HelpDesk helpDesk);

    List<HelpDesk> listAll();

    List<HelpDesk> listHelpDesks(HelpDeskSearchCriteria helpDeskSearchCriteria);

    Integer countHelpDesk(HelpDeskSearchCriteria helpDeskSearchCriteria);

}
