/*
 *
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package samples.com.microsoft.azure.sdk.iot;

import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.exceptions.IotHubClientException;
import com.microsoft.azure.sdk.iot.device.twin.Twin;
import com.microsoft.azure.sdk.iot.provisioning.device.*;
import com.microsoft.azure.sdk.iot.provisioning.device.internal.exceptions.ProvisioningDeviceClientException;
import com.microsoft.azure.sdk.iot.provisioning.security.SecurityProviderSymmetricKey;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Symmetric Key authenticated individual enrollment sample
 */
@SuppressWarnings("CommentedOutCode") // Ignored in samples as we use these comments to show other options.
public class DPSWithDeviceTwinSample
{
    // The scope Id of your DPS instance. This value can be retrieved from the Azure Portal
    private static final String ID_SCOPE = "0ne00DC4B4E";

    // Typically "global.azure-devices-provisioning.net"
    private static final String GLOBAL_ENDPOINT = "global.azure-devices-provisioning.net";

    // The symmetric key of the individual enrollment. Unlike with enrollment groups, this key can be used directly.

    // For the sake of security, you shouldn't save keys into String variables as that places them in heap memory. For the sake
    // of simplicity within this sample, though, we will save it as a string. Typically this key would be loaded as byte[] so that
    // it can be removed from stack memory.
    private static final String SYMMETRIC_KEY = "wYTa8QF/NZ8HhElxJ3QrVPjj2V53v1XlE+eDoax3/llM7FuS9tMQXQogTduP8GV3h4cjYjVNjK5IAIoTONUYOQ==";

    // The registration Id to provision the device to. When creating an individual enrollment prior to running this sample, you choose this value.
    private static final String REGISTRATION_ID = "Amrit-device1";

    // Uncomment one line to choose which protocol you'd like to use
    private static final ProvisioningDeviceClientTransportProtocol PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL = ProvisioningDeviceClientTransportProtocol.HTTPS;
    //private static final ProvisioningDeviceClientTransportProtocol PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL = ProvisioningDeviceClientTransportProtocol.MQTT;
    //private static final ProvisioningDeviceClientTransportProtocol PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL = ProvisioningDeviceClientTransportProtocol.MQTT_WS;
    //private static final ProvisioningDeviceClientTransportProtocol PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL = ProvisioningDeviceClientTransportProtocol.AMQPS;
    //private static final ProvisioningDeviceClientTransportProtocol PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL = ProvisioningDeviceClientTransportProtocol.AMQPS_WS;

    public static void main(String[] args) throws Exception
    {
        System.out.println("Starting...");
        System.out.println("Beginning setup.");
        SecurityProviderSymmetricKey securityClientSymmetricKey;

        securityClientSymmetricKey = new SecurityProviderSymmetricKey(SYMMETRIC_KEY.getBytes(StandardCharsets.UTF_8), REGISTRATION_ID);

        ProvisioningDeviceClient provisioningDeviceClient = ProvisioningDeviceClient.create(
            GLOBAL_ENDPOINT,
            ID_SCOPE,
            PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL,
            securityClientSymmetricKey);

        ProvisioningDeviceClientRegistrationResult provisioningDeviceClientRegistrationResult = provisioningDeviceClient.registerDeviceSync();
        provisioningDeviceClient.close();

        if (provisioningDeviceClientRegistrationResult.getProvisioningDeviceClientStatus() == ProvisioningDeviceClientStatus.PROVISIONING_DEVICE_STATUS_ASSIGNED)
        {
            System.out.println("IotHub Uri : " + provisioningDeviceClientRegistrationResult.getIothubUri());
            System.out.println("Device ID : " + provisioningDeviceClientRegistrationResult.getDeviceId());

            // connect to iothub
            String iotHubUri = provisioningDeviceClientRegistrationResult.getIothubUri();
            String deviceId = provisioningDeviceClientRegistrationResult.getDeviceId();
            
            System.out.println("Sending message from device to IoT Hub...");
            
//			for (int i = 0; i < 10; i++) {
				DeviceClient deviceClient = new DeviceClient(iotHubUri, deviceId, securityClientSymmetricKey,
						IotHubClientProtocol.MQTT);
				deviceClient.open(false);				
				deviceClient.sendEvent(new Message("Hello from device"));
//				Twin twin = deviceClient.getTwin();
				DeviceTwinSample.doDevice(deviceClient);
				deviceClient.close();
				Thread.sleep(1000);
//			}
			
        }

        System.out.println("Press any key to exit...");
        new Scanner(System.in, StandardCharsets.UTF_8.name()).nextLine();
    }
}
