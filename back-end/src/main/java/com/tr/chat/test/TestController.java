package com.tr.chat.test;

import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.GraphServiceClient;
import com.tr.chat.entity.Resp;
import com.tr.chat.mapper.GroupMapper;

import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "*")
public class TestController {
    private Resp resp;
    final String clientId = "c625532e-a36d-4789-85e3-52101552884f";
    final String tenantId = "5c5ac9f4-2119-4536-9b95-3e4266215774"; // or "common" for multi-tenant apps
    final List<String> scopes = Collections.singletonList("User.Read");
    final DeviceCodeCredential credential;
    final TokenCredentialAuthProvider authProvider;
    final GraphServiceClient<Request> graphClient;
    String message;


    @Autowired
    public TestController(Resp resp, GroupMapper mapper, GroupMapper groupMapper) throws Exception {
        this.resp = resp;
        credential = new DeviceCodeCredentialBuilder()
                .clientId(clientId).tenantId(tenantId).challengeConsumer(challenge -> {
                    // Display challenge to the user
                    message=challenge.getUserCode();
                }).build();
        if (null == scopes || null == credential) {
            throw new Exception("Unexpected error");
        }
        authProvider = new TokenCredentialAuthProvider(
                scopes, credential);
        graphClient = GraphServiceClient.builder()
                .authenticationProvider(authProvider).buildClient();
    }


    @GetMapping("/test")
    public Resp doGet(@RequestParam Map<Object, String> map, HttpServletRequest request)  {
        resp.success(graphClient.me().buildRequest(new QueryOption("client_secret",clientId )).select("displayName,jobTitle")
                .get(),"成功");


        return resp;
    }
}
