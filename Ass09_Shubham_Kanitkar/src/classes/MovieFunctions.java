package classes;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import db.*;


public class MovieFunctions {
	
	private static List<Movie> movies;
	
	static {
		movies = new ArrayList<Movie>();
	}
	
	public static List<Movie> addFromFile(File file) throws IOException {
		
		FileInputStream fin = new FileInputStream(file);
		Scanner sc = new Scanner(fin);
		String[] data;
		
		while(sc.hasNextLine()) {
			Movie movie = new Movie();
			List<String> cast = new ArrayList<>();
			data = sc.nextLine().split(",");
			
			movie.setMovieId(Integer.parseInt(data[0]));
			movie.setMovieName(data[1]);
			movie.setMovieLanguage(data[2]);
			movie.setReleaseDate(Date.valueOf(data[3]));
			movie.setMovieType(data[4]);
			String[] castNames = data[5].split("-");
			for(String actor : castNames)
				cast.add(actor);
			movie.setMovieCast(cast);
			movie.setMovieRating(Double.parseDouble(data[6]));
			movie.setMovieBusiness(Double.parseDouble(data[7]));
			
			
			movies.add(movie);
		}
		
		sc.close();
		fin.close();
		
		return movies;
	}
	
	public static boolean addToDatabase(List<Movie> m) throws IOException, SQLException {
		Connection conn;
		String sql;
		int i;
		PreparedStatement statement;
		
		sql = "Insert INTO MovieDb(mId, mName, mLang, mDate, mType, mCast, mRating, mBusiness) Values(?,?,?,?,?,?,?,?)";
		conn = CreateConnection.databaseConnection("src/database.properties");
		
		statement = conn.prepareStatement(sql);
		
		for(Movie movie : m) {
			statement.setInt(1, movie.getMovieId());
			statement.setString(2, movie.getMovieName());
			statement.setString(3, movie.getMovieLanguage());
			statement.setDate(4, movie.getReleaseDate());
			statement.setString(5, movie.getMovieType());
			String cast = "";
			for(i=0; i<movie.getMovieCast().size()-1;i++)
				cast += movie.getMovieCast().get(i) + ",";
			cast += movie.getMovieCast().get(i);
			statement.setString(6, cast);
			statement.setDouble(7, movie.getMovieRating());
			statement.setDouble(8, movie.getMovieBusiness());
			statement.executeUpdate();
		}
		
		return true;
		
	}
	
	public static boolean addToDatabase(Movie movie) throws IOException, SQLException {
		Connection conn;
		String sql;
		int i;
		PreparedStatement statement;
		
		sql = "Insert INTO MovieDb(mId, mName, mLang, mDate, mType, mCast, mRating, mBusiness) Values(?,?,?,?,?,?,?,?)";
		conn = CreateConnection.databaseConnection("src/database.properties");
		
		statement = conn.prepareStatement(sql);
		
		
		statement.setInt(1, movie.getMovieId());
		statement.setString(2, movie.getMovieName());
		statement.setString(3, movie.getMovieLanguage());
		statement.setDate(4, movie.getReleaseDate());
		statement.setString(5, movie.getMovieType());
		String cast = "";
		for(i=0; i<movie.getMovieCast().size()-1;i++)
			cast += movie.getMovieCast().get(i) + ",";
		cast += movie.getMovieCast().get(i);
		statement.setString(6, cast);
		statement.setDouble(7, movie.getMovieRating());
		statement.setDouble(8, movie.getMovieBusiness());
		statement.executeUpdate();
				
		return true;
		
	}
	
	public static void addMovie(Movie m, List<Movie> movies) throws IOException, SQLException {
		Scanner sc = new Scanner(System.in);
		List<String> cast = new ArrayList<>();
		
		System.out.println("Enter movie id : ");
		 m.setMovieId(sc.nextInt());
		 
		System.out.println("Enter movie name : ");
		sc.nextLine();
		m.setMovieName(sc.nextLine());
		 
		System.out.println("Enter movie language : ");
		m.setMovieLanguage(sc.nextLine());
		
		System.out.println("Enter release date (in YYYY-MM-DD) : ");
		m.setReleaseDate(Date.valueOf(sc.nextLine()));
		
		System.out.println("Enter movie category ");
		m.setMovieType(sc.nextLine());
		 
		System.out.println("Number of cast members ");
		int n = sc.nextInt();
		sc.nextLine();
		 
		for(int i=0;i<n;i++)
			cast.add(sc.nextLine());
		m.setMovieCast(cast);
		
		System.out.println("Enter movie rating ");
		m.setMovieRating(sc.nextDouble());
		
		System.out.println("Enter movie business ");
		m.setMovieBusiness(sc.nextDouble());
		
		addToDatabase(m);
		movies.add(m);
		sc.close();
	}
	
	public static void serialize(List<Movie> movies, String fName) throws IOException {
		FileOutputStream fout = new FileOutputStream(new File(fName));
		ObjectOutputStream out = new ObjectOutputStream(fout);
		
		for(Movie m : movies)
			out.writeObject(m);
		
		out.close();
		fout.close();
	}
	
	public static List<Movie> deserialize(String fName) throws IOException, ClassNotFoundException {
		List<Movie> m = new ArrayList<>();
		
		FileInputStream fin = new FileInputStream(new File(fName));
		ObjectInputStream in = new ObjectInputStream(fin);
		
		for(int i=0; i<movies.size(); i++)
			m.add((Movie)in.readObject());
		in.close();
		fin.close();
		
		return m;
	}
	
	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
		
		Scanner sc = new Scanner(System.in);
		
		File file = new File("src/movies");
		List<Movie> movies = addFromFile(file);
		//System.out.println(addToDatabase(movies));
		
		System.out.println("Enter choice ");
		System.out.println("1. Add new movie");
		System.out.println("2. Serialize");
		System.out.println("3. Deserialize");
		int n = sc.nextInt();
		
		switch(n) {
		case 1: addMovie(new Movie(), movies);
		        break;
		case 2: serialize(movies, "D:\\file.txt");
		        System.out.println("Done!!");
		        break;
		case 3 : List<Movie> t = deserialize("D:\\file.txt");
        		 System.out.println("Done!!");
        		 for(Movie m : t) {
        			 System.out.println(m.getMovieName());
        		 }
        		 break;
		}
		
		sc.close();
	}
}
