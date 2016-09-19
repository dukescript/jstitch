package com.dukescript.jstitch;

import net.java.html.js.JavaScriptBody;
import net.java.html.js.JavaScriptResource;

/** Use {@link JavaScriptBody} annotation on methods to
 * directly interact with JavaScript. See
 * http://bits.netbeans.org/html+java/1.2/net/java/html/js/package-summary.html
 * to understand how.
 */


@JavaScriptResource("jquery-1.11.0.min.js")
public final class Dialogs {
    private Dialogs() {
    }
    
    /** Shows confirmation dialog to the user.
     * 
     * @param msg the message
     * @param callback called back when the use accepts (can be null)
     */
    @JavaScriptBody(args = {},body="")
    public static native void init();
    
    
    @JavaScriptBody(
        args = { "msg", "callback" }, 
        javacall = true, 
        body = "if (confirm(msg)) {\n"
             + "  callback.@java.lang.Runnable::run()();\n"
             + "}\n"
    )
    public static native void confirmByUser(String msg, Runnable callback);
    
    @JavaScriptBody(
        args = {}, body = 
        "var w = window,\n" +
        "    d = document,\n" +
        "    e = d.documentElement,\n" +
        "    g = d.getElementsByTagName('body')[0],\n" +
        "    x = w.innerWidth || e.clientWidth || g.clientWidth,\n" +
        "    y = w.innerHeight|| e.clientHeight|| g.clientHeight;\n" +
        "\n" +
        "return 'Screen size is ' + x + ' times ' + y;\n"
    )
    public static native String screenSize();
    
    @JavaScriptBody(
        args = {}, body = 
                "return window.innerWidth;")
    
    public static native int getSreenWidth();
    
        @JavaScriptBody(
        args = {}, body = 
                "return window.innerHeight;")
    
    public static native int getSreenHeight();
    
        @JavaScriptBody(
        args = {}, body = 
                "var a = document.getElementById('controllsDiv'); \n" +
                "return a.clientHeight;")
    
    public static native int getGuiHeight();
    
        @JavaScriptBody(
        args = {}, body = 
                "var a = document.getElementById('controllsDiv'); \n" +
                "return a.clientWidth;")
    
    public static native int getGuiWidth();
    
    
        @JavaScriptBody(args = {}, body = "ko.bindingHandlers['mousepressed'] = {\n"
            + "    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {\n"
            + "        var allBindings = allBindingsAccessor();\n"
            + "        $(element).mousemove(function (event) {\n"
            + "             if (event.which==1){"
            + "             var el = window.document.getElementById('myCanvas');"
            + "             var rect = el.getBoundingClientRect();"
            + "             event.realX = event.clientX - rect.left;"                
            + "             event.realY = event.clientY -rect.top;"                
            + "             allBindings['mousepressed'].call(viewModel,null, event);\n"
            + "            return false;}"
            + "            return true;\n"
            + "        });\n"
            + "    }\n"
            + "};")
    public static native void registerMouseBinding();
    
    
    
            @JavaScriptBody(args = {}, body = "ko.bindingHandlers['customMouseover'] = {\n"
            + "    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {\n"
            + "        var allBindings = allBindingsAccessor();\n"
            + "        $(element).mousemove(function (event) {\n"
            + "             var el = window.document.getElementById('myCanvas');"
            + "             var rect = el.getBoundingClientRect();"
            + "             event.realX = event.clientX - rect.left;"                
            + "             event.realY = event.clientY -rect.top;"                
            + "             allBindings['customMouseover'].call(viewModel,null, event);\n"
            + "            return false;"
            + "        });\n"
            + "    }\n"
            + "};")
    public static native void registerMouseBinding2();
    
    

    
    

    
}
