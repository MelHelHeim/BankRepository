package data.credentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import data.DBConnection;

public class NumberPinValidator {
	public static boolean Credentials(String cardnumber, String pin) {
		String sql = ("SELECT COUNT(*) FROM USERS WHERE cardnumber = ? AND pin = ?");
		try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			
			ps.setString(1, cardnumber);
			ps.setString(2, pin);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				int count = rs.getInt(1);
				return count == 1;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean isValidPinFormat(String pin) {
		if(pin == null) {
			return false;
		}
		return pin.matches("\\d{4}");
	}
}
