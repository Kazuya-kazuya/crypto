package crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Main {
	public static void main(String[] args) throws Exception {
		Main program = new Main();
		program.start();
	}

	private void start() throws Exception {
		//キーの設定
		byte[] key = "FEDCBA9876543210".getBytes();
		//イニシャルベクターの設定
		byte[] iv = "0123456789ABCDEF".getBytes();
		//暗復号器の生成
		Crypto crypto = new Crypto(key, iv);

		String plain = "";
		String enc = "";
		String dec = "";

		while(true) {
			System.out.println("入力してください");
			plain = input();
			if(plain.equals("exit")) {
				break;
			}
			System.out.print("平文:");
			System.out.println(plain);
			enc = crypto.encrypto(plain);
			System.out.print("暗号文:");
			System.out.println(enc);
			dec = crypto.decrypto(enc);
			System.out.print("複合文:");
			System.out.println(dec);
		}
	}

	public String input() throws IOException {
		//コンソール入力
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		return br.readLine();
	}
}

class Crypto{
	private Cipher enc;
	private Cipher dec;

	public Crypto(byte[] secretKey, byte[] initialVector) throws Exception{
		//イニシャルベクターの設定
		IvParameterSpec iv = new IvParameterSpec(initialVector);
		//キーの設定
		SecretKeySpec key = new SecretKeySpec(secretKey, "AES");
		//暗号器の生成
		enc = Cipher.getInstance("AES/CBC/PKCS5Padding");
		enc.init(Cipher.ENCRYPT_MODE, key, iv);
		//複合器の生成
		dec = Cipher.getInstance("AES/CBC/PKCS5Padding");
		dec.init(Cipher.DECRYPT_MODE, key, iv);
	}

	public String encrypto(String text) throws IllegalBlockSizeException, BadPaddingException {
		//暗号化してエンコード
		byte[] crypto = this.enc.doFinal(text.getBytes());
		byte[] str = Base64.getEncoder().encode(crypto);
		return new String(str);
	}

	public String decrypto(String str) throws IllegalBlockSizeException, BadPaddingException {
		//デコードして複合化
		byte[] crypto = Base64.getDecoder().decode(str);
		byte[] text = this.dec.doFinal(crypto);
		return new String(text);
	}
}