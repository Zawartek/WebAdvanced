package ex01F;

import org.apache.log4j.Logger;

public class Circle {
	protected static Logger log = Logger.getLogger(Circle.class);
	int x;
	int y;
	int radius;

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public Circle(int x, int y, int radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		log.debug("x=" + x + " y=" + y + " radius=" + radius);
	}

	/**
	 * Overloading of method toString
	 * 
	 * @return String message with the attributes of Circle
	 */
	@Override
	public String toString() {
		String message = String.format("Circle with center (%d,%d) and radius %d (Perimter is %,.2f)", this.x, this.y,
				this.radius, (2 * java.lang.Math.PI * this.radius));
		log.info(message);
		return message;
	}
}
