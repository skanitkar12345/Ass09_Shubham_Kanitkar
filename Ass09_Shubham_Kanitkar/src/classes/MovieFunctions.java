package classes;

import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import db.*;
 
class BusinessComparator implements Comparator<Movie>{

	@Override
	public int compare(Movie m1, Movie m2) {
		if (m1.getMovieBusiness()>m2.getMovieBusiness()) {
			return -1;
		}
		
		if (m1.getMovieBusiness()==m2.getMovieBusiness()) {
			return 0;
		}
		
		return 1;
	}
	
}

public class MovieFunctions {
	
	private static List<Movie> movies;
	private static final String dbConfigFile = "src/database.properties";
	private static final String movieFile = "src/movies";
	private static final String outFile =  "D://file.txt";
	
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
		conn = CreateConnection.databaseConnection(dbConfigFile);
		
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
		
		statement.close();
		conn.close();
		
		return true;
		
	}
	
	public static boolean addToDatabase(Movie movie) throws IOException, SQLException {
		Connection conn;
		String sql;
		int i;
		PreparedStatement statement;
		
		sql = "Insert INTO MovieDb(mId, mName, mLang, mDate, mType, mCast, mRating, mBusiness) Values(?,?,?,?,?,?,?,?)";
		conn = CreateConnection.databaseConnection(dbConfigFile);
		
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
		
		statement.close();
		conn.close();
		
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
	
	public static List<Movie> getMovieReleasedInYear(int year) {
		
		List<Movie> m = new ArrayList<Movie>();
		
		for (Movie movie : movies) {
			Date d = movie.getReleaseDate();
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			if(c.get(Calendar.YEAR)==year)
				m.add(movie);
		}
		return m;
		
	}
	
	public static List<Movie> getMoviesByActor(String ...actorNames) {
		
		List<Movie> m = new ArrayList<Movie>();
		
		for (Movie movie : movies) {
			if(movie.getMovieCast().contains(actorNames[0])|movie.getMovieCast().contains(actorNames[1]))
				m.add(movie);
		}
		return m;
		
	}
	
	public static void updateRating(Movie movie, double rating, List<Movie> movies) throws IOException, SQLException {
		
		for (Movie movie2 : movies) {
			if(movie2.getMovieName().equals(movie.getMovieName()))
				movie2.setMovieRating(rating);
		}
		
		Connection conn = CreateConnection.databaseConnection(dbConfigFile);
		Statement statement = conn.createStatement();
		
		statement.executeUpdate("Update MovieDb Set mRating = ' "+rating+ "' where mName = '"+movie.getMovieName()+"'");
		
		System.out.println("Updated succesfully");
		
		statement.close();
		conn.close();
	}
	
	public static void updateBusiness(Movie movie, double business, List<Movie> movies) throws IOException, SQLException {
		
		for (Movie movie2 : movies) {
			if(movie2.getMovieName().equals(movie.getMovieName()))
				movie2.setMovieBusiness(business);
		}
		
		Connection conn = CreateConnection.databaseConnection(dbConfigFile);
		Statement statement = conn.createStatement();
		
		statement.executeUpdate("Update MovieDb Set mBusiness = ' "+business+ "' where mName = '"+movie.getMovieName()+"'");
		
		System.out.println("Updated succesfully");
		
		statement.close();
		conn.close();
	}
	
	public static Set<Movie> businessDone(double amount) {
		
		Comparator<Movie> c = new BusinessComparator();
		
		Set<Movie> movieSet = new TreeSet<>(c);
		
		for (Movie movie : movies) {
			if (movie.getMovieBusiness()>amount) {
				movieSet.add(movie);
			}
		}
		return movieSet;
		
	}
	
	public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
		
		Scanner sc = new Scanner(System.in);
		
		File file = new File(movieFile);
		List<Movie> movies = addFromFile(file);
		System.out.println(addToDatabase(movies));
		while (true) {
			System.out.println("------------------------------");
			System.out.println("Enter choice ");
			System.out.println("1. Add new movie");
			System.out.println("2. Serialize");
			System.out.println("3. Deserialize");
			System.out.println("4. Get Movie by year");
			System.out.println("5. Get Movie by actor names");
			System.out.println("6. Update movie rating");
			System.out.println("7. Update movie business value");
			System.out.println("8. Get business more than entered amount");
			System.out.println("------------------------------");
			int n = sc.nextInt();
			
			switch(n) {
			case 1: addMovie(new Movie(), movies);
			        break;
			        
			case 2: serialize(movies, outFile);
			        System.out.println("Done!!");
			        break;
			        
			case 3: List<Movie> movies1 = deserialize(outFile);
	        	    System.out.println("Done!!");
	        	    for(Movie m : movies1) {
	        	    	System.out.println(m.getMovieName());
	        		}
	        	    break;
			case 4: System.out.println("Enter the Year : ");
	                int year = sc.nextInt();
				    List<Movie> movies2 = getMovieReleasedInYear(year);
				    if (movies2.size()!=0) {
				    	System.out.println("Movies found in the entered year are ");
				    	System.out.println("------------------------------");
				    	for(Movie m : movies2) {
				        	System.out.println(m.getMovieName());
				        }
				    	System.out.println("------------------------------");
					} else {
						System.out.println("No movies found in the entered year");
						System.out.println("------------------------------");
					}
			        break;
			        
			case 5:  System.out.println("Enter actor names to search for movie: ");
			         sc.nextLine();
			         String s1 = sc.nextLine();
			         String s2 = sc.nextLine();
			         List<Movie> movies3 = getMoviesByActor(s1,s2);
			         if (movies3.size()!=0) {
			        	 System.out.println("Following movies have the entered actors as cast members.");
			        	 System.out.println("------------------------------");
			        	 for(Movie m : movies3) {
			        		 System.out.println(m.getMovieName());
			             System.out.println("------------------------------");
			        	 }
			         } else {
			        	 System.out.println("No movies stars the given actors.");
			        	 System.out.println("------------------------------");
			         }
			         break;
			         
			case 6: System.out.println("Enter movie name and changed rating");
			        sc.nextLine();
			        String movieName = sc.nextLine();
			        sc.nextLine();
			        double rating = sc.nextDouble();
			        
			        for (Movie movie : movies) {
			        	if (movie.getMovieName().equals(movieName)) {
							updateRating(movie, rating, movies);
							
						}
					}
			        
			        break;
			        
			case 7: System.out.println("Enter movie name and changed business value");
			        sc.nextLine();
	                String movieName2 = sc.nextLine();
	                sc.nextLine();
	                double business = sc.nextDouble();
	                for (Movie movie : movies) {
	                	if (movie.getMovieName().equals(movieName2)) {
	                		updateBusiness(movie, business, movies);
	                	}
	                }
	        
	                break;
			case 8: System.out.println("Enter amount : ");
			        double amount = sc.nextDouble();
			        Set<Movie> movieSet = businessDone(amount);
			        System.out.println("Movie with business greater than "+ amount +" million dollars");
			        for(Movie movie : movieSet)
			        	System.out.println(movie.getMovieName());
			        break;
			
			default: System.exit(1);
			}
			
		}
	}
}
