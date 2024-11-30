import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCategoryActivity extends AppCompatActivity {

    private EditText editTextCategoryName;
    private Button buttonSaveCategory;
    private Button buttonDeleteCategory;
    private ApiInterface apiInterface;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        buttonSaveCategory = findViewById(R.id.buttonSaveCategory);
        buttonDeleteCategory = findViewById(R.id.buttonDeleteCategory);

        // Ініціалізуйте Retrofit
        apiInterface = ApiClient.getClient().create(ApiInterface.class); // Додайте клас ApiClient для отримання Retrofit

        buttonSaveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryId == -1) {
                    saveCategory(); // Додати нову категорію
                } else {
                    updateCategory(); // Оновити існуючу категорію
                }
            }
        });

        buttonDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory();
            }
        });


        if (getIntent().hasExtra("CATEGORY_ID")) {
            categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
            loadCategory(categoryId);
        } else {
            categoryId = -1;
            buttonDeleteCategory.setVisibility(View.GONE);
        }
    }

    private void loadCategory(int categoryId) {

        Call<Category> call = apiInterface.getCategoryById(categoryId);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Category category = response.body();
                    editTextCategoryName.setText(category.getName());
                    buttonDeleteCategory.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(CreateCategoryActivity.this, "Error loading category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveCategory() {
        String categoryName = editTextCategoryName.getText().toString().trim();

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            return;
        }

         Category newCategory = new Category();
        newCategory.setName(categoryName);


        Call<Category> call = apiInterface.createCategory(newCategory);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateCategoryActivity.this, "Category saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateCategoryActivity.this, "Error saving category ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(CreateCategoryActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCategory() {
        String categoryName = editTextCategoryName.getText().toString().trim();

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show();
            return;
        }


        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(categoryName);

        Call<Category> call = apiInterface.updateCategory(categoryId, updatedCategory);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateCategoryActivity.this, "Category updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateCategoryActivity.this, "Error updating category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(CreateCategoryActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCategory() {
        if (categoryId == -1) {
            Toast.makeText(this, "No category to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Void> call = apiInterface.deleteCategory(categoryId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateCategoryActivity.this, "Category deleted", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateCategoryActivity.this, "Error deleting category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CreateCategoryActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}