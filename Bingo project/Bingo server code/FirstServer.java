package bingo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
					textField.setText("please input port number!);
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
			textArea.append("this socket is already using...\n");

		}

	}

	private void Connection() {

		Thread th = new Thread(new Runnable() { 

			public void run() {
				while (true) {
					try {
						textArea.append("User connection waiting...\n");
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

		private String Nickname = "";

		public UserInfo(Socket soc, Vector vc) 
		{
		
			this.user_socket = soc;
			this.user_vc = vc;

			User_network();

		}

		public void User_network() {
			try {
				is = user_socket.getInputStream();
				dis = new DataInputStream(is);
				os = user_socket.getOutputStream();
				dos = new DataOutputStream(os);

				Nickname = dis.readUTF(); 
				textArea.append("connection User ID :" +Nickname+"\n");

				//send_Message("successful complete");

			} catch (Exception e) {
				textArea.append("stream settint error\n");
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
				textArea.append("message send error occur\n");	
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
						textArea.append(vc.size() +" : currunt vector is User Count\n");
						textArea.append("User disconnect value return\n");
						break;

					} catch (Exception ee) {

					}
				}

			}



		}

	} 
}
