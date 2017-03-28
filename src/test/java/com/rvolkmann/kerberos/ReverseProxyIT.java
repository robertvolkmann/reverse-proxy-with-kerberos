package com.rvolkmann.kerberos;

import org.codelibs.spnego.SpnegoHttpURLConnection;
import org.ietf.jgss.GSSException;
import org.junit.Assert;
import org.junit.Test;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PrivilegedActionException;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class ReverseProxyIT {

    private static final String APACHE_URL = "http://localhost:8080";

    @Test
    public void should_get_unauthorized_without_a_kerberos_ticket() throws IOException {
        URL url = new URL(APACHE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();
        Assert.assertEquals(HTTP_UNAUTHORIZED, connection.getResponseCode());
        Assert.assertEquals("Negotiate", connection.getHeaderField("WWW-Authenticate"));
    }

    @Test
    public void should_get_the_username_from_camel() throws IOException, LoginException, PrivilegedActionException, GSSException {
        SpnegoHttpURLConnection connection = new SpnegoHttpURLConnection("client", "user@LOCALHOST", "password");

        connection.requestCredDeleg(true);
        connection.connect(new URL(APACHE_URL));
        Assert.assertEquals(HTTP_OK, connection.getResponseCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Assert.assertEquals("user@LOCALHOST", reader.readLine());
    }
}
