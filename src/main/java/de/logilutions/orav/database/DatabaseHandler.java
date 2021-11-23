package de.logilutions.orav.database;

import de.logilutions.orav.Orav;
import de.logilutions.orav.OravPlugin;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.session.PlaySession;
import de.logilutions.orav.team.OravTeam;
import de.logilutions.orav.team.TeamColor;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class DatabaseHandler {
    private final DatabaseConnectionHolder databaseConnectionHolder;

    public OravPlayer readOravPlayer(UUID uuid, long oravId) {
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM player WHERE uuid = ? AND fk_orav_id = ?");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setLong(2, oravId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new OravPlayer(
                        resultSet.getLong("id"),
                        readTeam(resultSet.getLong("fk_team_id")),
                        UUID.fromString(resultSet.getString("uuid")),
                        resultSet.getBoolean("dropped_out")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private OravTeam readTeam(long teamId) {
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM team WHERE id = ?");
            preparedStatement.setLong(1, teamId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new OravTeam(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        TeamColor.valueOf(resultSet.getString("color"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startSession(OravPlayer oravPlayer) {

    }

    public Orav readOrav(long oravId) {
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orav WHERE id = ?");
            preparedStatement.setLong(1, oravId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Orav(
                        resultSet.getInt("id"),
                        resultSet.getLong("play_time_day_one") * 60 * 1000,
                        resultSet.getLong("play_time") * 60 * 1000,
                        resultSet.getObject("start_date", LocalDateTime.class)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
