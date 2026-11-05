package net.minecraft.src;

public interface J_JsonListener {
    public void startDocument();

    public void endDocument();

    public void startArray();

    public void endArray();

    public void startObject();

    public void endObject();

    public void startField(String var1);

    public void endField();

    public void stringValue(String var1);

    public void numberValue(String var1);

    public void trueValue();

    public void falseValue();

    public void nullValue();
}

