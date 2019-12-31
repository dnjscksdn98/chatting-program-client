package chattingProgram;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.DataOutputStream;
import java.io.IOException;

import org.json.JSONObject;

public class Sender implements Runnable {
	Socket socket = null;
	
	PrintWriter out = null;
	BufferedReader in2 = null;
	
	// 파일 전송을 위한 파일 입출력
	DataOutputStream dos = null;
	
	JSONObject data = null;
	
	public Sender(Socket socket, JSONObject data) {
		try {
			this.socket = socket;
			this.data = data;
			
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			in2 = new BufferedReader(new InputStreamReader(System.in));
			dos = new DataOutputStream(socket.getOutputStream());
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//사용자가 입력한 메시지를 서버로 보내준다
		while(true) {
			try {
				System.out.print(">> 입력 : ");
				// 채팅 메시지 입력
				String msg = in2.readLine();
				// 채팅 메시지에 대한 JSONObject 객체 생성
				JSONObject message = new JSONObject();
				
				// 로그아웃 명령어
				if(msg.equals("/logout")) 
					message.put("command", "logout");
				
				/*
				// 이미지 전송 명령어
				else if(msg.equals("/image")) {
					message.put("command", "image");
					
					String fileName;
					System.out.print("enter file name : ");
					fileName = in2.readLine();
					
					dos.writeUTF(fileName);
					
				}
				*/
				
				// 채팅 명령어
				else {
					message.put("command", "chat");
					message.put("message", msg);
				}
				
				message.put("id", data.getString("id"));
				
				// 입력된 메시지를 서버로 보낸다
				out.println(message.toString());
				out.flush();
				
			}catch(Exception e) {
				
			}
		}
	}
}
