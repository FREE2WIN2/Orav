package de.logilutions.orav.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import de.logilutions.orav.exception.DatabaseConfigException;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
public class DatabaseConnectionHolder {
    private final String host;
    private final String database;
    private final String user;
    private final String password;

    private final MysqlDataSource mysqlDataSource;
    public DatabaseConnectionHolder(ConfigurationSection configurationSection) throws DatabaseConfigException {
        this.host = configurationSection.getString("host");
        this.database = configurationSection.getString("database");
        this.user = configurationSection.getString("user");
        this.password = configurationSection.getString("password");

        if(host == null || database == null || user == null || password == null){
            throw new DatabaseConfigException("Missing database configuration properties! please check it in the plugin directory!");
        }
        this.mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName(host);
        mysqlDataSource.setDatabaseName(database);
        mysqlDataSource.setUser(user);
        mysqlDataSource.setPassword(password);
    }

    public Connection getConnection() throws SQLException {
        return mysqlDataSource.getConnection();
    }
}
