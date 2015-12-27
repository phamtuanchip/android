import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class TomauVetDauLoang extends Applet {

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawArc(10, 100, 10, 100, 10, 20);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int getcolor(int x, int y) {
		return Color.RED;
	}
	

}
