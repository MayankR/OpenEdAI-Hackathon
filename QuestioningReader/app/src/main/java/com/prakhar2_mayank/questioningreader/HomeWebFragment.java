package com.prakhar2_mayank.questioningreader;

import android.animation.Animator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prakhar2_mayank.questioningreader.Helpers.DbHelper;
import com.prakhar2_mayank.questioningreader.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class HomeWebFragment extends Fragment implements View.OnClickListener {

    private View layoutView;

    private List<Card> mCardsList;
    private List<FlashCardItem> dbCardsList;
    private CardListView mFlashCardsList;
    private CardArrayAdapter mCardAdapter;
    private FloatingActionButton buttonFab;

    private HashMap<Card, String> cardAnswerHashMap;

    public List<FlashCardItem> getAllFlashCards() {
        List<FlashCardItem> flashcards = new ArrayList<FlashCardItem>();
        String selectQuery = "SELECT * FROM " + Utility.FLASHCARDS_TABLE;

        DbHelper dbHelper = new DbHelper(getActivity());

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

    public HomeWebFragment() {
        // Required empty public constructor
    }

    public static HomeWebFragment newInstance() {
        HomeWebFragment fragment = new HomeWebFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        mCardsList = new ArrayList<>();
        mCardAdapter = new CardArrayAdapter(getActivity(), mCardsList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_web, container, false);
        layoutView = v;

        cardAnswerHashMap = ((HomeActivity) getActivity()).cardAnswerHashMap;

        //populate cards
        mFlashCardsList = (CardListView) v.findViewById(R.id.flashcardsList);
        mFlashCardsList.setAdapter(mCardAdapter);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                mCardsList = new ArrayList<>();
                mCardAdapter = new CardArrayAdapter(getActivity(), mCardsList);
                dbCardsList = getAllFlashCards();
                for (int i = 0; i < dbCardsList.size(); i++) {
                    Card card = new Card(getActivity());
                    cardAnswerHashMap.put(card, dbCardsList.get(i).getAnswer());
                    card.setTitle(dbCardsList.get(i).getTitle()+'\n' + dbCardsList.get(i).getContent());
                    mCardsList.add(card);
                }
                mCardAdapter = new CardArrayAdapter(getActivity(), mCardsList);
                mCardAdapter.setEnableUndo(true);
                mFlashCardsList.setAdapter(mCardAdapter);
            }
        })).start();

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {

    }
}
