import java.util.ArrayList;
public class Coordinate {
    private final int _i;
    private final int _j;
    private final int _k;
    public Coordinate(int i, int j, int k) {
        _i = i;
        _j = j;
        _k = k;
    }
    public int getI() {
        return _i;
    }
    public int getJ() {
        return _j;
    }
    public int getK() {
        return _k;
    }
    public static Coordinate addCoordinates(Coordinate first, Coordinate second) {
        return new Coordinate(first.getI() + second.getI(), first.getJ() + second.getJ(), first.getK() + second.getK());
    }

    public static char charAtComplexIndex(char[][][] target, Coordinate goal) {
        //ensure we stay inbounds
        if(goal.getI() >= target.length || goal.getI() < 0)
            return '!';
        if(goal.getJ() >= target[goal.getI()].length || goal.getJ() < 0)
            return '!';
        if(goal.getK() >= target[goal.getI()][goal.getJ()].length || goal.getK() < 0)
            return '!';

        return target[goal.getI()][goal.getJ()][goal.getK()];
    }

    public static int[][] expandCoordinateArray(ArrayList<Coordinate> arr) {
        final int[][] result = new int[arr.size()][3];
        for(int i = 0; i < arr.size(); i++) {
            final Coordinate thisCoordinate = arr.get(i);
            result[i][0] = thisCoordinate.getI();
            result[i][1] = thisCoordinate.getJ();
            result[i][2] = thisCoordinate.getK();
        }
        return result;
    }

}
