package data.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import data.DBConnection;

public class Withdraw {
	public static boolean withdraw(String cardnumber, Scanner sc) {
			
		System.out.println("引き出す金額を数字のみで入力してください");
		System.out.println("全額の際は\"全額\"と記載してください");
		String takeOut = sc.nextLine().trim();
		
		if(takeOut.equals("全額")) {
			return withdrawAll(cardnumber);
		}
		
		int amount;
		try {
			amount = Integer.parseInt(takeOut);
			if(amount<=0) {
				System.out.println("正しい金額を入力してください");
				return false;
			}
		}catch (NumberFormatException e) {
			System.out.println("数字のみを入力してください");
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
				if(currentBalance < amount) {
					System.out.println("お金が足りません");
					return false;
				}
				
				int newBalance = currentBalance - amount;
				update.setInt(1, newBalance);
				update.setString(2, cardnumber);
				
				int affectedRows = update.executeUpdate();
				if(affectedRows>0) {
					System.out.println(amount+"円が引き抜かれ、"+newBalance+"円が残ってます");
					return true;
				}else {
					System.out.println("失敗しました");
					return false;
				}
			}else {
				System.out.println("失敗しました");
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		}
	public static boolean withdrawAll(String cardnumber) {
		String selectSql = "SELECT balance FROM users WHERE cardnumber = ?";
		String updateSql = "UPDATE users SET balance = ? WHERE cardnumber = ?";
		
		try(Connection con = DBConnection.getConnection();
				PreparedStatement select = con.prepareStatement(selectSql);
				PreparedStatement update = con.prepareStatement(updateSql)){
			
			select.setString(1, cardnumber);
			ResultSet rs = select.executeQuery();
			
			if(rs.next()) {
				int currentBalance = rs.getInt("balance");
				
				if(currentBalance == 0) {
					System.out.println("引き出せる金額がありません");
					return false;
				}
				
				update.setInt(1, 0);
				update.setString(2, cardnumber);
				
				int affectedRows = update.executeUpdate();
				if(affectedRows>0) {
					System.out.println("全額の"+currentBalance+"円が引き出されました");
					return true;
				}else {
					System.out.println("引き出しに失敗しました");
					return false;
				}
			}else {
				System.out.println("失敗しました");
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
