package start;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class EntryPoint {
	public static void main(String args[]) {
		Controller c = new Controller();
		c.start();
	}
}
