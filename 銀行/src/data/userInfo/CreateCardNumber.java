package data.userInfo;

import java.util.Random;

import data.credentials.CardValidator;

public class CreateCardNumber {
	private static Random rng = new Random();
	
	public static String GenerateCardNum() {
		String cardNum;
		do {
			cardNum = GenerateNum();
		}while(CardValidator.doesCardExist(cardNum));
			return cardNum;
		}
	
	private static String GenerateNum() {
		int num = rng.nextInt(100_000_000);
		return String.format("%08d", num);
	}
}
