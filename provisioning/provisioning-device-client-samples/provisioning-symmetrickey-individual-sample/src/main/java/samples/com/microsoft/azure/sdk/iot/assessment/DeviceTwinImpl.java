// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package samples.com.microsoft.azure.sdk.iot.assessment;

import java.io.IOException;
import java.net.URISyntaxException;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.exceptions.IotHubClientException;
import com.microsoft.azure.sdk.iot.device.twin.ReportedPropertiesUpdateResponse;
import com.microsoft.azure.sdk.iot.device.twin.Twin;
import com.microsoft.azure.sdk.iot.device.twin.TwinCollection;

// Traffic management device twin manager
public class DeviceTwinImpl {
	private enum VEHICAL {
		RUNNING, STOPPED
	}

	public static int intersection = 0;


	/**
	 * Reports properties to IotHub, receives desired property notifications from
	 * IotHub. Default protocol is to use use MQTT transport.
	 * 
	 * @param simulator
	 *
	 * @param args      args[0] = IoT Hub connection string
	 * @throws IotHubClientException 
	 * @throws InterruptedException 
	 * @throws IllegalStateException 
	 */
	public static void deviceTwin(DeviceClient client) throws IOException, URISyntaxException, IllegalStateException, InterruptedException, IotHubClientException {

			Twin twin  =  client.getTwin();
			TwinCollection reportedProperties = twin.getReportedProperties();
			if (SpeedSimulator.getInstance().isStopped()) {
				reportedProperties.put("VehicalStatus", VEHICAL.STOPPED);
			} else {
				reportedProperties.put("VehicalStatus", VEHICAL.RUNNING);
			}

			reportedProperties.put("CurrentLatitude", LocationSimulator.getInstance().getSourceLat());
			reportedProperties.put("CurrentLongitude", LocationSimulator.getInstance().getDestinationLng());
			reportedProperties.put("Speed", SpeedSimulator.getInstance().getCurrentSpeed());
			reportedProperties.put("DisanceTravelled", SpeedSimulator.getInstance().getDistanceTravelledInSecond());
			reportedProperties.put("DisanceRemained", LocationSimulator.getInstance().getDistanceRemain());
			reportedProperties.put("Intersection", intersection);
			System.out.println("Updating reported property");
			ReportedPropertiesUpdateResponse response = client.updateReportedProperties(reportedProperties);
			
			// After a successful update of the device's reported properties, the service
			// will provide the new
			// reported properties version for the twin. You'll need to save this value in
			// your twin object's reported
			// properties object so that subsequent updates don't fail with a "precondition
			// failed" error.
			twin.getReportedProperties().setVersion(response.getVersion());
	}
}
