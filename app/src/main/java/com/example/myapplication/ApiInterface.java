package com.example.myapplication;
import com.example.myapplication.model.Category;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("categories") /
    Call<List<Category>> getCategories();
}
@DELETE("categories/{id}")
Call<Void> deleteCategory(@Path("id") int id);
@PUT("categories/{id}")
Call<Category> updateCategory(@Path("id") int id, @Body Category category);