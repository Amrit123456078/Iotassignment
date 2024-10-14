package samples.com.microsoft.azure.sdk.iot.assessment;

public interface IVehicalManager {
	void startRide();
	void stopRide();
	void stopVehical();
	void startVehical();
	boolean getRideStatus();
}
