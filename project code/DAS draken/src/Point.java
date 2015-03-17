
public class Point
{
	int X, Y;
	public Point(int X, int Y)
	{
		this.X = X;
		this.Y = Y;
	}
	
	public int distanceTo(Point p)
	{
		return Math.abs(p.X - this.X) + Math.abs(p.Y - this.Y);
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Point)
		{
			Point p= (Point)o;
			return  (this.X == p.X) && (this.Y == p.Y);
		}
		else
		{
			return false;
		}
	}
}
