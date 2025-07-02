package com.example.pptxpresenter;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAPI {
    @GET("{user}/next")
    Call<ServerAns> nextSlide(@Path("user") String user);

    @GET("{user}/prev")
    Call<ServerAns> prevSlide(@Path("user") String user);

    @GET("{user}/hb")
    Call<ServerAns> heartBeat(@Path("user") String user);

    @GET("{user}/playdemovideo")
    Call<ServerAns> playVideo(@Path("user") String user);

}
