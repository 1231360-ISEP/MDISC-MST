package ipp.isep.p1231360;

public class PathInfo {
    String path;
    double distance;

    public PathInfo(String path, double distance) {
        this.path = path;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Path: " + path + ", Distance: " + distance;
    }
}
