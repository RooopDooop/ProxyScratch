package proxyfetcher.websiteClasses;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import java.io.IOException;
import java.util.*;  

public class advancedName {
    public class proxyInstance {
        String IP;
        String Port;
        ArrayList<String> Descriptors = new ArrayList<>();
        String Location;
        int Speed;
        long LastCheckup;

        public String serveAddress() {
            return this.IP + ":" + this.Port;
        }
    }

    public ArrayList<proxyInstance> fetchProxies() {
        WebClient client = new WebClient();
        ArrayList<proxyInstance> arrProxies = new ArrayList<>();

        try {
            client.getOptions().setCssEnabled(true);
            client.getOptions().setJavaScriptEnabled(false);
            String searchUrl = "https://advanced.name/freeproxy?page=1";

            HtmlPage page = client.getPage(searchUrl);
            page.getElementsByTagName("tr").forEach((tableRow) -> {
                //Insert variables here, latest IP, latest ports. populate them from where the println's are

                proxyInstance objProxy = new proxyInstance();
                DomNodeList<HtmlElement> arrColumns = tableRow.getElementsByTagName("td");

                for (int i = 0; i < arrColumns.getLength(); i++) {
                    switch(i) {
                        case 0:
                            //Skip, this is the row count
                            break;
                        case 1:
                            //Skip, this is the JS populated IP (From Base64)
                            objProxy.IP = new String(Base64.getDecoder().decode(arrColumns.get(i).getAttributeDirect("data-ip")));
                            break;
                        case 2:
                            //Skip, this is the JS populated port (From Base64)
                            objProxy.Port = new String(Base64.getDecoder().decode(arrColumns.get(i).getAttributeDirect("data-port")));
                            break;
                        case 3:
                            String[] subDescriptor = arrColumns.get(i).asNormalizedText().split(" ");

                            for (int w = 0; w < subDescriptor.length; w++) {
                                objProxy.Descriptors.add(subDescriptor[w]);
                            }
                            break;
                        case 4:
                            objProxy.Location = arrColumns.get(i).asNormalizedText();
                            break;
                        case 5:
                            objProxy.Speed = Integer.parseInt(arrColumns.get(i).asNormalizedText());
                            break;
                        case 6:
                            objProxy.LastCheckup = System.currentTimeMillis();
                            break;
                        default:
                            System.out.println(i);
                            break;
                    }
                }
                arrProxies.add(objProxy);
            });

            client.close();

        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }

        return arrProxies;
    }
}
