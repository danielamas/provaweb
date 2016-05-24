package dao;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.Movie;

public class MovieDAO extends DAO {

	private static Logger log = LoggerFactory.getLogger(MovieDAO.class);

	public Movie retrieveMovieByImdbID(String imdbID) {
		Movie movie = null;
		try {
			if(!em.isOpen()) {
				em = getEntityManager();
			}
			Query query = em.createQuery("from Movie where imdbID  = :imdbID");
			query.setParameter("imdbID",  imdbID);
			movie = (Movie) query.getSingleResult();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if(em != null) {
				em.close();
			}
		}
		return movie;
	}

	public Movie retrieveMovieByID(Long id) {
		Movie movie = null;
	    try {
	    	if(!em.isOpen()) {
				em = getEntityManager();
			}
	    	movie = em.find(Movie.class, id);
	    } finally {
	    	if(em != null) {
				em.close();
			}
	    }
	    return movie;
	}

	public List<Movie> retrieveMovieByName(Movie movie) {
		List<Movie> movies = null;
		try {
			if(!em.isOpen()) {
				em = getEntityManager();
			}
			Query query = em.createQuery("select m from Movie m where title like :title");
			query.setParameter("title", "%" + movie.getTitle() + "%");
			movies = (List<Movie>) query.getResultList();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if(em != null) {
				em.close();
			}
			if(factory != null) {
				factory.close();
			}
		}
		return movies;
	}

	public Movie save(Movie movie) throws Exception {
		EntityTransaction transaction = null;
		try {
			if(!em.isOpen()) {
				em = getEntityManager();
			}
			transaction = em.getTransaction();
			transaction.begin();
			movie = em.merge(movie);
			em.flush();
			transaction.commit();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			if(transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if(em != null) {
				em.close();
			}
		}
		return movie;
	}

	public boolean delete(Movie movie) {
		EntityTransaction transaction = null;
		boolean deleted = false;
		try {
			if(!em.isOpen()) {
				em = getEntityManager();
			}
			transaction = em.getTransaction();
			transaction.begin();
			movie = em.merge(movie);
			em.remove(movie);
			em.flush();
			transaction.commit();
			deleted = true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			if(transaction != null) {
				transaction.rollback();
			}
		} finally {
			if(em != null) {
				em.close();
			}
		}
		return deleted;
	}
}
