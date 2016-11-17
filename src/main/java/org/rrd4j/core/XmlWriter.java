package org.rrd4j.core;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Stack;

/**
 * Extremely simple utility class used to create XML documents.
 */
public class XmlWriter {
    static final String INDENT_STR = "   ";
    private static final String STYLE = "style";

    private final PrintWriter writer;
    private final StringBuilder indent = new StringBuilder("");
    private final Stack<String> openTags = new Stack<String>();

    /**
     * Creates XmlWriter with the specified output stream to send XML code to.
     *
     * @param stream Output stream which receives XML code
     */
    public XmlWriter(OutputStream stream) {
        writer = new PrintWriter(stream, true);
    }

    /**
     * Creates XmlWriter with the specified output stream to send XML code to.
     *
     * @param stream Output stream which receives XML code
     * @param autoFlush is the stream to be flushed automatically
     */
    public XmlWriter(OutputStream stream, boolean autoFlush) {
        writer = new PrintWriter(stream, autoFlush);
    }

    /**
     * Opens XML tag
     *
     * @param tag XML tag name
     */
    public void startTag(String tag) {
        writer.println(indent + "<" + tag + ">");
        openTags.push(tag);
        indent.append(INDENT_STR);
    }

    /**
     * Closes the corresponding XML tag
     */
    public void closeTag() {
        String tag = openTags.pop();
        indent.setLength(indent.length() - INDENT_STR.length());
        writer.println(indent + "</" + tag + ">");
    }

    /**
     * Writes &lt;tag&gt;value&lt;/tag&gt; to output stream
     *
     * @param tag   XML tag name
     * @param value value to be placed between <code>&lt;tag&gt;</code> and <code>&lt;/tag&gt;</code>
     */
    public void writeTag(String tag, Object value) {
        if (value != null) {
            writer.println(indent + "<" + tag + ">" +
                    escape(value.toString()) + "</" + tag + ">");
        }
        else {
            writer.println(indent + "<" + tag + "></" + tag + ">");
        }
    }

    /**
     * Writes &lt;tag&gt;value&lt;/tag&gt; to output stream
     *
     * @param tag   XML tag name
     * @param value value to be placed between <code>&lt;tag&gt;</code> and <code>&lt;/tag&gt;</code>
     */
    public void writeTag(String tag, int value) {
        writeTag(tag, Integer.toString(value));
    }

    /**
     * Writes &lt;tag&gt;value&lt;/tag&gt; to output stream
     *
     * @param tag   XML tag name
     * @param value value to be placed between <code>&lt;tag&gt;</code> and <code>&lt;/tag&gt;</code>
     */
    public void writeTag(String tag, long value) {
        writeTag(tag, Long.toString(value));
    }

    /**
     * Writes &lt;tag&gt;value&lt;/tag&gt; to output stream
     *
     * @param tag   XML tag name
     * @param value value to be placed between <code>&lt;tag&gt;</code> and <code>&lt;/tag&gt;</code>
     * @param nanString a {@link java.lang.String} object.
     */
    public void writeTag(String tag, double value, String nanString) {
        writeTag(tag, Util.formatDouble(value, nanString, true));
    }

    /**
     * Writes &lt;tag&gt;value&lt;/tag&gt; to output stream
     *
     * @param tag   XML tag name
     * @param value value to be placed between <code>&lt;tag&gt;</code> and <code>&lt;/tag&gt;</code>
     */
    public void writeTag(String tag, double value) {
        writeTag(tag, Util.formatDouble(value, true));
    }

    /**
     * Writes &lt;tag&gt;value&lt;/tag&gt; to output stream
     *
     * @param tag   XML tag name
     * @param value value to be placed between <code>&lt;tag&gt;</code> and <code>&lt;/tag&gt;</code>
     */
    public void writeTag(String tag, boolean value) {
        writeTag(tag, Boolean.toString(value));
    }




    /**
     * Writes &lt;tag&gt;value&lt;/tag&gt; to output stream
     *
     * @param tag   XML tag name
     * @param value value to be placed between <code>&lt;tag&gt;</code> and <code>&lt;/tag&gt;</code>
     */
    public void writeTag(String tag, File value) {
        writeTag(tag, value.getPath());
    }

    /**
     * Flushes the output stream
     */
    public void flush() {
        writer.flush();
    }

    /**
     * Writes XML comment to output stream
     *
     * @param comment comment string
     */
    public void writeComment(Object comment) {
        writer.println(indent + "<!-- " + escape(comment.toString()) + " -->");
	}

	private static String escape(String s) {
		return s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
}
