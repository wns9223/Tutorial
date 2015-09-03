

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class FirstServer extends JFrame{
	private JPanel contentPane;
	private JTextField textField; 
	private JButton Start; 
	JTextArea textArea; 

	private ServerSocket socket; 
	private Socket soc; 
	private int Port; 
	private int type;
	private Vector vc = new Vector();



	public static void main(String[] args)
	{				
		FirstServer frame = new FirstServer();

		frame.setVisible(true);
	}

	public void type(int ty){
		this.type=ty; 
	}
	public FirstServer() {
		init();

	}

	private void init() { 

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 280, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		JScrollPane js = new JScrollPane();

		textArea = new JTextArea();
		textArea.setColumns(20);
		textArea.setRows(5);
		js.setBounds(0, 0, 264, 254);
		contentPane.add(js);
		js.setViewportView(textArea);

		textField = new JTextField();
		textField.setBounds(98, 264, 154, 37);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(12, 264, 98, 37);
		contentPane.add(lblNewLabel);

		Start = new JButton("server start");

		Start.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent e) {

				if (textField.getText().equals("") || textField.getText().length()==0)
				{
					textField.setText("please input port number");
					textField.requestFocus();
				} 

				else 
				{
					try
					{
						Port = Integer.parseInt(textField.getText()); 

						server_start(); 



					}
					catch(Exception er)
					{
						textField.setText("please input number");
						textField.requestFocus(); 
					}



				}

			}
		}); 

		Start.setBounds(0, 325, 264, 37);
		contentPane.add(Start);


		textArea.setEditable(false);

	}

	private void server_start() {



		try {
			socket = new ServerSocket(Port); 
			Start.setText("server starting");
			Start.setEnabled(false); 
			textField.setEnabled(false); 

			if(socket!=null) 
			{
				Connection();
			}

		} catch (IOException e) {
			textArea.append("socket is alread using...\n");

		}

	}

	private void Connection() {

		Thread th = new Thread(new Runnable() { 

			public void run() {
				while (true) { 
					try {
						textArea.append("User connection wating...\n");
						soc = socket.accept(); 
						textArea.append("User connect!!\n");

						UserInfo user = new UserInfo(soc, vc); 
						vc.add(user); 
 
						user.start(); 

					} catch (IOException e) {
						textArea.append("!!!! accept error occur... !!!!\n");
					} 

				}

			}
		});

		th.start();

	}

	class UserInfo extends Thread {

		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket user_socket;
		private Vector user_vc;
		private DataBase database;
		private String msg = null;
		private String data = null;
		private PreparedStatement stmt;
		private Connection con;
		private String like;
		
		public UserInfo(Socket soc, Vector vc) 
		{
			this.user_socket = soc;
			this.user_vc = vc;
			
			try {
				is = user_socket.getInputStream();
				dis = new DataInputStream(is);
				os = user_socket.getOutputStream();
				dos = new DataOutputStream(os);
				System.out.println("---------------");
				con = database.makeConnection();
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			User_network();

		}

		public void User_network() {
			
			try {
				

				while ((msg = dis.readUTF()) != null) {
					String data = msg;

					try {
						
						stmt = con.prepareStatement(data);
						System.out.println(" data : " +data);

						stmt.executeUpdate("use test");
						
						String value = data.substring(0, 1);
						String value1 = data.substring(7, 8);
						String value2 = data.substring(11, 12);
						String value3 = data.substring(13, 14);
						
						System.out.println(data.length());
						if(data.length()>=50){
							like = data.substring(41,45);
						}
							

						
						System.out.println("value : "+value);
						System.out.println("value1 : "+value1);
						System.out.println("like : "+like);
						
					
						if (value.compareTo("s") == 0 && value3.compareTo("n")== 0 && like.compareTo("like")==0) {

						
							System.out.println("serarchView snack count return part");
							ResultSet rs = stmt.executeQuery(data);
							while (rs.next()) {
								System.out.println("Data command : " + data);
								String id = rs.getString("count(name)");
								System.out.println(id);
								
								System.out.println("-----------------------------");

								
								if(id.compareTo("0")==0){
									System.out.println("values : null");
									dos.writeUTF("0");
								}
								
								
								else if(Integer.parseInt(id)>0){
									System.out.println(id);
									dos.writeUTF(id);
								}
								
							}
						}
						
						
						
						if (value.compareTo("s") == 0 && value1.compareTo("c") == 0 && value3.compareTo("t")==0) {

				
							System.out.println("notice table count return part");
							ResultSet rs = stmt.executeQuery(data);
							while (rs.next()) {
								System.out.println("Data command : " + data);
								String id = rs.getString("count(title)");
								System.out.println(id);
								
								System.out.println("-----------------------------");
								
								
								if(id.compareTo("0")==0){ 
									System.out.println("values : null");
									dos.writeUTF("0");
								}
								
							
								else if(Integer.parseInt(id)>0){
									System.out.println(id);
									dos.writeUTF(id);
								}
								
							}
						}
						if (value.compareTo("s") == 0 && value1.compareTo("d") == 0) {

							System.out.println("url return part");
							ResultSet rs = stmt.executeQuery(data);
							while (rs.next()) {
								System.out.println("Data command : " + data);
								String url = rs.getString("url");
								String name = rs.getString("name");
								String company = rs.getString("company");
								String price = rs.getString("price");
								System.out.println(url);
								System.out.println(name);
								System.out.println(company);
								System.out.println(price);
								dos.writeUTF(url);
								dos.writeUTF(name);
								dos.writeUTF(company);
								dos.writeUTF(price);

							}
						}
						
						if (value.compareTo("s") == 0 && value1.compareTo("s") == 0) {
							System.out.println("Site detail Info get part");
							System.out.println(data.substring(0, 1));
							System.out.println(data.substring(7, 8));
							ResultSet rs = stmt.executeQuery(data);
							while (rs.next()) {
								System.out.println("Data command : " + data);
								String siteUrl = rs.getString("siteUrl");
								String TableValue = rs.getString("tableValue");
								
								System.out.println(siteUrl);
								System.out.println(TableValue);
							
								dos.writeUTF(siteUrl);
								dos.writeUTF(TableValue);
							
								
							}
						}
						
						//company or all count print
						if (value.compareTo("s") == 0 && value1.compareTo("c") == 0 && value3.compareTo("c")==0) {

							System.out.println("company or all count print");
							ResultSet rs = stmt.executeQuery(data);
							while (rs.next()) {
								System.out.println("Data command : " + data);
								String id = rs.getString("count(company)");
								System.out.println(id);
								
								System.out.println("-----------------------------");
								
								if(id.compareTo("0")==0){
									System.out.println("values : null");
									dos.writeUTF("0");
								}
								
								else if(Integer.parseInt(id)>0){
									System.out.println(id);
									dos.writeUTF(id);
								}
								
							}
						}
						
						
						if (value.compareTo("s") == 0 && value1.compareTo("t") == 0) {

							System.out.println("notice info part");
							ResultSet rs = stmt.executeQuery(data);
							while (rs.next()) {
								System.out.println("Data command : " + data);
								String title = rs.getString("title");
								String content = rs.getString("content");
								String date = rs.getString("created");
								System.out.println(title);
								System.out.println(content);
								System.out.println(date);
								dos.writeUTF(title);
								dos.writeUTF(content);
								dos.writeUTF(date);

							}
						}
						
						if (value.compareTo("s") == 0 && value1.compareTo("n") == 0) {

							System.out.println("snack info return part");
							ResultSet rs = stmt.executeQuery(data);
							
							while (rs.next()) {
								
								System.out.println("Data command : " + data);
								String name = rs.getString("name");
								String company = rs.getString("company");
								String price = rs.getString("price");
								String addee = rs.getString("addee");
								String nutritute = rs.getString("nutritute");
								String additivies = rs.getString("additivies");
								
								System.out.println(name);
								System.out.println(company);
								System.out.println(price);
								System.out.println(addee);
								System.out.println(nutritute);
								System.out.println(additivies);
								
								dos.writeUTF(name);
								dos.writeUTF(company);
								dos.writeUTF(price);
								dos.writeUTF(addee);
								dos.writeUTF(nutritute);
								dos.writeUTF(additivies);

							}
						}
						
						if (value.compareTo("s") == 0 && value1.compareTo("m") == 0 && value2.compareTo("c")==0) {
							System.out.println("notice count return part");
							ResultSet rs = stmt.executeQuery(data);
							while (rs.next()) {
								System.out.println("Data command : " + data);
								String code = rs.getString("max(code)")
										;
								String name = rs.getString("name");
								String password = rs.getString("password");
								System.out.println("---------------");
								System.out.println(code);
								System.out.println(name);
								System.out.println(password);
								
								System.out.println("---------------");
								
								
								
								if(code==null){	
									System.out.println(code);
									System.out.println(name);
									System.out.println(password);
									dos.writeUTF("0");
									dos.writeUTF("0");
									dos.writeUTF("0");
								}
								
								if(Integer.parseInt(code)>0 ){
									
									System.out.println(code);
									System.out.println(name);
									System.out.println(password);
									dos.writeUTF("1");		
									dos.writeUTF(name);
									dos.writeUTF(password);
									dos.writeUTF("connection complete");

								}
								
							}
						}
						else if(value.compareTo("i")==0){
							System.out.println("insert part");
							System.out.println(data.substring(0, 1));
							System.out.println("Insert Data : "+ data);
							stmt.executeUpdate(data);
							dos.writeUTF("Insert complete");
							
						}else if(value.compareTo("d")==0){
							System.out.println("delete part");
							System.out.println(data.substring(0, 1));
							System.out.println("Insert Data : "+ data);
							stmt.executeUpdate(data);
							dos.writeUTF("Delete complete");
						}
						System.out.println("Request complete");

					} catch (SQLException e) {
						// TODO: handle exception
						System.out.println("Error");
					}

					
				
				}

				

			} catch (Exception e) {
				textArea.append("Stream settint error\n");
			}

		}

		public void InMessage(String str) {
			textArea.append("Get message from User : " + str+"\n");
			broad_cast(str);

		}

		public void broad_cast(String str) {
			for (int i = 0; i < user_vc.size(); i++) {
				UserInfo imsi = (UserInfo) user_vc.elementAt(i);
				imsi.send_Message(str);

			}

		}

		public void send_Message(String str) {
			try {
				dos.writeUTF(str);
			} 
			catch (IOException e) {
				textArea.append("message send error occur메시지 송신 에러 발생\n");	
			}
		}

		public void run() 
		{

			while (true) {
				try {

					
					String msg = dis.readUTF();
					InMessage(msg);

				} 
				catch (IOException e) 
				{

					try {
						dos.close();
						dis.close();
						user_socket.close();
						vc.removeElement( this );
						textArea.append(vc.size() +" : current vector User count\n");
						textArea.append("User disconnect value return\n");
						break;

					} catch (Exception ee) {

					}
				}

			}



		}

	}
}
