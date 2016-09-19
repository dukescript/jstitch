/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.jstitch.js;

import com.dukescript.jstitch.Vector;
import java.util.ArrayList;
import java.util.List;
import net.java.html.js.JavaScriptBody;
import net.java.html.js.JavaScriptResource;

/**
 *
 * @author antonepple
 */
@JavaScriptResource("raphael.js")
public class SVGPathTool {

    @JavaScriptBody(args = {}, body = "return document.getElementsByTagName('path');")
    public static native Object getPaths_impl();

    public static List<Path> getPaths() {
        List<Path> result = new ArrayList<>();
        Object paths = getPaths_impl();
        int length = getLength_impl(paths);
        System.out.println("length " + length);
        for (int i = 0; i < length; i++) {
            result.add(getPathElement(paths, i));

        }
        return result;
    }

    @JavaScriptBody(args = {"paths"}, body = "return paths.length")
    public static native int getLength_impl(Object paths);

    private static Path getPathElement(Object paths, int i) {
        return new Path(getPathElement_impl(paths, i));
    }

    @JavaScriptBody(args = {"paths", "i"}, body = "return paths[i];")
    public static native Object getPathElement_impl(Object paths, int i);

    public static class Path {

        private final Object delegate;

        public Path(Object delegate) {
            this.delegate = delegate;
        }

        public String getPath() {
            return getPath_impl(delegate);
        }

        @JavaScriptBody(args = {"delegate"}, body = "return delegate.getAttribute('d');")
        private static native String getPath_impl(Object delegate);

        public List<Segment> getSegments() {
            List<Segment> result = new ArrayList<>();
            Object segments = getSegments_impl(delegate);
            int length = SVGPathTool.getLength_impl(segments);

            for (int i = 0; i < length; i++) {
                Object el = getPathElement_impl(segments, i);
                String type_impl = getType_impl(el);
                if (type_impl.trim().equals("M")) {
                    MoveTo moveTo = new MoveTo(el);
                    result.add(moveTo);
                } else if (type_impl.trim().equals("C")) {
                    CurveTo segment = new CurveTo(el);
                    result.add(segment);
                }
                else {
                    System.out.println("UNKNOWN ELEMENT "+type_impl);
                }
            }
            return result;
        }

        @JavaScriptBody(args = {"delegate"}, body = "return delegate[0];")
        static native String getType_impl(Object delegate);

        @JavaScriptBody(args = {"paths", "i"}, body = "return paths[i];")
        public static native Object getPathElement_impl(Object paths, int i);

        @JavaScriptBody(args = {"path"}, body = "return Raphael.path2curve( path.getAttribute('d'));")
        private static native Object getSegments_impl(Object path);

        public static class Segment {

            private final Object delegate;
            private final Vector p0;

            public Segment(Object delegate) {
                this.delegate = delegate;
                double x = getNum_impl(delegate, 1);
                double y = getNum_impl(delegate, 2);
                p0 = new Vector(x, y);
            }

            public Vector getP0() {
                return p0;
            }

            @JavaScriptBody(args = {"delegate", "i"}, body = "return delegate[i];")
            static native double getNum_impl(Object delegate, int i);

        }

        public static class MoveTo extends Segment {

            public MoveTo(Object delegate) {
                super(delegate);
            }

        }

        public static class CurveTo extends Segment {

            private final Vector p1, p2;

            public CurveTo(Object delegate) {
                super(delegate);
                p1 = new Vector(getNum_impl(delegate, 3), getNum_impl(delegate, 4));
                p2 = new Vector(getNum_impl(delegate, 5), getNum_impl(delegate, 6));
            }

            public Vector getP1() {
                return p1;
            }

            public Vector getP2() {
                return p2;
            }

        }
    }

}
