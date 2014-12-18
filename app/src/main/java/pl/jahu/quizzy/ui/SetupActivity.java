package pl.jahu.quizzy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import pl.jahu.quizzy.app.R;
import pl.jahu.quizzy.models.Question;
import pl.jahu.quizzy.providers.QuizzyDatabase;
import pl.jahu.quizzy.utils.Constants;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupActivity extends BaseActivity {

    @Inject
    QuizzyDatabase quizzyDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Question> questions = quizzyDatabase.selectAllQuestions();
        Map<String, Integer[]> stats = getCategoriesInfo(questions);
        SetupFragment setupFragment = new SetupFragment();
        setupFragment.setStats(stats);

        setContentView(R.layout.activity_setup);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, setupFragment)
                    .commit();
        }
    }

    private Map<String, Integer[]> getCategoriesInfo(List<Question> questions) {
        Map<String, Integer[]> result = new HashMap<>();
        for (Question question : questions) {
            String category = question.getCategory();
            if (!result.containsKey(category)) {
                result.put(category, new Integer[]{0, 0, 0, 0});
            }
            Integer[] stats = result.get(category);
            stats[Constants.DIFFICULTY_LEVEL_ALL]++;
            if (question.getDifficultValue() < 75) {
                stats[Constants.DIFFICULTY_LEVEL_BELOW_75]++;
            }
            if (question.getDifficultValue() < 50) {
                stats[Constants.DIFFICULTY_LEVEL_BELOW_50]++;
            }
            if (question.getDifficultValue() < 25) {
                stats[Constants.DIFFICULTY_LEVEL_BELOW_25]++;
            }
            result.put(category, stats);
        }


        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
