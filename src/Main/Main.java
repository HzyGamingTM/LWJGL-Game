package Main;
import Rendering.Window;

public class Main {
	public static void main(String[] args) {
		Window w = new Window(800, 600, "Among Us");
		w.create();
		w.update();
	}
}