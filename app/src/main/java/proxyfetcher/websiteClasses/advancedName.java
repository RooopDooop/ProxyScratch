package proxyfetcher.websiteClasses;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import java.io.IOException;
import java.util.*;

public class advancedName {
    public ArrayList<String> fetchProxies() {
        WebClient client = new WebClient();
        ArrayList<String> arrProxyies = new ArrayList<>();

        try {
            client.getOptions().setCssEnabled(true);
            client.getOptions().setJavaScriptEnabled(false);
            String searchUrl = "https://advanced.name/freeproxy?page=1";


            HtmlPage page = client.getPage(searchUrl);
            page.getElementsByTagName("tr").forEach((tableRow) -> {
                //Insert variables here, latest IP, latest ports. populate them from where the println's are
                tableRow.getElementsByTagName("td").forEach((tableColumn) -> {

                    if (!tableColumn.getAttributeDirect("data-ip").isEmpty()) {
                        arrProxyies.add(new String((Base64.getDecoder().decode(tableColumn.getAttributeDirect("data-ip")))));
                    }

                    if (!tableColumn.getAttributeDirect("data-port").isEmpty()) {
                        //System.out.println(tableColumn.getAttributeDirect("data-port"));
                        //arrNew[arrNew.length] = tableColumn.getAttributeDirect("data-port");

                        arrProxyies.set((arrProxyies.size() - 1), arrProxyies.get((arrProxyies.size() - 1)) + ":" + new String((Base64.getDecoder().decode(tableColumn.getAttributeDirect("data-port")))));
                    }

                    if (tableColumn.getElementsByTagName("a").getLength() > 0) {
                        tableColumn.getElementsByTagName("a").forEach((tagElement) -> {
                            if (!tagElement.asNormalizedText().equals("ELITE")) {
                                System.out.println(arrProxyies.get((arrProxyies.size() - 1)) + " - " +tagElement.asNormalizedText());
                            }
                        });
                    }
                });
            });

            client.close();

        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }

        return arrProxyies;
    }
}
