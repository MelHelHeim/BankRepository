package data.credentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import data.DBConnection;
import data.transactions.Deposit;
import data.transactions.Withdraw;
import data.userInfo.DeleteUser;
import data.userInfo.EditUserInfo;

public class UserAuthenticator {
	private final Scanner sc;
	
	public UserAuthenticator(Scanner sc) {
		this.sc = sc;
	}
	public String authenticate() {
	String cardnumber;
	String pin;
	
	while(true) {
		System.out.println("カード番号を入力してください。前の画面に戻るには\"esc\"と入力してください");
		cardnumber = sc.nextLine();
		
	if(cardnumber.equalsIgnoreCase("esc")) {
		return null;
	}
	if(!CardValidator.isValidCardFormat(cardnumber)) {
		System.out.println("正しい番号を入力してください");
		continue;
		}
	break;
	}
	
	while(true) {
		System.out.println("暗証番号を入力してください。終了するには\"esc\"と入力してください");
		pin = sc.nextLine();
		
		if(pin.equalsIgnoreCase("esc")) {
			return null;
		}
		if(!NumberPinValidator.isValidPinFormat(pin)) {
			System.out.println("正しい番号を入力してください");
			continue;
		}
		if(!NumberPinValidator.Credentials(cardnumber, pin)) {
			System.out.println("カード番号または暗証番号が間違っています");
			continue;
		}
		break;
	}
	return cardnumber;
}
	public static boolean doesCardExist(String cardnumber, String pin) {
		String sql = "SELECT * FROM users WHERE cardnumber = ? AND pin = ?";
		try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			
			ps.setString(1, cardnumber);
			ps.setString(2, pin);
			
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public void userMenu(String cardnumber) {
		while(true) {
			System.out.println("下記から該当の番号を選択してください");
			
			System.out.println("\n=== メニュー ===");
			System.out.println("1. 入金");
			System.out.println("2. 引き出し");
			System.out.println("3. ユーザー情報編集");
			System.out.println("4. アカウント削除");
			System.out.println("5. ログアウト");
			
			String choice = sc.nextLine().trim();
			
			switch(choice) {
			case "1":
				Deposit.deposit();
				break;
				
			case "2":
				Withdraw.withdraw();
				break;
				
			case "3":
				EditUserInfo.edit();
				break;
				
			case "4":
				boolean deleted = DeleteUser.delete();
				if(deleted) {
					System.out.println("アカウントが削除されました");
					return;
				}
				break;
				
			case "5":
				System.out.println("ログアウトします");
				return;
			
			default:
				System.out.println("無効な選択です");
				break;
				}
			}
		}
	}
