package com.nadeveloper.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.nadeveloper.bakingapp.R;
import com.nadeveloper.bakingapp.model.Recipe;
import com.nadeveloper.bakingapp.RecipeDetailsActivity;
import com.nadeveloper.bakingapp.utils.ConstantsUtil;
import com.nadeveloper.bakingapp.widget.WidgetService;

import static android.content.Context.MODE_PRIVATE;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private final Context mContext;
    private final ArrayList<Recipe> mRecipeList;
    private String mJsonResult;
    String recipeJson;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipeList, String jsonResult) {
        this.mContext = context;
        this.mRecipeList = recipeList;
        this.mJsonResult = jsonResult;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_recipe_name)
        TextView recipeName;

        @Nullable
        @BindView(R.id.iv_recipe_icon)
        ImageView recipeIcon;

        public RecipeViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_recipe_card, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {

        holder.recipeName.setText(mRecipeList.get(position).getName());

        switch (position){
            case 0 : holder.recipeIcon.setImageResource(R.drawable.nutellapie);
                break;
            case 1 : holder.recipeIcon.setImageResource(R.drawable.browines);
                break;
            case 2 : holder.recipeIcon.setImageResource(R.drawable.yellowcake);
                break;
            case 3 : holder.recipeIcon.setImageResource(R.drawable.cheesecake);
                break;
        }

        // On recipe click, get recipe details as parcelable and send to Details activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = mRecipeList.get(holder.getAdapterPosition());
                recipeJson = jsonToString(mJsonResult, holder.getAdapterPosition());
                Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
                ArrayList<Recipe> recipeArrayList = new ArrayList<>();
                recipeArrayList.add(recipe);
                intent.putParcelableArrayListExtra(ConstantsUtil.RECIPE_INTENT_EXTRA, recipeArrayList);
                intent.putExtra(ConstantsUtil.JSON_RESULT_EXTRA, recipeJson);
                mContext.startActivity(intent);

                SharedPreferences.Editor editor = mContext.getSharedPreferences(ConstantsUtil.SHARED_PREF, MODE_PRIVATE).edit();
                editor.putString(ConstantsUtil.JSON_RESULT_EXTRA, recipeJson);
                editor.apply();

                if(Build.VERSION.SDK_INT > 25){
                    //Start the widget service to update the widget
                    WidgetService.startActionOpenRecipeO(mContext);
                }
                else{
                    //Start the widget service to update the widget
                    WidgetService.startActionOpenRecipe(mContext);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    // Get selected Recipe as Json String
    private String jsonToString(String jsonResult, int position){
        JsonElement jsonElement = new JsonParser().parse(jsonResult);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonElement recipeElement = jsonArray.get(position);
        return recipeElement.toString();
    }

}