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

	public static int IP[] = { 2, 6, 3, 1, 4, 8, 5, 7 };// IP���������ڳ�ʼ�û�
	public static int IP_1[] = { 4, 1, 3, 5, 7, 2, 8, 6 };// IP�������溯��������ĩ���û�
	public static int EP[] = new int[] { 4, 1, 2, 3, 2, 3, 4, 1 };

	public static void CreateKey(String K) {// ����K1��K2
		int K1[] = new int[9];
		int K2[] = new int[9];
		int Ktemp[] = new int[11];
		int temp1[] = new int[11];// ����P10�任������
		int templ[] = new int[5];// ��temp1��Ϊ��5λ
		int tempr[] = new int[5];// ��temp1��Ϊ��5λ
		int temp2[] = new int[11];// P8�û�ǰ��һ����ʱ����
		int i, j, k, temp;

		char[] c = K.toCharArray();
		for (i = 0; i < c.length; i++) {
			char m = c[i];
			String s = m + "";
			Ktemp[i] = Integer.parseInt(s);
		}

		for (i = 0; i < Ktemp.length; i++) {// P10�任
			temp1[i] = Ktemp[p10[i]];
		}

		for (i = 0; i < templ.length; i++) {// ��Ϊ��5λ
			templ[i] = temp1[i + 1];
		}
		for (i = 0; i < tempr.length; i++) {// ��Ϊ��5λ
			tempr[i] = temp1[i + K.length() / 2 + 1];
		}

		temp = templ[0];// ��5λLS-1�û�
		for (i = 0; i < templ.length - 1; i++) {
			templ[i] = templ[i + 1];
		}
		templ[templ.length - 1] = temp;

		temp = tempr[0];// ��5λLS-1�û�
		for (i = 0; i < tempr.length - 1; i++) {
			tempr[i] = tempr[i + 1];
		}
		tempr[tempr.length - 1] = temp;

		temp2[0] = 0;// K1��P8�û�ǰ������
		for (i = 1; i <= templ.length; i++) {
			temp2[i] = templ[i - 1];
		}
		for (i = 0; i < tempr.length; i++) {
			temp2[i + 1 + templ.length] = tempr[i];
		}

		for (i = 0; i < K1.length; i++) {// K1��P8�任���õ�K1
			K1[i] = temp2[p8[i]];
		}

		for (i = 0; i < 2; i++) {
			temp = templ[0];// ��5λLS-2�û�
			for (j = 0; j < templ.length - 1; j++) {
				templ[j] = templ[j + 1];
			}
			templ[templ.length - 1] = temp;

			temp = tempr[0];// ��5λLS-2�û�
			for (k = 0; k < tempr.length - 1; k++) {
				tempr[k] = tempr[k + 1];
			}
			tempr[tempr.length - 1] = temp;
		}

		temp2[0] = 0;// K2��P8�û�ǰ������
		for (i = 1; i <= templ.length; i++) {
			temp2[i] = templ[i - 1];
		}
		for (i = 0; i < tempr.length; i++) {
			temp2[i + 1 + templ.length] = tempr[i];
		}

		for (i = 0; i < K2.length; i++) {// K2��P8�任���õ�K2
			K2[i] = temp2[p8[i]];
		}

		StringBuffer temps = new StringBuffer();
		for (i = 1; i < K1.length; i++) {
			temps.append(K1[i]);
		}
		key1 = temps.toString();// K1ת��String��ʽ
		temps.delete(0, temps.length());

		for (i = 1; i < K2.length; i++) {
			temps.append(K2[i]);
		}
		key2 = temps.toString();// K2ת��String��ʽ
		System.out.println("key1:" + key1);
		System.out.println("key2:" + key2);
	}

	public static String XOR(String str, String key) {// ����������
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

	public static String Sbox(String str, int n) {// S����ز���
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

	public static String Change(String str, int[] X) {// �����û�
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < X.length; i++) {
			temp.append(str.charAt((X[i]) - 1));
		}
		return new String(temp);
	}

	public static void encrypt() {// ��������
		System.out.println("------������Ϊ��������-----");
		Scanner input = new Scanner(System.in);

		System.out.println("���������ģ�8λ��");
		String plain = input.nextLine();
		System.out.println("����������Կ��11λ�����е�һλΪ0����10λΪ��Կ��");
		String K = input.nextLine();

		CreateKey(K);// ������ԿK1��K2

		plain = Change(plain, IP);// IP�û�

		String L0 = plain.substring(0, 4);// �����ķֳ������Ӵ�
		String R0 = plain.substring(4, 8);

		String R0_EP = Change(R0, EP);// ���Ӵ�����EP�任
		R0_EP = XOR(R0_EP, key1);// EP�任��������Ӵ���key1�����������

		String S0 = R0_EP.substring(0, 4);
		String S1 = R0_EP.substring(4, 8);

		S0 = Sbox(S0, 0);// ����S�в���
		S1 = Sbox(S1, 1);

		String S2 = S0 + S1;// S2Ϊ����P4�û����ַ���
		String fk1 = Change(S2, p4);// ��S2����P4�û�

		String L1 = R0;// �ڵڶ����������Ӵ������˽�������R1��������������
		String R1 = XOR(L0, fk1);// ���ˣ���һ�ֽ������ڶ��ֿ�ʼ

		String R1_1 = Change(R1, EP);// �ڶ������һ������
		R1_1 = XOR(R1_1, key2);
		S0 = R1_1.substring(0, 4);
		S1 = R1_1.substring(4, 8);
		S0 = Sbox(S0, 0);
		S1 = Sbox(S1, 1);
		S2 = S0 + S1;
		String fk2 = Change(S2, p4);
		String L2 = XOR(fk2, L1);
		String R2 = R1;// �������L2��R2

		cipher = L2 + R2;
		cipher = Change(cipher, IP_1);
		System.out.println("����Ϊ" + cipher);
	}

	public static void decrypt() {// �������̣�������������ƣ���Ҫ�ǽ�fk1��fk2˳����е���
		System.out.println("-----������Ϊ��������-----");
		System.out.println("���ģ�" + cipher);
		System.out.println("key2:" + key2);
		System.out.println("key1:" + key1);

		String tempcipher = Change(cipher, IP);// ����IP�û�
		String L0 = tempcipher.substring(0, 4);// �����ķֳ������Ӵ�
		String R0 = tempcipher.substring(4, 8);

		String R0_EP = Change(R0, EP);// ���Ӵ�����EP�任
		R0_EP = XOR(R0_EP, key2);// EP�任��������Ӵ���key2�����������

		String S0 = R0_EP.substring(0, 4);
		String S1 = R0_EP.substring(4, 8);

		S0 = Sbox(S0, 0);// ����S�в���
		S1 = Sbox(S1, 1);

		String S2 = S0 + S1;// S2Ϊ����P4�û����ַ���
		String fk2 = Change(S2, p4);// ��S2����P4�û�

		String L1 = R0;// �ڵڶ����������Ӵ������˽�������R1��������������
		String R1 = XOR(L0, fk2);// ���ˣ���һ�ֽ������ڶ��ֿ�ʼ

		String R1_1 = Change(R1, EP);// �ڶ������һ������
		R1_1 = XOR(R1_1, key1);
		S0 = R1_1.substring(0, 4);
		S1 = R1_1.substring(4, 8);
		S0 = Sbox(S0, 0);
		S1 = Sbox(S1, 1);
		S2 = S0 + S1;
		String fk1 = Change(S2, p4);
		String L2 = XOR(fk1, L1);
		String R2 = R1;// �������L2��R2

		String plain = L2 + R2;
		plain = Change(plain, IP_1);
		System.out.println("����Ϊ" + plain);
	}
}
