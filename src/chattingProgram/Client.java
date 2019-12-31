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
		
		// 사용자 명령어
		String command;
		
		// JSONObject 객체 선언
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
				// JSONObject 객체 생성
				data = new JSONObject();
				
				// 명령어 입력
				System.out.println("register/login");
				command = in2.readLine();
				// 명령어를 JSON에 삽입
				data.put("command", command);
				
				// 회원가입 명령어
				if(data.getString("command").equals("register")) {
					// 이름, 아이디, 비밀번호 입력한다
					// 테스트
					// data.put("name", "찬우");
					// data.put("id", "won");
					// data.put("passwd", "123");
					// data.put("name", "진완");
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
						System.out.println("비밀번호를 잘못 입력했습니다.");
						continue;
					}
					
					data.put("name", name);
					data.put("id", id);
					data.put("passwd", passwd);
					
					//사용자의 회원정보를 서버로 보내준다
					out.println(data.toString());
					out.flush();
					
					//회원가입 결과를 서버로부터 읽어온다
					String res = in.readLine();
					JSONObject result = new JSONObject(res);
					
					// true -> 회원가입에 성공
					if(result.getBoolean("result"))
						System.out.println("회원가입 메뉴[성공] : " + result.getString("message"));
					else
						System.out.println("회원가입 메뉴[실패] : " + result.getString("message"));
				}
				
				// 로그인 명령어
				else if(data.getString("command").equals("login")) {
					// 아이디와 비밀번호를 입력한다
					// 테스트
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
					
					// 입력한 아이디와 비밀번호를 서버로 보내준다
					out.println(data.toString());
					out.flush();
					
					// 로그인 결과를 서버로부터 읽어온다
					String res = in.readLine();
					JSONObject result = new JSONObject(res);
	
					// true -> 로그인에 성공
					// 로그인에 성공했으면 반복문을 나가 스레드를 실행한다
					if(result.getBoolean("result")) {
						System.out.println("로그인 메뉴[성공] : " + result.getString("message"));
						break;
					}
					else
						System.out.println("로그인 메뉴[실패] : " + result.getString("message"));
				}
			}
			
			//사용자가 입력한 메시지를 다른 Client로 보내는 스레드 생성 & 실행
			Thread t = new Thread(new Sender(socket, data));
			t.start();
			
		}catch(Exception e) {
			
		}
		// 로그인 상태에서 서버로부터 메시지 받기
		while(true) {
			try {
				String res = in.readLine();
				JSONObject result = new JSONObject(res);
				
				// 로그아웃 명령어
				if(result.getString("command").equals("logout")) {
					if(result.getBoolean("result")) {
						System.out.println("로그아웃 메뉴[성공] : " + result.getString("message"));
						
						// 로그아웃 완료시 클라이언트 닫기
						socket.close();
					}
					else
						System.out.println("로그아웃 메뉴[실패] : " + result.getString("message"));
				}
			}catch(Exception e) {
				
			}
		}
	}
}