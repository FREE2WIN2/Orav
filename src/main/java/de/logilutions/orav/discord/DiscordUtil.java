package de.logilutions.orav.discord;

import java.awt.*;
import java.io.IOException;

public class DiscordUtil {
    private DiscordWebhook discordWebhook;

    public DiscordUtil(String url) {
        discordWebhook = new DiscordWebhook(url);
    }

    public void send(String title, String description, String image, Color color, String authorName, String authorImage, String authorUrl) {
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        embedObject.setTitle(title);
        embedObject.setDescription(description);
        embedObject.setImage(image);
        embedObject.setColor(color);
        if (authorImage.equals("") || authorName.equals("")) {
            embedObject.setAuthor(authorName, authorUrl, authorImage);
        }
        discordWebhook.addEmbed(embedObject);
        try {
            discordWebhook.execute();
            System.out.println("send messsage to discord webhook");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
