package app.model;

public class DBModel {
	
	private int id_result;
	private String user_name;
	private double stopwatch;
	private int qty_mine;
	
	public DBModel() {
		super();
	}
	public DBModel(int id_result, String user_name, double stopwatch, int qty_mine) {
		super();
		this.id_result = id_result;
		this.user_name = user_name;
		this.stopwatch = stopwatch;
		this.qty_mine = qty_mine;
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
	public int getQty_mine() {
		return qty_mine;
	}
	public void setQty_mine(int qty_mine) {
		this.qty_mine = qty_mine;
	}
	
	
	
}
