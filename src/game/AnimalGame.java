package game;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;


@SuppressWarnings("serial")
public class AnimalGame extends Frame implements WindowListener, MouseListener {

	/**
	 * @param args
	 */
	private static int chesspiece[][]=new int[9][7];  //�洢�����ϸ���λ�õ�����
	private Map<Integer,String> map=new HashMap<Integer ,String>();  //���������ֵ�hash��Ӧ
	private static List<Integer> Next=new ArrayList<Integer>();  //�洢��ǰ���ӿɵִ��λ��
	private static int Selected=-1;  //��ʾ��ǰѡ�������
	private static int turn=1; //��ʾ��ǰ��һ������
	public AnimalGame(){
		setTitle("��궷����-��ɫ");  //����frame����
		setBounds(460, 300, 275, 355);  //����λ��
		setLayout(null);
		addWindowListener(this);  //��Ӵ��ڼ���
		addMouseListener(this);//���������
		inital();  //��ʼ��
		repaint();
		setResizable(false);//���ɱ��
		setVisible(true);  //���ô��ڿ���ʾ
	}
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.black);
		for(int i=0;i<8;i++)
			g.drawLine(30+30*i, 60, 30+30*i, 330);
		for(int i=0;i<10;i++)
			g.drawLine(30, 60+30*i,240, 60+30*i);  //�����������񣨺�ɫ��
		
		g.drawString("����",95,80);
		g.drawString("����",155,80);
		g.drawString("����",125,110);
		g.drawString("��Ѩ",125,80);
		
		g.setColor(Color.red);
		g.drawString("����",95,320);
		g.drawString("����",155,320);
		g.drawString("����",125,290);
		g.drawString("��Ѩ",125,320);  //������Ѩ�Լ�����
		
		for (int i=0;i<9;i++)
			for(int j=0;j<7;j++)
			{
				if(chesspiece[i][j]==0)
					continue;
				if(chesspiece[i][j]>0)
					g.setColor(Color.black);
				else
					g.setColor(Color.red);
				g.drawString(map.get(Math.abs(chesspiece[i][j])), 40+30*j, 80+30*i);  //�������ӣ�0��ʾ�հף�����0��ʾ��ɫ���ӣ�С��0��ʾ��ɫ���ӣ�
				g.setColor(Color.black);
				g.drawOval(35+30*j, 65+30*i, 20, 20);
			}
		g.setColor(Color.green);  //������һ����ѡ���λ�ã���ɫ��
		for(int i=0;i<Next.size();i++)
			g.drawRect(33+30*(Next.get(i)%7), 63+30*(Next.get(i)/7),24,24);
		if(Selected>=0)
		{
			g.setColor(Color.blue);  //������ǰѡ������ӣ���ɫ��
			g.drawRect(33+30*(Selected%7), 63+30*(Selected/7),24,24);
		}
	}
	public void inital(){  //��ʼ��
		turn=1;     //ִ������
		for(int i=0;i<9;i++)
			for(int j=0;j<7;j++)
				chesspiece[i][j]=0;  //��ʼ����������Ϊ��
		chesspiece[0][0]=1;	
		chesspiece[0][6]=2;
		chesspiece[1][1]=3;
		chesspiece[1][5]=4;
		chesspiece[2][0]=5;
		chesspiece[2][2]=6;
		chesspiece[2][4]=7;
		chesspiece[2][6]=8;  //��ɫ����
		
		chesspiece[8][6]=-1;	
		chesspiece[8][0]=-2;
		chesspiece[7][5]=-3;
		chesspiece[7][1]=-4;
		chesspiece[6][6]=-5;
		chesspiece[6][4]=-6;
		chesspiece[6][2]=-7;
		chesspiece[6][0]=-8;  //��ɫ����
		
		map.put(1, "ʨ");
		map.put(2, "��");
		map.put(3, "��");
		map.put(4, "��");
		map.put(5, "��");
		map.put(6, "��");
		map.put(7, "��");
		map.put(8, "��");  //����hash��Ӧ��
	}
	public void mouseClicked(MouseEvent e) {  //������¼�
		int x,y;
		x=e.getX();
		y=e.getY();  //��ȡ�������
		if(x<30||y<60||x>240||y>330)  //������ĵ��������
			return ;
		x=(x-30)/30;
		y=(y-60)/30;  //������chesspiece��ά�����е�����
		if(chesspiece[y][x]*turn>0){//ѡ�е�ǰ��ɫ��������Ϊ��һ��Ҫ�ƶ�������
			if((turn>0&&(7*y+x==60||7*y+x==58||7*y+x==52))||(turn<0&&(7*y+x==2||7*y+x==4||7*y+x==10)))
				return;   //��ǰ���Ӵ��ڶԷ�������ʧȥս�����򵱿հ״��������ƶ�
			Selected=7*y+x; //��ά����ת��Ϊһά���귽��洢
			Next.clear();  //�����һ����ѡ����ƶ�λ����ʷ��¼
			GetNext();  //������һ����ѡ����ƶ�λ��
			repaint();  //�ػ�
			return;
		}
		if(Selected>=0){  //�Ѿ�ѡ����Ҫ�ƶ�������������ƶ�����
			if(Next.contains(7*y+x)){  //�Ƿ��ǿ��ƶ���λ��
				Move(7*y+x); //ִ���ƶ�
				Selected=-1; //�޸ĵ�ǰѡ��Ϊ��
				Next.clear();  //�����һ����ѡ����ƶ�λ����ʷ��¼
				repaint();//�ػ�
				if(chesspiece[0][3]<0||GetBlack()==0){//�췽���ӽ���ڷ���Ѩȡ��ʤ�����ߺڷ��޿��ƶ�����
					JOptionPane.showMessageDialog(null, "��ɫ��ȡ��ʤ��");
					inital();//��ʼ�����¿�ʼ
					repaint();
					return;
				}
				else if(chesspiece[8][3]>0||GetRed()==0){//�ڷ����ӽ���췽��Ѩȡ��ʤ�����ߺ췽�޿��ƶ�����
					JOptionPane.showMessageDialog(null, "��ɫ��ȡ��ʤ��");
					inital();
					repaint();
					return;
				}
				if(turn >0){//�޸ĵ�ǰ�ƶ�����Ӫ���ڱ�����ʾ
					turn=-1;  
					setTitle("��궷����-��ɫ");
				}
				else{
					turn=1;
					setTitle("��궷����-��ɫ");
				}
			}
		}
		
	}
	public static int GetBlack(){
		int sum=0;
		for(int i=0;i<9;i++)
			for(int j=0;j<7;j++)
				if(chesspiece[i][j]>0&&!(7*i+j==60||7*i+j==58||7*i+j==52))
					sum+=1;//��ɫ���Ӳ��Ҳ��ڶԷ����������1
		return sum;
	}
	public static int GetRed(){
		int sum=0;
		for(int i=0;i<9;i++)
			for(int j=0;j<7;j++)
				if(chesspiece[i][j]<0&&!(7*i+j==10||7*i+j==2||7*i+j==4))
					sum+=1;//��ɫ���Ӳ��Ҳ��ڶԷ����������1
		return sum;
	}
	public static void GetNext(){
		int y=Selected/7;
		int x=Selected%7;
		for(int i=x-1;i<=x+1;i++)  //����������Ϊ���ĵ�һ���Ź������������ƶ�λ��
			for(int j=y-1;j<=y+1;j++)
				if((i==x&&j==y)||i<0||j<0||i>6||j>8) //���ӵ�ǰλ���Լ�������λ���޳�
					continue;
				else if(chesspiece[j][i]==0)  //�հ�λ�ÿ��ƶ�
					Next.add(7*j+i);
				else if(turn==1&&chesspiece[j][i]<0&&(7*j+i==2||7*j+i==4||7*j+i==10))
					Next.add(7*j+i);   //�Է������ڼ��������ͨ���ƶ��Ե������ƶ�
				else if(turn==-1&&chesspiece[j][i]>0&&(7*j+i==60||7*j+i==58||7*j+i==52))
					Next.add(7*j+i);
		switch(Math.abs(chesspiece[y][x])){  //��ѡ������ӷ������ۿ��ԳԵ����
		case 1:  //ʨ��
			if(x-2>=0&&chesspiece[y][x-2]*turn<0&&chesspiece[y][x-1]==0)
				Next.add(7*y+x-2);
			if(y-2>=0&&chesspiece[y-2][x]*turn<0&&chesspiece[y-1][x]==0)
				Next.add(7*(y-2)+x);
			if(x+2<7&&chesspiece[y][x+2]*turn<0&&chesspiece[y][x+1]==0)
				Next.add(7*y+x+2);
			if(y+2<9&&chesspiece[y+2][x]*turn<0&&chesspiece[y+1][x]==0)
				Next.add(7*(y+2)+x);
			break;
		case 2:  //�ϻ�
			if(x-2>=0&&y-2>=0&&chesspiece[y-2][x-2]*turn<0&&chesspiece[y-1][x-1]==0)
				Next.add(7*(y-2)+x-2);
			if(x-2>=0&&y+2<9&&chesspiece[y+2][x-2]*turn<0&&chesspiece[y+1][x-1]==0)
				Next.add(7*(y+2)+x-2);
			if(x+2<7&&y-2>=0&&chesspiece[y-2][x+2]*turn<0&&chesspiece[y-1][x+1]==0)
				Next.add(7*(y-2)+x+2);
			if(x+2<7&&y+2<9&&chesspiece[y+2][x+2]*turn<0&&chesspiece[y+1][x+1]==0)
				Next.add(7*(y+2)+x+2);
			break;
		case 3:  //��
			if(x-1>=0&&y-1>=0&&chesspiece[y-1][x-1]*turn<0){
				if(y-2>=0&&chesspiece[y-1][x]==0)
					Next.add(7*(y-2)+x);
				if(x-2>=0&&chesspiece[y][x-1]==0)
					Next.add(7*y+x-2);
			}
			if(x-1>=0&&y+1<9&&chesspiece[y+1][x-1]*turn<0){
				if(y+2<9&&chesspiece[y+1][x]==0)
					Next.add(7*(y+2)+x);
				if(x-2>=0&&chesspiece[y][x-1]==0)
					Next.add(7*y+x-2);
			}
			if(x+1<7&&y-1>=0&&chesspiece[y-1][x+1]*turn<0){
				if(y-2>=0&&chesspiece[y-1][x]==0)
					Next.add(7*(y-2)+x);
				if(x+2<7&&chesspiece[y][x+1]==0)
					Next.add(7*y+x+2);
			}
			if(x+1<7&&y+1<9&&chesspiece[y+1][x+1]*turn<0){
				if(y+2<9&&chesspiece[y+1][x]==0)
					Next.add(7*(y+2)+x);
				if(x+2<7&&chesspiece[y][x+1]==0)
					Next.add(7*y+x+2);
			}
			break;
		case 4:  //��
			if(x-1>=0&&chesspiece[y][x-1]*turn<0)
				Next.add(7*y+x-1);
			if(y+1<9&&chesspiece[y+1][x]*turn<0)
				Next.add(7*(y+1)+x);
			if(y-1>=0&&chesspiece[y-1][x]*turn<0)
				Next.add(7*(y-1)+x);
			if(x+1<7&&chesspiece[y][x+1]*turn<0)
				Next.add(7*y+x+1);
			break;
		case 5: //��
			if(x-1>=0&&chesspiece[y][x-1]*turn<0){
				if(y-1>=0)
					Next.add(7*(y-1)+x-1);
				if(y+1<9)
					Next.add(7*(y+1)+x-1);
			}
			if(y+1<9&&chesspiece[y+1][x]*turn<0){
				if(x-1>=0)
					Next.add(7*(y+1)+x-1);
				if(x+1<7)
					Next.add(7*(y+1)+x+1);
			}
			if(y-1>=0&&chesspiece[y-1][x]*turn<0){
				if(x+1<7)
					Next.add(7*(y-1)+x+1);
				if(x-1>=0)
					Next.add(7*(y-1)+x-1);
			}
			if(x+1<7&&chesspiece[y][x+1]*turn<0){
				if(y-2>=0)
					Next.add(7*(y-1)+x+1);
				if(y+1<9)
					Next.add(7*(y+1)+x+1);
			}
			break;
		case 6: //����
			if(x-2>=0&&chesspiece[y][x-1]*turn<0)
				Next.add(7*y+x-2);
			if(y+2<9&&chesspiece[y+1][x]*turn<0)
				Next.add(7*(y+2)+x);
			if(y-2>=0&&chesspiece[y-1][x]*turn<0)
				Next.add(7*(y-2)+x);
			if(x+2<7&&chesspiece[y][x+1]*turn<0)
				Next.add(7*y+x+2);
			break;
		case 7:  //��
			if(x-2>=0&&y-2>=0&&chesspiece[y-1][x-1]*turn<0)
				Next.add(7*(y-2)+x-2);
			if(x-2>=0&&y+2<9&&chesspiece[y+1][x-1]*turn<0)
				Next.add(7*(y+2)+x-2);
			if(x+2<7&&y-2>=0&&chesspiece[y-1][x+1]*turn<0)
				Next.add(7*(y-2)+x+2);
			if(x+2<7&&y+2<9&&chesspiece[y+1][x+1]*turn<0)
				Next.add(7*(y+2)+x+2);
			break;
		case 8:  //����
			if(x-1>=0&&y-1>=0&&chesspiece[y-1][x-1]*turn<0)
				Next.add(7*(y-1)+x-1);
			if(x-1>=0&&y+1<9&&chesspiece[y+1][x-1]*turn<0)
				Next.add(7*(y+1)+x-1);
			if(x+1<7&&y-1>=0&&chesspiece[y-1][x+1]*turn<0)
				Next.add(7*(y-1)+x+1);
			if(x+1<7&&y+1<9&&chesspiece[y+1][x+1]*turn<0)
				Next.add(7*(y+1)+x+1);
			break;
		}
	}
	public static void Move(int next){  //�ƶ�����
		int y=Selected/7;
		int x=Selected%7;
		
		switch(Math.abs(chesspiece[y][x])){  //���ж��ǲ�������Է������Ե����������Լ�����㲻һ�£����ӣ������ǣ��ߣ�
		case 3:  //��
			if(next-Selected==14){  //�Ե��Է�������
				if(x-1>=0)
					chesspiece[y+1][x-1]=0;
				if(x+1<7)
					chesspiece[y+1][x+1]=0;
			}
			if(Selected-next==14){
				if(x-1>=0)
					chesspiece[y-1][x-1]=0;
				if(x+1<7)
					chesspiece[y-1][x+1]=0;
			}
			if(Selected-next==2){
				if(y-1>=0)
					chesspiece[y-1][x-1]=0;
				if(y+1<9)
					chesspiece[y+1][x-1]=0;
			}
			if(next-Selected==2){
				if(y-1>=0)
					chesspiece[y-1][x+1]=0;
				if(y+1<9)
					chesspiece[y+1][x+1]=0;
			}
			break;
		case 5: //��
			if(next-Selected==8){
				chesspiece[y+1][x]=0;
				chesspiece[y][x+1]=0;
			}
			if(next-Selected==6){
				chesspiece[y+1][x]=0;
				chesspiece[y][x-1]=0;
			}
			if(Selected-next==8){
				chesspiece[y-1][x]=0;
				chesspiece[y][x-1]=0;
			}
			if(Selected-next==6){
				chesspiece[y-1][x]=0;
				chesspiece[y][x+1]=0;
			}
			break;
		case 6: //����
			if(next-Selected==14)
				chesspiece[y+1][x]=0;
			if(next-Selected==2)
				chesspiece[y][x+1]=0;
			if(Selected-next==2)
				chesspiece[y][x-1]=0;
			if(Selected-next==14)
				chesspiece[y-1][x]=0;
			break;
		case 7://��
			if(next-Selected==16)
				chesspiece[y+1][x+1]=0;
			if(next-Selected==12)
				chesspiece[y+1][x-1]=0;
			if(Selected-next==12)
				chesspiece[y-1][x+1]=0;
			if(Selected-next==16)
				chesspiece[y-1][x-1]=0;
			break;
		}
		chesspiece[next/7][next%7]=chesspiece[y][x];//�����ƶ�
		chesspiece[y][x]=0;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new AnimalGame();
	}
	
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);  //�˳�����
	}
//�����ĳ�����ʵ��WindowListener, MouseListener�����������������ĺ������հ׵�Ҳ���ܣ���ɾ��
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}

