package ipp.isep.p1231360;

public class PathInfo {
    String path;
    double distance;

    public PathInfo(String path, double distance) {
        this.path = path;
        this.distance = distance;
    }

    // Método ToString para esscrever o caminho e a distância no ficheiro .csv de output
    @Override
    public String toString() {
        return "Path: " + path + ", Distance: " + distance;
    }
}
