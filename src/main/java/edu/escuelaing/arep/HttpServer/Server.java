package edu.escuelaing.arep.HttpServer;

import java.net.*;
import java.io.*;

public class Server {

    static String HTTP_OK_HEADER = "HTTP/1.1 200 OK\r\n"
        + "Content-Type: text/html\r\n"
         + "\r\n";
    
         
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        //CICLO PARA QUE SIGA ESCUCHANDO
        boolean running = true;
        while(running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, resource;
            boolean flag = true;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("RecibÃ­: " + inputLine);
                //REVISA LA PRIMER LINEA GET /resource HTTP/1.1
                if(flag){
                    flag = false;
                    resource = inputLine.split(" ")[1];
                    reviewFirstLine(resource,out);
                }
                if (!in.ready()) {
                    break;
                }
            }
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Funcion encargada de revisar la primer linea y ver que recurso se esta pidiendo
     * @param resource
     */
    public static void reviewFirstLine(String resource, PrintWriter out){
        if(resource.contains("/calculadora")){
            out.println(HTTP_OK_HEADER+getForm());
        }
    }

    public static String getForm(){
        String outputline = "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "  <title>Calculator AREP</title>\n"
                + "  <meta charset=\"UTF-8\">\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Calculator AREP -- PARCIAL</h1>\n"
                + "<form action=\"/hello\">\n"
                + "  <label for=\"operation\">Operación: (cos,sin,tan,qck)</label><br>\n"
                + "  <input type=\"text\" id=\"operation\" name=\"operation\"><br><br>\n"
                + "  <label for=\"number\">numero: (Recordar que si seleccionaste qck, debes mandar un lista de enteros)</label><br>\n"
                + "  <input type=\"text\" id=\"number\" name=\"number\"><br><br>\n"
                + "  <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n"
                + "</form>\n"
                + "<div id=\"getrespmsg\"></div>\n"
                + "\n"
                + "<script>\n"
                + "            function loadGetMsg() {\n"
                + "                let nameVar = document.getElementById(\"operation\").value;\n"
                + "                let valueVar = document.getElementById(\"number\").value;\n"
                + "                const xhttp = new XMLHttpRequest();\n"
                + "                xhttp.onload = function() {\n"
                + "                    document.getElementById(\"getrespmsg\").innerHTML =\n"
                + "                    this.responseText;\n"
                + "                }\n"
                + "                xhttp.open(\"GET\", \"/\"+nameVar+\"?val=\"+valueVar);\n"
                + "                xhttp.send();\n"
                + "            }\n"
                + "        </script>\n"
                + "\n"
                + "</body>\n"
                + "</html>";
        return outputline;
    }
}




