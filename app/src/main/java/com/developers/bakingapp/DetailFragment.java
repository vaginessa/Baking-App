package com.developers.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developers.bakingapp.adapters.VideoAdapter;
import com.developers.bakingapp.model.Ingredient;
import com.developers.bakingapp.model.Step;
import com.developers.bakingapp.util.ClickCallBack;
import com.developers.bakingapp.util.Constants;
import com.google.android.exoplayer2.C;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements ClickCallBack {

    private static final String TAG = DetailFragment.class.getSimpleName();
    @BindView(R.id.ingredients_list_text_view)
    TextView ingredientsText;
    @BindView(R.id.step_recycler_view)
    RecyclerView stepRecyclerView;
    private String steps, ingredients;
    private Gson gson;
    private List<Step> stepList;
    private List<Ingredient> ingredientList;
    private VideoAdapter videoAdapter;
    private boolean twoPane;

    public DetailFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        steps = bundle.getString(Constants.KEY_STEPS_JSON);
        ingredients = bundle.getString(Constants.KEY_INGREDIENTS_JSON);
        gson = new Gson();
        ingredientList = gson.fromJson(ingredients,
                new TypeToken<List<Ingredient>>() {
                }.getType());
        stepList = gson.fromJson(steps,
                new TypeToken<List<Step>>() {
                }.getType());
        twoPane = bundle.getBoolean(Constants.KEY_PANE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        StringBuffer stringBuffer = new StringBuffer();
        for (Ingredient ingredient : ingredientList) {
            stringBuffer.append("\u2022 " + ingredient.getQuantity() + " " +
                    ingredient.getIngredient() + " " + ingredient.getMeasure() + "\n");
        }
        ingredientsText.setText(stringBuffer.toString());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stepRecyclerView.setLayoutManager(linearLayoutManager);
        Log.d(TAG, stepList.size() + "");
        videoAdapter = new VideoAdapter(getActivity(), stepList);
        videoAdapter.setOnClick(this);
        stepRecyclerView.setAdapter(videoAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(Context context, Integer id, String description, String url) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(Constants.KEY_STEPS_ID, id);
        intent.putExtra(Constants.KEY_STEPS_DESC, description);
        intent.putExtra(Constants.KEY_STEPS_URL, url);
        context.startActivity(intent);
    }
}
