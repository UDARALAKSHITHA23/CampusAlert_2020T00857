 // Replace with your actual package name
 package com.example.campusalert;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class ActivityNews extends AppCompatActivity {

    private LinearLayout newsContainer;
    private BottomNavigationView bottomNavigation;
    private ImageView btnBack;
    private TextView tvHeader;
    private String currentCategory = "academic"; // Default category

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initializeViews();
        setupBottomNavigation();

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityNews.this, ProfileActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
            finish();
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category")) {
            currentCategory = intent.getStringExtra("category");
        }

        loadNewsForCategory(currentCategory);
    }


    private void initializeViews() {
        newsContainer = findViewById(R.id.newsContainer);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        btnBack = findViewById(R.id.btnBack);
        tvHeader = findViewById(R.id.tvHeader);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) { // Replace with your actual menu item IDs
                    // Redirect to profile page
                    Intent intent = new Intent(ActivityNews.this, ProfileActivity.class); // Replace with your ProfileActivity
                    startActivity(intent);
                    finish();
                    return true;

                } else if (itemId == R.id.nav_academic) { // Replace with your actual menu item ID
                    currentCategory = "academic";
                    tvHeader.setText("Academic News");
                    loadNewsForCategory("academic");
                    return true;

                } else if (itemId == R.id.nav_events) { // Replace with your actual menu item ID
                    currentCategory = "events";
                    tvHeader.setText("Events News");
                    loadNewsForCategory("events");
                    return true;

                } else if (itemId == R.id.nav_Sports) { // Replace with your actual menu item ID
                    currentCategory = "sports";
                    tvHeader.setText("Sports News");
                    loadNewsForCategory("sports");
                    return true;
                }

                return false;
            }
        });
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadNewsForCategory(String category) {
        // Clear existing news cards
        newsContainer.removeAllViews();

        // Get news data based on category
        List<NewsItem> newsList = getNewsData(category);

        // Create and add news cards
        for (NewsItem news : newsList) {
            View newsCard = createNewsCard(news);
            newsContainer.addView(newsCard);
        }
    }

    private View createNewsCard(NewsItem newsItem) {
        // Inflate a custom news card layout or create programmatically
        View cardView = LayoutInflater.from(this).inflate(R.layout.news_card_item, null);

        // If you don't have a separate layout, create programmatically:
        // cardView = createNewsCardProgrammatically(newsItem);

        // Find views and set data
        TextView titleText = cardView.findViewById(R.id.tvNewsTitle);
        TextView descriptionText = cardView.findViewById(R.id.tvNewsDescription);
        TextView dateText = cardView.findViewById(R.id.tvNewsDate);
        ImageView newsImage = cardView.findViewById(R.id.ivNewsImage);

        titleText.setText(newsItem.getTitle());
        descriptionText.setText(newsItem.getDescription());
        dateText.setText(newsItem.getDate());

        // Set image if you have one
        if (newsItem.getImageResource() != 0) {
            newsImage.setImageResource(newsItem.getImageResource());
        }

        // Add click listener for the card
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle news item click - open detailed view
                openNewsDetail(newsItem);
            }
        });

        return cardView;
    }

    private View createNewsCardProgrammatically(NewsItem newsItem) {
        // Create card programmatically if you don't have XML layout
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(4f);
        cardView.setRadius(8f);
        cardView.setUseCompatPadding(true);

        // Create inner linear layout
        LinearLayout innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setPadding(16, 16, 16, 16);

        // Title
        TextView titleText = new TextView(this);
        titleText.setText(newsItem.getTitle());
        titleText.setTextSize(18f);
        titleText.setTextColor(getResources().getColor(android.R.color.black));
        titleText.setPadding(0, 0, 0, 8);

        // Description
        TextView descText = new TextView(this);
        descText.setText(newsItem.getDescription());
        descText.setTextSize(14f);
        descText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        descText.setPadding(0, 0, 0, 8);

        // Date
        TextView dateText = new TextView(this);
        dateText.setText(newsItem.getDate());
        dateText.setTextSize(12f);
        dateText.setTextColor(getResources().getColor(android.R.color.darker_gray));

        innerLayout.addView(titleText);
        innerLayout.addView(descText);
        innerLayout.addView(dateText);
        cardView.addView(innerLayout);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewsDetail(newsItem);
            }
        });

        return cardView;
    }

    private List<NewsItem> getNewsData(String category) {
        List<NewsItem> newsList = new ArrayList<>();

        switch (category) {
            case "academic":
                newsList.add(new NewsItem(
                        "New Research Grant Approved",
                        "The university has received a major research grant for AI studies...",
                        "June 15, 2025",
                        android.R.drawable.ic_dialog_info
                ));
                newsList.add(new NewsItem(
                        "Faculty Achievement Awards",
                        "Three professors recognized for outstanding academic contributions...",
                        "June 12, 2025",
                        android.R.drawable.ic_dialog_info
                ));
                break;

            case "events":
                newsList.add(new NewsItem(
                        "Annual Science Fair 2025",
                        "Students showcase innovative projects at the annual science fair...",
                        "June 14, 2025",
                        android.R.drawable.ic_dialog_alert
                ));
                newsList.add(new NewsItem(
                        "Cultural Night Celebration",
                        "Join us for an evening of music, dance, and cultural performances...",
                        "June 10, 2025",
                        android.R.drawable.ic_dialog_alert
                ));
                break;

            case "sports":
                newsList.add(new NewsItem(
                        "Basketball Team Wins Championship",
                        "Our basketball team secured the regional championship title...",
                        "June 13, 2025",
                        android.R.drawable.star_big_on
                ));
                newsList.add(new NewsItem(
                        "Swimming Competition Results",
                        "Outstanding performance by our swimmers in the inter-university meet...",
                        "June 8, 2025",
                        android.R.drawable.star_big_on
                ));
                break;
        }

        return newsList;
    }

    private void openNewsDetail(NewsItem newsItem) {
        // Create intent to open detailed news view
        Intent intent = new Intent(this, NewsDetailActivity.class); // Create this activity
        intent.putExtra("title", newsItem.getTitle());
        intent.putExtra("description", newsItem.getDescription());
        intent.putExtra("date", newsItem.getDate());
        intent.putExtra("image", newsItem.getImageResource());
        startActivity(intent);
    }

    // NewsItem class to hold news data
    public static class NewsItem {
        private String title;
        private String description;
        private String date;
        private int imageResource;

        public NewsItem(String title, String description, String date, int imageResource) {
            this.title = title;
            this.description = description;
            this.date = date;
            this.imageResource = imageResource;
        }

        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getDate() { return date; }
        public int getImageResource() { return imageResource; }
    }
}