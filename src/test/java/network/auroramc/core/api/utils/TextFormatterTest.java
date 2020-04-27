package network.auroramc.core.api.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextFormatterTest {

    @Test
    public void highlighter() {
        TextFormatter formatter = new TextFormatter();
        String highlightedMessage = formatter.highlight("**I am highlighted** and I am not");
        assertEquals("Highlighter failed, text was not equal.", "&bI am highlighted&r and I am not", highlightedMessage);
    }

    @Test
    public void pluginMessager() {
        TextFormatter formatter = new TextFormatter();
        String noPrefix = formatter.pluginMessage(null, "omg what a legend");
        String noPrefixBlank = formatter.pluginMessage("", "omg what a legend");
        String prefix = formatter.pluginMessage("legend", "omg what a legend");
        String noPrefixHighlight = formatter.pluginMessage(null, "**omg** what a legend");
        String prefixHighlight = formatter.pluginMessage("LeGeNd", "**omg** what a legend");

        assertEquals("No Prefix Null didn't match.","§romg what a legend",noPrefix);
        assertEquals("No Prefix Blank didn't match.","§romg what a legend",noPrefixBlank);
        assertEquals("Prefix didn't match.","§3§l«LEGEND» §romg what a legend",prefix);
        assertEquals("Prefix Highlight didn't match.","§r§bomg§r what a legend",noPrefixHighlight);
        assertEquals("Prefix didn't match.","§3§l«LEGEND» §r§bomg§r what a legend",prefixHighlight);
    }

}
