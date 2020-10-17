package net.auroramc.core.api.utils;

import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UUIDUtil {

    /**
     * @param player The player
     * @return The UUID of the given player
     */
    //Uncomment this if you want the helper method for BungeeCord:
	/*
	public static UUID getUUID(ProxiedPlayer player) {
		return getUUID(player.getName());
	}
	*/

    /**
     * @param player The player
     * @return The UUID of the given player
     */
    //Uncomment this if you want the helper method for Bukkit/Spigot:
    public static UUID getUUID(Player player) {
        return getUUID(player.getName());
    }


    /**
     * @param playername The name of the player
     * @return The UUID of the given player
     */
    public static UUID getUUID(String playername) {

        try {
            String output = callURL("https://api.mojang.com/profiles/minecraft","[\"" + playername + "\"]");

            StringBuilder result = new StringBuilder();

            readData(output, result, playername);
            if (result.toString().equals("")) {
                return null;
            }

            String u = result.toString();
            String uuid = "";

            for (int i = 0; i <= 31; i++) {
                uuid = uuid + u.charAt(i);
                if (i == 7 || i == 11 || i == 15 || i == 19) {
                    uuid = uuid + "-";
                }
            }

            return UUID.fromString(uuid);
        } catch (Exception e) {
            return null;
        }
    }

    private static void readData(String toRead, StringBuilder result, String playerName) {
        try {
            JSONArray jsonObject = new JSONArray(toRead);
            JSONObject object = jsonObject.getJSONObject(0);
            result.append(object.get("id"));
        } catch (Exception ignored) {
        }
    }

    public static String callURL(String URL, String payload) {
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            java.net.URL url = new URL(URL);
            urlConn = url.openConnection();

            if (urlConn != null) {
                urlConn.setReadTimeout(60 * 1000);
                if (payload != null) {
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    HttpURLConnection con = (HttpURLConnection) urlConn;
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8);
                    writer.write(payload);
                    writer.close();
                }
            }

            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);

                if (bufferedReader != null) {
                    int cp;

                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }

                    bufferedReader.close();
                }
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
