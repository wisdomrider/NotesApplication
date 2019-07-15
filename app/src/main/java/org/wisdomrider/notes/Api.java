package org.wisdomrider.notes;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @POST("login")
    Call<LoginPage.LoginResponse> Login(@Body LoginPage.Login login);

    @GET("notes")
    Call<LoginPage.NotesResponse> Notes();

    @POST("add/note")
    Call<LoginPage.LoginResponse> addNote(@Body LoginPage.Add add);

}
