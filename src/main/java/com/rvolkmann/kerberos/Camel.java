package com.rvolkmann.kerberos;

import org.apache.camel.main.Main;

public class Camel {

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new HttpRouteBuilder());
        main.run(args);
    }
}
