package bingogame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DFrame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField textField;

	private String id;
	private String ip;
	private int port;

	JButton sendBtn; 
	JTextArea textArea;

	private JButton three, five;
	private JPanel leftpanel, rightpanel;
	private String UserName;

	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	public DFrame(String id, String ip, int port)
	{
		this.id = id;
		this.ip = ip;
		this.port = port;

		init();

		network();

	}

	public void network() {

		try {
			socket = new Socket(ip, port);
			if (socket != null) 
			{
				Connection();
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			textArea.append("socket connect errot!!\n");
		}

	}

	public void Connection() { 

		try { 
			is = socket.getInputStream();
			dis = new DataInputStream(is);

			os = socket.getOutputStream();
			dos = new DataOutputStream(os);

		} catch (IOException e) {
			textArea.append("stream setting error!!\n");
		}

		send_Message(id); 

		Thread th = new Thread(new Runnable() { 

					public void run() {
						while (true) {

							try {
								String msg = dis.readUTF(); 

							} catch (IOException e) {
								textArea.append("message get error!!\n");
								
								try {
									os.close();
									is.close();
									dos.close();
									dis.close();
									socket.close();
									break; 
								} catch (IOException e1) {

								}

							}
						} 

					}
				});

		th.start();

	}

	public void send_Message(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) {
			textArea.append("message send error!!\n");
		}
	}

	public void init() { 

		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(UserName + "  {BinGo Start} ");

		this.UserName = UserName;

		setLayout(new GridLayout(1, 1));

		leftpanel = new JPanel();
		rightpanel = new JPanel();

		three = new JButton("Three Start");
		three.addActionListener(this);
		leftpanel.add(three);

		five = new JButton("Five Start");
		five.addActionListener(this);
		rightpanel.add(five);

		this.add(leftpanel);
		this.add(rightpanel);
		setVisible(true);

		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == three) {
			ThreeBinGo threebingo = new ThreeBinGo(id,ip,port);
			setVisible(false);
		}
		if (e.getSource() == five) {
			GameGo gameGo = new GameGo(id,ip,port);
			setVisible(false);
		}

	}
}

