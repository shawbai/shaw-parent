package com.shaw.common.io;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

/**
 * TCP通信处理类
 * 
 * @author shaw
 */
public class SocketUtils {
	public static final Logger log = Logger.getLogger(SocketUtils.class);

	private Socket socket;
	private DataOutputStream out = null; // 输出流
	private DataInputStream in = null; // 输入流
	private String ip; // 服务器IP地址
	private int port; // 服务器端口号
	private int timeOut = 15; // 接收数据的超时时间，单位：秒

	/* 以下是一些标志位 */
	public boolean connected = false; // 连接服务器是否正常
	public boolean recvOK = true; // 接收数据是否正常
	public boolean sendOK = true; // 发送数据是否正常

	/**
	 * 初始化
	 * 
	 * @param ip
	 *            服务器地址
	 * @param port
	 *            端口
	 */
	public SocketUtils(String ip, int port) //
	{
		this.ip = ip;
		this.port = port;
		this.timeOut = 15;
	}

	/**
	 * 连接目标主机
	 * 
	 * @return成功返回true，否则返回false
	 */
	protected boolean connect() //
	{
		try {
			log.info(String.format("尝试连接服务器<%s:%d>", this.ip, this.port));
			socket = new Socket();
			socket.setSoTimeout(this.timeOut * 1000); // 设置接收数据的超时时间
			socket.setReuseAddress(true);
			// socket.setReceiveBufferSize(5120);// 设置接收数据的缓冲区大小为5K
			// socket.setSoLinger(false, 0);
			SocketAddress sa = new InetSocketAddress(this.ip, this.port);
			socket.connect(sa, this.timeOut * 1000); // 连接服务器，连接超时时间为5秒钟
			this.connected = true;
			return true;
		} catch (Exception ex) {
			log.error("连接服务器发生异常:" + ex.getMessage());
			this.connected = false;
			close();// 关闭连接
			return false;
		}
	}

	/**
	 * 获得TCP连接输入流数据
	 * 
	 * @param charset
	 *            字符集编码
	 * @return 根据指定字符集编码所生成的字符串
	 */
	protected String recvData(Charset charset) {
		byte[] data = recvData();
		if (data != null) {
			return new String(data, charset);
		} else {
			return "";
		}
	}

	/**
	 * 获得TCP连接输入流数据
	 * 
	 * @return 收到的回应byte数组
	 */
	protected byte[] recvData() //
	{
		try {
			this.in = new DataInputStream(socket.getInputStream());

			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int count = -1;
			while ((count = in.read(data, 0, 1024)) != -1) {
				outStream.write(data, 0, count);
			}
			data = null;
			int length = outStream.toByteArray().length;
			log.debug(String.format("接收到：%d bytes", length));
			return outStream.toByteArray();
		} catch (Exception ex) {
			log.error("SOCKET读取返回数据发生异常:", ex);
			this.recvOK = false;
			close();// 关闭连接
			return null;
		}
	}

	/**
	 * 向目标主机发送数据
	 * 
	 * @param data
	 *            所要发送的数据
	 * @return 成功返回true，否则返回false
	 */
	protected boolean sendData(String data) {
		return sendData(data.getBytes());
	}

	/**
	 * 向目标主机发送数据
	 * 
	 * @param data
	 *            所要发送的byte数组
	 * @return 成功返回true，否则返回false
	 */
	protected boolean sendData(byte[] data)
	{// 参数：OutputData-输出的数据
		try {
			out = new DataOutputStream(socket.getOutputStream());
			out.flush();
			out.write(data);
			out.flush();
			return true;
		} catch (Exception ex) {
			log.error("SOCKET发送数据异常:", ex);
			this.sendOK = false;
			close(); // 关闭连接
			return false;
		}
	}

	/**
	 * 关闭socket连接，以及输入输出流
	 * @return 成功返回true，否则返回false
	 */
	protected boolean close()
	{
		try {
			if (out != null) {
				out.close();
				out = null;
			}
			if (in != null) {
				in.close();
				in = null;
			}
			if (socket != null) {
				socket.close();
				socket = null;
			}
			return true;
		} catch (IOException ex) {
			log.error("关闭银行服务器连接发生异常:" + ex.getMessage());
			return false;
		}
	}

	/**
	 * 向目标主机发送一个字符串请求报文，并接受一个指定字符集的字符串回应
	 * @param input 字符串请求报文
	 * @param charset 字符编码
	 * @return 字符串回应报文（发生错误时返回null）
	 */
	public String trade(String input, Charset charset) {
		connect(); // 连接服务器
		log.info("连接服务器状态" + connected);
		if (connected) {
			sendData(input);
			log.info("客户端发送内容:" + input);
			String output = recvData(charset);
			log.info("获得原始的返回包:" + output); // 获得原始返回包
			return output;
		}
		close();
		return null;
	}

	/**
	 * 向目标主机发送一个byte请求数组，并接受一个byte回应数组
	 * @param input byte请求数组
	 * @return byte回应数组
	 */
	public byte[] trade(byte[] input) {
		connect(); // 连接服务器
		log.info("连接服务器状态" + connected);
		if (connected) {
			sendData(input);
			String line = "";
			String hexLine = "";
			for (int i = 0; i < input.length; i++) {
				line += String.format("%c", (char) input[i]);
				int hex = input[i] & 0xff;
				hexLine += String.format("%d ", hex);
			}
			log.info("发送请求: " + line);
			log.debug("HEX: " + hexLine);

			byte[] output = recvData();
			// hexLine = "";
			// for (int i = 0; i < output.length; i++) {
			// line += String.format("%c", (char) output[i]);
			// int hex = output[i] & 0xff;
			// hexLine += String.format("%d ", hex);
			// }
			log.info("收到回应: " + new String(output)); // 获得原始返回包
			// log.debug("HEX: " + hexLine);
			return output;
		}
		close();
		return null;
	}
}
