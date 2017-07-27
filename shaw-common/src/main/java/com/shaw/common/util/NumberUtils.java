package com.shaw.common.util;

import java.util.HashMap;
import java.util.Random;
/**
 * 数字处理工具类：产生随机整数
 * @author shaw
 */
public class NumberUtils {

	/**
	 * 产生一个随机数，在给定范围[start, end]内均匀分布
	 * @param start 下限
	 * @param end 上限
	 * @return
	 */
	public static int getRandomInt(int start, int end) {
		Random r = new Random();
		int intValue = start
				+ (int) ((end - start + 1) * Math.abs(((float) r.nextInt())) / Integer.MAX_VALUE);
		return intValue;
	}

	public static void main(String[] args) {
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		for (int i =0; i<10000; i++){
			int v = NumberUtils.getRandomInt(0, 20);
			if (result.containsKey(v)){
				result.put(v, result.get(v)+1);
			}else{
				result.put(v, 1);
			}
		}
		
		for (Integer k: result.keySet()){
			System.out.println(k+" => "+result.get(k));
		}
	}
}
