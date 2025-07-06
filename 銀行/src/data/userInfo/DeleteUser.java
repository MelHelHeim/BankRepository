package data.userInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import data.DBConnection;
import data.credentials.UserAuthenticator;
import data.transactions.Withdraw;

public class DeleteUser {
	public static boolean delete() {
		try(Scanner sc = new Scanner(System.in)){
		
		UserAuthenticator auth = new UserAuthenticator(sc);
		String cardnumber = auth.authenticate();
		
		if(cardnumber == null) {
			return false;
		}
		
		System.out.println("認証のためにメールアドレスに送信されたコードを入力してください");
		int rng = (int)(Math.random() * 900000) + 100000;
		String rngcode = String.valueOf(rng);
		//send code to email of account
		
		String inputcode = sc.nextLine();		
		if(!inputcode.equals(rngcode)) {
			System.out.println("コードが違います");
			return false;
		}else {
			
			System.out.println("全金額を引き出します");
			Withdraw.withdrawAll(cardnumber);
			
			System.out.println("アカウントを削除します");
			String sql = ("DELETE FROM users WHERE cardnumber = ?");
			try(Connection con = DBConnection.getConnection();
					PreparedStatement ps = con.prepareStatement(sql)){
				
				ps.setString(1, cardnumber);
				int affectedRows = ps.executeUpdate();
				if(affectedRows>0) {
					System.out.println("アカウントが正常に削除されました");
					return true;
				}else {
					System.out.println("アカウントが見つかりませんでした");
					return false;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}catch (Exception e) {
		e.printStackTrace();
		}
		return false;
	}
}
