package client.service;

import client.dao.H2DBWorker;
import client.model.Calculator;
import client.model.HistoryUnit;
import client.model.JsonConverterNTransporter;
import client.model.Operation;

import java.math.BigDecimal;
import java.util.List;

public class InitializeService {

    private final H2DBWorker dbWorker = new H2DBWorker();
    private final JsonConverterNTransporter jcNt = new JsonConverterNTransporter();
    private final Calculator calculator = new Calculator();
    private long idCount;
    private long firstCount;

    public void onAppStart() {
        //Создание inmemory таблицы
        dbWorker.createTable();

        //проверка БД на сервере на наличие содержимого
        if (jcNt.dbIsntEmpty()) {

            //Подгрузка посделних пятнадцати записей с сервера
            List<HistoryUnit> history = jcNt.loadHistory();
            System.out.println("HISTORY:");
            //Вывод подгруженных записей на экран
            for (HistoryUnit hu : history) {
                printResult(hu);
            }
            System.out.println("HISTORY END");
            //Вычисление стартового ID и IDcount
            idCount = history.get(history.size() - 1).getId() + 1;
            firstCount = idCount;
        }
    }

    public void calculate() {

        //Новый юнит для записи
        HistoryUnit unitForCalc = new HistoryUnit();

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
            unitForCalc = calculator.calculate(unitForCalc);
        } catch (ArithmeticException ex) {
            System.out.println("Здесь на ноль делить нельзя!");
            return;
        }

        //Вывод строки на печать в окно консоли
        printResult(unitForCalc);
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

    private void printResult(HistoryUnit historyUnit) {

        String unitToPrint = historyUnit.getId() +
                " " +
                historyUnit.getInitVal() +
                " " +
                historyUnit.getOperation().getSymbol() +
                " " +
                historyUnit.getOpVal() +
                " = " +
                historyUnit.getFinVal();

        System.out.println(unitToPrint);

    }


    public void printFromDB() {

        List<HistoryUnit> testList = dbWorker.getData();

        for (HistoryUnit hu2d : testList) {
            printResult(hu2d);
        }

    }

    public void inAppEnd() {
        //выгрузка оставшихся записей из памяти в основную базу
        jcNt.uploadHistory(dbWorker.getData());
    }

}
