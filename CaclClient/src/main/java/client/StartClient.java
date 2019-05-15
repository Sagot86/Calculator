package client;

import client.service.InitializeService;


public class StartClient {

    public static void main(String[] args) {

        InitializeService service = new InitializeService();

        service.onAppStart();

        service.calculate();



        System.out.println("CURRENT IN DB");
        service.printFromDB();

        service.inAppEnd();

    }
}