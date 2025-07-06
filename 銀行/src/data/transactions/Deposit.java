package data.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import data.DBConnection;

public class Deposit {
	public static boolean deposit(String cardnumber, Scanner sc) {
			
			System.out.println("入金する金額を入力してください");
			String input = sc.nextLine();
			
			int amount;
			try {
				amount = Integer.parseInt(input);
				if(amount<=0) {
					System.out.println("0円以下は入金できません");
					return false;
				}
			}catch(NumberFormatException e) {
				System.out.println("数字のみを入力してください");
				e.printStackTrace();
				return false;
			}
			
			String selectSql = "SELECT balance FROM users WHERE cardnumber = ?";
			String updateSql = "UPDATE users SET balance = ? WHERE cardnumber = ?";
			
			try(Connection con = DBConnection.getConnection();
				PreparedStatement select = con.prepareStatement(selectSql);
				PreparedStatement update = con.prepareStatement(updateSql)){
				
				select.setString(1, cardnumber);
				ResultSet rs = select.executeQuery();
				if(rs.next()) {
					int currentBalance = rs.getInt("balance");
					int newBalance = currentBalance + amount;
					
					update.setInt(1, newBalance);
					update.setString(2, cardnumber);
					
					int affectedrows = update.executeUpdate();
					if(affectedrows>0) {
						System.out.println("入金が完了しました。現在の残高：" + newBalance + "円");
						return true;
					}else {
						System.out.println("ユーザーが見つかりませんでした");
						return false;
					}	
				}	
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		return false;
	}
}
