package net.minecraft.src;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public final class J_SajParser {
    public void parse(Reader in, J_JsonListener jsonListener) throws IOException, J_InvalidSyntaxException {
        J_PositionTrackingPushbackReader j_PositionTrackingPushbackReader = new J_PositionTrackingPushbackReader(in);
        char c = (char)j_PositionTrackingPushbackReader.read();
        switch (c) {
            case '{': {
                j_PositionTrackingPushbackReader.unread(c);
                jsonListener.startDocument();
                this.objectString(j_PositionTrackingPushbackReader, jsonListener);
                break;
            }
            case '[': {
                j_PositionTrackingPushbackReader.unread(c);
                jsonListener.startDocument();
                this.arrayString(j_PositionTrackingPushbackReader, jsonListener);
                break;
            }
            default: {
                throw new J_InvalidSyntaxException("Expected either [ or { but got [" + c + "].", j_PositionTrackingPushbackReader);
            }
        }
        int n = this.readNextNonWhitespaceChar(j_PositionTrackingPushbackReader);
        if (n != -1) {
            throw new J_InvalidSyntaxException("Got unexpected trailing character [" + (char)n + "].", j_PositionTrackingPushbackReader);
        }
        jsonListener.endDocument();
    }

    private void arrayString(J_PositionTrackingPushbackReader pushbackReader, J_JsonListener jsonListener) throws IOException, J_InvalidSyntaxException {
        char c0 = (char) this.readNextNonWhitespaceChar(pushbackReader);
        if (c0 != '[') {
            throw new J_InvalidSyntaxException("Expected object to start with [ but got [" + c0 + "].", pushbackReader);
        }
        jsonListener.startArray();
        char c1 = (char) this.readNextNonWhitespaceChar(pushbackReader);
        pushbackReader.unread(c1);
        if (c1 != ']') {
            this.aJsonValue(pushbackReader, jsonListener);
        }
        boolean flag = false;
        while (!flag) {
            char c2 = (char) this.readNextNonWhitespaceChar(pushbackReader);
            switch(c2) {
                case ',':
                    this.aJsonValue(pushbackReader, jsonListener);
                    break;
                case ']':
                    flag = true;
                    break;
                default:
                    throw new J_InvalidSyntaxException("Expected either , or ] but got [" + c2 + "].", pushbackReader);
                }
        }
        jsonListener.endArray();
    }

    private void objectString(J_PositionTrackingPushbackReader pushbackReader, J_JsonListener jsonListener) throws IOException, J_InvalidSyntaxException {
        char c0 = (char) this.readNextNonWhitespaceChar(pushbackReader);
        if (c0 != '{') {
            throw new J_InvalidSyntaxException("Expected object to start with { but got [" + c0 + "].", pushbackReader);
        }
        jsonListener.startObject();
        char c1 = (char) this.readNextNonWhitespaceChar(pushbackReader);
        pushbackReader.unread(c1);
        if (c1 != '}') {
            this.aFieldToken(pushbackReader, jsonListener);
        }
        boolean flag = false;
        while (!flag) {
            char c2 = (char) this.readNextNonWhitespaceChar(pushbackReader);
            switch(c2) {
                case ',':
                    this.aFieldToken(pushbackReader, jsonListener);
                    break;
                case '}':
                    flag = true;
                    break;
                default:
                    throw new J_InvalidSyntaxException("Expected either , or } but got [" + c2 + "].", pushbackReader);
                }
        }
        jsonListener.endObject();
    }

    private void aFieldToken(J_PositionTrackingPushbackReader pushbackReader, J_JsonListener jsonListener) throws IOException, J_InvalidSyntaxException {
        char c = (char)this.readNextNonWhitespaceChar(pushbackReader);
        if ('\"' != c) {
            throw new J_InvalidSyntaxException("Expected object identifier to begin with [\"] but got [" + c + "].", pushbackReader);
        }
        pushbackReader.unread(c);
        jsonListener.startField(this.stringToken(pushbackReader));
        char c2 = (char)this.readNextNonWhitespaceChar(pushbackReader);
        if (c2 != ':') {
            throw new J_InvalidSyntaxException("Expected object identifier to be followed by : but got [" + c2 + "].", pushbackReader);
        }
        this.aJsonValue(pushbackReader, jsonListener);
        jsonListener.endField();
    }

    private void aJsonValue(J_PositionTrackingPushbackReader pushbackReader, J_JsonListener jsonListener) throws IOException, J_InvalidSyntaxException {
        char c = (char)this.readNextNonWhitespaceChar(pushbackReader);
        switch (c) {
            case '\"': {
                pushbackReader.unread(c);
                jsonListener.stringValue(this.stringToken(pushbackReader));
                break;
            }
            case 't': {
                char[] cArray = new char[3];
                int n = pushbackReader.read(cArray);
                if (n != 3 || cArray[0] != 'r' || cArray[1] != 'u' || cArray[2] != 'e') {
                    pushbackReader.uncount(cArray);
                    throw new J_InvalidSyntaxException("Expected 't' to be followed by [[r, u, e]], but got [" + Arrays.toString(cArray) + "].", pushbackReader);
                }
                jsonListener.trueValue();
                break;
            }
            case 'f': {
                char[] cArray = new char[4];
                int n = pushbackReader.read(cArray);
                if (n != 4 || cArray[0] != 'a' || cArray[1] != 'l' || cArray[2] != 's' || cArray[3] != 'e') {
                    pushbackReader.uncount(cArray);
                    throw new J_InvalidSyntaxException("Expected 'f' to be followed by [[a, l, s, e]], but got [" + Arrays.toString(cArray) + "].", pushbackReader);
                }
                jsonListener.falseValue();
                break;
            }
            case 'n': {
                char[] cArray = new char[3];
                int n = pushbackReader.read(cArray);
                if (n != 3 || cArray[0] != 'u' || cArray[1] != 'l' || cArray[2] != 'l') {
                    pushbackReader.uncount(cArray);
                    throw new J_InvalidSyntaxException("Expected 'n' to be followed by [[u, l, l]], but got [" + Arrays.toString(cArray) + "].", pushbackReader);
                }
                jsonListener.nullValue();
                break;
            }
            case '-': 
            case '0': 
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': {
                pushbackReader.unread(c);
                jsonListener.numberValue(this.numberToken(pushbackReader));
                break;
            }
            case '{': {
                pushbackReader.unread(c);
                this.objectString(pushbackReader, jsonListener);
                break;
            }
            case '[': {
                pushbackReader.unread(c);
                this.arrayString(pushbackReader, jsonListener);
                break;
            }
            default: {
                throw new J_InvalidSyntaxException("Invalid character at start of value [" + c + "].", pushbackReader);
            }
        }
    }

    private String numberToken(J_PositionTrackingPushbackReader pushbackReader) throws IOException, J_InvalidSyntaxException {
        StringBuilder stringBuilder = new StringBuilder();
        char c = (char)pushbackReader.read();
        if ('-' == c) {
            stringBuilder.append('-');
        } else {
            pushbackReader.unread(c);
        }
        stringBuilder.append(this.nonNegativeNumberToken(pushbackReader));
        return stringBuilder.toString();
    }

    private String nonNegativeNumberToken(J_PositionTrackingPushbackReader pushbackReader) throws IOException, J_InvalidSyntaxException {
        StringBuilder stringBuilder = new StringBuilder();
        char c = (char)pushbackReader.read();
        if ('0' == c) {
            stringBuilder.append('0');
            stringBuilder.append(this.possibleFractionalComponent(pushbackReader));
            stringBuilder.append(this.possibleExponent(pushbackReader));
        } else {
            pushbackReader.unread(c);
            stringBuilder.append(this.nonZeroDigitToken(pushbackReader));
            stringBuilder.append(this.digitString(pushbackReader));
            stringBuilder.append(this.possibleFractionalComponent(pushbackReader));
            stringBuilder.append(this.possibleExponent(pushbackReader));
        }
        return stringBuilder.toString();
    }

    private char nonZeroDigitToken(J_PositionTrackingPushbackReader pushbackReader) throws IOException, J_InvalidSyntaxException {
        char c;
        char c2 = (char)pushbackReader.read();
        switch (c2) {
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': {
                c = c2;
                break;
            }
            default: {
                throw new J_InvalidSyntaxException("Expected a digit 1 - 9 but got [" + c2 + "].", pushbackReader);
            }
        }
        return c;
    }

    private char digitToken(J_PositionTrackingPushbackReader pushbackReader) throws IOException, J_InvalidSyntaxException {
        char c;
        char c2 = (char)pushbackReader.read();
        switch (c2) {
            case '0': 
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': {
                c = c2;
                break;
            }
            default: {
                throw new J_InvalidSyntaxException("Expected a digit 1 - 9 but got [" + c2 + "].", pushbackReader);
            }
        }
        return c;
    }

    private String digitString(J_PositionTrackingPushbackReader pushbackReader) throws IOException {
        StringBuilder stringbuilder = new StringBuilder();
        boolean flag = false;
        while (!flag) {
            char c0 = (char) pushbackReader.read();
            switch(c0) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    stringbuilder.append(c0);
                    break;
                default:
                    flag = true;
                    pushbackReader.unread(c0);
                }
        }
        return stringbuilder.toString();
    }

    private String possibleFractionalComponent(J_PositionTrackingPushbackReader pushbackReader) throws IOException, J_InvalidSyntaxException {
        StringBuilder stringBuilder = new StringBuilder();
        char c = (char)pushbackReader.read();
        if (c == '.') {
            stringBuilder.append('.');
            stringBuilder.append(this.digitToken(pushbackReader));
            stringBuilder.append(this.digitString(pushbackReader));
        } else {
            pushbackReader.unread(c);
        }
        return stringBuilder.toString();
    }

    private String possibleExponent(J_PositionTrackingPushbackReader pushbackReader) throws IOException, J_InvalidSyntaxException {
        StringBuilder stringBuilder = new StringBuilder();
        char c = (char)pushbackReader.read();
        if (c == '.' || c == 'E') {
            stringBuilder.append('E');
            stringBuilder.append(this.possibleSign(pushbackReader));
            stringBuilder.append(this.digitToken(pushbackReader));
            stringBuilder.append(this.digitString(pushbackReader));
        } else {
            pushbackReader.unread(c);
        }
        return stringBuilder.toString();
    }

    private String possibleSign(J_PositionTrackingPushbackReader pushbackReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        char c = (char)pushbackReader.read();
        if (c == '+' || c == '-') {
            stringBuilder.append(c);
        } else {
            pushbackReader.unread(c);
        }
        return stringBuilder.toString();
    }

    private String stringToken(J_PositionTrackingPushbackReader pushbackReader) throws IOException, J_InvalidSyntaxException {
        StringBuilder stringbuilder = new StringBuilder();
        char c0 = (char) pushbackReader.read();
        if ('"' != c0) {
            throw new J_InvalidSyntaxException("Expected [\"] but got [" + c0 + "].", pushbackReader);
        }
        boolean flag = false;
        while (!flag) {
            char c1 = (char) pushbackReader.read();
            switch(c1) {
                case '"':
                    flag = true;
                    break;
                case '\\':
                    char c2 = this.escapedStringChar(pushbackReader);
                    stringbuilder.append(c2);
                    break;
                default:
                    stringbuilder.append(c1);
                }
        }
        return stringbuilder.toString();
    }

    private char escapedStringChar(J_PositionTrackingPushbackReader pushbackReader) throws IOException, J_InvalidSyntaxException {
        char c;
        char c2 = (char)pushbackReader.read();
        switch (c2) {
            case '\"': {
                c = '\"';
                break;
            }
            case '\\': {
                c = '\\';
                break;
            }
            case '/': {
                c = '/';
                break;
            }
            case 'b': {
                c = '\b';
                break;
            }
            case 'f': {
                c = '\f';
                break;
            }
            case 'n': {
                c = '\n';
                break;
            }
            case 'r': {
                c = '\r';
                break;
            }
            case 't': {
                c = '\t';
                break;
            }
            case 'u': {
                c = (char)this.hexadecimalNumber(pushbackReader);
                break;
            }
            default: {
                throw new J_InvalidSyntaxException("Unrecognised escape character [" + c2 + "].", pushbackReader);
            }
        }
        return c;
    }

    private int hexadecimalNumber(J_PositionTrackingPushbackReader pushbackReader) throws IOException, J_InvalidSyntaxException {
        int n;
        char[] cArray = new char[4];
        int n2 = pushbackReader.read(cArray);
        if (n2 != 4) {
            throw new J_InvalidSyntaxException("Expected a 4 digit hexidecimal number but got only [" + n2 + "], namely [" + String.valueOf(cArray, 0, n2) + "].", pushbackReader);
        }
        try {
            n = Integer.parseInt(String.valueOf(cArray), 16);
        }
        catch (NumberFormatException numberFormatException) {
            pushbackReader.uncount(cArray);
            throw new J_InvalidSyntaxException("Unable to parse [" + String.valueOf(cArray) + "] as a hexidecimal number.", numberFormatException, pushbackReader);
        }
        return n;
    }

    private int readNextNonWhitespaceChar(J_PositionTrackingPushbackReader pushbackReader) throws IOException {
        int n;
        boolean bl = false;
        do {
            n = pushbackReader.read();
            switch (n) {
                case 9: 
                case 10: 
                case 13: 
                case 32: {
                    break;
                }
                default: {
                    bl = true;
                }
            }
        } while (!bl);
        return n;
    }
}

