package samples.com.microsoft.azure.sdk.iot.assessment;

import com.microsoft.azure.sdk.iot.device.ConnectionStatusChangeContext;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.exceptions.IotHubClientException;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;
import com.microsoft.azure.sdk.iot.device.twin.DesiredPropertiesCallback;
import com.microsoft.azure.sdk.iot.device.twin.Twin;

public class MyCallBack {
	
	public void setCallBack(DeviceClient client) throws IllegalStateException, InterruptedException, IotHubClientException {
		client.setConnectionStatusChangeCallback(new IotHubConnectionStatusChangeCallbackLogger(),
				new Object());
		client.subscribeToDesiredProperties(new DesiredPropertiesUpdatedHandler(), null);
	}

	private class DesiredPropertiesUpdatedHandler implements DesiredPropertiesCallback {
		@Override
		public void onDesiredPropertiesUpdated(Twin desiredPropertyUpdateTwin, Object context) {
			if (desiredPropertyUpdateTwin == null) {
				// No need to care about this update because these properties will be present in
				// the twin retrieved by getTwin.
				System.out.println(
						"Received desired properties update before getting current twin. Ignoring this update.");
				return;
			}

			// desiredPropertyUpdateTwin.getDesiredProperties() contains all the newly
			// updated desired properties
			// as well as the new version of the desired properties
			desiredPropertyUpdateTwin.getDesiredProperties().putAll(desiredPropertyUpdateTwin.getDesiredProperties());
			desiredPropertyUpdateTwin.getDesiredProperties().setVersion(desiredPropertyUpdateTwin.getDesiredProperties().getVersion());
			System.out.println("Received desired property update. Current twin:");
			System.out.println(desiredPropertyUpdateTwin);
		}
	}

	protected class IotHubConnectionStatusChangeCallbackLogger implements IotHubConnectionStatusChangeCallback {
		@Override
		public void onStatusChanged(ConnectionStatusChangeContext connectionStatusChangeContext) {
			IotHubConnectionStatus status = connectionStatusChangeContext.getNewStatus();
			IotHubConnectionStatusChangeReason statusChangeReason = connectionStatusChangeContext.getNewStatusReason();
			Throwable throwable = connectionStatusChangeContext.getCause();

			System.out.println();
			System.out.println("CONNECTION STATUS UPDATE: " + status);
			System.out.println("CONNECTION STATUS REASON: " + statusChangeReason);
			System.out.println("CONNECTION STATUS THROWABLE: " + (throwable == null ? "null" : throwable.getMessage()));
			System.out.println();

			if (throwable != null) {
				throwable.printStackTrace();
			}

			if (status == IotHubConnectionStatus.DISCONNECTED) {
				System.out.println("The connection was lost, and is not being re-established."
						+ " Look at provided exception for how to resolve this issue."
						+ " Cannot send messages until this issue is resolved, and you manually re-open the device client");
			} else if (status == IotHubConnectionStatus.DISCONNECTED_RETRYING) {
				System.out.println("The connection was lost, but is being re-established."
						+ " Can still send messages, but they won't be sent until the connection is re-established");
			} else if (status == IotHubConnectionStatus.CONNECTED) {
				System.out.println("The connection was successfully established. Can send messages.");
			}
		}
	}
}
