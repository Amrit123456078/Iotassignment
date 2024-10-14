package samples.com.microsoft.azure.sdk.iot.assessment;

import com.microsoft.azure.sdk.iot.provisioning.device.ProvisioningDeviceClientTransportProtocol;

class Configuration {

	public static final ProvisioningDeviceClientTransportProtocol PROVISIONING_DEVICE_CLIENT_TRANSPORT_PROTOCOL = ProvisioningDeviceClientTransportProtocol.MQTT;


    public static final String ID_SCOPE = "0ne00DC4B4E";

    // Typically "global.azure-devices-provisioning.net"
    public static final String GLOBAL_ENDPOINT = "global.azure-devices-provisioning.net";

    // The symmetric key of the individual enrollment. Unlike with enrollment groups, this key can be used directly.

    // For the sake of security, you shouldn't save keys into String variables as that places them in heap memory. For the sake
    // of simplicity within this sample, though, we will save it as a string. Typically this key would be loaded as byte[] so that
    // it can be removed from stack memory.
    public static final String SYMMETRIC_KEY = "2sJVBLn0gqy5rv6kwlnAuFVba036qzD6ou9T5w/vIQ5fM77Wn0isQQQ+ivMZJMXGgFV3sOyehvzUAIoTCQJ1kw==";

    // The registration Id to provision the device to. When creating an individual enrollment prior to running this sample, you choose this value.
    public static final String REGISTRATION_ID = "batch2-hp-vehicle1";
}
