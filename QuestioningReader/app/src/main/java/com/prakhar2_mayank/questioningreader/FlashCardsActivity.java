package com.prakhar2_mayank.questioningreader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.prakhar2_mayank.questioningreader.Helpers.DbHelper;
import com.prakhar2_mayank.questioningreader.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class FlashCardsActivity extends AppCompatActivity {

    private List<Card> mCardsList;
    private List<FlashCardItem> dbCardsList;
    private CardListView mFlashCardsList;
    private CardArrayAdapter mCardAdapter;
    private FloatingActionButton buttonFab;


    public List<FlashCardItem> getAllFlashCards() {
        List<FlashCardItem> flashcards = new ArrayList<FlashCardItem>();
        String selectQuery = "SELECT * FROM " + Utility.FLASHCARDS_TABLE;

        DbHelper dbHelper = new DbHelper(FlashCardsActivity.this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToNext()) {
            do {
                FlashCardItem flashCardItem = new FlashCardItem();
                flashCardItem.setId(cursor.getLong(cursor.getColumnIndex(Utility.FLASHCARD_ID)));
                flashCardItem.setTitle(cursor.getString(cursor.getColumnIndex(Utility.FLASHCARD_TITLE)));
                flashCardItem.setContent(cursor.getString(cursor.getColumnIndex(Utility.FLASHCARD_CONTENT)));
                flashCardItem.setAnswer(cursor.getString(cursor.getColumnIndex(Utility.FLASHCARD_ANSWER)));
                flashcards.add(flashCardItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close();
        return flashcards;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        // Initialize the FloatingActionButton and set it's colors
        buttonFab = (FloatingActionButton) findViewById(R.id.addNewFlashCard);
        buttonFab.setColor(getResources().getColor(R.color.action_bar_color));
        buttonFab.setTextColor(getResources().getColor(R.color.action_bar_text_color));

        //populate cards
        mFlashCardsList = (CardListView) findViewById(R.id.flashcardsList);
        dbCardsList = getAllFlashCards();
        mCardsList = new ArrayList<>();
        for (int i = 0; i < dbCardsList.size(); i++) {
            Card card = new Card(this);
            card.setTitle(dbCardsList.get(i).getTitle()+'\n'+dbCardsList.get(i).getContent()+"\n"+dbCardsList.get(i).getAnswer());
            mCardsList.add(card);
        }
        mCardAdapter = new CardArrayAdapter(this, mCardsList);
        mCardAdapter.setEnableUndo(true);
        mFlashCardsList.setAdapter(mCardAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flash_cards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
