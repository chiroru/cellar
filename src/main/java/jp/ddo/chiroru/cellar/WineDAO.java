package jp.ddo.chiroru.cellar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jp.ddo.chiroru.cellar.util.ConnectionHelper;

/**
 * <p>
 * WineのDaoです.
 * </p>
 */
public class WineDAO {

    /**
     * <p>
     * 永続化されているWine情報を全て検索します.
     * </p>
     * @return Wine情報のリスト
     */
    public final List<Wine> findAll() {
        List<Wine> list = new ArrayList<Wine>();
        Connection c = null;
        String sql = "SELECT * FROM wine ORDER BY name";
        try {
            c = ConnectionHelper.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return list;
    }

    /**
     * <p>
     * 指定されたWineの銘柄を基に永続化されたWine情報の検索を行います.
     * </p>
     * @param name Wineの銘柄
     * @return Wine情報のリスト
     */
    public final List<Wine> findByName(final String name) {
        List<Wine> list = new ArrayList<Wine>();
        Connection c = null;
        String sql = "SELECT * FROM wine as e "
                + "WHERE UPPER(name) LIKE ? "
                + "ORDER BY name";
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "%" + name.toUpperCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return list;
    }

    public Wine findById(int id) {
        String sql = "SELECT * FROM wine WHERE id = ?";
        Wine wine = null;
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                wine = processRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return wine;
    }

    /**
     * <p>
     * Wine情報を永続化します.
     * </p>
     * @param wine wine情報
     * @return 永続化したwine情報
     */
    public final Wine save(final Wine wine) {
        return wine.getId() > 0 ? update(wine) : create(wine);
    }

    public Wine create(Wine wine) {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = ConnectionHelper.getConnection();
            ps = c.prepareStatement("INSERT INTO wine (name, grapes, country, region, year, picture, description) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    new String[] { "ID" });
            ps.setString(1, wine.getName());
            ps.setString(2, wine.getGrapes());
            ps.setString(3, wine.getCountry());
            ps.setString(4, wine.getRegion());
            ps.setString(5, wine.getYear());
            ps.setString(6, wine.getPicture());
            ps.setString(7, wine.getDescription());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            // Update the id in the returned object. This is important as this value must be returned to the client.
            int id = rs.getInt(1);
            wine.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return wine;
    }

    public Wine update(Wine wine) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps = c.prepareStatement("UPDATE wine SET name=?, grapes=?, country=?, region=?, year=?, picture=?, description=? WHERE id=?");
            ps.setString(1, wine.getName());
            ps.setString(2, wine.getGrapes());
            ps.setString(3, wine.getCountry());
            ps.setString(4, wine.getRegion());
            ps.setString(5, wine.getYear());
            ps.setString(6, wine.getPicture());
            ps.setString(7, wine.getDescription());
            ps.setInt(8, wine.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
        return wine;
    }

    public boolean remove(int id) {
        Connection c = null;
        try {
            c = ConnectionHelper.getConnection();
            PreparedStatement ps =
                    c.prepareStatement("DELETE FROM wine WHERE id=?");
            ps.setInt(1, id);
            int count = ps.executeUpdate();
            return count == 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            ConnectionHelper.close(c);
        }
    }

    /**
     * <p>
     * DBのRowをオブジェクトに変換します.
     * </p>
     * @param rs
     * @return
     * @throws SQLException 変換に失敗した場合
     */
    protected Wine processRow(ResultSet rs)
            throws SQLException {
        Wine wine = new Wine();
        wine.setId(rs.getInt("id"));
        wine.setName(rs.getString("name"));
        wine.setGrapes(rs.getString("grapes"));
        wine.setCountry(rs.getString("country"));
        wine.setRegion(rs.getString("region"));
        wine.setYear(rs.getString("year"));
        wine.setPicture(rs.getString("picture"));
        wine.setDescription(rs.getString("description"));
        return wine;
    }
}
