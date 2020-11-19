package com.rymcu.forest.util;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;

/**
 * @author ronger
 */
public class Html2TextUtil extends HTMLEditorKit.ParserCallback {

    private static Html2TextUtil html2Text = new Html2TextUtil();

    private StringBuffer s;

    public Html2TextUtil() {
    }

    public void parse(String html) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Reader reader = new InputStreamReader(inputStream);
        ParserDelegator delegator = new ParserDelegator();
        s = new StringBuffer();
        // the third parameter is TRUE to ignore charset directive
        delegator.parse(reader, this, Boolean.TRUE);
    }

    @Override
    public void handleText(char[] text, int pos) {
        s.append(text);
    }

    public String getText() {
        return s.toString();
    }

    public static String getContent(String str) {
        try {
            html2Text.parse(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return html2Text.getText();
    }

}
