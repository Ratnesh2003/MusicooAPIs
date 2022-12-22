package com.musicoo.apis.helper;

import com.musicoo.apis.model.enums.Provider;
import com.musicoo.apis.payload.request.RegisterReq;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class TokenDecoder {
    public RegisterReq getRegisterRequestFromToken(String token) {
        String[] tokenParts = token.split("\\.");
        byte[] decodedStringByte = Base64.decodeBase64(tokenParts[1]);
        String decodedString = new String(decodedStringByte);
        JsonObject jsonObject = JsonParser.parseString(decodedString).getAsJsonObject();
        StringBuilder sb = new StringBuilder(jsonObject.get("name").toString());
        sb.deleteCharAt(0);
        sb.deleteCharAt(jsonObject.get("name").toString().length()-2);
        String[] nameParts = sb.toString().split(" ", 2);
        return new RegisterReq(
                nameParts[0],
                nameParts[1],
                jsonObject.get("email").getAsString(),
                "xxx",
                Provider.GOOGLE
        );
    }
}
