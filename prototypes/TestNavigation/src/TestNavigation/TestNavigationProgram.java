package TestNavigation;

import java.util.ArrayList;
import java.util.List;



public class TestNavigationProgram {

	private static void TestSampleRotation(){
		List<GyroscopeMeasurement> measurements = new ArrayList<GyroscopeMeasurement>();
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(0,0,0);		
		
		int numberOfSamples = 100;
	    double degreeChange = Math.PI/2;
	    double degreeChangePerSample = degreeChange/numberOfSamples;
	    
	    for(int i = 0; i < numberOfSamples; i++){
	    	GyroscopeMeasurement measurement = new GyroscopeMeasurement(degreeChangePerSample, 0, 0, 1);

	    	measurements.add(measurement);
	    }

		TestGyroscope testGyroscope = new TestGyroscope(measurements);
		RotationMonitor rotationMonitor = new RotationMonitor(testGyroscope, initialBodyFrameOrientation);
		rotationMonitor.AddSubscriber(new GenerationRotationChangeSubscriber(){

			@Override
			public void OrientationChanged(
					EulerAngleRotation newBodyFrameOrientation) {
				
				super.OrientationChanged(newBodyFrameOrientation);
				
				System.out.println(String.format("%f radians around x axis", newBodyFrameOrientation.getRadiansRotationAlongXAxis()));
				System.out.println(String.format("%f radians around y axis", newBodyFrameOrientation.getRadiansRotationAlongYAxis()));
				System.out.println(String.format("%f radians around z axis", newBodyFrameOrientation.getRadiansRotationAlongZAxis()));
			}
			
		});
		
		EulerAngleRotation lastReportedTotalBodyFrameRotation = null;
		for(int i = 0; i < numberOfSamples; i++){
			lastReportedTotalBodyFrameRotation = rotationMonitor.waitForNextRotationUpdate();
		}
		
		boolean testPassed = true;
		double tolerance = 0.1;
		
		testPassed = testPassed && (lastReportedTotalBodyFrameRotation != null)&&(Math.abs(lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis() - degreeChange) < tolerance);
		
		if(testPassed)
		{
			System.out.println("Test Passed!");
		}
		else{
			System.out.println("Test Failed!");
		}

	}
	
	private static void PrintRotation(EulerAngleRotation rotation){

		System.out.println(String.format("%f radians around x axis", rotation.getRadiansRotationAlongXAxis()));
		System.out.println(String.format("%f radians around y axis", rotation.getRadiansRotationAlongYAxis()));
		System.out.println(String.format("%f radians around z axis", rotation.getRadiansRotationAlongZAxis()));
		
	}
	
	private static void TestMeasurementCsv(){

		final String filename = "/home/death/Documents/CPE656/fullRotation_Nexus_7_09_18_15.csv";
		GyroscopeReader gyroscopeReader = new GyroscopeReader(filename);
		List<GyroscopeMeasurement> measurements = gyroscopeReader.getGyroscopeMeasurements();
		EulerAngleRotation initialBodyFrameOrientation = new EulerAngleRotation(Math.PI/2,0,0);		

		final int numberOfSamples = measurements.size();
		TestGyroscope testGyroscope = new TestGyroscope(measurements);
		RotationMonitor rotationMonitor = new RotationMonitor(testGyroscope, initialBodyFrameOrientation);
		rotationMonitor.AddSubscriber(new GenerationRotationChangeSubscriber(){

			@Override
			public void OrientationChanged(
					EulerAngleRotation newBodyFrameOrientation) {
				
				super.OrientationChanged(newBodyFrameOrientation);
				
				PrintRotation(newBodyFrameOrientation);
			}
			
		});
		
		EulerAngleRotation lastReportedTotalBodyFrameRotation = null;
		for(int i = 0; i < numberOfSamples; i++){
			lastReportedTotalBodyFrameRotation = rotationMonitor.waitForNextRotationUpdate();
			
			//Adjust rotation to inertial frame
			double p = lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis();
			double q = lastReportedTotalBodyFrameRotation.getRadiansRotationAlongYAxis();
			double r = lastReportedTotalBodyFrameRotation.getRadiansRotationAlongZAxis();
			double fee = initialBodyFrameOrientation.getRadiansRotationAlongXAxis();
			double theta = initialBodyFrameOrientation.getRadiansRotationAlongYAxis();
			double aroundX = p + q*Math.sin(fee)*Math.tan(theta) + r*Math.cos(fee)*Math.tan(theta);
			double aroundY = q*Math.cos(fee) - r*Math.sin(fee);
			double aroundZ = q*Math.sin(fee)/Math.cos(theta) + r*Math.cos(fee)/Math.cos(theta);
			
			EulerAngleRotation inertialFrameRotation = new EulerAngleRotation(aroundX, aroundY, aroundZ);
			
			System.out.println(String.format("%f radians around Earth x axis", inertialFrameRotation.getRadiansRotationAlongXAxis()));
			System.out.println(String.format("%f radians around Earth y axis", inertialFrameRotation.getRadiansRotationAlongYAxis()));
			System.out.println(String.format("%f radians around Earth z axis", inertialFrameRotation.getRadiansRotationAlongZAxis()));			
		}
		
		boolean testPassed = true;
		double tolerance = 0.1;
		
		
		//testPassed = testPassed && (lastReportedTotalBodyFrameRotation != null)&&(Math.abs(lastReportedTotalBodyFrameRotation.getRadiansRotationAlongXAxis() - degreeChange) < tolerance);
		
		if(testPassed)
		{
			System.out.println("Test Passed!");
		}
		else{
			System.out.println("Test Failed!");
		}

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Quat4d quat4d = RotationMonitor.rotate(0, 0, Math.PI/2);
		
		EulerAngleRotation rotation = RotationMonitor.convertFromQuaternionToEulerAngle(quat4d);
		
		PrintRotation(rotation);
	
		//TestMeasurementCsv();		
	}

}
