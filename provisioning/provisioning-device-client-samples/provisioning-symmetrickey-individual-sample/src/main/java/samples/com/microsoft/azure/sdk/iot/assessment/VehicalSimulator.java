package samples.com.microsoft.azure.sdk.iot.assessment;

import java.util.Timer;
import java.util.TimerTask;

public abstract class VehicalSimulator implements IVehicalManager {
	private int timeDelay = 5000;
	private boolean keepRunning = true;

	public abstract void postUpdateToIotHub();

	public abstract void closeClient();

	// Start the vehicle simulator
	// Note: this will make vehicle ride start between start to end destination.
	public void start() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (shallContinueRide()) {
					simulateMovement();
				} else {
					closeClient();
				}
			}

			private boolean shallContinueRide() {
				if (LocationSimulator.getInstance().getDestinationLat()
						- LocationSimulator.getInstance().getSourceLat() <= 0.10000) {
					return false;
				}
				return true;
			}
		}, 0, timeDelay);
	}

	// Simulate the vehicle's movement and speed
	private void simulateMovement() {
		if (keepRunning) {
			SpeedSimulator.getInstance().start();
			SpeedSimulator.getInstance().getDistanceTravelledInSecond();
			LocationSimulator.getInstance().updateLocation();
			System.out.println(String.format("Source address lat/lng: %.6f, %.6f to %.6f, %.6f",
					LocationSimulator.getInstance().getSourceLat(), LocationSimulator.getInstance().getSourceLng(),
					LocationSimulator.getInstance().getDestinationLat(),
					LocationSimulator.getInstance().getDestinationLng()));
		}else {
			SpeedSimulator.getInstance().setCurrentSpeed();
		}
		postUpdateToIotHub();
	}

	@Override
	public void stopVehical() {
		keepRunning = false;
		if (!SpeedSimulator.getInstance().isStopped()) {
			System.out.println("The vehicle is stopped at the signal.");
			SpeedSimulator.getInstance().stop();
		}
		++DeviceTwinImpl.intersection;
		simulateMovement();
	}

	@Override
	public void startVehical() {
		keepRunning = true;
		System.out.println("The vehicle is started from the signal.");
		SpeedSimulator.getInstance().start();
		simulateMovement();
	}

	@Override
	public void startRide() {
		keepRunning = true;
		start();
		simulateMovement();
	}

	@Override
	public void stopRide() {
		keepRunning = false;
		LocationSimulator.getInstance().clearLocation();
		closeClient();
	}

	@Override
	public boolean getRideStatus() {
		return keepRunning;
	}

}
