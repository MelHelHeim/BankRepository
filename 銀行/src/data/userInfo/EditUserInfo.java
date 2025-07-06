package data.userInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import data.DBConnection;
import data.credentials.UserAuthenticator;

public class EditUserInfo {
	public static boolean edit() {
		try(Scanner sc = new Scanner(System.in)){
			
			UserAuthenticator auth = new UserAuthenticator(sc);
			String cardnumber = auth.authenticate();
			
			if(cardnumber == null) {
				return false;
			}
			
			System.out.println("変更したい項目の番号を入力してください");
			System.out.println("1. メールアドレス");
			System.out.println("2. 暗証番号");
			System.out.println("3. 名前");
			String option = sc.nextLine();
			
			String newValue = null;
			String sql = null;
			String lastname = null;
			String firstname = null;
			
			switch(option) {
			case "1":
				System.out.println("新しいアドレスを入力してください");
				newValue = sc.nextLine();
				if(!newValue.contains("@")) {
					System.out.println("無効なアドレスです");
					return false;
				}
				sql = "UPDATE users SET email = ? WHERE cardnumber = ?";
				break;
				
			case "2":
				System.out.println("新し暗証番号を入力してください");
				newValue = sc.nextLine();
				if(!newValue.matches("\\d{4}")) {
					System.out.println("無効な番号です");
					return false;
				}
				sql = "UPDATE users SET pin = ? WHERE cardnumber = ?";
				break;
				
			case "3":
				System.out.println("新しい姓と名を入力してください。例: 山田 太郎");
				newValue = sc.nextLine();
				String[] newName = newValue.split(" ");
				if(newName.length != 2) {
					System.out.println("正しく入力してください。（姓と名の間にスペース）");
					return false;
				}
				lastname = newName[0];
				firstname = newName[1];
				
				sql = "UPDATE users SET lastname = ?, firstname = ? WHERE cardnumber = ?";
				break;
				
			default:
				System.out.println("無効な選択です");
				return false;
			}
			try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
				
				if(option.equals("3")) {
					ps.setString(1, lastname);
					ps.setString(2, firstname);
					ps.setString(3, cardnumber);
				}else {
					ps.setString(1, newValue);
					ps.setString(2, cardnumber);	
				}
				int affected = ps.executeUpdate();
				if(affected>0) {
					System.out.println("更新が完了しました");
					return true;
				}else {
					System.out.println("更新に失敗しました");
					return false;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			}
		return false;
		}
	}

