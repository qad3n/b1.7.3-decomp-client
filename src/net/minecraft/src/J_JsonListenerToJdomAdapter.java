package net.minecraft.src;

import java.util.Stack;

final class J_JsonListenerToJdomAdapter
implements J_JsonListener {
    private final Stack<J_NodeContainer> stack = new Stack();
    private J_JsonNodeBuilder root;

    J_JsonListenerToJdomAdapter() {
    }

    J_JsonRootNode getDocument() {
        return (J_JsonRootNode)this.root.build();
    }

    public void startDocument() {
    }

    public void endDocument() {
    }

    public void startArray() {
        final J_JsonArrayNodeBuilder j_JsonArrayNodeBuilder = J_JsonNodeBuilders.anArrayBuilder();
        this.addRootNode(j_JsonArrayNodeBuilder);
        this.stack.push(new J_NodeContainer(){

            @Override
            public void addNode(J_JsonNodeBuilder node) {
                j_JsonArrayNodeBuilder.withElement(node);
            }

            @Override
            public void addField(J_JsonFieldBuilder field) {
                throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
            }
        });
    }

    public void endArray() {
        this.stack.pop();
    }

    public void startObject() {
        final J_JsonObjectNodeBuilder j_JsonObjectNodeBuilder = J_JsonNodeBuilders.anObjectBuilder();
        this.addRootNode(j_JsonObjectNodeBuilder);
        this.stack.push(new J_NodeContainer(){

            @Override
            public void addNode(J_JsonNodeBuilder node) {
                throw new RuntimeException("Coding failure in Argo:  Attempt to add a node to an object.");
            }

            @Override
            public void addField(J_JsonFieldBuilder field) {
                j_JsonObjectNodeBuilder.withField(field);
            }
        });
    }

    public void endObject() {
        this.stack.pop();
    }

    public void startField(String name) {
        final J_JsonFieldBuilder j_JsonFieldBuilder = J_JsonFieldBuilder.aJsonFieldBuilder().withKey(J_JsonNodeBuilders.aStringBuilder(name));
        this.stack.peek().addField(j_JsonFieldBuilder);
        this.stack.push(new J_NodeContainer(){

            @Override
            public void addNode(J_JsonNodeBuilder node) {
                j_JsonFieldBuilder.withValue(node);
            }

            @Override
            public void addField(J_JsonFieldBuilder field) {
                throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to a field.");
            }
        });
    }

    public void endField() {
        this.stack.pop();
    }

    public void numberValue(String value) {
        this.addValue(J_JsonNodeBuilders.aNumberBuilder(value));
    }

    public void trueValue() {
        this.addValue(J_JsonNodeBuilders.aTrueBuilder());
    }

    public void stringValue(String value) {
        this.addValue(J_JsonNodeBuilders.aStringBuilder(value));
    }

    public void falseValue() {
        this.addValue(J_JsonNodeBuilders.aFalseBuilder());
    }

    public void nullValue() {
        this.addValue(J_JsonNodeBuilders.aNullBuilder());
    }

    private void addRootNode(J_JsonNodeBuilder node) {
        if (this.root == null) {
            this.root = node;
        } else {
            this.addValue(node);
        }
    }

    private void addValue(J_JsonNodeBuilder node) {
        this.stack.peek().addNode(node);
    }
}

