package samples.com.microsoft.azure.sdk.iot.assessment;

import java.util.Random;

public class SpeedSimulator {
	private Random random = new Random();
	private double currentSpeed;
	private static final double STOPPED_THRESHOLD = 1.0;
	private static SpeedSimulator speedSimulator = null;

	private SpeedSimulator() {

	}

	public static SpeedSimulator getInstance() {
		if (speedSimulator == null) {
			speedSimulator = new SpeedSimulator();
		}
		return speedSimulator;
	}

	// Generate random speed between 0 and 120 km/h
	private double generateRandomSpeed() {
		return currentSpeed = random.nextDouble() * 120; // Generates speed between 0 and 120
	}

	public double getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed() {
		currentSpeed=0;
	}
	public double getDistanceTravelledInSecond() {
		return currentSpeed * (15.0 / 3600.0);
	}

	// Determine if the vehicle is stopped
	public boolean isStopped() {
		return currentSpeed < STOPPED_THRESHOLD;
	}

	// Determine if the vehicle is stopped
	public void stop() {
		currentSpeed = 0;
	}

	// Determine if the vehicle is stopped
	public void start() {
		currentSpeed = generateRandomSpeed();
	}
}
