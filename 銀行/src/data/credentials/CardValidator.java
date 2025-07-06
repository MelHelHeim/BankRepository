package data.credentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import data.DBConnection;

public class CardValidator {
	public static boolean doesCardExist(String cardnumber) {
		String sql = ("SELECT COUNT(*) FROM users WHERE cardnumber = ?");
		try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			
			ps.setString(1, cardnumber);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getInt(1)>0;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean isValidCardFormat(String cardnumber) {
		return cardnumber != null && cardnumber.matches("\\d{8}");
		
	}
}
