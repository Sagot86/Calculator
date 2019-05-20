package client.service;

import client.dao.H2DBWorker;
import client.model.HistoryUnit;
import client.model.JsonConverterNTransporter;

import java.util.ArrayList;
import java.util.List;

public class DataTransferService {

    private final H2DBWorker dbWorker = new H2DBWorker();
    private final JsonConverterNTransporter jcNt = new JsonConverterNTransporter();

    public void onAppStart() {
        /* Create inmemoryDB */
        dbWorker.createTable();
    }

    /**
     * Load history from server DB & convert to List<String>
     */
    public List<String> getHistory() {
        List<HistoryUnit> history = jcNt.loadHistory();
        return unitToString(history);
    }

    void saveToDB(HistoryUnit unit) {
        dbWorker.saveData(unit);
    }

    /**
     * Conversion List<HistoryUnit> to List<String>
     */
    private List<String> unitToString(List<HistoryUnit> units) {
        List<String> testList = new ArrayList<>();
        for (HistoryUnit unit : units) {
            testList.add(
                    unit.getInitVal() +
                            " " +
                            unit.getOperation().getSymbol() +
                            " " +
                            unit.getOpVal() +
                            " = " +
                            unit.getFinVal()
            );
        }
        return testList;
    }

    public void onAppStop() {
        //выгрузка оставшихся записей из памяти в основную базу
        jcNt.uploadHistory(dbWorker.getData());
    }

}
