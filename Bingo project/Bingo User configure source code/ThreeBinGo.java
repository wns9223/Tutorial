package bingogame;

import java.awt.Color;
import java.awt.Graphics;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class DialoguePanel extends JPanel implements ActionListener {
	private int size = 1;
	private boolean[][] bArr = new boolean[size][size];
	private JTextField[] btnArr = new JTextField[size * size];
	
	public DialoguePanel() {
		// TODO Auto-generated constructor stub

		for (int i = 0; i < size * size; i++) {
			btnArr[i] = new JTextField("");
			this.add(btnArr[i]);

		}
		this.setLayout(new GridLayout(size, size));

	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}

public class ThreeBinGo extends JFrame {

	private JPanel contentPane;
	private JTextField textField; 

	private String id;
	private String ip;
	private int port;

	JButton sendBtn; 
	JTextArea textArea;

	private Socket socket; 
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	BingoAreaPanel bingoarea;
	
	

	public ThreeBinGo(String id, String ip, int port)
	{
		this.id = id;
		this.ip = ip;
		this.port = port;

		init();
		start();

		textArea.append("parameter come value : " + id + " " + ip + " " + port + "\n");

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
			textArea.append("socket connect error!!\n");
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


		Thread th = new Thread(new Runnable() 
		{ 

			@Override
			public void run() {
				String msg = null;
				while (true) {

					try {
						
						msg = dis.readUTF();
						textArea.append(msg + "\n");
						
						if(Integer.parseInt(msg)==1005) {
							bingoarea.loser();
							os.close();
							is.close();
							dos.close();
							dis.close();
							socket.close();
						}
						else
							bingoarea.check((String)msg);
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

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("BinGo");


		
		setBounds(50, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(100, 600, 732, 120);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setForeground(Color.black);
		scrollPane.setViewportView(textArea);

		textField = new JTextField();
		textField.setBounds(100, 720, 650, 30);
		contentPane.add(textField);
		textField.setColumns(10);

		sendBtn = new JButton("Send");
		sendBtn.setBounds(750, 720, 80, 30);
		contentPane.add(sendBtn);

		
		bingoarea =  new BingoAreaPanel() ;
		bingoarea.setBounds(100, 0, 732, 600);
		contentPane.add(bingoarea);


		textArea.setEditable(false);
		
		setVisible(true);

	}

	public void start() { 

		sendBtn.addActionListener(new Myaction()); 


	}


	class BingoAreaPanel extends JPanel implements ActionListener {
		private ImageIcon im; 
		private int size = 3;
		private boolean[][] bArr = new boolean[size][size];
		private JButton[] btnArr = new JButton[size * size];
		private int num[]=new int[size*size];
		private int bingoCnt;
		private int ch2,ch3;
		private int win=0;
		

		public int loser(){
			if(win==0){
			for(int x=0;x<size*size;x++)
				disableBtn(x);
			JOptionPane.showMessageDialog(null, "You Fail!!", "fail",JOptionPane.DEFAULT_OPTION);
			}
			return 0;			
		}

		public int check(String msg) {
			ch3=Integer.parseInt(msg);
			
			System.out.println(ch3+"get");
			for(int i=0;i<size*size;i++) {
				ch2=Integer.parseInt(btnArr[i].getText());
				if(ch3==ch2) {
					System.out.println(ch3+"eraser");
					bArr[i / size][i % size] = true;
					print();
					checkBingo();
					disableBtn(i);
					
					break;
				}
			}
			return 0;
		}

		public BingoAreaPanel() {

			for (int i = 0; i < size * size; i++) {
				btnArr[i] = new JButton(""+creatRand(i));
				this.add(btnArr[i]);
				btnArr[i].addActionListener(this);
			}
			this.setLayout(new GridLayout(size, size));

		}
		public void print() 
		{
			for (int i = 0; i < bArr.length; i++)
			{
				for (int j = 0; j < bArr[0].length; j++)
				{
					System.out.print(bArr[i][j] ? "O" : "X");
				}
				System.out.println();
			}
		}
		
		int checkBingo()
		
		{
			bingoCnt = 0;

			for (int i = 0; i < size; i++)
			{
				int wcount = 0;
				int hcount = 0;
				for (int j = 0; j < size; j++)
				{
					if (bArr[i][j] == true)
					{
						wcount++;
					}
					if (bArr[j][i] == true)
					{
						hcount++;
					}
				}
				if (wcount == 3)
				{
					count();
				}
				if (hcount == 3)
				{
					count();
				}
			}
			int crossCnt = 0;
			int rcrossCnt = 0;
			for (int i = 0; i < size; i++)
			{
				if (bArr[i][i] == true)
				{
					crossCnt++;
				}
				if (bArr[i][2 - i] == true)
				{
					rcrossCnt++;
				}
			}
			if (crossCnt == 3)
			{
				count();
			}
			if (rcrossCnt == 5)
			{
				count();
			}
			


			
			if (bingoCnt >= 3)
			{
				System.out.println("Bingo Complete");
				win+=1;
				send_Message(1005+"");
			
				for(int i=0;i<size*size;i++) 
					disableBtn(i);
				
				JOptionPane.showMessageDialog(null, "You Win !!", "win",JOptionPane.DEFAULT_OPTION);
				

			}

			return 0;
		}

		public void count() {
			bingoCnt++;

		}



		public int creatRand(int x) {
			num[x]=(int)(Math.random()*size*size)+1;
			if(x==0)
				return num[x];
			else {
				for(int i=0;i<x;i++){
					if(num[i]==num[x]) {
						num[x]=(int)(Math.random()*(size*size))+1;
						i=-1;					
					}
				}
				return num[x];
			}
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
		}

		public void actionPerformed(ActionEvent e) { 
			// TODO Auto-generated method stub
			System.out.println(e.getActionCommand()); 
			im=new ImageIcon("binimg.png");
			for(int i=0;i<size*size;i++) {
				if(e.getSource()==btnArr[i]) {
					System.out.println(btnArr[i].getText()+" click");
					btnArr[i].setBackground(Color.YELLOW);
					
					send_Message((String)btnArr[i].getText());
					disableBtn(i);
				}

			}
			for (int i = 0; i < btnArr.length; i++)
			{
				if (btnArr[i] == e.getSource())
				{
					bArr[i / size][i % size] = true;
					print();
					break;
				}
			}
			checkBingo();
		}
		public void disableBtn(int i) { 
			btnArr[i].setEnabled(false);
		}
		public void ableBtn(int i) {
			btnArr[i].setEnabled(true);
		}


	}


	class Myaction implements ActionListener 
	{

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == sendBtn) 
			{

				send_Message(textField.getText());
				textField.setText(""); 
				textField.requestFocus();

			}


		}

	}

}

