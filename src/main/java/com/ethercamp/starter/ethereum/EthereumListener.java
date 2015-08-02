package com.ethercamp.starter.ethereum;

import org.ethereum.crypto.HashUtil;
import org.ethereum.facade.Ethereum;
import org.ethereum.listener.EthereumListenerAdapter;
import org.ethereum.net.eth.StatusMessage;
import org.ethereum.net.rlpx.Node;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class EthereumListener extends EthereumListenerAdapter {

    Ethereum ethereum;

    private static String endpoint = "https://hooks.slack.com/services/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";


    public EthereumListener(Ethereum ethereum) {
        this.ethereum = ethereum;
    }

    @Override
    public void onEthStatusUpdated(Node node, StatusMessage statusMessage) {

        if (statusMessage.getNetworkId() == 1){

            String out = " Updated => Node: " + node.getHost() + ":" + node.getPort() +
                    " best: " + HashUtil.shortHash(statusMessage.getBestHash());
            System.out.println(out);

            HttpHeaders requestHeaders=new HttpHeaders();
            requestHeaders.setContentType(APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate();

            String report = String.format(" { 'text' : '%s'} ", out);
            report = report.replaceAll("'", "\"");

            HttpEntity<String> requestEntity =new HttpEntity<>(report, requestHeaders);
            String response = restTemplate.postForObject(endpoint, requestEntity, String.class);
        }


    }
}
