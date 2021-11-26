package de.logilutions.orav.database;

import de.logilutions.orav.Orav;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.session.PlaySession;
import de.logilutions.orav.team.OravTeam;
import de.logilutions.orav.team.TeamColor;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DatabaseHandler {
    private final DatabaseConnectionHolder databaseConnectionHolder;

    public OravPlayer readOravPlayer(UUID uuid, long oravId) {
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT player.id, uuid, fk_team_id, dropped_out" +
                            " FROM player,team" +
                            " WHERE uuid = ? AND fk_orav_id = ? AND player.fk_team_id = team.id");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setLong(2, oravId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return makeOravPlayerFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private OravPlayer makeOravPlayerFromResultSet(ResultSet resultSet) throws SQLException {
        return new OravPlayer(
                resultSet.getLong("id"),
                readTeam(resultSet.getLong("fk_team_id")),
                UUID.fromString(resultSet.getString("uuid")),
                resultSet.getBoolean("dropped_out")
        );
    }

    public OravTeam readTeam(long teamId) {
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM team " +
                            "JOIN color ON color.id = team.fk_color_id " +
                            "WHERE team.id = ?");
            preparedStatement.setLong(1, teamId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new OravTeam(
                        resultSet.getLong("team.id"),
                        resultSet.getString("team.name"),
                        resultSet.getString("team.shortname"),
                        new TeamColor(
                                resultSet.getLong("color.id"),
                                resultSet.getString("color.hexcode"),
                                resultSet.getString("color.name")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
                        resultSet.getObject("start_date", LocalDateTime.class),
                        resultSet.getObject("earliest_login", LocalTime.class),
                        resultSet.getObject("latest_login", LocalTime.class),
                        Orav.State.valueOf(resultSet.getString("state")),
                        resultSet.getLong("protection_time"),
                        resultSet.getLong("protection_time_after")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateOrav(Orav orav) {
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE orav SET state = ? WHERE id = ?");
            preparedStatement.setString(1, orav.getState().name());
            preparedStatement.setLong(2, orav.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OravPlayer getTeamMate(OravPlayer oravPlayer) {
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM player WHERE fk_team_id = ?");
            preparedStatement.setLong(1, oravPlayer.getOravTeam().getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (!resultSet.getString("uuid").equalsIgnoreCase(oravPlayer.getUuid().toString())) {
                    return makeOravPlayerFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void startSession(OravPlayer oravPlayer) {
        if (oravPlayer.getCurrentSession() == null) {
            return;
        }
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO session(`fk_player_id`, `login`) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, oravPlayer.getId());
            preparedStatement.setObject(2, oravPlayer.getCurrentSession().getStart());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                oravPlayer.getCurrentSession().setId(resultSet.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void stopSession(OravPlayer oravPlayer) {
        if (oravPlayer.getCurrentSession() == null) {
            return;
        }
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE session SET logout = ? where id = ?");
            preparedStatement.setObject(1, LocalDateTime.now());
            preparedStatement.setLong(2, oravPlayer.getCurrentSession().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<PlaySession> getSessions(OravPlayer oravPlayer) {
        List<PlaySession> playSessionList = new ArrayList<>();
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM session WHERE fk_player_id = ?");
            preparedStatement.setLong(1, oravPlayer.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                playSessionList.add(createSessionFromResultSet(resultSet, oravPlayer));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playSessionList;
    }

    private PlaySession createSessionFromResultSet(ResultSet resultSet, OravPlayer oravPlayer) throws SQLException {
        return new PlaySession(
                resultSet.getLong("id"),
                oravPlayer,
                resultSet.getObject("login", LocalDateTime.class),
                resultSet.getObject("logout", LocalDateTime.class)
        );
    }

    public Collection<PlaySession> getSessionsFromToday(OravPlayer oravPlayer) {
        Collection<PlaySession> playSessionList = getSessions(oravPlayer);
        LocalDate localDate = LocalDate.now();
        playSessionList.removeIf(playSession -> localDate.isAfter(playSession.getStart().toLocalDate()));
        return playSessionList;
    }

    public void updatePlayer(OravPlayer oravPlayer) {
        if (oravPlayer == null) {
            return;
        }
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player SET dropped_out = ? where id = ?");
            preparedStatement.setObject(1, oravPlayer.isDroppedOut());
            preparedStatement.setLong(2, oravPlayer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<OravPlayer> getAllPlayers(long oravId) {
        List<OravPlayer> oravPlayers = new ArrayList<>();
        try (Connection connection = databaseConnectionHolder.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT player.id, uuid, fk_team_id, dropped_out" +
                            " FROM player,team" +
                            " WHERE fk_orav_id = ? AND player.fk_team_id = team.id");
            preparedStatement.setLong(1, oravId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                oravPlayers.add(makeOravPlayerFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oravPlayers;
    }
}
