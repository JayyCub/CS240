package Service;

import DAOs.ClearDAO;
import DAOs.DatabaseUtil;
import ReqRes.ResultMessage;

import java.sql.SQLException;

public class ClearService {
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
