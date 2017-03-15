package hu.oe.nik.szfmv17t;

import hu.oe.nik.szfmv17t.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv17t.environment.World;
import hu.oe.nik.szfmv17t.visualisation.CourseDisplay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

	private static final Logger logger = LogManager.getLogger();
	private static final int CYCLE_PERIOD = 200;
	public static final int worldHeight = 600;
	public static final int worldWidth= 800;
	public static final int FPS=30;
	public static void main(String[] args) throws InterruptedException {
		CourseDisplay vis = new CourseDisplay();
		//not a pun
		// create the world
		World w = new World(800,600);
		// create an automated car
		AutomatedCar car = new AutomatedCar(20,20, "bosch1.png");
		// add car to the world
		w.addObjectToWorld(car);
		// init visualisation module with the world
		vis.init(w);

		Thread drawThread=new Thread(vis);
		drawThread.start();

		while(true) {
			try {
				car.drive();
				Thread.sleep(CYCLE_PERIOD);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
