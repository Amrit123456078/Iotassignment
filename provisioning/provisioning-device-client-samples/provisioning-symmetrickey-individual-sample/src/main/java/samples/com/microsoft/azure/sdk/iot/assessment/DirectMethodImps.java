package samples.com.microsoft.azure.sdk.iot.assessment;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.exceptions.IotHubClientException;
import com.microsoft.azure.sdk.iot.device.twin.DirectMethodResponse;

public class DirectMethodImps {

	public static void connectDirectMethod(DeviceClient client, IVehicalManager vehicalManager) {
		System.out.println("Successfully created an IoT Hub client.");

		try {
			System.out.println("Opened connection to IoT Hub.");
			client.subscribeToMethods((methodName, methodData, context) -> {
				switch (methodName) {
				case "start":
					vehicalManager.startVehical();
					break;
				case "stop":
					vehicalManager.stopVehical();
					break;
				default:
					vehicalManager.stopRide();
					break;
				}

				System.out.println("Received a direct method invocation with name " + methodName + " and payload "
						+ methodData.getPayloadAsJsonString());
				return new DirectMethodResponse(200, methodData);
			}, null);

			System.out.println("Successfully subscribed to direct methods");
		} catch (IotHubClientException | IllegalStateException | InterruptedException e) {
			System.out.println("Failed to subscribe to direct methods. Error code: " + e.getCause());
			client.close();
			System.out.println("Shutting down...");
			return;
		}
	}

}
