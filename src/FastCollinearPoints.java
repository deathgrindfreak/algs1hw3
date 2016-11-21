import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        // Check for nulls and duplicates
        Point r, s;
        for (int i = 0; i < points.length; i++)
            for (int j = i+1; j < points.length; j++) {
                r = points[i];
                s = points[j];

                // Points cannot be null
                if (r == null || s == null)
                    throw new NullPointerException("Point cannot be null!");

                if (r.compareTo(s) == 0)
                    throw new IllegalArgumentException("Cannot contain duplicates!");
            }

        List<List<Point>> segs = new ArrayList<>();
        for (Point p : points) {
            // Sort according to slope order
            Arrays.sort(points, p.slopeOrder());

            List<Point> pts = new ArrayList<>();
            Point q = points[0];
            double slope = p.slopeTo(q);
            for (int i = 1; i < points.length; i++) {
                Point n = points[i];
                if (p.slopeTo(n) == slope) {
                    pts.add(n);
                } else {
                    // Need at least 2 other points to have 4 collinear points
                    if (pts.size() >= 2) {
                        pts.add(p);
                        pts.add(q);
                        Collections.sort(pts);

                        // Ignore if point has already been added
/*                        boolean add = true;
                        for (List<Point> sg : segs)
                            if (sg.get(0).compareTo(pts.get(0)) == 0
                                    || sg.get(pts.size() - 1).compareTo(pts.get(pts.size() - 1)) == 0)
                                add = false;
                        if (add)*/
                        segs.add(pts);
                    }

                    pts = new ArrayList<>();
                    q = points[i];
                    slope = p.slopeTo(q);
                }
            }
        }

        List<LineSegment> lSegs = new ArrayList<>();
        for (List<Point> sg : segs)
            lSegs.add(new LineSegment(sg.get(0), sg.get(sg.size() - 1)));

        // Set the array
        segments = lSegs.toArray(new LineSegment[lSegs.size()]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }
}
