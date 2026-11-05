package net.minecraft.src;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class J_JdomParser {
    public J_JsonRootNode parse(Reader json) throws IOException, J_InvalidSyntaxException {
        J_JsonListenerToJdomAdapter j_JsonListenerToJdomAdapter = new J_JsonListenerToJdomAdapter();
        new J_SajParser().parse(json, j_JsonListenerToJdomAdapter);
        return j_JsonListenerToJdomAdapter.getDocument();
    }

    public J_JsonRootNode parse(String json) throws J_InvalidSyntaxException {
        J_JsonRootNode j_JsonRootNode;
        try {
            j_JsonRootNode = this.parse(new StringReader(json));
        }
        catch (IOException iOException) {
            throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", iOException);
        }
        return j_JsonRootNode;
    }
}

