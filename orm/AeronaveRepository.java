import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class AeronaveRepository {
    private static final Logger logger = LoggerFactory.getLogger(AeronaveRepository.class);

    private Database database;
    private Dao<Aeronave, Integer> dao;
    private List<Aeronave> loadedAeronaves;
    private Aeronave loadedAeronave;

    public AeronaveRepository(Database database) {
        this.database = database;
        this.loadedAeronaves = new ArrayList<>();
        try {
            this.dao = DaoManager.createDao(database.getConnection(), Aeronave.class);
            TableUtils.createTableIfNotExists(database.getConnection(), Aeronave.class);
        } catch (SQLException e) {
            logger.error("Erro ao configurar o DAO ou criar a tabela", e);
        }
    }

    public Aeronave create(Aeronave aeronave) {
        try {
            int nrows = dao.create(aeronave);
            if (nrows == 0) {
                throw new SQLException("Erro: objeto não salvo");
            }
            this.loadedAeronave = aeronave;
            loadedAeronaves.add(aeronave);
        } catch (SQLException e) {
            logger.error("Erro ao criar aeronave", e);
        }
        return aeronave;
    }

    public void update(Aeronave aeronave) {
        try {
            dao.update(aeronave);
            loadedAeronaves.removeIf(a -> a.getId() == aeronave.getId());
            loadedAeronaves.add(aeronave);
        } catch (SQLException e) {
            logger.error("Erro ao atualizar aeronave", e);
        }
    }

    public void delete(Aeronave aeronave) {
        try {
            dao.delete(aeronave);
            loadedAeronaves.removeIf(a -> a.getId() == aeronave.getId());
        } catch (SQLException e) {
            logger.error("Erro ao deletar aeronave", e);
        }
    }

    public Aeronave loadFromId(int id) {
        try {
            this.loadedAeronave = dao.queryForId(id);
            if (this.loadedAeronave != null) {
                this.loadedAeronaves.add(this.loadedAeronave);
            }
        } catch (SQLException e) {
            logger.error("Erro ao carregar aeronave pelo ID", e);
        }
        return this.loadedAeronave;
    }

    public List<Aeronave> loadAll() {
        try {
            this.loadedAeronaves = dao.queryForAll();
            if (!this.loadedAeronaves.isEmpty()) {
                this.loadedAeronave = this.loadedAeronaves.get(0);
            }
        } catch (SQLException e) {
            logger.error("Erro ao carregar todas as aeronaves", e);
        }
        return this.loadedAeronaves;
    }

    // Getters and setters omitted for brevity...
}
