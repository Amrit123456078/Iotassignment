package samples.com.microsoft.azure.sdk.iot;

import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.exceptions.IotHubClientException;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;
import com.microsoft.azure.sdk.iot.device.twin.DirectMethodResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DirectMethodSample {
    protected static class IotHubConnectionStatusChangeCallbackLogger implements IotHubConnectionStatusChangeCallback {
        @Override
        public void onStatusChanged(ConnectionStatusChangeContext context) {
            IotHubConnectionStatus status = context.getNewStatus();
            System.out.println("CONNECTION STATUS UPDATE: " + status);
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException, IotHubClientException, InterruptedException {
        System.out.println("Starting...");

        if (args.length < 1) {
            System.out.println("Expected a Device connection string.");
            return;
        }

        String connString = args[0];
        IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;

        DeviceClient client = new DeviceClient(connString, protocol);
        client.setConnectionStatusChangeCallback(new IotHubConnectionStatusChangeCallbackLogger(), new Object());
        client.open(true);
        System.out.println("Opened connection to IoT Hub.");

        client.subscribeToMethods(
                (methodName, methodData, context) -> {
                    System.out.println("Received method call: " + methodName);
                    String payload = methodData.getPayloadAsJsonString();
                    System.out.println("Payload: " + payload);
                    return new DirectMethodResponse(200, "Vehical stopped successfully");
                }, null);

        System.out.println("Successfully subscribed to direct methods");

        System.out.println("Press Enter to exit...");
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
        scanner.nextLine();
        client.close();
        System.out.println("Shutting down...");
    }
}
