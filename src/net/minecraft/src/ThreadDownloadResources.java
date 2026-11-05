package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.minecraft.client.Minecraft;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ThreadDownloadResources extends Thread {
    public File resourcesFolder;
    private Minecraft mc;
    private boolean closing = false;

    public ThreadDownloadResources(File gameDir, Minecraft minecraft) {
        this.mc = minecraft;
        this.setName("Resource download thread");
        this.setDaemon(true);
        this.resourcesFolder = new File(gameDir, "resources/");
        if (!this.resourcesFolder.exists() && !this.resourcesFolder.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + this.resourcesFolder);
        }
    }

    @Override
    public void run() {
        try {
            URL url = new URL("http://s3.amazonaws.com/MinecraftResources/");
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
            Document document = documentbuilder.parse(url.openStream());
            NodeList nodelist = document.getElementsByTagName("Contents");

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < nodelist.getLength(); j++) {
                    Node node = nodelist.item(j);
                    if (node.getNodeType() == 1) {
                        Element element = (Element)node;
                        String s = ((Element)element.getElementsByTagName("Key").item(0)).getChildNodes().item(0).getNodeValue();
                        long k = Long.parseLong(((Element)element.getElementsByTagName("Size").item(0)).getChildNodes().item(0).getNodeValue());
                        if (k > 0L) {
                            this.downloadAndInstallResource(url, s, k, i);
                            if (this.closing) {
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception exception) {
            this.loadResource(this.resourcesFolder, "");
            exception.printStackTrace();
        }
    }

    public void reloadResources() {
        this.loadResource(this.resourcesFolder, "");
    }

    private void loadResource(File dir, String prefix) {
        File[] afile = dir.listFiles();

        for (int i = 0; i < afile.length; i++) {
            if (afile[i].isDirectory()) {
                this.loadResource(afile[i], prefix + afile[i].getName() + "/");
            } else {
                try {
                    this.mc.installResource(prefix + afile[i].getName(), afile[i]);
                } catch (Exception exception) {
                    System.out.println("Failed to add " + prefix + afile[i].getName());
                }
            }
        }
    }

    private void downloadAndInstallResource(URL url, String name, long size, int pass) {
        try {
            int i = name.indexOf("/");
            String s = name.substring(0, i);
            if (!s.equals("sound") && !s.equals("newsound")) {
                if (pass != 1) {
                    return;
                }
            } else if (pass != 0) {
                return;
            }

            File file1 = new File(this.resourcesFolder, name);
            if (!file1.exists() || file1.length() != size) {
                file1.getParentFile().mkdirs();
                String s1 = name.replaceAll(" ", "%20");
                this.downloadResource(new URL(url, s1), file1, size);
                if (this.closing) {
                    return;
                }
            }

            this.mc.installResource(name, file1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void downloadResource(URL url, File dst, long size) throws IOException {
        byte[] abyte = new byte[4096];
        DataInputStream datainputstream = new DataInputStream(url.openStream());
        DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(dst));
        int i = 0;

        while ((i = datainputstream.read(abyte)) >= 0) {
            dataoutputstream.write(abyte, 0, i);
            if (this.closing) {
                return;
            }
        }

        datainputstream.close();
        dataoutputstream.close();
    }

    public void closeMinecraft() {
        this.closing = true;
    }
}
