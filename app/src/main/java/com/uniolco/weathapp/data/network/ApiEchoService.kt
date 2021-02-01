package com.uniolco.weathapp.data.network

import android.util.Log
import okhttp3.*

const val url = "wss://echo.websocket.org"

interface ApiEchoService {

companion object{
    operator fun invoke(message: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val webSocket = client.newWebSocket(request, object: WebSocketListener(){
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send("Hello, I am websocket")
                webSocket.send("My name's Biba")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                Log.d("CLOSING", "$code,  $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("FAILURE", t.message.toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("MESSAGE", text)
            }
        })

        webSocket.send(message)
        webSocket.close(1000, "Closing... See you later!")
    }
}

}