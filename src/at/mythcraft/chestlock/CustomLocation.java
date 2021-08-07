package at.mythcraft.chestlock;

public class CustomLocation {
    private int x, y, z;

    public CustomLocation(double x, double y, double z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public boolean matches(CustomLocation other) {
        if(other == null) {
            return false;
        }
        return this.x == other.getX() && this.y == other.getY() && this.z == other.getZ();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "CustomLocation{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
