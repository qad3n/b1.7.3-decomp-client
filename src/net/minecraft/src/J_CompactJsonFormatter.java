package net.minecraft.src;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.TreeSet;

public final class J_CompactJsonFormatter
implements J_JsonFormatter {
    public String format(J_JsonRootNode json) {
        StringWriter stringWriter = new StringWriter();
        try {
            this.format(json, (Writer)stringWriter);
        }
        catch (IOException iOException) {
            throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", iOException);
        }
        return stringWriter.toString();
    }

    public void format(J_JsonRootNode jsonNode, Writer writer) throws IOException {
        this.format((J_JsonNode)jsonNode, writer);
    }

    private void format(J_JsonNode jsonNode, Writer writer) throws IOException {
        boolean bl = true;
        switch (jsonNode.getType()) {
            case ARRAY: {
                writer.append('[');
                for (J_JsonNode j_JsonNode : jsonNode.getElements()) {
                    if (!bl) {
                        writer.append(',');
                    }
                    bl = false;
                    this.format(j_JsonNode, writer);
                }
                writer.append(']');
                break;
            }
            case OBJECT: {
                writer.append('{');
                for (J_JsonStringNode j_JsonStringNode : new TreeSet<J_JsonStringNode>(jsonNode.getFields().keySet())) {
                    if (!bl) {
                        writer.append(',');
                    }
                    bl = false;
                    this.format(j_JsonStringNode, writer);
                    writer.append(':');
                    this.format(jsonNode.getFields().get(j_JsonStringNode), writer);
                }
                writer.append('}');
                break;
            }
            case STRING: {
                writer.append('\"').append(new J_JsonEscapedString(jsonNode.getText()).toString()).append('\"');
                break;
            }
            case NUMBER: {
                writer.append(jsonNode.getText());
                break;
            }
            case FALSE: {
                writer.append("false");
                break;
            }
            case TRUE: {
                writer.append("true");
                break;
            }
            case NULL: {
                writer.append("null");
                break;
            }
            default: {
                throw new RuntimeException("Coding failure in Argo:  Attempt to format a JsonNode of unknown type [" + (Object)((Object)jsonNode.getType()) + "];");
            }
        }
    }

    static final class J_JsonEscapedString {
        private final String text;

        J_JsonEscapedString(String text) {
            this.text = text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
        }

        public String toString() {
            return this.text;
        }
    }
}

