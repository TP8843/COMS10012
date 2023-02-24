package org.example;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DataService  implements AutoCloseable {
    private Connection c;

    DataService (String cs) throws DataServiceException {
        try {
            c = DriverManager.getConnection(cs);
        } catch (SQLException e) {
            throw new DataServiceException(e);
        }
    }

    @Override
    public void close() throws DataServiceException {
        try {
            c.close();
            c = null;
        } catch (SQLException e) {
            throw new DataServiceException(e);
        }
    }

    public List<Party> getParties () throws DataServiceException {
        String SQL = "SELECT id, name FROM Party";
        List<Party> output = new LinkedList<>();
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            ResultSet r = s.executeQuery();
            while (r.next()) {
                int id = r.getInt("id");
                String name = r.getString("name");
                output.add(new Party(id, name));
            }
            return output;
        } catch (SQLException e) {
            throw new DataServiceException(e);
        }
    }

    public Party getParty (int id) throws DataServiceException {
        String SQL = "SELECT name FROM Party WHERE id = ?";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            if (r.next()) {
                String name = r.getString("name");
                return new Party(id, name);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DataServiceException(e);
        }
    }
}