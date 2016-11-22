import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        Point[] cpy = Arrays.copyOf(points, points.length);
        for (Point p : cpy) {
            // Sort according to slope order
            Arrays.sort(points, p.slopeOrder());

            List<Point> pts = new ArrayList<>();
            Point q = points[0];
            double slope = p.slopeTo(q);
            for (int i = 1; i < points.length; i++) {
                Point n = points[i];
                if (p.slopeTo(n) == slope) {
                    pts.add(n);

                    // Continue unless on last element
                    if (i < points.length - 1)
                        continue;
                }

                // Need at least 2 other points to have 4 collinear points
                if (pts.size() >= 2) {
                    pts.add(p);
                    pts.add(q);
                    Collections.sort(pts);
                    segs.add(pts);
                }

                pts = new ArrayList<>();
                q = n;
                slope = p.slopeTo(q);
            }
        }

        List<LineSegment> lSegs = new ArrayList<>();
        Comparator<List<Point>> comp = new SegmentComparator();
        Collections.sort(segs, comp);

        if (!segs.isEmpty()) {
            List<Point> currSeg = segs.get(0);
            lSegs.add(new LineSegment(currSeg.get(0), currSeg.get(currSeg.size() - 1)));
            for (int i = 1; i < segs.size(); i++) {
                List<Point> sg = segs.get(i);
                if (comp.compare(currSeg, sg) == 0)
                    continue;

                // Add to line segments if unique
                currSeg = sg;
                lSegs.add(new LineSegment(sg.get(0), sg.get(sg.size() - 1)));
            }
        }

        // Set the array
        segments = lSegs.toArray(new LineSegment[lSegs.size()]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }

    private static class SegmentComparator implements Comparator<List<Point>> {
        @Override
        public int compare(List<Point> o1, List<Point> o2) {
            int comp = o1.get(0).compareTo(o2.get(0));
            if (comp == 0)
                return o1.get(o1.size() - 1).compareTo(o2.get(o2.size() - 1));
            return comp;
        }
    }
}
