package Service;

import DAOs.ClearDAO;
import DAOs.DatabaseUtil;
import ReqRes.ResultMessage;

import java.sql.SQLException;

/**
 * Class to manage the clearing of all DB tables
 */
public class ClearService {
    /**
     * Constructor method that initiates clearing through ClearDAO
     * @return ResultMessage
     */
    public ResultMessage clear() {
        DatabaseUtil DB = new DatabaseUtil();
        DB.open();
        ClearDAO clearDAO = new ClearDAO(DB.getConn());
        clearDAO.clearDatabase();

        DB.close(true);

        return new ResultMessage(null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                "Clear Succeeded ", true);
    }
}
