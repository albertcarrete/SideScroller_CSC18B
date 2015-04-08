package appstate;

public abstract class AppState {
	
	protected AppStateManager asm;
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(java.awt.Graphics g);
	public abstract void drawToScreen(java.awt.Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
}
