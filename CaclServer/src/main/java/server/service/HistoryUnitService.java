package server.service;

import server.dao.HistoryUnitDAO;
import server.model.HistoryUnit;

import java.util.List;

public class HistoryUnitService {

    private HistoryUnitDAO historyUnitDAO = new HistoryUnitDAO();

    public HistoryUnitService() {
    }

    public void save(List<HistoryUnit> historyUnitList) {
        historyUnitDAO.saveMany(historyUnitList);
    }

    public List<HistoryUnit> getLastFifteen() {
        return historyUnitDAO.getLastFifteen();
    }

    public Long getLastID() {
        return historyUnitDAO.getLastID();
    }
}
