package client.dao;

import client.model.HistoryUnit;
import client.model.Operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class H2DBWorker implements H2DBConnector {

    private static final String CREATE_TABLE = "CREATE TABLE history_data " +
            "(id INTEGER not NULL PRIMARY KEY, " +
            " init_value DECIMAL(20,6), " +
            " operation VARCHAR(50), " +
            " op_value DECIMAL(20,6), " +
            " fin_value DECIMAL(20,6)) ";
    private static final String GET_DATA = "SELECT id, init_value, operation, op_value, fin_value FROM history_data";
    private static final String SAVE_DATA = "INSERT INTO history_data (id, init_value, operation, op_value, fin_value) VALUES (?, ?, ?, ?, ?)";
    private static final String CLEAR_DATA = "DELETE FROM history_data WHERE id BETWEEN ? AND ?";

    public void createTable() {
        try (Connection connection = connectToDB();
             PreparedStatement pstmnt = connection.prepareStatement(CREATE_TABLE)) {
            pstmnt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public List<HistoryUnit> getData() {

        try (Connection connection = connectToDB();
             PreparedStatement pstmnt = connection.prepareStatement(GET_DATA);
             ResultSet rs = pstmnt.executeQuery()) {

            ArrayList<HistoryUnit> historyUnits = new ArrayList<>();

            while (rs.next()) {
                historyUnits.add(new HistoryUnit(
                        rs.getLong("id"),
                        rs.getBigDecimal("init_value"),
                        Operation.valueOf(rs.getString("operation")),
                        rs.getBigDecimal("op_value"),
                        rs.getBigDecimal("fin_value")
                ));
            }

            return historyUnits;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Collections.emptyList();
    }


    public void saveData(HistoryUnit historyUnit) {

        try (Connection connection = connectToDB();

             PreparedStatement pstmnt = connection.prepareStatement(SAVE_DATA)) {

            pstmnt.setLong(1, historyUnit.getId());
            pstmnt.setBigDecimal(2, historyUnit.getInitVal());
            pstmnt.setString(3, historyUnit.getOperation().toString());
            pstmnt.setBigDecimal(4, historyUnit.getOpVal());
            pstmnt.setBigDecimal(5, historyUnit.getFinVal());
            pstmnt.addBatch();
            pstmnt.executeBatch();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void clearData(long startID, long endID) {
        try (Connection connection = connectToDB();
             PreparedStatement pstmnt = connection.prepareStatement(CLEAR_DATA)) {
            pstmnt.setLong(1, startID);
            pstmnt.setLong(2, endID);
            pstmnt.addBatch();
            pstmnt.executeBatch();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
