/*
 *
 *  Copyright (c) Microsoft. All rights reserved.
 *  Licensed under the MIT license. See LICENSE file in the project root for full license information.
 *
 */

package samples.com.microsoft.azure.sdk.iot.assessment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.exceptions.IotHubClientException;
import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClient;
import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClientRegistrationResult;
import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClientStatus;
import com.microsoft.azure.sdk.iot.provisioning.security.SecurityProviderSymmetricKey;

public class AssessmentTracficMgmtSystem {

	private static IVehicalManager simulator = null;
	private static DeviceClient deviceClient = null;

	public static void main(String[] args) throws Exception {

		simulator = new VehicalSimulator() {
			@Override
			public void closeClient() {
				System.out.println("close client called");
				deviceClient.close();
			}

			@Override
			public void postUpdateToIotHub() {
				System.out.println("postUpdatetoIotHub called");
				try {
					DeviceTwinImpl.deviceTwin(deviceClient);
				} catch (IOException e) {
					deviceClient.close();
					e.printStackTrace();
				} catch (URISyntaxException e) {
					deviceClient.close();
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IotHubClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};


		System.out.println("Starting...");
		System.out.println("Beginning setup.");
		SecurityProviderSymmetricKey securityClientSymmetricKey;

		securityClientSymmetricKey = new SecurityProviderSymmetricKey(
				Configuration.SYMMETRIC_KEY.getBytes(StandardCharsets.UTF_8), Configuration.REGISTRATION_ID);

		ProvisioningDeviceClient provisioningDeviceClient = ProvisioningDeviceClient.create(
				Configuration.GLOBAL_ENDPOINT, Configuration.ID_SCOPE,
				Configuration.PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL, securityClientSymmetricKey);

		ProvisioningDeviceClientRegistrationResult provisioningDeviceClientRegistrationResult = provisioningDeviceClient
				.registerDeviceSync();
		provisioningDeviceClient.close();

		if (provisioningDeviceClientRegistrationResult
				.getProvisioningDeviceClientStatus() == ProvisioningDeviceClientStatus.PROVISIONING_DEVICE_STATUS_ASSIGNED) {
			System.out.println("IotHub Uri : " + provisioningDeviceClientRegistrationResult.getIothubUri());
			System.out.println("Device ID : " + provisioningDeviceClientRegistrationResult.getDeviceId());

			// connect to iothub
			String iotHubUri = provisioningDeviceClientRegistrationResult.getIothubUri();
			String deviceId = provisioningDeviceClientRegistrationResult.getDeviceId();

			System.out.println("Sending message from device to IoT Hub...");

			deviceClient = new DeviceClient(iotHubUri, deviceId, securityClientSymmetricKey, IotHubClientProtocol.MQTT);
			deviceClient.open(false);
			new MyCallBack().setCallBack(deviceClient);
			DirectMethodImps.connectDirectMethod(deviceClient, simulator);
			deviceClient.sendEvent(new Message("Hello from device"));
			simulator.startRide();
		}

	}
	
	
}
