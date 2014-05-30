package com.mingrisoft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;

public class Connect {

	private static final String HOST = "192.168.191.1";
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
			Log.d("client", ex.toString());
		}
	}

	public void sendMsg(String msg) {
		if (socket.isConnected()) {
			if (!socket.isOutputShutdown()) {
				Log.d("client", msg);
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