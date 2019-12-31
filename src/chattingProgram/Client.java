package chattingProgram;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import org.json.JSONObject;

public class Client {
	public static void main(String[] args) {
		Socket socket = null;
		BufferedReader in = null;
		BufferedReader in2 = null;
		PrintWriter out = null;
		
		// ����� ��ɾ�
		String command;
		
		// JSONObject ��ü ����
		JSONObject data = null;
		
		try {
			socket = new Socket("localhost", 54571);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			in2 = new BufferedReader(new InputStreamReader(System.in));
			
		}catch(UnknownHostException e) {
			e.printStackTrace();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		try {
			while(true) {
				// JSONObject ��ü ����
				data = new JSONObject();
				
				// ��ɾ� �Է�
				System.out.println("register/login");
				command = in2.readLine();
				// ��ɾ JSON�� ����
				data.put("command", command);
				
				// ȸ������ ��ɾ�
				if(data.getString("command").equals("register")) {
					// �̸�, ���̵�, ��й�ȣ �Է��Ѵ�
					// �׽�Ʈ
					// data.put("name", "����");
					// data.put("id", "won");
					// data.put("passwd", "123");
					// data.put("name", "����");
					// data.put("id", "jeon");
					// data.put("passwd", "456");
					
					String name;
					String id;
					String passwd;
					String checkpwd;
					
					System.out.print("enter name : ");
					name = in2.readLine();
					System.out.print("enter ID : ");
					id = in2.readLine();
					System.out.print("enter PW : ");
					passwd = in2.readLine();
					System.out.print("enter PW again : ");
					checkpwd = in2.readLine();
					
					if(!passwd.equals(checkpwd)) {
						System.out.println("��й�ȣ�� �߸� �Է��߽��ϴ�.");
						continue;
					}
					
					data.put("name", name);
					data.put("id", id);
					data.put("passwd", passwd);
					
					//������� ȸ�������� ������ �����ش�
					out.println(data.toString());
					out.flush();
					
					//ȸ������ ����� �����κ��� �о�´�
					String res = in.readLine();
					JSONObject result = new JSONObject(res);
					
					// true -> ȸ�����Կ� ����
					if(result.getBoolean("result"))
						System.out.println("ȸ������ �޴�[����] : " + result.getString("message"));
					else
						System.out.println("ȸ������ �޴�[����] : " + result.getString("message"));
				}
				
				// �α��� ��ɾ�
				else if(data.getString("command").equals("login")) {
					// ���̵�� ��й�ȣ�� �Է��Ѵ�
					// �׽�Ʈ
					// data.put("id", "won");
					// data.put("passwd", "123");
					// data.put("id", "jeon");
					// data.put("passwd", "456");
					
					String id;
					String passwd;
					
					System.out.print("enter ID : ");
					id = in2.readLine();
					System.out.print("enter PW : ");
					passwd = in2.readLine();
					
					data.put("id", id);
					data.put("passwd", passwd);
					
					// �Է��� ���̵�� ��й�ȣ�� ������ �����ش�
					out.println(data.toString());
					out.flush();
					
					// �α��� ����� �����κ��� �о�´�
					String res = in.readLine();
					JSONObject result = new JSONObject(res);
	
					// true -> �α��ο� ����
					// �α��ο� ���������� �ݺ����� ���� �����带 �����Ѵ�
					if(result.getBoolean("result")) {
						System.out.println("�α��� �޴�[����] : " + result.getString("message"));
						break;
					}
					else
						System.out.println("�α��� �޴�[����] : " + result.getString("message"));
				}
			}
			
			//����ڰ� �Է��� �޽����� �ٸ� Client�� ������ ������ ���� & ����
			Thread t = new Thread(new Sender(socket, data));
			t.start();
			
		}catch(Exception e) {
			
		}
		// �α��� ���¿��� �����κ��� �޽��� �ޱ�
		while(true) {
			try {
				String res = in.readLine();
				JSONObject result = new JSONObject(res);
				
				// �α׾ƿ� ��ɾ�
				if(result.getString("command").equals("logout")) {
					if(result.getBoolean("result")) {
						System.out.println("�α׾ƿ� �޴�[����] : " + result.getString("message"));
						
						// �α׾ƿ� �Ϸ�� Ŭ���̾�Ʈ �ݱ�
						socket.close();
					}
					else
						System.out.println("�α׾ƿ� �޴�[����] : " + result.getString("message"));
				}
			}catch(Exception e) {
				
			}
		}
	}
}