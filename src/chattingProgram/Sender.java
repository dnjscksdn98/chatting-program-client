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
	
	// ���� ������ ���� ���� �����
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
		//����ڰ� �Է��� �޽����� ������ �����ش�
		while(true) {
			try {
				System.out.print(">> �Է� : ");
				// ä�� �޽��� �Է�
				String msg = in2.readLine();
				// ä�� �޽����� ���� JSONObject ��ü ����
				JSONObject message = new JSONObject();
				
				// �α׾ƿ� ��ɾ�
				if(msg.equals("/logout")) 
					message.put("command", "logout");
				
				/*
				// �̹��� ���� ��ɾ�
				else if(msg.equals("/image")) {
					message.put("command", "image");
					
					String fileName;
					System.out.print("enter file name : ");
					fileName = in2.readLine();
					
					dos.writeUTF(fileName);
					
				}
				*/
				
				// ä�� ��ɾ�
				else {
					message.put("command", "chat");
					message.put("message", msg);
				}
				
				message.put("id", data.getString("id"));
				
				// �Էµ� �޽����� ������ ������
				out.println(message.toString());
				out.flush();
				
			}catch(Exception e) {
				
			}
		}
	}
}
