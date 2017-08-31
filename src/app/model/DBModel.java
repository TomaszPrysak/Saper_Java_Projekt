package app.model;

public class DBModel {
	
	private int id_result;
	private String user_name;
	private double stopwatch;
	
	public DBModel(int id_result, String user_name, double stopwatch) {
		super();
		this.id_result = id_result;
		this.user_name = user_name;
		this.stopwatch = stopwatch;
	}

	public DBModel() {
		super();
	}

	public int getId_result() {
		return id_result;
	}

	public void setId_result(int id_result) {
		this.id_result = id_result;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public double getStopwatch() {
		return stopwatch;
	}

	public void setStopwatch(double stopwatch) {
		this.stopwatch = stopwatch;
	}
	
}
