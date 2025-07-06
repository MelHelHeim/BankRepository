package main;

import java.util.Scanner;

import data.credentials.UserAuthenticator;
import data.userInfo.CreateUser;

public class ATMApp {
	public static void main(String[] args) {
		CreateSQLfile.setup();
		
		try(Scanner sc = new Scanner(System.in)){
			while(true) {
				System.out.println("該当の番号を入力してください");
				System.out.println("\n=== ATM メインメニュー ===");
				System.out.println("1. ログイン");
				System.out.println("2. アカウント作成");
				System.out.println("3. 終了");
				
				String choice = sc.nextLine().trim();
				
				switch(choice) {
				case "1":
					UserAuthenticator auth = new UserAuthenticator(sc);
					String cardnumber = auth.authenticate();
					if(cardnumber != null) {
						auth.userMenu(cardnumber);
					}
					break;
					
				case "2":
					CreateUser.create(sc);
					break;
					
				case "3":
					System.out.println("終了します");
					sc.close();
					return;
					
				default:
					System.out.println("正しい番号を選択してください");
					break;
				}
			}
		}
	}
}
