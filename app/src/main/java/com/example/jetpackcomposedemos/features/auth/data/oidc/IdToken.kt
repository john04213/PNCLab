package com.example.jetpackcomposedemos.features.auth.data.oidc

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Base64


object IdToken {

    data class Claims(
        val subject: String,         // "sub" the customer id
        val names: String?,          // "name" this will be present if the profile scope is requested
        val nonce: String?
    )


    // a JWT (JSON web Token) is three base64url parts separated by dots
    // header.payloard.signature - atp, we only need the middle one
    // NOTE: we will NOT verify the signature here
    // (that is allowed by OpenID Connect spec b/c we got it  directly from the endpoint over HTTPS)
    fun parse(jwt:String): Claims {
        // split on a "." get the second part
        val payload = jwt.split('.').getOrNull(1)
            ?:  throw IllegalArgumentException("The ID token is not a JWT.")

        // payload is a URL_encoded string, unencode it
        val raw = String(Base64.getUrlDecoder().decode(payload), Charsets.UTF_8)

        //converts from JSON-format string to an object
        val json = Json.parseToJsonElement(raw).jsonObject

        return Claims(
            subject = json["sub"]?.jsonPrimitive?.contentOrNull
                ?: throw IllegalArgumentException("The ID token is missing the 'sub' claim."),
            names = json["name"]?.jsonPrimitive?.contentOrNull,
            nonce = json["nonce"]?.jsonPrimitive?.contentOrNull
        )

    }

}



