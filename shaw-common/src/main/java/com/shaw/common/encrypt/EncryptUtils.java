package com.shaw.common.encrypt;

import com.shaw.common.util.ByteUtils;
import com.shaw.common.util.ShawStringUtils;

/**
 * 加密工具类：1DES加密、3DES加解密、ANSI91（MAC算法）、ANSIX98（PINBLOCK算法）
 * 
 * @author shaw
 */
public final class EncryptUtils {

	/**
	 * 单DES加密
	 * 
	 * @param plainTextBytes
	 *            明文
	 * @param key
	 *            密钥
	 */
	public static byte[] oneDesEncrypt(byte[] plainTextBytes, String key) {
		byte[] keyBytes = ByteUtils.hexString2byteArray(key.substring(0, 16));
		byte[] cipherTextBytes = DesEncrypt(plainTextBytes, keyBytes, 1);
		return cipherTextBytes;
	}

	// 主密钥解密工作密钥
	public static String oneDesDecrypt(String cipherHexString, String key) {
		byte[] keyBytes1 = ByteUtils.hexString2byteArray(key.substring(0, 16));
		byte[] cipherTextBytes = ByteUtils.hexString2byteArray(cipherHexString
				.substring(0, 16));
		byte[] plainTestBytes = DesEncrypt(cipherTextBytes, keyBytes1, 1);
		return ByteUtils.byteArray2hexString(plainTestBytes);
	}

	/**
	 * 3DES加密
	 * 
	 * @param plainTextBytes
	 *            明文
	 * @param key
	 *            密钥
	 * @return
	 */
	public static byte[] threeDesEncrypt(byte[] plainTextBytes, String key) {
		byte[] keyBytes1 = ByteUtils.hexString2byteArray(key.substring(0, 16));
		byte[] keyBytes2 = ByteUtils.hexString2byteArray(key.substring(16, 32));
		byte[] cipherTextBytes = DesEncrypt(plainTextBytes, keyBytes1, 1);
		cipherTextBytes = DesEncrypt(cipherTextBytes, keyBytes2, 0);
		cipherTextBytes = DesEncrypt(cipherTextBytes, keyBytes1, 1);
		return cipherTextBytes;
	}

	/**
	 * 3DES 解密
	 * 
	 * @param cipherText
	 *            明文
	 * @param key
	 * @return
	 */
	public static byte[] threeDesDecrypt(byte[] cipherText, String key) {
		byte[] keyBytes1 = ByteUtils.hexString2byteArray(key.substring(0, 16));
		byte[] keyBytes2 = ByteUtils.hexString2byteArray(key.substring(16, 32));
		byte[] plainTextBytes = DesEncrypt(cipherText, keyBytes1, 0);
		plainTextBytes = DesEncrypt(plainTextBytes, keyBytes2, 1);
		plainTextBytes = DesEncrypt(plainTextBytes, keyBytes1, 0);
		return plainTextBytes;
	}

