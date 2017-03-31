import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Solves the warp gates problem.
 * @author Steven McCracken
 */
public class WarpGates {
    static ArrayList<Point> stars, wormholes;
    public static void main(String[] args) {
        if(args == null || args.length == 0) {
            System.out.println("Must provide paths for the input files in the arguments");
            System.exit(-1);
        }
        String file_path_stars = args[0];
        String file_path_wormholes = args[1];
        long t1, t2;

        t1 = System.nanoTime();
        Object[] params = getPoints(file_path_stars, file_path_wormholes);
        int k = (int)params[0];
        double d = (double)params[1];
        t2 = System.nanoTime();
        duration(t1, t2, "parsing input");
    }

    /**
     * Gets point coordinates from input files and creates point objects
     * @param input_file_path_1 the path of the first input text file
     * @param input_file_path_2 the path of the second input text file
     * @return Object array containing parameters k (int) and d (double)
     */
    public static Object[] getPoints(String input_file_path_1, String input_file_path_2) {
        Object[] params = new Object[2];
        stars = new ArrayList<>();
        wormholes = new ArrayList<>();

        try (Stream<String> stream1 = Files.lines(Paths.get(input_file_path_1));
             Stream<String> stream2 = Files.lines(Paths.get(input_file_path_2)))
        {
            Object[] input_file_1 = stream1.toArray();
            params[0] = Integer.parseInt(input_file_1[0].toString()); // Get parameter k from first input file
            params[1] = Double.parseDouble(input_file_1[1].toString()); // Get parameter d from first input file

            // Get the rest of the star coordinates from first input file
            Stream<String> stars_stream = Arrays.stream(Arrays.copyOfRange(input_file_1, 2, input_file_1.length)).map(Object::toString);
            stars_stream.forEach(line -> {
                // Split the input line string on each comma into an array. Turn the string array into a double array
                double[] coordinates = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
                stars.add(new Point(coordinates));
            });

            stream2.forEach(line -> {
                // Split the input line string on each comma into an array. Turn the string array into a double array
                double[] coordinates = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
                wormholes.add(new Point(coordinates));
            });
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Invalid input file(s)!");
            System.exit(-1);
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Invalid data in input file!");
            System.exit(-1);
        }

        return params;
    }

    /**
     * Writes the input arraylist to a file
     * @param warp_gates the answer to the problem containing the warp gates in the universe
     */
    public static void writeToFile(ArrayList<Point> warp_gates) {
        try(FileWriter fw = new FileWriter("warp.txt", false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for(Point warp_gate : warp_gates) out.println(warp_gate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to help benchmark execution time of arbitrary sections of code
     * @param t1 the start time
     * @param t2 the end time
     * @param message the purpose of the benchmark
     */
    public static void duration(long t1, long t2, String message) {
        double d = ((double)t2 - t1) / 1000000000;
        System.out.printf("Duration for %s: %.5f seconds%n", message, d);
    }
}

class Point {
    private double x, y, z;
    private double[] coordinates;

    public Point(double[] coordinates) {
        this.coordinates = coordinates;
        this.x = coordinates[0];
        this.y = coordinates[1];
        this.z = coordinates[2];
    }

    public Point(double x, double y, double z) {
        this.coordinates = new double[] {x,y,z};
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double[] getCoordinates() {
        return this.coordinates;
    }

    @Override
    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }
}