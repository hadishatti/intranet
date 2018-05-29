package qa.tecnositafgulf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qa.tecnositafgulf.dao.helpDesk.HelpDeskDao;
import qa.tecnositafgulf.model.helpDesk.HelpDesk;
import qa.tecnositafgulf.searchcriteria.helpDesk.HelpDeskSearchCriteria;

import java.util.List;

/**
 * Created by klajdi on 20/05/18.
 */
@Service
public class HelpDeskServiceImpl implements HelpDeskService {

    @Autowired
    private HelpDeskDao helpDeskDao;

    @Transactional
    public void saveHelpDesk(HelpDesk helpDesk) {
        helpDeskDao.save(helpDesk);
    }

    @Transactional
    public void removeHelpDesk(HelpDesk helpDesk) {
        helpDeskDao.remove(helpDesk);
    }

    @Transactional
    public List<HelpDesk> listHelpDesks() {
        return helpDeskDao.listAll();
    }

    @Override
    @Transactional
    public List<HelpDesk> listHelpDesks(HelpDeskSearchCriteria helpDeskSearchCriteria) {
        return helpDeskDao.listHelpDesks(helpDeskSearchCriteria);
    }

    @Override
    @Transactional
    public Integer countHelpDesk(HelpDeskSearchCriteria helpDeskSearchCriteria) {
        return helpDeskDao.countHelpDesk(helpDeskSearchCriteria);
    }
}