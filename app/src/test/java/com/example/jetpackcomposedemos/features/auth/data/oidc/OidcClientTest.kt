package com.example.jetpackcomposedemos.features.auth.data.oidc

import com.example.jetpackcomposedemos.BuildConfig
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OidcClientTest {
    private val redirect = BuildConfig.OIDC_REDIRECT_URI
    private val api = FakeAuthApi()
    val client = OidcClient(api)
    private fun tokens(idToken: String?) = TokenResponse(
        accessToken = "access-123",
        idToken = idToken,
        refreshToken = "refresh-456",
        expiresIn = 3600,
        tokenType = "Bearer"
    )

    private fun startAndRedirect(code: String = "the-code"): Pair<String, String> {
        val url = client.beginSignIn()
        val state = url.queryParameter("state")!!
        val nonce = url.queryParameter("nonce")!!
        return "$redirect?code=$code&state=$state" to nonce
    }

    @Test
    fun `beginSignIn builds a PKCE url`() {
        val url =  client.beginSignIn()

        // challenge method is s256
        assertEquals("S256", url.queryParameter("code_challenge_method"))


        // client id in the url is the same as our BuildConfig
        assertEquals(BuildConfig.OIDC_CLIENT_ID, url.queryParameter("client_id"))

        // code challenge in the url is 43 characters long
        assertEquals(43, url.queryParameter("code_challenge")?.length)
    }

    @Test
    fun `accepts a redirect carrying our state`() {
        val client = client
        val state = client.beginSignIn().queryParameter("state")

        val mockServerResponse = "$redirect?code=abc&state=$state"

        val result = client.codeFrom(mockServerResponse)

        assertEquals("abc", result.getOrNull())
    }

    @Test
    fun `reject a redirect with a different state`() {
        val client = client
        client.beginSignIn()

        val mockServerResponse = "$redirect?code=abc&state=somebody_else"

        val result = client.codeFrom(mockServerResponse)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("state mismatch"))
    }

    @Test
    fun `reports an error from the server`() {
        val client = client
        val state = client.beginSignIn().queryParameter("state")

        val mockServerResponse = "$redirect?error=access_denied&state=$state"

        val result = client.codeFrom(mockServerResponse)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("access_denied"))
    }

    @Test
    fun `fails if no sign in was started`() {
        val result = client.codeFrom("$redirect?code=abc&state=x")

        assertTrue(result.isFailure)
    }

     // ------------------- the token half --------------------------

    @Test
    fun `completeSignIn returns a user built from the token`() = runTest{
        val (redirectUri, nonce) = startAndRedirect()
        api.response = tokens(FakeAuthApi.idToken("11000", "Cara Cole", nonce))

        val user = client.completeSignIn(redirectUri).getOrThrow()

        assertEquals("11000", user.id)
        assertEquals("Cara Cole", user.username)
        assertEquals("access-123", user.token)
        assertEquals("refresh-456", user.refreshToken)
    }

    @Test
    fun `the verifier sent to the server hashes to the challenge sent to the browser`() = runTest {

        val url = client.beginSignIn()
        val state = url.queryParameter("state")!!
        val nonce = url.queryParameter("nonce")!!
        api.response = tokens(FakeAuthApi.idToken("1", "A", nonce))

        client.completeSignIn("$redirect?code=the-code&state=$state").getOrThrow()

        assertEquals(url.queryParameter("code_challenge"), Pkce.codeChallenge(api.lastVerifier!!))
        assertEquals("the-code", api.lastCode)
    }
}