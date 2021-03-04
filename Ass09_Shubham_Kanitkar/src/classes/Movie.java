package classes;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class Movie implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3792831221749206447L;
	private int movieId;
    private String movieName;
    private String movieType;
    private String movieLang;
    private Date releaseDate;
    private List<String> movieCast;
    private double movieRating, movieBusiness;

	public int getMovieId() {
	    return movieId;
	}

	public void setMovieId(int movieId) {
	    this.movieId = movieId;
	}
	
	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName)
	{
	    this.movieName = movieName;
	}

	public String getMovieType() {
		return movieType;
	}

	public void setMovieType(String movieType) {
		this.movieType = movieType;
	}
	

	public String getMovieLanguage() {
		return movieLang;
	}

	public void setMovieLanguage(String movieLang) {
		this.movieLang = movieLang;
	}

	public List<String> getMovieCast() {
		return movieCast;
	}

	public void setMovieCast(List<String> movieCast) {
		this.movieCast = movieCast;
	}

	public double getMovieBusiness() {
		return movieBusiness;
	}

	public void setMovieBusiness(double movieBusiness) {
		this.movieBusiness = movieBusiness;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date d) {
		 releaseDate = d;
	}

	public double getMovieRating() {
		return movieRating;
	}

	public void setMovieRating(double movieRating)
	{
	    this.movieRating = movieRating;
	}
}
