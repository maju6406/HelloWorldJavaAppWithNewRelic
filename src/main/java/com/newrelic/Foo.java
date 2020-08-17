package com.newrelic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.GenericParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Random;

/**
 * Hello world!
 */
@SuppressWarnings("ALL")
public class Foo {

    /**
     * How long to wait.
     */
    public static final int MILLIS = 10000;
    /**
     * The Count.
     */
    private Integer transaction = 1;

    /**
     * Hello world!
     */
    public Integer getTransactionCount() {
        return transaction;
    }

    /**
     * Hello world!
     */
    public void setTransactionCount(Integer t) {
        this.transaction = t;
    }

    /**
     * Begin.
     */
    @Trace(dispatcher = true)
    public void beginTransaction() {
        System.out.println("Begin! " + transaction);
    }

    /**
     * End.
     */
    @Trace(dispatcher = true)
    public void endTransaction() {
        System.out.println("End! " + transaction);
        transaction++;
        setTransactionCount(transaction);
    }

    /**
     * Process.
     */
    @SuppressWarnings("checkstyle:Indentation")
    @Trace(dispatcher = true)
    public void process() {
        try {
            long millisToSleep  = new Random().nextInt(MILLIS);
            System.out.println("Process! " + millisToSleep);
            Thread.sleep(millisToSleep);
            var values = new HashMap<String, String>() {{
                put("count", String.valueOf(getTransactionCount()));
                put ("time", String.valueOf(millisToSleep));
            }};

            if (getTransactionCount() % 30 == 0) {
                throw new RuntimeException();
            }

            var objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(values);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/post"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ExternalParameters params = GenericParameters
                    .library("imitator()")
                    .uri(URI.create("https://httpbin.org/post"))
                    .procedure("POST")
                    .build();
            NewRelic.getAgent().getTracedMethod().reportAsExternal(params);

            final String payload = NewRelic.getAgent().getTransaction().createDistributedTracePayload().text();
			System.out.println(response.body());

        } catch (com.fasterxml.jackson.core.JsonProcessingException jpe) {
            jpe.printStackTrace();
            NewRelic.noticeError(jpe, false);
        }
        catch (java.io.IOException ioe) {
            ioe.printStackTrace();
            NewRelic.noticeError(ioe, false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            NewRelic.noticeError(e, false);
        }
    }

}
