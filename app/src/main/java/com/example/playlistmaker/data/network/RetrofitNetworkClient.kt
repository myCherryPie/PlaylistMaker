package com.example.playlistmaker.data.network

import com.example.playlistmaker.R
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.SearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val urlItunesApi = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(urlItunesApi)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val playListService = retrofit.create(ItunseApi::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            val resp = playListService.search(dto.text).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
    }

