package Main;
import Rendering.Window;

public class Main implements Runnable {
	public static void main(String[] args) {
		new Main().start();
	}

	public void start() {
		Thread game = new Thread(this, "game");
		game.start();
	}

	public void run() {
		Window w = new Window(800, 600, "Among Us");
		w.create();
		w.update();
	}
}