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
	private static int chesspiece[][]=new int[9][7];  //存储棋盘上各个位置的棋子
	private Map<Integer,String> map=new HashMap<Integer ,String>();  //棋子与数字的hash对应
	private static List<Integer> Next=new ArrayList<Integer>();  //存储当前棋子可抵达的位置
	private static int Selected=-1;  //表示当前选择的棋子
	private static int turn=1; //表示当前哪一方下棋
	public AnimalGame(){
		setTitle("灵魂斗兽棋-黑色");  //设置frame标题
		setBounds(460, 300, 275, 355);  //设置位置
		setLayout(null);
		addWindowListener(this);  //添加窗口监听
		addMouseListener(this);//添加鼠标监听
		inital();  //初始化
		repaint();
		setResizable(false);//不可变大
		setVisible(true);  //设置窗口可显示
	}
	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.black);
		for(int i=0;i<8;i++)
			g.drawLine(30+30*i, 60, 30+30*i, 330);
		for(int i=0;i<10;i++)
			g.drawLine(30, 60+30*i,240, 60+30*i);  //画出棋盘网格（黑色）
		
		g.drawString("陷阱",95,80);
		g.drawString("陷阱",155,80);
		g.drawString("陷阱",125,110);
		g.drawString("巢穴",125,80);
		
		g.setColor(Color.red);
		g.drawString("陷阱",95,320);
		g.drawString("陷阱",155,320);
		g.drawString("陷阱",125,290);
		g.drawString("巢穴",125,320);  //画出巢穴以及陷阱
		
		for (int i=0;i<9;i++)
			for(int j=0;j<7;j++)
			{
				if(chesspiece[i][j]==0)
					continue;
				if(chesspiece[i][j]>0)
					g.setColor(Color.black);
				else
					g.setColor(Color.red);
				g.drawString(map.get(Math.abs(chesspiece[i][j])), 40+30*j, 80+30*i);  //画出棋子（0表示空白，大于0表示黑色棋子，小于0表示红色棋子）
				g.setColor(Color.black);
				g.drawOval(35+30*j, 65+30*i, 20, 20);
			}
		g.setColor(Color.green);  //画出下一步可选择的位置（绿色）
		for(int i=0;i<Next.size();i++)
			g.drawRect(33+30*(Next.get(i)%7), 63+30*(Next.get(i)/7),24,24);
		if(Selected>=0)
		{
			g.setColor(Color.blue);  //画出当前选择的棋子（蓝色）
			g.drawRect(33+30*(Selected%7), 63+30*(Selected/7),24,24);
		}
	}
	public void inital(){  //初始化
		turn=1;     //执黑先行
		for(int i=0;i<9;i++)
			for(int j=0;j<7;j++)
				chesspiece[i][j]=0;  //初始化所有棋子为空
		chesspiece[0][0]=1;	
		chesspiece[0][6]=2;
		chesspiece[1][1]=3;
		chesspiece[1][5]=4;
		chesspiece[2][0]=5;
		chesspiece[2][2]=6;
		chesspiece[2][4]=7;
		chesspiece[2][6]=8;  //黑色棋子
		
		chesspiece[8][6]=-1;	
		chesspiece[8][0]=-2;
		chesspiece[7][5]=-3;
		chesspiece[7][1]=-4;
		chesspiece[6][6]=-5;
		chesspiece[6][4]=-6;
		chesspiece[6][2]=-7;
		chesspiece[6][0]=-8;  //红色棋子
		
		map.put(1, "狮");
		map.put(2, "虎");
		map.put(3, "狗");
		map.put(4, "熊");
		map.put(5, "蛇");
		map.put(6, "豹");
		map.put(7, "狼");
		map.put(8, "象");  //生成hash对应表
	}
	public void mouseClicked(MouseEvent e) {  //鼠标点击事件
		int x,y;
		x=e.getX();
		y=e.getY();  //获取鼠标坐标
		if(x<30||y<60||x>240||y>330)  //棋盘外的点击不处理
			return ;
		x=(x-30)/30;
		y=(y-60)/30;  //计算在chesspiece二维数组中的坐标
		if(chesspiece[y][x]*turn>0){//选中当前颜色的棋子作为下一步要移动的棋子
			if((turn>0&&(7*y+x==60||7*y+x==58||7*y+x==52))||(turn<0&&(7*y+x==2||7*y+x==4||7*y+x==10)))
				return;   //当前棋子处在对方陷阱中失去战斗力则当空白处理不可再移动
			Selected=7*y+x; //二维坐标转换为一维坐标方便存储
			Next.clear();  //清空下一步可选择的移动位置历史记录
			GetNext();  //计算下一步可选择的移动位置
			repaint();  //重绘
			return;
		}
		if(Selected>=0){  //已经选择了要移动的棋子则进行移动操作
			if(Next.contains(7*y+x)){  //是否是可移动的位置
				Move(7*y+x); //执行移动
				Selected=-1; //修改当前选择为空
				Next.clear();  //清空下一步可选择的移动位置历史记录
				repaint();//重绘
				if(chesspiece[0][3]<0||GetBlack()==0){//红方棋子进入黑方巢穴取得胜利或者黑方无可移动棋子
					JOptionPane.showMessageDialog(null, "红色方取得胜利");
					inital();//初始化重新开始
					repaint();
					return;
				}
				else if(chesspiece[8][3]>0||GetRed()==0){//黑方棋子进入红方巢穴取得胜利或者红方无可移动棋子
					JOptionPane.showMessageDialog(null, "黑色方取得胜利");
					inital();
					repaint();
					return;
				}
				if(turn >0){//修改当前移动的阵营并在标题显示
					turn=-1;  
					setTitle("灵魂斗兽棋-红色");
				}
				else{
					turn=1;
					setTitle("灵魂斗兽棋-黑色");
				}
			}
		}
		
	}
	public static int GetBlack(){
		int sum=0;
		for(int i=0;i<9;i++)
			for(int j=0;j<7;j++)
				if(chesspiece[i][j]>0&&!(7*i+j==60||7*i+j==58||7*i+j==52))
					sum+=1;//黑色棋子并且不在对方陷阱中则加1
		return sum;
	}
	public static int GetRed(){
		int sum=0;
		for(int i=0;i<9;i++)
			for(int j=0;j<7;j++)
				if(chesspiece[i][j]<0&&!(7*i+j==10||7*i+j==2||7*i+j==4))
					sum+=1;//红色棋子并且不在对方陷阱中则加1
		return sum;
	}
	public static void GetNext(){
		int y=Selected/7;
		int x=Selected%7;
		for(int i=x-1;i<=x+1;i++)  //先在以棋子为中心的一个九宫格内搜索可移动位置
			for(int j=y-1;j<=y+1;j++)
				if((i==x&&j==y)||i<0||j<0||i>6||j>8) //棋子当前位置以及棋盘外位置剔除
					continue;
				else if(chesspiece[j][i]==0)  //空白位置可移动
					Next.add(7*j+i);
				else if(turn==1&&chesspiece[j][i]<0&&(7*j+i==2||7*j+i==4||7*j+i==10))
					Next.add(7*j+i);   //对方棋子在己方陷阱可通过移动吃掉，可移动
				else if(turn==-1&&chesspiece[j][i]>0&&(7*j+i==60||7*j+i==58||7*j+i==52))
					Next.add(7*j+i);
		switch(Math.abs(chesspiece[y][x])){  //按选择的棋子分类讨论可以吃的情况
		case 1:  //狮子
			if(x-2>=0&&chesspiece[y][x-2]*turn<0&&chesspiece[y][x-1]==0)
				Next.add(7*y+x-2);
			if(y-2>=0&&chesspiece[y-2][x]*turn<0&&chesspiece[y-1][x]==0)
				Next.add(7*(y-2)+x);
			if(x+2<7&&chesspiece[y][x+2]*turn<0&&chesspiece[y][x+1]==0)
				Next.add(7*y+x+2);
			if(y+2<9&&chesspiece[y+2][x]*turn<0&&chesspiece[y+1][x]==0)
				Next.add(7*(y+2)+x);
			break;
		case 2:  //老虎
			if(x-2>=0&&y-2>=0&&chesspiece[y-2][x-2]*turn<0&&chesspiece[y-1][x-1]==0)
				Next.add(7*(y-2)+x-2);
			if(x-2>=0&&y+2<9&&chesspiece[y+2][x-2]*turn<0&&chesspiece[y+1][x-1]==0)
				Next.add(7*(y+2)+x-2);
			if(x+2<7&&y-2>=0&&chesspiece[y-2][x+2]*turn<0&&chesspiece[y-1][x+1]==0)
				Next.add(7*(y-2)+x+2);
			if(x+2<7&&y+2<9&&chesspiece[y+2][x+2]*turn<0&&chesspiece[y+1][x+1]==0)
				Next.add(7*(y+2)+x+2);
			break;
		case 3:  //狗
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
		case 4:  //熊
			if(x-1>=0&&chesspiece[y][x-1]*turn<0)
				Next.add(7*y+x-1);
			if(y+1<9&&chesspiece[y+1][x]*turn<0)
				Next.add(7*(y+1)+x);
			if(y-1>=0&&chesspiece[y-1][x]*turn<0)
				Next.add(7*(y-1)+x);
			if(x+1<7&&chesspiece[y][x+1]*turn<0)
				Next.add(7*y+x+1);
			break;
		case 5: //蛇
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
		case 6: //豹子
			if(x-2>=0&&chesspiece[y][x-1]*turn<0)
				Next.add(7*y+x-2);
			if(y+2<9&&chesspiece[y+1][x]*turn<0)
				Next.add(7*(y+2)+x);
			if(y-2>=0&&chesspiece[y-1][x]*turn<0)
				Next.add(7*(y-2)+x);
			if(x+2<7&&chesspiece[y][x+1]*turn<0)
				Next.add(7*y+x+2);
			break;
		case 7:  //狼
			if(x-2>=0&&y-2>=0&&chesspiece[y-1][x-1]*turn<0)
				Next.add(7*(y-2)+x-2);
			if(x-2>=0&&y+2<9&&chesspiece[y+1][x-1]*turn<0)
				Next.add(7*(y+2)+x-2);
			if(x+2<7&&y-2>=0&&chesspiece[y-1][x+1]*turn<0)
				Next.add(7*(y-2)+x+2);
			if(x+2<7&&y+2<9&&chesspiece[y+1][x+1]*turn<0)
				Next.add(7*(y+2)+x+2);
			break;
		case 8:  //大象
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
	public static void Move(int next){  //移动处理
		int y=Selected/7;
		int x=Selected%7;
		
		switch(Math.abs(chesspiece[y][x])){  //先判断是不是特殊吃法（即吃掉的棋子与自己的落点不一致，豹子，狗，狼，蛇）
		case 3:  //狗
			if(next-Selected==14){  //吃掉对方的棋子
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
		case 5: //蛇
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
		case 6: //豹子
			if(next-Selected==14)
				chesspiece[y+1][x]=0;
			if(next-Selected==2)
				chesspiece[y][x+1]=0;
			if(Selected-next==2)
				chesspiece[y][x-1]=0;
			if(Selected-next==14)
				chesspiece[y-1][x]=0;
			break;
		case 7://狼
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
		chesspiece[next/7][next%7]=chesspiece[y][x];//棋子移动
		chesspiece[y][x]=0;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new AnimalGame();
	}
	
	public void windowClosing(WindowEvent arg0) {
		System.exit(0);  //退出程序
	}
//这后面的程序是实现WindowListener, MouseListener这两个抽象类所带的函数，空白的也不管，别删掉
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

