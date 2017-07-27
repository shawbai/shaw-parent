package com.shaw.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class FileUtils {
	/**
	 * 获取klass所在的目录
	 * 
	 * @param klass
	 *            类
	 * @return 绝对路径
	 */
	public static String getPath(Class<?> klass) {
		String path = klass.getProtectionDomain().getCodeSource().getLocation()
				.getPath();
		if (!new File(path).isDirectory())
			path = new File(path).getParent();
		return path;
	}

	/**
	 * 一次性建立嵌套的目录，嵌套深度有数组索引决定，例如：<br/>
	 * mkdirs("/", ["a","b","c"] 会建立/a/b/c这样的嵌套目录
	 * 
	 * @param root
	 *            根目录
	 * @param dirs
	 *            字符串数组
	 */
	public static void mkdirs(File root, String[] dirs) {
		mkdirs(root, dirs, dirs.length);
	}

	/**
	 * 用于建立嵌套目录的辅助方法，私有
	 * 
	 * @param root
	 * @param dirs
	 * @param depth
	 */
	private static void mkdirs(File root, String[] dirs, int depth) {
		if (depth == 0)
			return;
		File subdir = new File(root, dirs[dirs.length - depth]);
		subdir.mkdir();
		mkdirs(subdir, dirs, depth - 1);
	}

	/**
	 * 根据给定的内容、编码写出文件
	 * 
	 * @param file
	 *            需要写出的文件对象
	 * @param encoding
	 *            字符编码
	 * @param content
	 *            内容
	 * @throws IOException
	 */
	public static void write(File file, String content, Charset encoding)
			throws IOException {
		OutputStream outs = new FileOutputStream(file);
		write(outs, content, encoding);
	}

	/**
	 * 根据给定的内容、编码写出文件
	 * 
	 * @param outs
	 *            输出流
	 * @param encoding
	 *            字符编码
	 * @param content
	 *            内容
	 * @throws IOException
	 */
	public static void write(OutputStream outs, String content, Charset encoding)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outs,
				encoding));
		writer.write(content);
		writer.close();
	}

	/**
	 * 根据给定的内容使用默认编码（UTF-8）写出文件
	 * 
	 * @param outs
	 *            输出流
	 * @param content
	 *            内容
	 * @throws IOException
	 */
	public static void write(OutputStream outs, String content)
			throws IOException {
		write(outs, content, Charset.forName("UTF-8"));
	}

	/**
	 * 根据给定的编码读出指定文件
	 * 
	 * @param ins
	 *            输入流
	 * @param encoding
	 *            字符编码
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String read(InputStream ins, Charset encoding)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(ins,
				encoding));
		StringBuffer content = new StringBuffer();
		String line;
		while (reader.ready() && (line = reader.readLine()) != null) {
			content.append(line).append("\n");
		}
		reader.close();
		return content.toString();
	}

	/**
	 * 根据给定的编码读出指定文件
	 * 
	 * @param file
	 *            文件对象
	 * @param encoding
	 *            字符编码
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String read(File file, Charset encoding) throws IOException {
		InputStream is = new FileInputStream(file);
		return read(is, encoding);
	}

	/**
	 * 使用默认字符集（UTF-8）读取指定文件
	 * 
	 * @param ins
	 *            输入流
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String read(InputStream ins) throws IOException {
		return read(ins, Charset.forName("UTF-8"));
	}
}
