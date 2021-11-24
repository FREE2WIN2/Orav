package de.logilutions.orav.discord;

import java.awt.*;
import java.io.IOException;

public class DiscordUtil {
    private DiscordWebhook discordWebhook;

    public DiscordUtil(String url) {
        discordWebhook = new DiscordWebhook(url);
    }

    public void send(String title, String description, String image, Color color, String authorName, String authorUrl, String authorImage) {
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        if (title != null) embedObject.setTitle(title);
        if (description != null) embedObject.setDescription(description);
        if (image != null) embedObject.setImage(image);
        if (color != null) embedObject.setColor(color);

        if (authorImage != null || authorUrl != null || authorName != null) {
            if (authorName == null) authorName = "";
            if (authorUrl == null) authorUrl = "";
            if (authorImage == null) authorImage = "";
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
