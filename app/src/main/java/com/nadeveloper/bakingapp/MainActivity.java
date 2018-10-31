package com.nadeveloper.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.nadeveloper.bakingapp.model.Recipe;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecipeService mRecipeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecipeService = new RecipeClient().mRecipeService;
        fetchRecipes();
    }
        // Fetch recipes
        private void fetchRecipes() {
            Call<ArrayList<Recipe>> call = mRecipeService.getRecipes();

            call.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    //Test if response is succesfull
                    ArrayList<Recipe> recipe = response.body();
                    Log.d("BAKING_APP", recipe.get(0).getName());
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    Log.d("FAILURE", t.toString());
                }
            });
        }
    }
