package TestNavigation;

/**
 * @author death
 *Class represents a Euler-Angle
 * representation of a rotation.
 */
public class EulerAngleRotation {

	private double radiansRotationAlongXAxis;
	private double radiansRotationAlongYAxis;
	private double radiansRotationAlongZAxis;
	
	///Constructor
	public EulerAngleRotation(double radiansRotationAlongXAxis,
			double radiansRotationAlongYAxis,
			double radiansRotationAlongZAxis){
		
		this.radiansRotationAlongXAxis = radiansRotationAlongXAxis;
		this.radiansRotationAlongYAxis = radiansRotationAlongYAxis;
		this.radiansRotationAlongZAxis = radiansRotationAlongZAxis;
	}
	
	///Describes the degree of rotation from the original position
	///along the X axis of the reference frame in radians
	public double getRadiansRotationAlongXAxis(){
		return radiansRotationAlongXAxis;
	}

	///Describes the degree of rotation from the original position
	///along the Y axis of the reference frame in radians
	public double getRadiansRotationAlongYAxis() {
		return radiansRotationAlongYAxis;
	}
	
	///Describes the degree of rotation from the original position
	///along the Z axis of the reference frame in radians
	public double getRadiansRotationAlongZAxis() {
		return radiansRotationAlongZAxis;
	}
	
}
