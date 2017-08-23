package com.prakhar2_mayank.questioningreader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.prakhar2_mayank.questioningreader.Helpers.DbHelper;
import com.prakhar2_mayank.questioningreader.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;


public class FlashCardsActivity extends AppCompatActivity {

    private List<Card> mCardsList;
    private List<FlashCardItem> dbCardsList;
    private CardListView mFlashCardsList;
    private CardArrayAdapter mCardAdapter;
    private FloatingActionButton buttonFab;

    private HashMap<Card, String> cardAnswerHashMap;

    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

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

        cardAnswerHashMap = new HashMap<>();

        //populate cards
        mFlashCardsList = (CardListView) findViewById(R.id.flashcardsList);
        dbCardsList = getAllFlashCards();
        mCardsList = new ArrayList<>();
        for (int i = 0; i < dbCardsList.size(); i++) {
            Card card = new Card(this);
            cardAnswerHashMap.put(card, dbCardsList.get(i).getAnswer());
            card.setTitle(dbCardsList.get(i).getTitle()+'\n' + dbCardsList.get(i).getContent());
            mCardsList.add(card);
        }
        mCardAdapter = new CardArrayAdapter(this, mCardsList);
        mCardAdapter.setEnableUndo(true);
        mFlashCardsList.setAdapter(mCardAdapter);

        for (int i = 0; i < dbCardsList.size(); i++) {
            Log.d("i3t04t943t43t43", mCardAdapter.getView(i, null, mFlashCardsList).findViewById(R.id.list_cardId1).toString());
            Card card = new Card(this);
            ((CardView)(mCardAdapter.getView(i, null, mFlashCardsList).findViewById(R.id.list_cardId1))).setCard(card);

//            mCardAdapter.getView(i, null, mFlashCardsList).findViewById(R.id.transparent_layer_card).performClick();
//            mCardAdapter.getView(i, null, mFlashCardsList).findViewById(R.id.transparent_layer_card1).performClick();
        }

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
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

    private void zoomImageFromThumb(final View thumbView, final CardView expandedImageView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        // TODO: BAD HACK NEED TO REMOVE
        while (mCurrentAnimator != null) {
            //mCurrentAnimator.cancel();
        }

        AnimatorSet set = new AnimatorSet();

        thumbView.setAlpha(1);
        expandedImageView.setRotationY(-180);
        set.play(ObjectAnimator.ofFloat(thumbView, View.ROTATION_Y, 0, -180)).with(ObjectAnimator.ofFloat(thumbView, View.ALPHA, 1, 0)).with(ObjectAnimator.ofFloat(expandedImageView, View.ALPHA, 0, 1)).with(ObjectAnimator.ofFloat(expandedImageView, View.ROTATION_Y, -180, -360));

        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
                thumbView.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
                thumbView.setRotationY(-180);
                thumbView.setAlpha(0);
                thumbView.setVisibility(View.INVISIBLE);
                expandedImageView.setRotationY(0);
                expandedImageView.setAlpha(1);
                expandedImageView.setVisibility(View.VISIBLE);
            }
        });
        set.start();
        mCurrentAnimator = set;

    }

    private void zoomImageFromThumbRev(final View thumbView, final CardView expandedImageView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        // TODO: BAD HACK NEED TO REMOVE
        while (mCurrentAnimator != null) {
            //mCurrentAnimator.cancel();
        }

        AnimatorSet set = new AnimatorSet();

        expandedImageView.setRotationY(-180);
        thumbView.setAlpha(1);
        set.play(ObjectAnimator.ofFloat(thumbView, View.ROTATION_Y, 0, 180)).with(ObjectAnimator.ofFloat(thumbView, View.ALPHA, 1, 0)).with(ObjectAnimator.ofFloat(expandedImageView, View.ALPHA, 0, 1)).with(ObjectAnimator.ofFloat(expandedImageView, View.ROTATION_Y, -180, 0));

        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
                thumbView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
                thumbView.setRotationY(180);
                thumbView.setAlpha(0);
                thumbView.setVisibility(View.INVISIBLE);
                expandedImageView.setRotationY(0);
                expandedImageView.setAlpha(1);
                expandedImageView.setVisibility(View.VISIBLE);
            }
        });
        set.start();
        mCurrentAnimator = set;

    }

    public void cardFlipper(View view) {
        CardView cardView = (CardView) view.getParent().getParent().getParent().getParent().getParent();
        CardView cardView1 = (CardView) ((FrameLayout) (cardView.getParent())).findViewById(R.id.list_cardId1);
        cardView1.setVisibility(View.VISIBLE);
        //cardView.setVisibility(View.INVISIBLE);
        Card card = new Card(this);
        card.setTitle(cardAnswerHashMap.get(cardView.getCard()));
        cardView1.setCard(card);
        zoomImageFromThumb(cardView, cardView1);
//        cardView.setVisibility(View.INVISIBLE);
    }
    public void undoCardFlipper(View view) {
        CardView cardView = (CardView) view.getParent().getParent().getParent().getParent().getParent();
        CardView cardView1 = (CardView) ((FrameLayout) (cardView.getParent())).findViewById(R.id.list_cardId);
        cardView1.setVisibility(View.VISIBLE);
        //cardView.setVisibility(View.INVISIBLE);
        zoomImageFromThumbRev(cardView, cardView1);
//        cardView.setVisibility(View.INVISIBLE);
    }
}
