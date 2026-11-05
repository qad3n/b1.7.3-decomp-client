package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

public class ThreadDownloadImageData {
    public BufferedImage image;
    public int referenceCount = 1;
    public int textureName = -1;
    public boolean textureSetupComplete = false;

    public ThreadDownloadImageData(final String url, final ImageBuffer processor) {
        new Thread(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL uRL = new URL(url);
                    httpURLConnection = (HttpURLConnection)uRL.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(false);
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode() / 100 == 4) {
                        return;
                    }
                    ThreadDownloadImageData.this.image = processor == null ? ImageIO.read(httpURLConnection.getInputStream()) : processor.parseUserSkin(ImageIO.read(httpURLConnection.getInputStream()));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                finally {
                    httpURLConnection.disconnect();
                }
            }
        }.start();
    }
}