	/**
	 * DES加密
	 * 
	 * @param plainTextBytes
	 *            明文bytes
	 * @param keyBytes
	 *            密钥bytes
	 * @param round
	 *            加密次数
	 * @return 密文bytes
	 */
	public static byte[] DesEncrypt(byte plainTextBytes[], byte keyBytes[],
			int round) {
		String s = "";
		byte abyte2[] = new byte[plainTextBytes.length];
		byte abyte3[] = { 0, 0, 0, 0, 0, 0, 0, 0 };
		byte abyte4[] = new byte[8];
		int j = plainTextBytes.length;
		for (int k = 0; k < j / 8; k++) {
			for (int l = 0; l < 8; l++) {
				abyte4[l] = plainTextBytes[k * 8 + l];
			}

			byte abyte5[] = new byte[64];
			byte abyte6[] = new byte[56];
			byte abyte7[] = new byte[48];
			byte abyte8[] = new byte[48];
			byte abyte9[] = new byte[64];
			byte abyte10[] = new byte[64];
			byte abyte11[][] = {
					{ 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
					{ 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
					{ 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
					{ 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } };
			byte abyte12[][] = {
					{ 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
					{ 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
					{ 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
					{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } };
			byte abyte13[][] = {
					{ 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
					{ 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
					{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
					{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } };
			byte abyte14[][] = {
					{ 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
					{ 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
					{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
					{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } };
			byte abyte15[][] = {
					{ 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
					{ 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
					{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
					{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } };
			byte abyte16[][] = {
					{ 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
					{ 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
					{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
					{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } };
			byte abyte17[][] = {
					{ 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
					{ 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
					{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
					{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } };
			byte abyte18[][] = {
					{ 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
					{ 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
					{ 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
					{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };
			byte abyte19[] = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
			byte abyte20[] = { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1,
					0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0,
					1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1,
					1, 1, 1, 0, 1, 1, 1, 1 };
			for (int j1 = 0; j1 <= 7; j1++) {
				int l3;
				if (keyBytes[j1] >= 0) {
					l3 = keyBytes[j1];
				} else {
					l3 = keyBytes[j1] + 256;
				}
				l3 %= 256;
				abyte10[8 * j1] = (byte) ((l3 / 128) % 2);
				abyte10[8 * j1 + 1] = (byte) ((l3 / 64) % 2);
				abyte10[8 * j1 + 2] = (byte) ((l3 / 32) % 2);
				abyte10[8 * j1 + 3] = (byte) ((l3 / 16) % 2);
				abyte10[8 * j1 + 4] = (byte) ((l3 / 8) % 2);
				abyte10[8 * j1 + 5] = (byte) ((l3 / 4) % 2);
				abyte10[8 * j1 + 6] = (byte) ((l3 / 2) % 2);
				abyte10[8 * j1 + 7] = (byte) (l3 % 2);
			}

			for (int k1 = 0; k1 <= 7; k1++) {
				int i4 = abyte4[k1];
				if (i4 < 0) {
					i4 += 256;
				}
				abyte9[8 * k1] = (byte) ((i4 / 128) % 2);
				abyte9[8 * k1 + 1] = (byte) ((i4 / 64) % 2);
				abyte9[8 * k1 + 2] = (byte) ((i4 / 32) % 2);
				abyte9[8 * k1 + 3] = (byte) ((i4 / 16) % 2);
				abyte9[8 * k1 + 4] = (byte) ((i4 / 8) % 2);
				abyte9[8 * k1 + 5] = (byte) ((i4 / 4) % 2);
				abyte9[8 * k1 + 6] = (byte) ((i4 / 2) % 2);
				abyte9[8 * k1 + 7] = (byte) (i4 % 2);
			}

			abyte5[0] = abyte9[57];
			abyte5[1] = abyte9[49];
			abyte5[2] = abyte9[41];
			abyte5[3] = abyte9[33];
			abyte5[4] = abyte9[25];
			abyte5[5] = abyte9[17];
			abyte5[6] = abyte9[9];
			abyte5[7] = abyte9[1];
			abyte5[8] = abyte9[59];
			abyte5[9] = abyte9[51];
			abyte5[10] = abyte9[43];
			abyte5[11] = abyte9[35];
			abyte5[12] = abyte9[27];
			abyte5[13] = abyte9[19];
			abyte5[14] = abyte9[11];
			abyte5[15] = abyte9[3];
			abyte5[16] = abyte9[61];
			abyte5[17] = abyte9[53];
			abyte5[18] = abyte9[45];
			abyte5[19] = abyte9[37];
			abyte5[20] = abyte9[29];
			abyte5[21] = abyte9[21];
			abyte5[22] = abyte9[13];
			abyte5[23] = abyte9[5];
			abyte5[24] = abyte9[63];
			abyte5[25] = abyte9[55];
			abyte5[26] = abyte9[47];
			abyte5[27] = abyte9[39];
			abyte5[28] = abyte9[31];
			abyte5[29] = abyte9[23];
			abyte5[30] = abyte9[15];
			abyte5[31] = abyte9[7];
			abyte5[32] = abyte9[56];
			abyte5[33] = abyte9[48];
			abyte5[34] = abyte9[40];
			abyte5[35] = abyte9[32];
			abyte5[36] = abyte9[24];
			abyte5[37] = abyte9[16];
			abyte5[38] = abyte9[8];
			abyte5[39] = abyte9[0];
			abyte5[40] = abyte9[58];
			abyte5[41] = abyte9[50];
			abyte5[42] = abyte9[42];
			abyte5[43] = abyte9[34];
			abyte5[44] = abyte9[26];
			abyte5[45] = abyte9[18];
			abyte5[46] = abyte9[10];
			abyte5[47] = abyte9[2];
			abyte5[48] = abyte9[60];
			abyte5[49] = abyte9[52];
			abyte5[50] = abyte9[44];
			abyte5[51] = abyte9[36];
			abyte5[52] = abyte9[28];
			abyte5[53] = abyte9[20];
			abyte5[54] = abyte9[12];
			abyte5[55] = abyte9[4];
			abyte5[56] = abyte9[62];
			abyte5[57] = abyte9[54];
			abyte5[58] = abyte9[46];
			abyte5[59] = abyte9[38];
			abyte5[60] = abyte9[30];
			abyte5[61] = abyte9[22];
			abyte5[62] = abyte9[14];
			abyte5[63] = abyte9[6];
			abyte6[0] = abyte10[56];
			abyte6[1] = abyte10[48];
			abyte6[2] = abyte10[40];
			abyte6[3] = abyte10[32];
			abyte6[4] = abyte10[24];
			abyte6[5] = abyte10[16];
			abyte6[6] = abyte10[8];
			abyte6[7] = abyte10[0];
			abyte6[8] = abyte10[57];
			abyte6[9] = abyte10[49];
			abyte6[10] = abyte10[41];
			abyte6[11] = abyte10[33];
			abyte6[12] = abyte10[25];
			abyte6[13] = abyte10[17];
			abyte6[14] = abyte10[9];
			abyte6[15] = abyte10[1];
			abyte6[16] = abyte10[58];
			abyte6[17] = abyte10[50];
			abyte6[18] = abyte10[42];
			abyte6[19] = abyte10[34];
			abyte6[20] = abyte10[26];
			abyte6[21] = abyte10[18];
			abyte6[22] = abyte10[10];
			abyte6[23] = abyte10[2];
			abyte6[24] = abyte10[59];
			abyte6[25] = abyte10[51];
			abyte6[26] = abyte10[43];
			abyte6[27] = abyte10[35];
			abyte6[28] = abyte10[62];
			abyte6[29] = abyte10[54];
			abyte6[30] = abyte10[46];
			abyte6[31] = abyte10[38];
			abyte6[32] = abyte10[30];
			abyte6[33] = abyte10[22];
			abyte6[34] = abyte10[14];
			abyte6[35] = abyte10[6];
			abyte6[36] = abyte10[61];
			abyte6[37] = abyte10[53];
			abyte6[38] = abyte10[45];
			abyte6[39] = abyte10[37];
			abyte6[40] = abyte10[29];
			abyte6[41] = abyte10[21];
			abyte6[42] = abyte10[13];
			abyte6[43] = abyte10[5];
			abyte6[44] = abyte10[60];
			abyte6[45] = abyte10[52];
			abyte6[46] = abyte10[44];
			abyte6[47] = abyte10[36];
			abyte6[48] = abyte10[28];
			abyte6[49] = abyte10[20];
			abyte6[50] = abyte10[12];
			abyte6[51] = abyte10[4];
			abyte6[52] = abyte10[27];
			abyte6[53] = abyte10[19];
			abyte6[54] = abyte10[11];
			abyte6[55] = abyte10[3];
			for (int k5 = 1; k5 <= 16; k5++) {
				for (int l1 = 0; l1 <= 31; l1++) {
					abyte9[l1] = abyte5[l1 + 32];
				}

				abyte7[0] = abyte9[31];
				abyte7[1] = abyte9[0];
				abyte7[2] = abyte9[1];
				abyte7[3] = abyte9[2];
				abyte7[4] = abyte9[3];
				abyte7[5] = abyte9[4];
				abyte7[6] = abyte9[3];
				abyte7[7] = abyte9[4];
				abyte7[8] = abyte9[5];
				abyte7[9] = abyte9[6];
				abyte7[10] = abyte9[7];
				abyte7[11] = abyte9[8];
				abyte7[12] = abyte9[7];
				abyte7[13] = abyte9[8];
				abyte7[14] = abyte9[9];
				abyte7[15] = abyte9[10];
				abyte7[16] = abyte9[11];
				abyte7[17] = abyte9[12];
				abyte7[18] = abyte9[11];
				abyte7[19] = abyte9[12];
				abyte7[20] = abyte9[13];
				abyte7[21] = abyte9[14];
				abyte7[22] = abyte9[15];
				abyte7[23] = abyte9[16];
				abyte7[24] = abyte9[15];
				abyte7[25] = abyte9[16];
				abyte7[26] = abyte9[17];
				abyte7[27] = abyte9[18];
				abyte7[28] = abyte9[19];
				abyte7[29] = abyte9[20];
				abyte7[30] = abyte9[19];
				abyte7[31] = abyte9[20];
				abyte7[32] = abyte9[21];
				abyte7[33] = abyte9[22];
				abyte7[34] = abyte9[23];
				abyte7[35] = abyte9[24];
				abyte7[36] = abyte9[23];
				abyte7[37] = abyte9[24];
				abyte7[38] = abyte9[25];
				abyte7[39] = abyte9[26];
				abyte7[40] = abyte9[27];
				abyte7[41] = abyte9[28];
				abyte7[42] = abyte9[27];
				abyte7[43] = abyte9[28];
				abyte7[44] = abyte9[29];
				abyte7[45] = abyte9[30];
				abyte7[46] = abyte9[31];
				abyte7[47] = abyte9[0];
				if (round == 1) {
					byte byte0 = abyte19[k5 - 1];
					for (int i2 = 0; i2 <= byte0 - 1; i2++) {
						byte byte2 = abyte6[0];
						byte byte4 = abyte6[28];
						for (int j4 = 0; j4 <= 26; j4++) {
							abyte6[j4] = abyte6[j4 + 1];
							abyte6[28 + j4] = abyte6[j4 + 29];
						}

						abyte6[27] = byte2;
						abyte6[55] = byte4;
					}

				} else if (k5 > 1) {
					byte byte1 = abyte19[17 - k5];
					for (int j2 = 0; j2 <= byte1 - 1; j2++) {
						byte byte3 = abyte6[27];
						byte byte5 = abyte6[55];
						for (int k4 = 27; k4 >= 1; k4--) {
							abyte6[k4] = abyte6[k4 - 1];
							abyte6[k4 + 28] = abyte6[k4 + 27];
						}

						abyte6[0] = byte3;
						abyte6[28] = byte5;
					}

				}
				abyte8[0] = abyte6[13];
				abyte8[1] = abyte6[16];
				abyte8[2] = abyte6[10];
				abyte8[3] = abyte6[23];
				abyte8[4] = abyte6[0];
				abyte8[5] = abyte6[4];
				abyte8[6] = abyte6[2];
				abyte8[7] = abyte6[27];
				abyte8[8] = abyte6[14];
				abyte8[9] = abyte6[5];
				abyte8[10] = abyte6[20];
				abyte8[11] = abyte6[9];
				abyte8[12] = abyte6[22];
				abyte8[13] = abyte6[18];
				abyte8[14] = abyte6[11];
				abyte8[15] = abyte6[3];
				abyte8[16] = abyte6[25];
				abyte8[17] = abyte6[7];
				abyte8[18] = abyte6[15];
				abyte8[19] = abyte6[6];
				abyte8[20] = abyte6[26];
				abyte8[21] = abyte6[19];
				abyte8[22] = abyte6[12];
				abyte8[23] = abyte6[1];
				abyte8[24] = abyte6[40];
				abyte8[25] = abyte6[51];
				abyte8[26] = abyte6[30];
				abyte8[27] = abyte6[36];
				abyte8[28] = abyte6[46];
				abyte8[29] = abyte6[54];
				abyte8[30] = abyte6[29];
				abyte8[31] = abyte6[39];
				abyte8[32] = abyte6[50];
				abyte8[33] = abyte6[44];
				abyte8[34] = abyte6[32];
				abyte8[35] = abyte6[47];
				abyte8[36] = abyte6[43];
				abyte8[37] = abyte6[48];
				abyte8[38] = abyte6[38];
				abyte8[39] = abyte6[55];
				abyte8[40] = abyte6[33];
				abyte8[41] = abyte6[52];
				abyte8[42] = abyte6[45];
				abyte8[43] = abyte6[41];
				abyte8[44] = abyte6[49];
				abyte8[45] = abyte6[35];
				abyte8[46] = abyte6[28];
				abyte8[47] = abyte6[31];
				for (int k2 = 0; k2 <= 47; k2++) {
					abyte7[k2] = (byte) (abyte7[k2] ^ abyte8[k2]);
				}

				int i1 = abyte11[2 * abyte7[0] + abyte7[5]][2
						* (2 * (2 * abyte7[1] + abyte7[2]) + abyte7[3])
						+ abyte7[4]];
				i1 *= 4;
				abyte8[0] = abyte20[0 + i1];
				abyte8[1] = abyte20[1 + i1];
				abyte8[2] = abyte20[2 + i1];
				abyte8[3] = abyte20[3 + i1];
				i1 = abyte12[2 * abyte7[6] + abyte7[11]][2
						* (2 * (2 * abyte7[7] + abyte7[8]) + abyte7[9])
						+ abyte7[10]];
				i1 *= 4;
				abyte8[4] = abyte20[0 + i1];
				abyte8[5] = abyte20[1 + i1];
				abyte8[6] = abyte20[2 + i1];
				abyte8[7] = abyte20[3 + i1];
				i1 = abyte13[2 * abyte7[12] + abyte7[17]][2
						* (2 * (2 * abyte7[13] + abyte7[14]) + abyte7[15])
						+ abyte7[16]];
				i1 *= 4;
				abyte8[8] = abyte20[0 + i1];
				abyte8[9] = abyte20[1 + i1];
				abyte8[10] = abyte20[2 + i1];
				abyte8[11] = abyte20[3 + i1];
				i1 = abyte14[2 * abyte7[18] + abyte7[23]][2
						* (2 * (2 * abyte7[19] + abyte7[20]) + abyte7[21])
						+ abyte7[22]];
				i1 *= 4;
				abyte8[12] = abyte20[0 + i1];
				abyte8[13] = abyte20[1 + i1];
				abyte8[14] = abyte20[2 + i1];
				abyte8[15] = abyte20[3 + i1];
				i1 = abyte15[2 * abyte7[24] + abyte7[29]][2
						* (2 * (2 * abyte7[25] + abyte7[26]) + abyte7[27])
						+ abyte7[28]];
				i1 *= 4;
				abyte8[16] = abyte20[0 + i1];
				abyte8[17] = abyte20[1 + i1];
				abyte8[18] = abyte20[2 + i1];
				abyte8[19] = abyte20[3 + i1];
				i1 = abyte16[2 * abyte7[30] + abyte7[35]][2
						* (2 * (2 * abyte7[31] + abyte7[32]) + abyte7[33])
						+ abyte7[34]];
				i1 *= 4;
				abyte8[20] = abyte20[0 + i1];
				abyte8[21] = abyte20[1 + i1];
				abyte8[22] = abyte20[2 + i1];
				abyte8[23] = abyte20[3 + i1];
				i1 = abyte17[2 * abyte7[36] + abyte7[41]][2
						* (2 * (2 * abyte7[37] + abyte7[38]) + abyte7[39])
						+ abyte7[40]];
				i1 *= 4;
				abyte8[24] = abyte20[0 + i1];
				abyte8[25] = abyte20[1 + i1];
				abyte8[26] = abyte20[2 + i1];
				abyte8[27] = abyte20[3 + i1];
				i1 = abyte18[2 * abyte7[42] + abyte7[47]][2
						* (2 * (2 * abyte7[43] + abyte7[44]) + abyte7[45])
						+ abyte7[46]];
				i1 *= 4;
				abyte8[28] = abyte20[0 + i1];
				abyte8[29] = abyte20[1 + i1];
				abyte8[30] = abyte20[2 + i1];
				abyte8[31] = abyte20[3 + i1];
				abyte7[0] = abyte8[15];
				abyte7[1] = abyte8[6];
				abyte7[2] = abyte8[19];
				abyte7[3] = abyte8[20];
				abyte7[4] = abyte8[28];
				abyte7[5] = abyte8[11];
				abyte7[6] = abyte8[27];
				abyte7[7] = abyte8[16];
				abyte7[8] = abyte8[0];
				abyte7[9] = abyte8[14];
				abyte7[10] = abyte8[22];
				abyte7[11] = abyte8[25];
				abyte7[12] = abyte8[4];
				abyte7[13] = abyte8[17];
				abyte7[14] = abyte8[30];
				abyte7[15] = abyte8[9];
				abyte7[16] = abyte8[1];
				abyte7[17] = abyte8[7];
				abyte7[18] = abyte8[23];
				abyte7[19] = abyte8[13];
				abyte7[20] = abyte8[31];
				abyte7[21] = abyte8[26];
				abyte7[22] = abyte8[2];
				abyte7[23] = abyte8[8];
				abyte7[24] = abyte8[18];
				abyte7[25] = abyte8[12];
				abyte7[26] = abyte8[29];
				abyte7[27] = abyte8[5];
				abyte7[28] = abyte8[21];
				abyte7[29] = abyte8[10];
				abyte7[30] = abyte8[3];
				abyte7[31] = abyte8[24];
				for (int l2 = 0; l2 < 32; l2++) {
					abyte5[l2 + 32] = (byte) (abyte5[l2] ^ abyte7[l2]);
					abyte5[l2] = abyte9[l2];
				}

			}

			for (int i3 = 0; i3 < 32; i3++) {
				int l4 = abyte5[i3];
				abyte5[i3] = abyte5[32 + i3];
				abyte5[32 + i3] = (byte) l4;
			}

			abyte9[0] = abyte5[39];
			abyte9[1] = abyte5[7];
			abyte9[2] = abyte5[47];
			abyte9[3] = abyte5[15];
			abyte9[4] = abyte5[55];
			abyte9[5] = abyte5[23];
			abyte9[6] = abyte5[63];
			abyte9[7] = abyte5[31];
			abyte9[8] = abyte5[38];
			abyte9[9] = abyte5[6];
			abyte9[10] = abyte5[46];
			abyte9[11] = abyte5[14];
			abyte9[12] = abyte5[54];
			abyte9[13] = abyte5[22];
			abyte9[14] = abyte5[62];
			abyte9[15] = abyte5[30];
			abyte9[16] = abyte5[37];
			abyte9[17] = abyte5[5];
			abyte9[18] = abyte5[45];
			abyte9[19] = abyte5[13];
			abyte9[20] = abyte5[53];
			abyte9[21] = abyte5[21];
			abyte9[22] = abyte5[61];
			abyte9[23] = abyte5[29];
			abyte9[24] = abyte5[36];
			abyte9[25] = abyte5[4];
			abyte9[26] = abyte5[44];
			abyte9[27] = abyte5[12];
			abyte9[28] = abyte5[52];
			abyte9[29] = abyte5[20];
			abyte9[30] = abyte5[60];
			abyte9[31] = abyte5[28];
			abyte9[32] = abyte5[35];
			abyte9[33] = abyte5[3];
			abyte9[34] = abyte5[43];
			abyte9[35] = abyte5[11];
			abyte9[36] = abyte5[51];
			abyte9[37] = abyte5[19];
			abyte9[38] = abyte5[59];
			abyte9[39] = abyte5[27];
			abyte9[40] = abyte5[34];
			abyte9[41] = abyte5[2];
			abyte9[42] = abyte5[42];
			abyte9[43] = abyte5[10];
			abyte9[44] = abyte5[50];
			abyte9[45] = abyte5[18];
			abyte9[46] = abyte5[58];
			abyte9[47] = abyte5[26];
			abyte9[48] = abyte5[33];
			abyte9[49] = abyte5[1];
			abyte9[50] = abyte5[41];
			abyte9[51] = abyte5[9];
			abyte9[52] = abyte5[49];
			abyte9[53] = abyte5[17];
			abyte9[54] = abyte5[57];
			abyte9[55] = abyte5[25];
			abyte9[56] = abyte5[32];
			abyte9[57] = abyte5[0];
			abyte9[58] = abyte5[40];
			abyte9[59] = abyte5[8];
			abyte9[60] = abyte5[48];
			abyte9[61] = abyte5[16];
			abyte9[62] = abyte5[56];
			abyte9[63] = abyte5[24];
			int i5 = 0;
			for (int j3 = 0; j3 < 8; j3++) {
				abyte3[j3] = 0;
				for (int j5 = 0; j5 < 7; j5++) {
					abyte3[j3] = (byte) ((abyte3[j3] + abyte9[i5 + j5]) * 2);
				}

				abyte3[j3] = (byte) (abyte3[j3] + abyte9[i5 + 7]);
				i5 += 8;
			}

			for (int k3 = 0; k3 < 8; k3++) {
				if (abyte3[k3] < 0) {
					abyte3[k3] = (byte) (abyte3[k3] + 256);
				}
				s = s + (char) abyte3[k3];
				abyte2[k * 8 + k3] = abyte3[k3];
			}
		}
		return abyte2;
	}

	public static String toHexString(byte abyte0[]) {
		String s = "";
		for (int i = 0; i < abyte0.length; i++) {
			byte byte0 = abyte0[i];
			String s1 = Integer.toHexString(byte0);
			if (s1.length() > 2) {
				s1 = s1.substring(s1.length() - 2);
			}
			if (s1.length() < 2) {
				s1 = "0" + s1;
			}
			s = s + s1;
		}

		return s.toUpperCase();
	}

	/**
	 * 标准MAC算法（已成功在芜湖、安庆等地使用）<br>
	 * 使用单位：畅付通
	 * 
	 * @param macData
	 *            输入HEX字符串
	 * @param mackey
	 *            16位的HEX字符串
	 */
	public static String ansix91(String macData, String mackey) {
		byte[] macBytes = { 0, 0, 0, 0, 0, 0, 0, 0 };
		String[] macArrays = ShawStringUtils.splitStringByLen(macData, 16);
		int macArraysLen = macArrays.length;
		for (int i = 0; i < macArraysLen; i++) {
			byte[] initMacBytes = { 0, 0, 0, 0, 0, 0, 0, 0 };
			byte[] macBcdBytes = ByteUtils.hexString2byteArray(macArrays[i]);
			System.arraycopy(macBcdBytes, 0, initMacBytes, 0,
					macBcdBytes.length);
			initMacBytes = ByteUtils.XOR(macBytes, initMacBytes);
			macBytes = oneDesEncrypt(initMacBytes, mackey);
		}
		return ByteUtils.byteArray2hexString(macBytes);
	}

	/**
	 * 密码加密ANSIX98标准
	 * 
	 * @param cardNo
	 *            主账号
	 * @param pin
	 *            密码明文
	 * @param pinkey
	 *            工作密钥
	 * @return pinBlock 密码密文
	 */
	public static String ansix98(String cardNo, String pin, String pinkey) {
		// 组织初始个人识别号数据块
		byte initBytes[] = { (byte) 0x06, 0, 0, 0, (byte) 0xff, (byte) 0xff,
				(byte) 0xff, (byte) 0xff };
		byte pinBcd[] = ByteUtils.str2BCD(pin);
		System.arraycopy(pinBcd, 0, initBytes, 1, 3);
		// 对账号进行处理，倒数第二开始往前取12位
		String cardNoNew = "0000"
				+ cardNo.substring(cardNo.length() - 13, cardNo.length() - 1);
		byte[] cardNobcd = ByteUtils.str2BCD(cardNoNew);
		byte[] resultXor = new byte[8];
		// 密码和卡号做异或操作
		for (int i = 0; i <= 7; i++) {
			int b = (int) initBytes[i] ^ (int) cardNobcd[i];
			resultXor[i] = (byte) b;
		}
		int pinkeyLen = pinkey.length();
		String cipherTextPin = null;
		if (pinkeyLen == 32) { // 3Des加密
			cipherTextPin = ByteUtils.byteArray2hexString(threeDesEncrypt(
					resultXor, pinkey));
		}
		if (pinkeyLen == 16) { // 单D加密
			cipherTextPin = ByteUtils.byteArray2hexString(oneDesEncrypt(
					resultXor, pinkey));
		}
		return cipherTextPin;
	}

	/**
	 * 遵义畅付通密码加密计算
	 * 
	 * @param pin
	 *            密码明文
	 * @param pinkey
	 *            工作密钥
	 * @return pinBlock 密码密文
	 */
	public static String ansix98WithoutCardNo(String pin, String pinkey) {
		byte[] pinBytes = { (byte) 0x06, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
		byte[] pinBcd = ByteUtils.str2BCD(pin);
		System.arraycopy(pinBcd, 0, pinBytes, 1, 3);

		int pinkeyLen = pinkey.length();
		String cipherTextPin = null;
		if (pinkeyLen == 32) { // 3DES加密
			cipherTextPin = ByteUtils.byteArray2hexString(threeDesEncrypt(
					pinBytes, pinkey));
		}
		if (pinkeyLen == 16) { // 单DES加密
			cipherTextPin = ByteUtils.byteArray2hexString(oneDesEncrypt(
					pinBytes, pinkey));
		}
		return cipherTextPin;
	}

}
