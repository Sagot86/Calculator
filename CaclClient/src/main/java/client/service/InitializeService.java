package client.service;

import client.dao.H2DBWorker;
import client.model.Calculator;
import client.model.HistoryUnit;
import client.model.JsonConverterNTransporter;
import client.model.Operation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InitializeService {

    private final H2DBWorker dbWorker = new H2DBWorker();
    private final JsonConverterNTransporter jcNt = new JsonConverterNTransporter();
    private final Calculator calculator = new Calculator();
    private HistoryUnit unitForCalc = new HistoryUnit();
    private long idCount;
    private long firstCount;

    public void onAppStart() {

        /* Create inmemoryDB */
        dbWorker.createTable();

        //Присвоение значений счетчикам
        idCount = jcNt.getLastID() + 1;
        firstCount = idCount;

    }

    /** Load history from server DB % convert to List<String> */
    public List<String> getHistory() {
        List<HistoryUnit> history = jcNt.loadHistory();
        return unitToString(history);
    }

    public void calculate() {
        //Ставим ID
        unitForCalc.setId(idCount);

        //Вычислить начальное значение
        unitForCalc.setInitVal(getNumber(getStringNumber()));

        //Вычислить знак
        unitForCalc.setOperation(Operation.DIVISION);

        //Вычислить второе значение в выражении
        unitForCalc.setOpVal(getNumber("0"));

        //Произвести рассчет

        try {
            unitForCalc.setFinVal(calculator.calculate(unitForCalc));
        } catch (ArithmeticException ex) {
            System.out.println("Здесь на ноль делить нельзя!");
            return;
        }

        //Вывод строки на печать в окно консоли

        dbWorker.saveData(unitForCalc);
        idCount++;

        //Выгрузка записей в основную базу в случае, если в памяти их больше пятидесяти.
        if (idCount - firstCount > 50) {
            jcNt.uploadHistory(dbWorker.getData());
            dbWorker.clearData(firstCount, idCount);
            firstCount = idCount;
        }
    }


    private String getStringNumber() {
        //В этом методе формируется строка из нажатий кнопок на калькуляторе, пока заглушка, возвращающая одно число.
        return "5";
    }

    private BigDecimal getNumber(String inputNum) {
        return new BigDecimal(inputNum);
    }

    /** Conversion List<HistoryUnit> to List<String> */
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

    /** Conversion HistoryUnit to String */
    private String unitToString(HistoryUnit unit) {
        return
                unit.getInitVal() +
                " " +
                unit.getOperation().getSymbol() +
                " " +
                unit.getOpVal() +
                " = " +
                unit.getFinVal();
    }

    public void onAppStop() {
        //выгрузка оставшихся записей из памяти в основную базу
        jcNt.uploadHistory(dbWorker.getData());
    }

}
