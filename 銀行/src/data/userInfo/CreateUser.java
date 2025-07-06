package data.userInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import data.DBConnection;

public class CreateUser {
	public static void create(Scanner sc) {	
		String lastname = "";
		
		while(true) {
			System.out.println("姓を漢字、またはカタカナで入力してください");
			lastname = sc.nextLine();
			if(lastname.matches("[\\p{IsHan}\\u30A0-\\u30FF]+")) {
				break;
			}else {
				System.out.println("入力に間違いがあります");
			}
		}
		String firstname = "";
		while(true) {
			System.out.println("名を漢字、またはカタカナで入力してください");
			firstname = sc.nextLine();
			if(firstname.matches("[\\p{IsHan}\\u30A0-\\u30FF]+")) {
				break;
			}else {
				System.out.println("入力に間違いがあります");
			}
		}
		String pin = "";
		while(true) {
			System.out.println("4桁の暗証番号を作成してください");
			pin = sc.nextLine();
			if(pin.matches("\\d+") && pin.length() == 4) {
				break;
			}else {
				System.out.println("正しく入力してください");
			}
		}
		String email = "";
		while(true) {
			System.out.println("メールアドレスを追加してください");
			email = sc.nextLine();
			if(email.contains("@")) {
				break;
			}else{
				System.out.println("正しいアドレスを入力してください");
			}
		}
		sc.close();
		
		addUserToDatabase(lastname, firstname, pin, email);
		}
	
		private static void addUserToDatabase(String lastname, String firstname, String pin, String email) {
			String sql = "INSERT INTO users(lastname, firstname, pin, email) VALUES(?,?,?,?)";
			
			try (Connection con = DBConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql)){
				
				ps.setString(1, lastname);
				ps.setString(2, firstname);
				ps.setString(3, pin);
				ps.setString(4, email);
				
				int result = ps.executeUpdate();
				if(result>0) {
					System.out.println("ユーザーが作成されました");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}	
	}
}
