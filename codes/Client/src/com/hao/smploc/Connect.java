package com.hao.smploc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Connect {

	private static final String HOST = "192.168.1.100";
	private static final int PORT = 9999;
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;

	public Connect() {

		try {
			socket = new Socket(HOST, PORT);
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendMsg(String msg) {
		if (socket.isConnected()) {
			if (!socket.isOutputShutdown()) {
				out.println(msg);
			}
		}
	}

	public String receiveMsg() {
		if (socket.isConnected()) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				return in.readLine();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}