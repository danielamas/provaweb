package ws;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.omertron.omdbapi.OmdbApi;
import com.omertron.omdbapi.model.OmdbVideoBasic;
import com.omertron.omdbapi.model.OmdbVideoFull;
import com.omertron.omdbapi.model.SearchResults;
import com.omertron.omdbapi.tools.OmdbBuilder;

import bd.CreateBd;
import biz.Movie;
import dao.MovieDAO;

@Path("/action")
public class Action {

	private static Logger log = LoggerFactory.getLogger(Action.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{title}")
	public String showMoviesBD(@PathParam("title") String title) {
		String resp = "";
		Movie movie = new Movie();
		if(title.contains("*")){
			title = title.replaceAll("*", "%");
		}
		movie.setTitle(title);
		MovieDAO movieDAO = new MovieDAO();
		List<Movie> movieListBD = new ArrayList<Movie>();
		movieListBD = movieDAO.retrieveMovieByName(movie);

		List<Movie> movieListAPI = new ArrayList<Movie>();
		if(!movie.getTitle().equals("")) {
			movieListAPI = searchMovieInOMDB(movie.getTitle());
		}
		if(!movieListBD.isEmpty() && !movieListAPI.isEmpty()) {
			List<Movie> movieListAPIRemove = new ArrayList<Movie>();
			for(Movie movieBD : movieListBD){
				for(Movie movieAPI : movieListAPI){
					if(movieAPI.getImdbID().equalsIgnoreCase(movieBD.getImdbID())) {
						movieListAPIRemove.add(movieAPI);
						break;
					}
				}
			}
			if(!movieListAPIRemove.isEmpty()) {
				movieListAPI.removeAll(movieListAPIRemove);
			}
		}

		movieListBD.addAll(movieListAPI);
		
		if(!movieListBD.isEmpty()) {
			resp = objectToJSON(movieListBD);
		}
		return resp;
	}

	private List<Movie> searchMovieInOMDB(String movieName) {
		List<Movie> movieList = new ArrayList<Movie>();
		OmdbApi omdb = new OmdbApi();
		try {
			SearchResults results = omdb.search(new OmdbBuilder().setSearchTerm(movieName).build());
			if(results.isResponse()) {
				if(results.getResults() != null && !results.getResults().isEmpty()) {
					List<OmdbVideoBasic> list = results.getResults();
					Movie movie;
					for(OmdbVideoBasic item : list) {
						movie = new Movie();
						movie.setImdbID(item.getImdbID());
						movie.setTitle(item.getTitle());
						movie.setType(item.getType());
						movieList.add(movie);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return movieList;
	}

	@POST
	@Path("/searchFilme/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({"application/json"})
	public String searchMovie(String movieName) {
		String resp = "";
		if(movieName != null) {
			Movie movie = new Movie();
			if(movieName.contains("*")) {
				movie.setTitle(movieName.replaceAll("*", "%"));
			}
			MovieDAO movieDAO = new MovieDAO();
			List<Movie> movieList = movieDAO.retrieveMovieByName(movie);
			if(movieList == null || movieList.isEmpty()) {
				searchMovieInOMDB(movieName.replaceAll("*", ""));
			}
			if(movieList != null && !movieList.isEmpty()) {
				resp = objectToJSON(movieList);
			}
		}
		return resp;
	}

	@POST
	@Path("/searchFilmeById/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({"application/json"})
	public String searchMovieByID(String paramOmdbID) {
		String resp = "";
		String[] param = paramOmdbID.split("=");
		if(param != null && param.length > 1) {
			MovieDAO movieDAO = new MovieDAO();
			Movie movie = movieDAO.retrieveMovieByImdbID(param[1]);
			if(movie == null) {
				movie = searchMovieInOMDBById(param[1]);
			}
			if(movie != null) {
				resp = objectToJSON(movie);
			}
		}
		return resp;
	}

	private Movie searchMovieInOMDBById(String id) {
		Movie movie = null;
		OmdbApi omdb = new OmdbApi();
		try {
			OmdbVideoFull result = omdb.getInfo(new OmdbBuilder().setImdbId(id).build());
			if(result != null) {
				movie = new Movie();
				movie.setTitle(result.getTitle());
				movie.setYear(result.getYear());
				movie.setReleased(result.getReleased());
				movie.setGenre(result.getGenre());
				movie.setType(result.getType());
				movie.setIsOMDB("true");
				movie.setImdbID(result.getImdbID());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return movie;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({"application/json"})
	public String addMovie(String parameters) {
		String resp = "";
		Movie movie = null;
		String[] param = parameters.split("=");
		if(param != null && param.length > 10) {
			movie = new Movie();
			movie.setImdbID(param[1]);
			movie.setImdbID(param[3]);
			movie.setImdbID(param[5]);
			movie.setImdbID(param[7]);
			movie.setImdbID(param[9]);
			movie.setImdbID(param[11]);
			try {
				MovieDAO movieDAO = new MovieDAO();
				movie = movieDAO.save(movie);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				movie = null;
			}
		}
		if(movie != null) {
			resp = objectToJSON(movie);
		}
		return resp;
	}

	public String[] splitTokenizer(String str, String delim) {
		List<String> result = new ArrayList<String>();
		if(str != null && !str.equals("") && delim != null && !delim.equals("")) {
			StringTokenizer st2 = new StringTokenizer(str, delim);
			while (st2.hasMoreElements()) {
				result.add(st2.nextElement().toString());
			}
		}
		return result.toArray(new String[result.size()]);
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({"application/json"})
	public String updateMovie(String parameters) {
		String resp = "";
		Movie movie = null;
		try {
			parameters = URLDecoder.decode(parameters, "UTF-8");
			String[] param = splitTokenizer(parameters, "&=");
			if(param != null && param.length > 13) {
				movie = new Movie();
				try {
					if(!param[1].equals("")){
						movie.setId(Long.parseLong(param[1]));
					}
				} catch (Exception e) {}
				movie.setImdbID(param[3]);
				movie.setTitle(param[5]);
				movie.setYear(param[7]);
				movie.setType(param[9]);
				movie.setReleased(param[11]);
				movie.setGenre(param[13]);

			} else if(param != null && param.length > 12) {
				movie = new Movie();
				movie.setImdbID(param[2]);
				movie.setTitle(param[4]);
				movie.setYear(param[6]);
				movie.setType(param[8]);
				movie.setReleased(param[10]);
				movie.setGenre(param[12]);
			}
			MovieDAO movieDAO = new MovieDAO();
			movie = movieDAO.save(movie);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			movie = null;
		}
		if(movie != null) {
			resp = objectToJSON(movie);
		}
		return resp;
	}

	@POST
	@Path("/deteleByID/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({"application/json"})
	public String deleteMovie(String parameters) {
		Boolean deleted = Boolean.FALSE;
		try {
			parameters = URLDecoder.decode(parameters, "UTF-8");
			String[] param = splitTokenizer(parameters, "&=");
			if(param != null && param.length > 1) {
				MovieDAO movieDAO = new MovieDAO();
				Movie movie = movieDAO.retrieveMovieByID(Long.parseLong(param[1]));
				if(movie != null) {
					deleted = movieDAO.delete(movie);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return objectToJSON(deleted);
	}

	@GET
	@Path("/createdb/")
	@Produces(MediaType.APPLICATION_JSON)
	public String createDB() {
		Gson gson = new Gson();
		CreateBd teste = new CreateBd();
		teste.createDB();
		return gson.toJson("BD CREATED/UPDATED");
	}

	private String objectToJSON(Object object) {
		String resp = "";
		if(object != null) {
			Gson gson = new Gson();
			resp = gson.toJson(object);
		}
		return resp;
	}
}
