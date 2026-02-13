package kh.edu.rupp.s2h1_reviews1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private LinearLayout groupLinearLayout;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        groupLinearLayout = view.findViewById(R.id.groupLinearLayout);

        loadTaskGroups();
    }

    private void loadTaskGroups() {
        String url = "https://raw.githubusercontent.com/toladev0/jsonFile/refs/heads/main/tasks.json";

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject taskGroup = response.getJSONObject(i);

                                String title = taskGroup.getString("title");
                                int taskCount = taskGroup.getInt("task_count");
                                int progress = taskGroup.getInt("progress");
                                String iconName = taskGroup.getString("icon");

                                addTaskCard(title, taskCount, progress, iconName);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

    private void addTaskCard(String title, int taskCount, int progress, String iconName) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        CardView card = (CardView) inflater.inflate(R.layout.card_task_item, null);

        // Find views
        TextView titleText = card.findViewById(R.id.cardTitle);
        TextView countText = card.findViewById(R.id.cardTaskCount);
        ProgressBar progressBar = card.findViewById(R.id.cardProgress);
        TextView progressPercentage = card.findViewById(R.id.cardProgressPercentage);
        ImageView icon = card.findViewById(R.id.cardIcon);

        // Set texts
        titleText.setText(title);
        countText.setText(taskCount + " tasks");
        progressPercentage.setText(progress + "%");

        // Set progress (optional: determinate)
        progressBar.setProgress(progress);

        // Load drawable by name
        int iconRes = getResources().getIdentifier(iconName, "drawable", requireContext().getPackageName());
        icon.setImageResource(iconRes);

        // Add card to LinearLayout
        groupLinearLayout.addView(card);
    }
}
