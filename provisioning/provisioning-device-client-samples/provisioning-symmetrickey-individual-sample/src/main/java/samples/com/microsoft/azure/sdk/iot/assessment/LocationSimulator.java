package samples.com.microsoft.azure.sdk.iot.assessment;

public class LocationSimulator {

	// Initial latitude and longitude
	private double latitude = 21.000000;
	private double longitude = 79.594600;
	private double destinationLat = 24.000000;
	private double destinationLng = 79.594600;

	private static LocationSimulator location;
	

	private LocationSimulator() {

	}

	public static LocationSimulator getInstance() {
		if (location == null) {
			location = new LocationSimulator();
		}
		return location;
	}

	public double getSourceLat() {
		return latitude;
	}

	public double getSourceLng() {
		return longitude;
	}

	public double getDestinationLat() {
		return destinationLat;
	}

	public double getDestinationLng() {
		return destinationLng;
	}

	// Update latitude and longitude based on speed
	public void updateLocation() {
//		latitude += distanceTraveled / 110.574; // 1 degree latitude ~ 110.574 km
//		longitude += distanceTraveled / (111.320 * Math.cos(Math.toRadians(latitude)));
		latitude += 0.10000; // 1 degree latitude ~ 110.574 km
		longitude += 0;
	}

	// calculate distance using Haversine formula
	public double getDistanceRemain() {
		final int EARTH_RADIUS = 6371; // Earth radius in kilometers

		// Convert latitude and longitude from degrees to radians
		double lat1 = Math.toRadians(latitude);
		double lon1 = Math.toRadians(longitude);
		double lat2 = Math.toRadians(destinationLat);
		double lon2 = Math.toRadians(destinationLng);

		// Haversine formula
		double dLat = lat2 - lat1;
		double dLon = lon2 - lon1;

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		// Distance in kilometers
		double distanceBetweenTwoPoints = EARTH_RADIUS * c;

		return distanceBetweenTwoPoints;
	}

	public void clearLocation() {
		destinationLat = 0;
		destinationLng = 0;
		latitude = 0;
		longitude = 0;
	}
}
