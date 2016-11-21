import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException("Points cannot be null!");

        // Check for nulls and duplicates
        Point n, m;
        for (int i = 0; i < points.length; i++)
            for (int j = i+1; j < points.length; j++) {
                n = points[i];
                m = points[j];

                // Points cannot be null
                if (n == null || m == null)
                    throw new NullPointerException("Point cannot be null!");

                if (n.compareTo(m) == 0)
                    throw new IllegalArgumentException("Cannot contain duplicates!");
            }

        // Find all of the connected segments (4 on a line)
        List<LineSegment> segs = new ArrayList<>();
        Point p, q, r, s;
        for (int i = 0; i < points.length; i++)
            for (int j = i+1; j < points.length; j++)
                for (int k = j+1; k < points.length; k++)
                    for (int l = k+1; l < points.length; l++) {
                        p = points[i];
                        q = points[j];
                        r = points[k];
                        s = points[l];

                        double ptq = p.slopeTo(q);
                        double ptr = p.slopeTo(r);
                        double pts = p.slopeTo(s);
                        if (ptq == ptr && ptq == pts && ptr == pts) {
                            Point[] ps = new Point[] { p, q, r, s };
                            Arrays.sort(ps);
                            segs.add(new LineSegment(ps[0], ps[3]));
                        }
                    }

        // Set the array
        segments = segs.toArray(new LineSegment[segs.size()]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }
}
