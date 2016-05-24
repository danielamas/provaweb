package dao;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAO {

	private static Logger log = LoggerFactory.getLogger(DAO.class);
	protected EntityManager em;
	protected static EntityManagerFactory factory = null;

	public DAO() {
		em = getEntityManager();
	}

	public static EntityManager getEntityManager() {
		EntityManager em = null;
		try {
			factory =  Persistence.createEntityManagerFactory ("movie-PU");
			em = factory.createEntityManager();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return em;
	}
}
