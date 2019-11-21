import java.util.Arrays;
import java.util.Scanner;

public class DES {
	public static String key1;
	public static String key2;
	public static String cipher;

	public static int p10[] = { 0, 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
	public static int p8[] = { 0, 6, 3, 7, 4, 8, 5, 10, 9 };
	public static int p4[] = { 2, 4, 3, 1 };

	public static String[][] S0_box = new String[][] { { "01", "00", "11", "10" }, { "11", "10", "01", "00" },
			{ "00", "10", "01", "11" }, { "11", "01", "00", "10" } };
	public static String[][] S1_box = new String[][] { { "00", "01", "10", "11" }, { "10", "00", "01", "11" },
			{ "11", "10", "01", "00" }, { "10", "01", "00", "11" } };

	public static int IP[] = { 2, 6, 3, 1, 4, 8, 5, 7 };// IP函数，用于初始置换
	public static int IP_1[] = { 4, 1, 3, 5, 7, 2, 8, 6 };// IP函数的逆函数，用于末端置换
	public static int EP[] = new int[] { 4, 1, 2, 3, 2, 3, 4, 1 };

	public static void CreateKey(String K) {// 产生K1、K2
		int K1[] = new int[9];
		int K2[] = new int[9];
		int Ktemp[] = new int[11];
		int temp1[] = new int[11];// 经过P10变换的数组
		int templ[] = new int[5];// 将temp1分为左5位
		int tempr[] = new int[5];// 将temp1分为右5位
		int temp2[] = new int[11];// P8置换前的一个临时数组
		int i, j, k, temp;

		char[] c = K.toCharArray();
		for (i = 0; i < c.length; i++) {
			char m = c[i];
			String s = m + "";
			Ktemp[i] = Integer.parseInt(s);
		}

		for (i = 0; i < Ktemp.length; i++) {// P10变换
			temp1[i] = Ktemp[p10[i]];
		}

		for (i = 0; i < templ.length; i++) {// 分为左5位
			templ[i] = temp1[i + 1];
		}
		for (i = 0; i < tempr.length; i++) {// 分为右5位
			tempr[i] = temp1[i + K.length() / 2 + 1];
		}

		temp = templ[0];// 左5位LS-1置换
		for (i = 0; i < templ.length - 1; i++) {
			templ[i] = templ[i + 1];
		}
		templ[templ.length - 1] = temp;

		temp = tempr[0];// 右5位LS-1置换
		for (i = 0; i < tempr.length - 1; i++) {
			tempr[i] = tempr[i + 1];
		}
		tempr[tempr.length - 1] = temp;

		temp2[0] = 0;// K1的P8置换前的数组
		for (i = 1; i <= templ.length; i++) {
			temp2[i] = templ[i - 1];
		}
		for (i = 0; i < tempr.length; i++) {
			temp2[i + 1 + templ.length] = tempr[i];
		}

		for (i = 0; i < K1.length; i++) {// K1的P8变换，得到K1
			K1[i] = temp2[p8[i]];
		}

		for (i = 0; i < 2; i++) {
			temp = templ[0];// 左5位LS-2置换
			for (j = 0; j < templ.length - 1; j++) {
				templ[j] = templ[j + 1];
			}
			templ[templ.length - 1] = temp;

			temp = tempr[0];// 右5位LS-2置换
			for (k = 0; k < tempr.length - 1; k++) {
				tempr[k] = tempr[k + 1];
			}
			tempr[tempr.length - 1] = temp;
		}

		temp2[0] = 0;// K2的P8置换前的数组
		for (i = 1; i <= templ.length; i++) {
			temp2[i] = templ[i - 1];
		}
		for (i = 0; i < tempr.length; i++) {
			temp2[i + 1 + templ.length] = tempr[i];
		}

		for (i = 0; i < K2.length; i++) {// K2的P8变换，得到K2
			K2[i] = temp2[p8[i]];
		}

		StringBuffer temps = new StringBuffer();
		for (i = 1; i < K1.length; i++) {
			temps.append(K1[i]);
		}
		key1 = temps.toString();// K1转成String形式
		temps.delete(0, temps.length());

		for (i = 1; i < K2.length; i++) {
			temps.append(K2[i]);
		}
		key2 = temps.toString();// K2转成String形式
		System.out.println("key1:" + key1);
		System.out.println("key2:" + key2);
	}

	public static String XOR(String str, String key) {// 进行异或操作
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == key.charAt(i)) {
				temp.append("0");
			} else {
				temp.append("1");
			}
		}
		return new String(temp);
	}

	public static String Sbox(String str, int n) {// S盒相关操作
		StringBuilder temp = new StringBuilder();
		temp.append(str.charAt(0));
		temp.append(str.charAt(3));
		String ret = new String(temp);
		StringBuilder temp1 = new StringBuilder();
		temp1.append(str.charAt(1));
		temp1.append(str.charAt(2));
		String ret1 = new String(temp1);
		String retu = new String();
		if (n == 0) {
			retu = S0_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
		} else {
			retu = S1_box[Integer.parseInt(ret, 2)][Integer.parseInt(ret1, 2)];
		}
		return retu;
	}

	public static String Change(String str, int[] X) {// 进行置换
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < X.length; i++) {
			temp.append(str.charAt((X[i]) - 1));
		}
		return new String(temp);
	}

	public static void encrypt() {// 加密流程
		System.out.println("------接下来为加密流程-----");
		Scanner input = new Scanner(System.in);

		System.out.println("请输入明文（8位）");
		String plain = input.nextLine();
		System.out.println("请输入主密钥（11位，其中第一位为0，后10位为秘钥）");
		String K = input.nextLine();

		CreateKey(K);// 创建秘钥K1和K2

		plain = Change(plain, IP);// IP置换

		String L0 = plain.substring(0, 4);// 将明文分成左右子串
		String R0 = plain.substring(4, 8);

		String R0_EP = Change(R0, EP);// 右子串进行EP变换
		R0_EP = XOR(R0_EP, key1);// EP变换过后的右子串与key1进行异或运算

		String S0 = R0_EP.substring(0, 4);
		String S1 = R0_EP.substring(4, 8);

		S0 = Sbox(S0, 0);// 进行S盒操作
		S1 = Sbox(S1, 1);

		String S2 = S0 + S1;// S2为进行P4置换的字符串
		String fk1 = Change(S2, p4);// 对S2进行P4置换

		String L1 = R0;// 在第二轮中左右子串进行了交换，且R1还进行了异或操作
		String R1 = XOR(L0, fk1);// 至此，第一轮结束，第二轮开始

		String R1_1 = Change(R1, EP);// 第二轮与第一轮类似
		R1_1 = XOR(R1_1, key2);
		S0 = R1_1.substring(0, 4);
		S1 = R1_1.substring(4, 8);
		S0 = Sbox(S0, 0);
		S1 = Sbox(S1, 1);
		S2 = S0 + S1;
		String fk2 = Change(S2, p4);
		String L2 = XOR(fk2, L1);
		String R2 = R1;// 至此求出L2和R2

		cipher = L2 + R2;
		cipher = Change(cipher, IP_1);
		System.out.println("密文为" + cipher);
	}

	public static void decrypt() {// 解密流程，与加密流程类似，主要是将fk1与fk2顺序进行调整
		System.out.println("-----接下来为解密流程-----");
		System.out.println("密文：" + cipher);
		System.out.println("key2:" + key2);
		System.out.println("key1:" + key1);

		String tempcipher = Change(cipher, IP);// 密文IP置换
		String L0 = tempcipher.substring(0, 4);// 将密文分成左右子串
		String R0 = tempcipher.substring(4, 8);

		String R0_EP = Change(R0, EP);// 右子串进行EP变换
		R0_EP = XOR(R0_EP, key2);// EP变换过后的右子串与key2进行异或运算

		String S0 = R0_EP.substring(0, 4);
		String S1 = R0_EP.substring(4, 8);

		S0 = Sbox(S0, 0);// 进行S盒操作
		S1 = Sbox(S1, 1);

		String S2 = S0 + S1;// S2为进行P4置换的字符串
		String fk2 = Change(S2, p4);// 对S2进行P4置换

		String L1 = R0;// 在第二轮中左右子串进行了交换，且R1还进行了异或操作
		String R1 = XOR(L0, fk2);// 至此，第一轮结束，第二轮开始

		String R1_1 = Change(R1, EP);// 第二轮与第一轮类似
		R1_1 = XOR(R1_1, key1);
		S0 = R1_1.substring(0, 4);
		S1 = R1_1.substring(4, 8);
		S0 = Sbox(S0, 0);
		S1 = Sbox(S1, 1);
		S2 = S0 + S1;
		String fk1 = Change(S2, p4);
		String L2 = XOR(fk1, L1);
		String R2 = R1;// 至此求出L2和R2

		String plain = L2 + R2;
		plain = Change(plain, IP_1);
		System.out.println("明文为" + plain);
	}
}
